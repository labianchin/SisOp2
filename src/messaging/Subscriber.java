/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messaging;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author luisarmando
 */
public class Subscriber {
    public String address;
    public Map<String, Set<String>> subscription;
    public Date lastTimestamp;
    
    public Subscriber(String address){
        this.address = address;
        this.subscription = new HashMap();
    }
    
    public void subscribe(String topic, String title){
        if (subscription.containsKey(topic)) {
            Set<String> topicList = subscription.get(topic);
            topicList.add(title);
        } else {
            Set<String> topicList = new HashSet();
            topicList.add(title);
            subscription.put(topic, topicList);
        }
    }
}
