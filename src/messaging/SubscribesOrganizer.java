/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messaging;

/**
 *
 * @author luisarmando
 */
public interface SubscribesOrganizer {
    public void subscribe(String address, String topic, String title);
    public void subscribe(String address, String subscription);
}
