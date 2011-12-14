/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messager;

import java.util.Random;
import messaging.Message;
import messaging.PSServer;
import messaging.Peer;
import testing.TopicTitle;

/**
 *
 * @author luisarmando
 */
public class PeerSample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here
         Random rand = new Random();
        System.out.println("Sending Message");
        Peer peer = new Peer((rand.nextInt()%900)+1000);
         Thread.sleep(1000);
        while (true) {            
            try {
                //System.in.
                peer.sendMessage(new Message("localhost", "teste"));
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Got it: " + peer.readMessage());
                break;
            }
        }
    }
}
