/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messaging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Publish/Subscriber central server
 * @author luisarmando
 */
public class PSServer implements MessagesOrganizer {
    /* Listen for incomming messages */

    public PeerListener listener;
    public Collection<Message> queue;
    //public subscribers;
    public Map<String, Subscriber> subscribers;
    public Map<String, Map<String, Set<Message>>> messages;

    public PSServer() {
        this.queue = new PriorityBlockingQueue();
        this.subscribers = new HashMap<String, Subscriber>();
        this.messages = new ConcurrentHashMap<String, Map<String, Set<Message>>>();
        listener = new PeerListener((MessagesOrganizer) this);
        listener.start();
    }

    public Collection<Message> read(String topic, String title) {
        Collection<Message> list = new ArrayList<Message>();
        for (Message msg : queue) {
            if (((topic == null)
                    || msg.topic.equals(topic))
                    && ((title == null)
                    || (msg.title.equals(title)))) {
                list.add(msg);
            }
        }
        return list;
    }
    
    public Collection<Message> read(String topic){
        return read(topic, null);
    }
    
    public void subscribe(String address, String topic, String title){
        if (this.subscribers.containsKey(address)){
            Subscriber subscriber = this.subscribers.get(address);
            subscriber.subscribe(topic, title);
        } else {
            Subscriber subscriber = new Subscriber(address);
            subscriber.subscribe(topic, title);
            this.subscribers.put(address, subscriber);
        }
    }
    
    public boolean dispatchToSubscribers(){
        for (Subscriber subscriber : this.subscribers.values()){
            
        }
        return true;
    }
    
    @Override
    public boolean addMessage(Message message){
        if (this.messages.containsKey(message.topic)){
            Map<String, Set<Message>> topicList = this.messages.get(message.topic);
            if (topicList.containsKey(message.title)){
                Set<Message> messageList = topicList.get(message.title);
                messageList.add(message);
            } else {
                Set<Message> messageList = new CopyOnWriteArraySet<Message>();
                messageList.add(message);
                topicList.put(message.title, messageList);
            }
        } else {
            Map<String, Set<Message>> topicList = new ConcurrentHashMap<String, Set<Message>>();
            Set<Message> messageList = new CopyOnWriteArraySet<Message>();
            messageList.add(message);
            topicList.put(message.title, messageList);
            this.messages.put(message.topic, topicList);
        }
        return this.queue.add(message);
    }
}
