package jvm.mohawk.chatter.clientapi.model.friendship;

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

import jvm.mohawk.chatter.clientapi.model.conversation.ChatMessage;
import jvm.mohawk.chatter.clientapi.model.conversation.Conversable;
import jvm.mohawk.chatter.clientapi.model.conversation.Conversation;
import jvm.mohawk.chatter.clientapi.model.profile.Profile;

/**
 * this class represents a friendship between the client and another profile
 * friendships are distinguished by id (instead of profile id)
 * the reason why we don't need to store the client id here is because
 * in order to get an actual instance of this class you must have a chatter client instance.
 */
public class Friendship implements Conversable{

    private final int id;
    private final Profile profile;

    private final Conversation conversation;

    public Friendship(final int id,
                      final Profile profile){
        this.id = id;
        this.profile = profile;

        conversation = new Conversation();
    }

    public int id(){
        return id;
    }

    public Profile profile(){
        return profile;
    }

    @Override
    public ChatMessage.Type messageType(){
        return ChatMessage.Type.FRIENDSHIP;
    }

    @Override
    public Conversation conversation(){
        return conversation;
    }

    @Override
    public boolean equals(final Object obj){
        if(!(obj instanceof Friendship))
            return false;
        final Friendship f = (Friendship) obj;
        return id == f.id;
    }
}
