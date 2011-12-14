/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messaging;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;

/**
 * Contem informações sobre uma mensagem.
 * Mensagem são serializadas e enviadas.
 * Na instanciação da mensagem é definido o timestamp de envio.
 * No recebimento a mensagem é "carimbada" com o timestamp de recebimento.
 * @author luisarmando
 */
public class Message implements Serializable, Comparable<Message> {
    
    public String contents;
    public String from;
    public String to;
    public Date timestampSender;
    public Date timestampReciever;
    public String title = "";
    public String topic = "";
    public int priority = 0;
    
    public Message(String to, String contents){
        this.timestampSender = Calendar.getInstance().getTime();
        this.to = to;
        this.contents = contents;
    }
    
    public Message(String contents){
        this.timestampSender = Calendar.getInstance().getTime();
        this.contents = contents;
    }
    
    @Override
    public String toString(){
        return this.from + ": " + this.contents;
    }
    
    public void stampRecieve(String sender){
        this.from = sender;
        this.timestampReciever = Calendar.getInstance().getTime();
    }
    
    @Override
    public int compareTo(Message message){
        if (this.priority > message.priority)
            return 1;
        else if (this.priority < message.priority)
            return -1;
        else
            return this.timestampReciever.compareTo(message.timestampReciever);
    }
    
    public static int defaultPort = 1001;
    //http://lycog.com/java/tcp-object-transmission-java/
    public boolean dispatchTo(String to){
        Socket socket = null;
        String[] addport = to.split(":");
        String address = addport[0];
        int port;
        if (addport.length<2)
            port = this.defaultPort;
        else
            port = Integer.parseInt(addport[1]);
        try {
            socket = new Socket(InetAddress.getByName(address), port);
            ObjectOutputStream oo = new ObjectOutputStream(socket.getOutputStream());
            //Send object over the network
            oo.writeObject(this);
            oo.flush();
            socket.close();
            return true;
        } catch (IOException ioe) {
            System.out.println(ioe);
            return false;
        }
    }
    
    public boolean peerDispath(){
        return this.dispatchTo(this.to);
    }
    
    public Message buildReply(String replyContents){
        Message reply = new Message(this.from, replyContents);
        reply.title = this.title;
        reply.topic = this.topic;
        return reply;
    }
}
