/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messaging;

import java.sql.Timestamp;
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
    public Timestamp lastTimestamp;
    
    public Subscriber(String address){
        this.address = address;
        this.subscription = new HashMap();
        this.lastTimestamp = new Timestamp(0);
    }
    
    public void subscribe(String topic, String title){
        if (topic != null)
            if (subscription.containsKey(topic)) {
                Set<String> topicList = subscription.get(topic);
                topicList.add(title);
            } else {
                Set<String> topicList = new HashSet();
                subscription.put(topic, topicList);
                if (title!=null)
                    topicList.add(title);
            }
    }
}
