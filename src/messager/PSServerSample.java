/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messager;

import java.util.Random;
import messaging.PSServer;

/**
 *
 * @author labianchin
 */
public class PSServerSample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {

        Random rand = new Random();
        PSServer serv = new PSServer((rand.nextInt(999)) + 1001);
        Thread.sleep(1000);
        serv.subscribe("localhost:1002", "%");
        serv.dispatchToSubscribers();
        Thread.sleep(1000);
    }
}
