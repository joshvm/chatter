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
import jvm.mohawk.chatter.server.net.Buffer;

public class GeneralLog extends Log{

    private final String data;

    public GeneralLog(final int id,
                      final Timestamp timestamp,
                      final int profileId,
                      final String data){
        super(id, timestamp, profileId);
        this.data = data;
    }

    public String data(){
        return data;
    }

    public Buffer serialize(){
        return new Buffer()
                .writeLong(timestamp().getTime())
                .writeString(data);
    }
}
