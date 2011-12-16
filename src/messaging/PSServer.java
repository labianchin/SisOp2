/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messaging;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Publish/Subscriber central server
 * @author luisarmando
 */
public class PSServer implements MessagesOrganizer, SubscribesOrganizer {
    /* Listen for incomming messages */

    public PeerListener listener;
    public SubscriptionListener subscriptionListener;
    //public subscribers;
    public Map<String, Subscriber> subscribers;
    public Map<String, Map<String, Queue<Message>>> messages;
    public SubscriberSender sender;
    private int port;

    public PSServer() {
        this.port = 2001;
        this.preapre();
        this.startAll();
    }

    public PSServer(int port) {
        this.port = port;
        this.preapre();
        this.startAll();
    }

    //preapara listas e threads servidoras
    public void preapre() {
        this.subscribers = new HashMap<String, Subscriber>();
        this.messages = new ConcurrentHashMap<String, Map<String, Queue<Message>>>();
        this.listener = new PeerListener((MessagesOrganizer) this);
        this.listener.setPort(this.port);
        this.sender = new SubscriberSender(this);
        this.subscriptionListener = new SubscriptionListener(this);
        subscriptionListener.setPort(this.port+1);
    }

    //inicia threads
    public void startAll() {
        listener.start();
        sender.start();
        subscriptionListener.start();
    }

    //inscreve endereço num topico/titulo
    @Override
    public synchronized void subscribe(String address, String topic, String title) {
        if (!this.subscribers.containsKey(address)) { //assinante ainda não possui alguma inscrição
            this.subscribers.put(address, new Subscriber(address));
        }
        Subscriber subscriber = this.subscribers.get(address);
        subscriber.subscribe(topic, title); //cria inscrição
        //this.sender.notify();
    }

    @Override
    public synchronized void subscribe(String address, String subscription) {
        String[] s = subscription.split("%"); //gera assinatura quebrando a string subscription
        String title, topic;
        if (s.length < 1 || s[0].isEmpty()) {
            topic = null;
        } else {
            topic = s[0];
        }
        if (s.length < 2 || s[1].isEmpty()) {
            title = null;
        } else {
            title = s[1];
        }
        this.subscribe(address, topic, title); //assina
    }
    public static int defaultPort = 1001;

    /* envia mensagens para os assinantes */
    public boolean dispatchToSubscribers() throws ConcurrentModificationException {
        System.out.println("Dispatching messsages to : "+this.subscribers.values());
        for (Subscriber subscriber : this.subscribers.values()) { //percorre assinantes
            Socket socket = null;
            ObjectOutputStream oo = null;
            String[] addport = subscriber.address.split(":"); //quebra o endereço, no formanto <ip:porta>
            String address = addport[0];
            int port;
            if (addport.length < 2) {
                port = this.defaultPort;
            } else {
                port = Integer.parseInt(addport[1]);
            }
            try { //envia várias mensagens para um assinante numa mesma conexão
                //Send object over the network
                for (String topic : this.messages.keySet()) {//vare os topicos disponíves
                    if (!subscriber.subscription.isEmpty()
                            && !subscriber.subscription.containsKey(topic)) {
                        continue; //se assinante não está inscrito no tópico, não envia
                    }
                    Set<String> titleList = subscriber.subscription.get(topic);

                    for (String title : this.messages.get(topic).keySet()) { //percorre lista de titulos
                        boolean found = titleList == null //está inscrito em todos titulos
                                || titleList.isEmpty()
                                || titleList.contains(title); //ou possui mensagem no titulo cadastrado
                        if (found) {
                            //System.out.println(subscriber.lastTimestamp);
                            for (Message message : this.messages.get(topic).get(title)) {//percorre mensagens
                                //System.out.println(message.timestampReciever);
                                if (message.timestampReciever.compareTo(subscriber.lastTimestamp) > 0) { //mensagem possui timestamp maior q a da ultima mensagem enviada
                                    if (socket == null) { //cria a conexão na primeira mensagem a ser enviada para esse incrito
                                        System.out.println("Debug: connecting with "+subscriber.address);
                                        socket = new Socket(InetAddress.getByName(address), port);
                                        oo = new ObjectOutputStream(socket.getOutputStream());
                                    }
                                    oo.writeObject(message); //envia mensagem
                                    oo.flush();
                                    subscriber.lastTimestamp = message.timestampReciever; //atualiza ultima timestamp
                                }
                            }
                        }
                    }
                }
                if (socket != null) { //terminou esse assinante, fecha a conexão
                    socket.close();
                    System.out.println("Debug: connection with "+subscriber.address+" closed");
                }
            } catch (IOException ioe) {
                System.out.println(ioe);
                continue;
            }
        }
        return true;
    }

    //lista os topicos das mensagens atuais
    public Collection<String> getTopics() {
        return this.messages.keySet();
    }

    //lista os titulos de um tópico
    public Collection<String> getTitles(String topic) {
        if (!this.messages.containsKey(topic)) {
            return new HashSet<String>();
        } else {
            return this.messages.get(topic).keySet();
        }
    }

    //coloca mensagem na fila
    @Override
    public synchronized boolean addMessage(Message message) {
        if (!this.messages.containsKey(message.topic)) { //topico ainda não cadastrado?
            this.messages.put(message.topic, new ConcurrentHashMap<String, Queue<Message>>());
        }
        Map<String, Queue<Message>> topicList = this.messages.get(message.topic); //pega topico
        if (!topicList.containsKey(message.title)) { //titulo ainda não cadastrado
            topicList.put(message.title, new PriorityBlockingQueue<Message>());
        }
        Boolean ret = topicList.get(message.title).add(message); //adiciona mensagem no topico/titulo
        //this.sender.notify();
        return ret;
    }

    //cria tópicos/titulos na listagem
    public void registerTopicTitles(List<Entry<String, String>> list) {
        for (Entry<String, String> entry : list) {
            if (!this.messages.containsKey(entry.getKey())) {
                this.messages.put(entry.getKey(), new ConcurrentHashMap<String, Queue<Message>>());
            }
            Map<String, Queue<Message>> topicList = this.messages.get(entry.getKey());
            if (!topicList.containsKey(entry.getValue())) {
                topicList.put(entry.getValue(), new PriorityBlockingQueue<Message>());
            }
        }
        System.out.println("Topic/Title list: ");
        for (String topic : this.messages.keySet()) {
            System.out.println("\t" + topic);
            for (String title : this.messages.get(topic).keySet()) {
                System.out.println("\t\t" + title);
            }
        }
        System.out.println(this.getTopics());
    }
}
