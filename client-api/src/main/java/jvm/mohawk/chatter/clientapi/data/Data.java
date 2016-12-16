package jvm.mohawk.chatter.clientapi.data;

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
import jvm.mohawk.chatter.clientapi.utils.Utils;

/**
 * this is the base data class, all packets are mapped to some subclass of this.
 *
 */
public class Data {

    private final Timestamp timestamp;

    protected Data(){
        timestamp = Utils.timestamp();
    }

    public Timestamp timestamp(){
        return timestamp;
    }
}
