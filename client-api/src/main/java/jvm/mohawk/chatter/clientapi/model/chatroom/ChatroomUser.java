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

import javafx.beans.property.SimpleObjectProperty;
import jvm.mohawk.chatter.clientapi.model.Rank;
import jvm.mohawk.chatter.clientapi.model.profile.Profile;

public class ChatroomUser {

    private final Chatroom chatroom;
    private final Profile profile;
    private final SimpleObjectProperty<Rank> rank;

    public ChatroomUser(final Chatroom chatroom,
                        final Profile profile,
                        final Rank rank){
        this.chatroom = chatroom;
        this.profile = profile;
        this.rank = new SimpleObjectProperty<>(rank);
    }

    public Chatroom chatroom(){
        return chatroom;
    }

    public Profile profile(){
        return profile;
    }

    public SimpleObjectProperty<Rank> rankProperty(){
        return rank;
    }

    public Rank rank(){
        return rank.get();
    }

    public void rank(final Rank rank){
        this.rank.set(rank);
    }

    @Override
    public boolean equals(final Object obj){
        if(!(obj instanceof ChatroomUser))
            return false;
        final ChatroomUser u = (ChatroomUser) obj;
        return chatroom.equals(u.chatroom)
                && profile.equals(u.profile);
    }
}
