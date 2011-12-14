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

    private int port = 1001; /* port the server listens on */

    public MessagesOrganizer organizer;

    public PeerListener(MessagesOrganizer organizer) {
        this.organizer = organizer; //guarda o objeto q deve ser chamado para salvar mensagens
    }
    private ServerSocket server = null;

    @Override
    public void run() {

        try { //cria o servidor
            server = new ServerSocket(getPort()); /* start listening on the port */
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + getPort());
            System.err.println(e);
            System.exit(1);
        }
        System.out.println("Serving at hostname " + server.getInetAddress().getHostName() + " at port " + getPort());


        while (true) {
            Socket client = null;
            try {
                client = server.accept(); //recebe conexão do cliente
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.err.println(e);
                System.exit(1);
            }


            try {
                ObjectInputStream oi =
                        new ObjectInputStream(client.getInputStream());
                while (client.isConnected()) { //enquanto possue conexão ativa, recebe mensangens
                    //Read serialized object
                    Message message = (Message) oi.readObject(); //recebe mensagem do buffer
                    message.stampRecieve(client.getInetAddress().getHostName()+":"+client.getPort()); //carimba mensagem
                    organizer.addMessage(message);//pede para salvar mensagem (na fila)

                    System.out.println(
                            "Listener ("
                            + (server.getInetAddress().getHostName() + ":" + server.getLocalPort())
                            + ") recieved Message: " + message);
                }

            } catch (ClassNotFoundException cnfe) {
                System.out.println(cnfe);
            } catch (java.io.EOFException bla) {
                ;
            } catch (IOException ioe) {
                System.out.println(ioe);
            }
        }
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    public synchronized boolean getIsAlive() {
        return (server != null && server.isBound());
    }
}
