package jvm.mohawk.chatter.server.message.types;

/*
  Capstone Project - Chatter
  
  An instant messenger that lets you connect with your friends 
  and meet new people with similar interests.
  
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import java.sql.Timestamp;
import jvm.mohawk.chatter.server.message.Message;
import jvm.mohawk.chatter.server.message.MessageType;

public class SendChatMessage extends Message {

    private final int typeId;
    private final int conversableId;
    private final String text;

    public SendChatMessage(final Timestamp timestamp,
                           final int typeId,
                           final int conversableId,
                           final String text){
        super(MessageType.SEND_CHAT_MESSAGE, timestamp);
        this.typeId = typeId;
        this.conversableId = conversableId;
        this.text = text;
    }

    public int typeId(){
        return typeId;
    }

    public int conversableId(){
        return conversableId;
    }

    public String text(){
        return text;
    }

}
