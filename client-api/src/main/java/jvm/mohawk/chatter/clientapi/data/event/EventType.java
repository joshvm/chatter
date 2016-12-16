package jvm.mohawk.chatter.clientapi.data.event;

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

public enum EventType {

    ADD_FRIEND,
    REMOVE_FRIEND,
    ADD_FRIEND_REQUEST,
    REMOVE_FRIEND_REQUEST,
    FRIEND_MESSAGE,
    ADD_CHATROOM,
    ADD_CHATROOM_USER,
    REMOVE_CHATROOM,
    REMOVE_CHATROOM_USER,
    CHATROOM_MESSAGE,
    ADD_COMMAND_MESSAGE,
    SET_PROFILE_STATUS,
    ADD_LOGIN_LOG,
    ADD_GENERAL_LOG
}
