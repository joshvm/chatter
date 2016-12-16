package jvm.mohawk.chatter.server.command;

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
import jvm.mohawk.chatter.server.net.client.Client;

public interface Command {

    String key();

    String usage();

    String desc();

    void execute(final Client client,
                 final String text,
                 final List<String> responseStrings);

    default void onError(final Client client,
                         final String text,
                         final List<String> responseStrings,
                         final Exception err){
        err.printStackTrace();
        responseStrings.add("Error processing command: " + text);
    }
}
