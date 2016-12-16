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

public class RemoveFriend extends Message {

    private final int friendshipId;

    public RemoveFriend(final Timestamp timestamp,
                        final int friendshipId){
        super(MessageType.REMOVE_FRIEND, timestamp);
        this.friendshipId = friendshipId;
    }

    public int friendshipId(){
        return friendshipId;
    }


}
