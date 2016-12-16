package jvm.mohawk.chatter.server.message;

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

public class Message {

    private final MessageType type;
    private final Timestamp timestamp;

    protected Message(final MessageType type,
                      final Timestamp timestamp){
        this.type = type;
        this.timestamp = timestamp;
    }

    public MessageType type(){
        return type;
    }

    public Timestamp timestamp(){
        return timestamp;
    }
}
