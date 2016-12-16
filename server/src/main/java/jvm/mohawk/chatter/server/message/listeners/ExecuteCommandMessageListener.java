package jvm.mohawk.chatter.server.message.listeners;

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

import jvm.mohawk.chatter.server.command.CommandHandler;
import jvm.mohawk.chatter.server.message.ClientMessageListener;
import jvm.mohawk.chatter.server.message.types.ExecuteCommandMessage;
import jvm.mohawk.chatter.server.net.client.Client;

public class ExecuteCommandMessageListener implements ClientMessageListener<ExecuteCommandMessage> {

    @Override
    public void onMessage(final Client client, final ExecuteCommandMessage msg) throws Exception{
        CommandHandler.execute(client, msg.command());
    }
}
