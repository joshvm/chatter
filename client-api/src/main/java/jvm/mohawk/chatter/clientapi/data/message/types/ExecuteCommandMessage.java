package jvm.mohawk.chatter.clientapi.data.message.types;

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

import jvm.mohawk.chatter.clientapi.data.message.Message;
import jvm.mohawk.chatter.clientapi.data.message.MessageType;
import jvm.mohawk.chatter.clientapi.net.Opcode;
import jvm.mohawk.chatter.clientapi.net.Packet;

public class ExecuteCommandMessage extends Message {

    private final String command;

    public ExecuteCommandMessage(final String command){
        super(MessageType.EXECUTE_COMMAND);
        this.command = command;
    }

    public String command(){
        return command;
    }

    @Override
    public Packet serialize(){
        return new Packet(Opcode.EXECUTE_COMMAND)
                .writeString(command);
    }
}
