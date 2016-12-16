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
import jvm.mohawk.chatter.server.utils.Utils;

public class Friendship {

    private final Timestamp timestamp;
    private final int id;

    private final int profile1Id;
    private final int profile2Id;

    public Friendship(final Timestamp timestamp,
                      final int id,
                      final int profile1Id,
                      final int profile2Id){
        this.timestamp = timestamp;
        this.id = id;
        this.profile1Id = profile1Id;
        this.profile2Id = profile2Id;
    }

    public Timestamp timestamp(){
        return timestamp;
    }

    public int id(){
        return id;
    }

    public int profile1Id(){
        return profile1Id;
    }

    public int profile2Id(){
        return profile2Id;
    }

    public int otherProfileId(final int checkProfileId){
        return Utils.other(checkProfileId, profile1Id, profile2Id);
    }
}
