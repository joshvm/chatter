package jvm.mohawk.chatter.server.message;

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

public enum MessageType {

    REGISTER,
    LOGIN,
    KICK,
    CREATE_FRIEND_REQUEST,
    CANCEL_PENDING_FRIEND_REQUEST,
    ANSWER_FRIEND_REQUEST,
    REMOVE_FRIEND,
    SEND_CHAT_MESSAGE,
    GET_PROFILE_BY_USER,
    CREATE_CHATROOM,
    JOIN_CHATROOM,
    LEAVE_CHATROOM,
    GET_CHATROOM_BY_NAME,
    SEND_CHATROOM_MESSAGE,
    EXECUTE_COMMAND,
    UPDATE_PROFILE_STATUS,
    RECOVER_ACCOUNT_REQUEST,
    TRY_RECOVER_ACCOUNT

}
