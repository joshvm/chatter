package jvm.mohawk.chatter.clientapi.model.log;

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

import java.sql.Timestamp;

/**
 * this class represents a single general purpose log
 */
public class GeneralLog {

    private final Timestamp timestamp;
    private final String data;

    public GeneralLog(final Timestamp timestamp,
                      final String data){
        this.timestamp = timestamp;
        this.data = data;
    }

    public Timestamp timestamp(){
        return timestamp;
    }

    public String data(){
        return data;
    }

}
