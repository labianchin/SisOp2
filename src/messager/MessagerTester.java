/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messager;

import messaging.Message;
import messaging.PSServer;
import messaging.Peer;
import testing.TopicTitle;

/**
 *
 * @author luisarmando
 */
public class MessagerTester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here
        Peer peer = new Peer();
        Thread.sleep(1000);
        System.out.println("Sending Message");
        peer.sendMessage(new Message("localhost", "teste"));
        peer.sendMessage(new Message("localhost", "teste2"));
        peer.sendMessage(new Message("localhost", "teste3"));
        peer.sendMessage(new Message("localhost", "teste4"));
        System.out.println("Sent");
        System.out.println("Now, let's read it");
        System.out.println("Got it: " + peer.readMessage());
        System.out.println("Got it: " + peer.readMessage());
        System.out.println("Got it: " + peer.readMessage());
        System.out.println("Got it: " + peer.readMessage());

        PSServer serv = new PSServer();
        serv.registerTopicTitles(TopicTitle.getTopicTitles());
        peer.sendMessage(new Message("localhost:2001", "teste"));
        peer.sendMessage(new Message("localhost:2001", "teste2"));
        peer.sendMessage(new Message("localhost:2001", "teste3"));
        peer.sendMessage(new Message("localhost:2001", "teste4"));
        Peer peer2 = new Peer(1002);
        Thread.sleep(1000);
        serv.subscribe("localhost:1002", "%");
        serv.dispatchToSubscribers();
        Thread.sleep(1000);
        System.out.println("Got it: " + peer2.readMessage());
        System.out.println("Got it: " + peer2.readMessage());
    }
}
