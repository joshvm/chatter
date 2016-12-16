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
import jvm.mohawk.chatter.clientapi.model.chatroom.Chatroom;
import jvm.mohawk.chatter.clientapi.model.conversation.ChatMessage;

public class ChatroomMessageEvent extends Event {

    private final Chatroom chatroom;
    private final ChatMessage message;

    public ChatroomMessageEvent(final Chatroom chatroom,
                                final ChatMessage message){
        super(EventType.CHATROOM_MESSAGE);
        this.chatroom = chatroom;
        this.message = message;
    }

    public Chatroom chatroom(){
        return chatroom;
    }

    public ChatMessage message(){
        return message;
    }
}
