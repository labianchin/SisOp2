/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messaging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Publish/Subscriber central server
 * @author luisarmando
 */
public class PSServer {
    /* Listen for incomming messages */

    public PeerListener listener;
    public Collection<Message> queue;

    public PSServer() {
        this.queue = new PriorityBlockingQueue();
        listener = new PeerListener((Queue) queue);
        listener.start();
    }

    public Collection<Message> read(String topic, String title) {
        Collection<Message> list = new ArrayList<Message>();
        for (Message msg : queue) {
            if (((topic == null)
                    || msg.topic == topic)
                    && ((title == null)
                    || (msg.title == title))) {
                list.add(msg);
            }
        }
        return list;
    }
    
    public Collection<Message> read(String topic){
        return read(topic, null);
    }
}
