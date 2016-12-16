package jvm.mohawk.chatter.server.model.conversation;

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
import jvm.mohawk.chatter.server.net.Buffer;

public class ChatMessage {

    private final Timestamp timestamp;
    private final int id;

    private final ChatMessageType type;
    private final int conversableId;

    private final int senderId;
    private final String message;

    public ChatMessage(final Timestamp timestamp,
                       final int id,
                       final ChatMessageType type,
                       final int conversableId,
                       final int senderId,
                       final String message){
        this.timestamp = timestamp;
        this.id = id;
        this.type = type;
        this.conversableId = conversableId;
        this.senderId = senderId;
        this.message = message;
    }

    public Timestamp timestamp(){
        return timestamp;
    }

    public int id(){
        return id;
    }

    public ChatMessageType type(){
        return type;
    }

    public int conversableId(){
        return conversableId;
    }

    public int senderId(){
        return senderId;
    }

    public String message(){
        return message;
    }

    public Buffer serialize(){
        return new Buffer()
                .writeInt(id)
                .writeLong(timestamp.getTime())
                .writeByte(type.id())
                .writeInt(conversableId)
                .writeInt(senderId)
                .writeString(message);
    }
}
