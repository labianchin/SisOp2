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
import java.sql.Timestamp;
import java.util.Calendar;

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
    public Timestamp timestampSender;
    public Timestamp timestampReciever;
    public String title = "";
    public String topic = "";
    public int priority = 0;
    
    /* Cria uma nova mensagem, inicializa a timestamp de envio na data atual */
    public Message(String to, String contents){
        this.timestampSender = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());//Calendar.getInstance().getTime();
        this.to = to;
        this.contents = contents;
    }
    
    public Message(String contents){
        this.timestampSender = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
        this.contents = contents;
    }
    
    public Message(String to, String topic, String title, String contents){
        this.timestampSender = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());//Calendar.getInstance().getTime();
        this.to = to;
        this.topic = topic;
        this.title = title;
        this.contents = contents;
    }
    
    /* Mostra a mensagem como string */
    @Override
    public String toString(){
        String body = this.contents;
        if (this.title.length()>0 || this.topic.length()>0)
            body = "[" + this.topic + "]/" + "[" + this.title + "]: " + body;
        return this.from +
                "(" + this.timestampReciever + ")" +
                ": " + body;
    }
    
    /* Carimba recebimento na mensagem */
    public void stampRecieve(String sender){
        this.from = sender; //seta quem enviou a mensagem
        this.timestampReciever = //seta timestamp de recebimento
                new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
    }
    
    //Ordena mensagem pela prioridade e após pela timestamp
    @Override
    public int compareTo(Message message){
        if (this.priority > message.priority)
            return 1;
        else if (this.priority < message.priority)
            return -1;
        else //se prioridades das mensagens são iguais, verifica pela timestamp
            return this.timestampReciever.compareTo(message.timestampReciever);
    }
    
    public static int defaultPort = 1001;
    //http://lycog.com/java/tcp-object-transmission-java/
    public boolean dispatchTo(String to){
        Socket socket = null;
        String[] addport = to.split(":");
        String address = addport[0];
        int port;
        if (addport.length<2) //quebra a string de envio, no formato <ip:porta>
            port = this.defaultPort;
        else
            port = Integer.parseInt(addport[1]);
        try {
            socket = new Socket(InetAddress.getByName(address), port); //cria conexão
            ObjectOutputStream oo = new ObjectOutputStream(socket.getOutputStream());
            oo.writeObject(this); // envia a própria mensagem
            oo.flush();
            socket.close(); //fecha a conexão
            return true;
        } catch (IOException ioe) {
            System.out.println(ioe);
            return false;
        }
    }
    
    /* Envia a mensagem para o endereço registrado no destinatário dela */
    public boolean peerDispath(){
        return this.dispatchTo(this.to);
    }
    
    /* Constrói uma mensagem de réplica, usando mesmo titulo e tópicos */
    public Message buildReply(String replyContents){
        Message reply = new Message(this.from, replyContents);
        reply.title = this.title;
        reply.topic = this.topic;
        return reply;
    }
}
