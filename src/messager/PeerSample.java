/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import messaging.Message;
import messaging.Peer;

/**
 *
 * @author luisarmando
 */
public class PeerSample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        // TODO code application logic here
        Random rand = new Random();
        Peer peer = new Peer((rand.nextInt(800)) + 1025);
        Thread.sleep(1000);

        InputStreamReader inp = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(inp);
        String defaultHost = "localhost:1001";
        System.out.println("Possible inputs \"read\", \"resend\", \"default:<address>:<port>\", \"subscribe:topic/title\", address:port/message or address:port/topic/title/message");
        while (true) {
            try {
                //System.in.
                System.out.print("> ");
                String str = in.readLine();
                if (str.equals("read")) {
                    System.out.println("Read one message: " + peer.readMessage());
                } else if (str.equals("resend")) {
                    peer.proccessOutgoingMessages();
                } else if (str.startsWith("default:")) {
                    defaultHost = str.split(":", 2)[1];
                    if (!defaultHost.contains(":"))
                        defaultHost = "localhost:"+defaultHost;
                    System.out.println("Default host set to: "+defaultHost);
                } else if (str.startsWith("subscribe:")) {
                    String[] topicTitle = str.split(":", 2)[1].split("/",2);
                    if (peer.askSubscription(defaultHost, topicTitle[0], topicTitle[1]))
                        System.out.println("Subscription sent");
                    else
                        System.out.println("Error sending subscription");
                } else {
                    String[] strs = str.split("/");
                    Boolean sent = false;
                    if (strs[0].contains(":")) {
                        if (strs.length >= 4) {
                            sent = peer.sendMessage(new Message(strs[0], strs[1], strs[2], strs[3]));
                        } else {
                            sent = peer.sendMessage(new Message(strs[0], strs[1]));
                        }
                    } else {
                        if (strs.length >= 3) {
                            sent = peer.sendMessage(new Message(defaultHost, strs[0], strs[1], strs[2]));
                        } else {
                            sent = peer.sendMessage(new Message(defaultHost, strs[0]));
                        }
                    }

                    if (sent){
                        System.out.println("Message sent");
                    } else {
                        System.out.println("Error sending message");
                    }


                    Thread.sleep(1000);

                }
            } catch (InterruptedException e) {
                System.out.println("Got it: " + peer.readMessage());
            } catch (IOException e) {
                System.out.println(e);
                continue;
            }
        }
    }
}
