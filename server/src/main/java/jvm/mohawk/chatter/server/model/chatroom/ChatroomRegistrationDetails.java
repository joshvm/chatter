package jvm.mohawk.chatter.server.model.chatroom;

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

import java.sql.Timestamp;

public class ChatroomRegistrationDetails {

    private final int chatroomId;
    private final Timestamp timestamp;
    private final int ownerProfileId;

    public ChatroomRegistrationDetails(final int chatroomId,
                                       final Timestamp timestamp,
                                       final int ownerProfileId){
        this.chatroomId = chatroomId;
        this.timestamp = timestamp;
        this.ownerProfileId = ownerProfileId;
    }

    public int chatroomId(){
        return chatroomId;
    }

    public Timestamp timestamp(){
        return timestamp;
    }

    public int ownerProfileId(){
        return ownerProfileId;
    }

}
