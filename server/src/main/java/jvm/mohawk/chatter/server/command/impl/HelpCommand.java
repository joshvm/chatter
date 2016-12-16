package jvm.mohawk.chatter.server.command.impl;

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

import java.util.List;
import jvm.mohawk.chatter.server.command.Command;
import jvm.mohawk.chatter.server.command.CommandHandler;
import jvm.mohawk.chatter.server.net.client.Client;

public class HelpCommand implements Command {

    @Override
    public String key(){
        return "help";
    }

    @Override
    public String desc(){
        return "Lists all of the commands and their usage";
    }

    @Override
    public String usage(){
        return key();
    }

    @Override
    public void execute(final Client client, final String text, final List<String> responseStrings){
        for(final Command c : CommandHandler.commands())
            responseStrings.add(String.format("%s (%s)", c.usage(), c.desc()));
    }
}
