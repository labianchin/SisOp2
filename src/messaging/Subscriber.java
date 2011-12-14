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

    public Subscriber(String address) {
        this.address = address;
        this.subscription = new HashMap();
        this.lastTimestamp = new Timestamp(0);
    }

    public void subscribe(String topic, String title) {
        if (topic != null && topic.length() > 0) {
            if (!subscription.containsKey(topic)) {
                subscription.put(topic, new HashSet());
            }
            Set<String> topicList = subscription.get(topic);
            if (title != null && title.length() > 0) {
                topicList.add(title);
            } else {
                topicList.clear();
            }
        } else {
            subscription.clear();
        }
    }

    public String toString() {
        return this.address;
    }
}
