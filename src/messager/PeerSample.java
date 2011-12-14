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
        Peer peer = new Peer((rand.nextInt(999)) + 1001);
        Thread.sleep(1000);

        InputStreamReader inp = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(inp);
        while (true) {
            try {
                //System.in.
                System.out.println("Enter \"read\", \"resend\", address:port/message or address:port/topic/title/message");
                String str = in.readLine();
                if (str.equals("read")) {
                    System.out.println("Read one message: " + peer.readMessage());
                } else if (str.equals("resend")) {
                    peer.proccessOutgoingMessages();
                } else {
                    String[] strs = str.split("/");
                    if (strs.length >= 4) {
                        peer.sendMessage(new Message(strs[0], strs[1], strs[2], strs[3]));
                    } else {
                        peer.sendMessage(new Message(strs[0], strs[1]));
                    }
                    Thread.sleep(1000);

                }
            } catch (InterruptedException e) {
                System.out.println("Got it: " + peer.readMessage());
                break;
            }
        }
    }
}
