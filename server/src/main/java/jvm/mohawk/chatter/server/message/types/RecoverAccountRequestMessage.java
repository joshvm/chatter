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

import jvm.mohawk.chatter.server.message.Message;
import jvm.mohawk.chatter.server.message.MessageType;
import jvm.mohawk.chatter.server.utils.Utils;

public class RecoverAccountRequestMessage extends Message {

    private final String targetUser;

    public RecoverAccountRequestMessage(final String targetUser){
        super(MessageType.RECOVER_ACCOUNT_REQUEST, Utils.timestamp());
        this.targetUser = targetUser;
    }

    public String targetUser(){
        return targetUser;
    }
}
