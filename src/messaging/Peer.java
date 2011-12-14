/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messaging;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 *
 * @author luisarmando
 */
public class Peer implements MessagesOrganizer {

    public PeerListener listener;
    public Queue<Message> incomingQueue; //fila das mensagens recebidas
    public Queue<Message> outgoingQueue; //filas das mensagens a enviar (não conseguiram ser enviadas)
    public int port;

    public Peer() {
        this.prepare();
        this.startAll();
    }
    
    public Peer(int port) {
        this.prepare();
        listener.setPort(port);
        this.port = port;
        this.startAll();
    }
    
    //Prepara filas e thread servidora (que escuta mensagens)
    public void prepare(){
        this.incomingQueue = new PriorityBlockingQueue();
        this.outgoingQueue = new PriorityBlockingQueue();
        listener = new PeerListener((MessagesOrganizer)this);
    }
    
    //inicia thread servidora
    public void startAll(){
        listener.start();  
    }

    //envia uma mensagem
    public boolean sendMessage(Message message) {
        if (message.peerDispath()) { //tenta enviar mensagem
            //this.proccessOutgoingMessages();
            return true;
        } else { //não conseguiu enviar, coloca numa fila para ser enviado
            this.outgoingQueue.add(message);
            return false;
        }
    }

    //lê mensagem da fila de mensagens
    public Message readMessage() {
        return incomingQueue.poll();
    }
    
    //recebe mensagem da thread servidora, coloca na fila
    @Override
    public boolean addMessage(Message message){
        return this.incomingQueue.add(message);
    }
    
    //tenta reenviar mensagens que não foram enviadas ainda
    public void proccessOutgoingMessages(){
        Message message;
        while (!outgoingQueue.isEmpty()){
            message = outgoingQueue.poll();
            this.sendMessage(message);
        }
    }

    public int defaultPort = 1001;
    //envia para o servidor PS a requisição de assinar topico e titulo
    public boolean askSubscription(String server, String topic, String title){
        //TODO
        Socket socket = null;
        String[] addport = server.split(":");
        String address = addport[0];
        int port;
        if (addport.length<2)
            port = this.defaultPort;
        else
            port = Integer.parseInt(addport[1])+1;
        try {
            socket = new Socket(InetAddress.getByName(address), port);
            ObjectOutputStream oo = new ObjectOutputStream(socket.getOutputStream());
            //Send object over the network
            String msg = (this.port)+"%"+topic+"%"+title;
            oo.writeObject(msg);
            oo.flush();
            socket.close();
            return true;
        } catch (IOException ioe) {
            System.out.println(ioe);
            return false;
        }
    }
}
