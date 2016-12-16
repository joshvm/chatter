package jvm.mohawk.chatter.clientapi.model.conversation;

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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * manages a list of messages
 */
public class Conversation {

    private final ObservableList<ChatMessage> messages;

    public Conversation(){
        messages = FXCollections.observableArrayList();
    }

    public ObservableList<ChatMessage> messages(){
        return messages;
    }

    public void add(final ChatMessage message){
        messages.add(message);
    }

    public void remove(final ChatMessage message){
        messages.remove(message);
    }
}
