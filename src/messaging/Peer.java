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
        listener = new PeerListener((MessagesOrganizer)this);
        listener.start();
    }

    public boolean sendMessage(Message message) {
        if (message.peerDispath())
            return true;
        else { //não conseguiu enviar, coloca numa fila para ser enviado
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
    
    public void proccessOutgoingMessages(){
        Message message = outgoingQueue.poll();
        while (message!=null){
            this.sendMessage(message);
            message = outgoingQueue.poll();
        }
    }
}
