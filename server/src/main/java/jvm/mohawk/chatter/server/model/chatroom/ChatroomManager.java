package jvm.mohawk.chatter.server.model.chatroom;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatroomManager {

    private final Map<Object, Chatroom> map;
    private final List<Chatroom> list;

    public ChatroomManager(){
        map = new HashMap<>();
        list = new ArrayList<>();
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

    public List<Chatroom> list(){
        return list;
    }

    public Chatroom forNameOrId(final Object idOrName){
        return map.get(idOrName);
    }

    public Chatroom forNameOrId(final Object idOrName, final Chatroom defaultValue){
        return map.getOrDefault(idOrName, defaultValue);
    }

    public boolean contains(final Chatroom chatroom){
        return forNameOrId(chatroom.id()) != null;
    }
}
