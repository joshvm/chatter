package jvm.mohawk.chatter.clientapi.data.message;

/*
  Capstone Project - Chatter
  
  An instant messenger that lets you connect with your friends 
  and meet new people with similar interests.
  
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

import jvm.mohawk.chatter.clientapi.data.SendableData;

/**
 * this class represents a message that can be sent (subclassed so 1 message per type)
 * the difference between messages and requests is that requests yield responses
 * and messages (generally) yield events (that don't need to be really waited for)
 */
public abstract class Message extends SendableData {

    private final MessageType type;

    protected Message(final MessageType type){
        this.type = type;
    }

    public MessageType type(){
        return type;
    }

}
