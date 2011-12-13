/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messaging;

import java.io.*;
import java.net.*;
import java.util.concurrent.PriorityBlockingQueue;

/**
 *
 * @author luisarmando
 */
public class Peer implements MessagesOrganizer {

    public PeerListener listener;
    public PriorityBlockingQueue<Message> queue;

    public Peer() {
        this.queue = new PriorityBlockingQueue();
        listener = new PeerListener((MessagesOrganizer)this);
        listener.start();
    }

    //http://lycog.com/java/tcp-object-transmission-java/
    public void sendMessage(Message message) {
        message.peerDispath();
    }

    public Message readMessage() {
        return queue.poll();
    }
    
    @Override
    public boolean addMessage(Message message){
        return this.queue.add(message);
    }
}
