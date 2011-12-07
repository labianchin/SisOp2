/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messaging;

import java.io.Serializable;

/**
 *
 * @author luisarmando
 */
public class Message implements Serializable {
    
    public String contents;
    public String from;
    public String to;
    public int timestamp;
    public String title = null;
    public String topic = null;
    
    Message(String to, String contents){
        this.to = to;
        this.contents = contents;
    }
    
    @Override
    public String toString(){
        return this.from + ": " + this.contents;
    }
}
