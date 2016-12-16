package jvm.mohawk.chatter.server.model.log;

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

public class Log {

    private final int id;
    private final Timestamp timestamp;
    private final int profileId;

    public Log(final int id,
               final Timestamp timestamp,
               final int profileId){
        this.id = id;
        this.timestamp = timestamp;
        this.profileId = profileId;
    }

    public int id(){
        return id;
    }

    public Timestamp timestamp(){
        return timestamp;
    }

    public int profileId(){
        return profileId;
    }
}
