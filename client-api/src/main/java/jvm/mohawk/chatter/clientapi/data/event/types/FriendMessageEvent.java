package jvm.mohawk.chatter.clientapi.data.event.types;

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

import jvm.mohawk.chatter.clientapi.data.event.Event;
import jvm.mohawk.chatter.clientapi.data.event.EventType;
import jvm.mohawk.chatter.clientapi.model.conversation.ChatMessage;
import jvm.mohawk.chatter.clientapi.model.friendship.Friendship;

public class FriendMessageEvent extends Event {

    private final Friendship friendship;
    private final ChatMessage message;

    public FriendMessageEvent(final Friendship friendship,
                              final ChatMessage message){
        super(EventType.FRIEND_MESSAGE);
        this.friendship = friendship;
        this.message = message;
    }

    public Friendship friendship(){
        return friendship;
    }

    public ChatMessage message(){
        return message;
    }
}
