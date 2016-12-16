package jvm.mohawk.chatter.clientapi.data.request;

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

public enum RequestType {

    CREATE_FRIEND_REQUEST,
    CANCEL_FRIEND_REQUEST,
    ANSWER_FRIEND_REQUEST,
    REMOVE_FRIEND,
    SEND_CHAT_MESSAGE,
    GET_PROFILE_BY_USER,
    CREATE_CHATROOM,
    JOIN_CHATROOM,
    LEAVE_CHATROOM,
    GET_CHATROOM_BY_NAME,
    UPDATE_PROFILE_STATUS
}
