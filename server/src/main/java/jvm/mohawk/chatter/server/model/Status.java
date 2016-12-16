package jvm.mohawk.chatter.server.model;

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

import java.util.Map;
import jvm.mohawk.chatter.server.utils.Utils;

public enum Status {

    ONLINE(1),
    BUSY(2),
    AWAY(3),
    OFFLINE(4);

    public static final Map<Integer, Status> MAP = Utils.map(values(), Status::id);

    private final int id;

    Status(final int id){
        this.id = id;
    }

    public int id(){
        return id;
    }

    public static Status forId(final int id){
        return MAP.get(id);
    }
}
