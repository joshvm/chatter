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
import jvm.mohawk.chatter.clientapi.model.chatroom.ChatroomUser;

public class RemoveChatroomUserEvent extends Event {

    private final ChatroomUser user;

    public RemoveChatroomUserEvent(final ChatroomUser user){
        super(EventType.REMOVE_CHATROOM_USER);
        this.user = user;
    }

    public ChatroomUser user(){
        return user;
    }
}
