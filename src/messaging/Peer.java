/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messaging;

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
    
    //envia para o servidor PS a requisição de assinar topico e titulo
    public void askSubscription(String topic, String title){
        //TODO
        
    }
}
