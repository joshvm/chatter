package jvm.mohawk.chatter.server.model.profile;

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

public class ProfileRegistrationDetails {

    private final int profileId;
    private final Timestamp timestamp;

    private final String ip;
    private final String device;

    public ProfileRegistrationDetails(final int profileId,
                                      final Timestamp timestamp,
                                      final String ip,
                                      final String device){
        this.profileId = profileId;
        this.timestamp = timestamp;
        this.ip = ip;
        this.device = device;
    }

    public int profileId(){
        return profileId;
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
