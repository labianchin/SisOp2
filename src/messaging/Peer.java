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
public class Peer {

    public PeerListener listener;

    public Peer() {
        listener = new PeerListener();
        listener.start();
    }

    public void sendMessage(Message message) {
        Socket socket = null;
        try {

            socket = new Socket(InetAddress.getByName("127.0.0.1"), 1001);

            ObjectOutputStream oo = new ObjectOutputStream(socket.getOutputStream());

            //Send object over the network
            oo.writeObject(message);
            oo.flush();
            /*ObjectInputStream oi = new ObjectInputStream(socket.getInputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("FROM SERVER: " + inFromServer.readLine());*/
            socket.close();

        } catch (IOException ioe) {
            System.out.println(ioe);
        }

        /*
        
        // The default port	
        
        int port_number = 2222;
        String host = "localhost";
        Socket clientSocket = null;
        PrintStream os = null;
        DataInputStream is = null;
        BufferedReader inputLine = null;
        
        // Initialization section:
        // Try to open a socket on a given host and port
        // Try to open input and output streams
        try {
        clientSocket = new Socket(host, port_number);
        inputLine = new BufferedReader(new InputStreamReader(System.in));
        os = new PrintStream(clientSocket.getOutputStream());
        is = new DataInputStream(clientSocket.getInputStream());
        } catch (UnknownHostException e) {
        System.err.println("Don't know about host " + host);
        } catch (IOException e) {
        System.err.println("Couldn't get I/O for the connection to the host " + host);
        }
        
        // If everything has been initialized then we want to write some data
        // to the socket we have opened a connection to on port port_number 
        
        if (clientSocket != null && os != null && is != null) {
        try {
        
        // Create a thread to read from the server
        String responseLine;
        
        // Keep on reading from the socket till we receive the "Bye" from the server,
        // once we received that then we want to break.
        try {
        while ((responseLine = is.readLine()) != null) {
        System.out.println(responseLine);
        if (responseLine.indexOf("*** Bye") != -1) {
        break;
        }
        }
        os.println(inputLine.readLine());
        } catch (IOException e) {
        System.err.println("IOException:  " + e);
        }
        
        // Clean up:
        // close the output stream
        // close the input stream
        // close the socket
        
        os.close();
        is.close();
        clientSocket.close();
        } catch (IOException e) {
        System.err.println("IOException:  " + e);
        }
        }*/
    }

    public Message readMessage() {
        return null;
    }
}
