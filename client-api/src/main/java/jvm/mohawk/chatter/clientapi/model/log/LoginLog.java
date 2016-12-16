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
 * this class specifically represents a login log
 * the initial intention was to have a log type enum to distinguish log data but kind of skipped out on that
 * as it would really only be beneficial if i did some sort of data processing based on the type
 */
public class LoginLog {

    private final Timestamp timestamp;
    private final String ip;
    private final String device;

    public LoginLog(final Timestamp timestamp,
                    final String ip,
                    final String device){
        this.timestamp = timestamp;
        this.ip = ip;
        this.device = device;
    }

    public Timestamp timestamp(){
        return timestamp;
    }

    public String ip(){
        return ip;
    }

    public String device(){
        return device;
    }
}
