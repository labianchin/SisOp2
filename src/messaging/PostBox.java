/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messaging;

import java.io.*;
import java.net.*;

/**
 *
 * @author luisarmando
 */
public class PostBox {
    // Declaration section:
    // declare a server socket and a client socket for the server
    // declare an input and an output stream

    static Socket clientSocket = null;
    static ServerSocket serverSocket = null;
    // This chat server can accept up to 10 clients' connections
    static PostBoxThread t[] = new PostBoxThread[10];

    public static void main(String args[]) {

        // The default port

        int port_number = 2222;

        if (args.length < 1) {
            System.out.println("Usage: java MultiThreadChatServer \n"
                    + "Now using port number=" + port_number);
        } else {
            port_number = Integer.valueOf(args[0]).intValue();
        }

        // Initialization section:
        // Try to open a server socket on port port_number (default 2222)
        // Note that we can't choose a port less than 1023 if we are not
        // privileged users (root)

        try {
            serverSocket = new ServerSocket(port_number);
        } catch (IOException e) {
            System.out.println(e);
        }

        // Create a socket object from the ServerSocket to listen and accept 
        // connections.
        // Open input and output streams for this socket will be created in 
        // client's thread since every client is served by the server in
        // an individual thread

        while (true) {
            try {
                clientSocket = serverSocket.accept();
                for (int i = 0; i <= 9; i++) {
                    if (t[i] == null) {
                        (t[i] = new PostBoxThread(clientSocket, t)).start();
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
