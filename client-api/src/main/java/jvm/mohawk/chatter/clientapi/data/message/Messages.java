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

import jvm.mohawk.chatter.clientapi.data.message.types.ExecuteCommandMessage;

public final class Messages {

    private Messages(){}

    public static ExecuteCommandMessage executeCommand(final String command){
        return new ExecuteCommandMessage(command);
    }
}
