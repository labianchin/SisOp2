/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messaging;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author luisarmando
 */
public class PeerListener extends Thread {

    private static int port = 1001; /* port the server listens on */


    public void run() {
        ServerSocket server = null;
        try {
            server = new ServerSocket(port); /* start listening on the port */
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port);
            System.err.println(e);
            System.exit(1);
        }

        Socket client = null;
        try {
            client = server.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.err.println(e);
            System.exit(1);
        }


        try {
            /* obtain an input stream to the client *//*
            BufferedReader in = new BufferedReader(new InputStreamReader(
            client.getInputStream()));*/

            ObjectInputStream oi =
                    new ObjectInputStream(client.getInputStream());

            //Read serialized object
            Message message = (Message) oi.readObject();
            System.out.println("MyDate = " + message);

        } catch (ClassNotFoundException cnfe) {
            System.out.println(cnfe);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
}
