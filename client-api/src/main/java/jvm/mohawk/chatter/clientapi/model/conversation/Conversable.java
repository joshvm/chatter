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

/**
 * considering chat messages are applied to more than just friends, decided to make this interface
 * everywhere where chat messages could be sent, the target must be an implementation of this interface.
 * this is so that i could reuse a lot of conversation-tracking code instead of
 * creating different conversation handling code for friendships and chatrooms.
 */
public interface Conversable {

    int id();

    ChatMessage.Type messageType();

    Conversation conversation();
}
