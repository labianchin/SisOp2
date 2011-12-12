/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messaging;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;

/**
 *
 * @author luisarmando
 */
public class PeerListener extends Thread {

    private static int port = 1001; /* port the server listens on */
    public Queue<Message> queue;
    
    public PeerListener(Queue queue){
        this.queue = queue;
    }


    @Override
    public void run() {
        ServerSocket server = null;

            try {
                server = new ServerSocket(port); /* start listening on the port */
            } catch (IOException e) {
                System.err.println("Could not listen on port: " + port);
                System.err.println(e);
                System.exit(1);
            }
            System.out.println("Serving at port " + port);

        while (true) {
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
                message.stampRecieve(client.getInetAddress().getHostName());
                queue.add(message);
                System.out.println("Listener recieved Message: " + message);
                /*DataOutputStream outToClient = new DataOutputStream(client.getOutputStream());
                outToClient.writeBytes("ok hehe");*/

            } catch (ClassNotFoundException cnfe) {
                System.out.println(cnfe);
            } catch (IOException ioe) {
                System.out.println(ioe);
            }
        }
    }
}
