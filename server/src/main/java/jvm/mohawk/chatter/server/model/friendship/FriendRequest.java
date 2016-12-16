package jvm.mohawk.chatter.server.model.friendship;

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

public class FriendRequest {

    private final Timestamp timestamp;
    private final int id;

    private final int requesterProfileId;
    private final int targetProfileId;

    public FriendRequest(final Timestamp timestamp,
                         final int id,
                         final int requesterProfileId,
                         final int targetProfileId){
        this.timestamp = timestamp;
        this.id = id;
        this.requesterProfileId = requesterProfileId;
        this.targetProfileId = targetProfileId;
    }

    public Timestamp timestamp(){
        return timestamp;
    }

    public int id(){
        return id;
    }

    public int requesterProfileId(){
        return requesterProfileId;
    }

    public int targetProfileId(){
        return targetProfileId;
    }
}
