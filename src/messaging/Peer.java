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
    public Queue<Message> incomingQueue;
    public Queue<Message> outgoingQueue;
    public int port;

    public Peer() {
        this.incomingQueue = new PriorityBlockingQueue();
        this.outgoingQueue = new PriorityBlockingQueue();
        listener = new PeerListener((MessagesOrganizer)this);
        listener.start();
    }
    
    public Peer(int port) {
        this.incomingQueue = new PriorityBlockingQueue();
        this.outgoingQueue = new PriorityBlockingQueue();
        listener = new PeerListener((MessagesOrganizer)this);
        listener.setPort(port);
        this.port = port;
        listener.start();
    }

    public boolean sendMessage(Message message) {
        if (message.peerDispath()) {
            //this.proccessOutgoingMessages();
            return true;
        } else { //não conseguiu enviar, coloca numa fila para ser enviado
            this.outgoingQueue.add(message);
            return false;
        }
    }

    public Message readMessage() {
        return incomingQueue.poll();
    }
    
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
