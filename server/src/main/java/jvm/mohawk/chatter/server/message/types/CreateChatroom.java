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

public class CreateChatroom extends Message {

    private final String name;
    private final String desc;

    public CreateChatroom(final Timestamp timestamp,
                          final String name,
                          final String desc){
        super(MessageType.CREATE_CHATROOM, timestamp);
        this.name = name;
        this.desc = desc;
    }

    public String name(){
        return name;
    }

    public String desc(){
        return desc;
    }
}
