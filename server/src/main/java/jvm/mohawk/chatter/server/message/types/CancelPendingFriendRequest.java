package jvm.mohawk.chatter.server.message.types;

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
import jvm.mohawk.chatter.server.message.Message;
import jvm.mohawk.chatter.server.message.MessageType;

public class CancelPendingFriendRequest extends Message {

    private final int friendRequestId;

    public CancelPendingFriendRequest(final Timestamp timestamp,
                                      final int friendRequestId){
        super(MessageType.CANCEL_PENDING_FRIEND_REQUEST, timestamp);
        this.friendRequestId = friendRequestId;
    }

    public int friendRequestId(){
        return friendRequestId;
    }

}
