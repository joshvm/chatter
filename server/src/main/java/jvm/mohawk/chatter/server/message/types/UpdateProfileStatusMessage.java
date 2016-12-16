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

public class UpdateProfileStatusMessage extends Message {

    private final int statusId;

    public UpdateProfileStatusMessage(final int statusId){
        super(MessageType.UPDATE_PROFILE_STATUS, Utils.timestamp());
        this.statusId = statusId;
    }

    public int statusId(){
        return statusId;
    }
}
