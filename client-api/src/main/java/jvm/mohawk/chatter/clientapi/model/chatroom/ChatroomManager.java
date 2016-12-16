package jvm.mohawk.chatter.clientapi.model.chatroom;

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

import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ChatroomManager {

    private final Map<Object, Chatroom> map;
    private final ObservableList<Chatroom> list;

    public ChatroomManager(){
        map = new HashMap<>();
        list = FXCollections.observableArrayList();
    }

    public Map<Object, Chatroom> map(){
        return map;
    }

    public Chatroom forIdOrName(final Object idOrName){
        return map.get(idOrName);
    }

    public boolean contains(final Chatroom chatroom){
        return forIdOrName(chatroom.id()) != null;
    }

    public ObservableList<Chatroom> list(){
        return list;
    }

    public void add(final Chatroom chatroom){
        map.put(chatroom.id(), chatroom);
        map.put(chatroom.name(), chatroom);
        list.add(chatroom);
    }

    public void remove(final Chatroom chatroom){
        map.remove(chatroom.id());
        map.remove(chatroom.name());
        list.remove(chatroom);
    }
}
