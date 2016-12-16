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
import jvm.mohawk.chatter.clientapi.model.Rank;
import jvm.mohawk.chatter.clientapi.model.profile.Profile;

public class ChatroomUserManager {

    private final Chatroom chatroom;

    private final Map<Object, ChatroomUser> users;
    private final ObservableList<ChatroomUser> list;

    public ChatroomUserManager(final Chatroom chatroom){
        this.chatroom = chatroom;

        users = new HashMap<>();
        list = FXCollections.observableArrayList();
    }

    public Map<Object, ChatroomUser> map(){
        return users;
    }

    public ObservableList<ChatroomUser> list(){
        return list;
    }

    public void add(final Profile profile, final Rank rank){
        add(new ChatroomUser(chatroom, profile, rank));
    }

    public void add(final ChatroomUser user){
        if(!chatroom.equals(user.chatroom()))
            throw new IllegalArgumentException("chatrooms don't match");
        if(forIdOrUser(user.profile().id()) == null)
            list.add(user);
        users.put(user.profile().id(), user);
        users.put(user.profile().user(), user);
    }

    public void remove(final ChatroomUser user){
        users.remove(user.profile().id());
        users.remove(user.profile().user());
        list.remove(user);
    }

    public ChatroomUser forIdOrUser(final Object idOrUser){
        return users.get(idOrUser);
    }
}
