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

public enum Rank {

    NONE(1),
    HELPER(2),
    MODERATOR(3),
    ADMINISTRATOR(4),
    OWNER(5);

    public static final Map<Integer, Rank> MAP = Utils.map(values(), Rank::id);

    private final int id;

    Rank(final int id){
        this.id = id;
    }

    public int id(){
        return id;
    }

    public static Rank forId(final int id){
        return MAP.get(id);
    }
}
