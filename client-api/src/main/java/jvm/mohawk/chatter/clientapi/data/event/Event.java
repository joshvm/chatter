package jvm.mohawk.chatter.clientapi.data.event;

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

import jvm.mohawk.chatter.clientapi.data.Data;

/**
 * this class represents an event (subclassed so 1 class per event)
 */
public class Event extends Data {

    private final EventType type;

    protected Event(final EventType type){
        this.type = type;
    }

    public EventType type(){
        return type;
    }
}
