package jvm.mohawk.chatter.clientapi.data.request;

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

import jvm.mohawk.chatter.clientapi.data.SendableData;

/**
 * represents a single request (subclassed so 1 request per type) that can be sent
 * requests are also capable of producing events
 */
public abstract class Request extends SendableData {

    private final RequestType type;

    protected Request(final RequestType type){
        this.type = type;
    }

    public RequestType type(){
        return type;
    }
}
