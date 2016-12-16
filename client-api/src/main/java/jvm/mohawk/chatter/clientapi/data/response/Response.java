package jvm.mohawk.chatter.clientapi.data.response;

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
import jvm.mohawk.chatter.clientapi.data.request.RequestType;

/**
 *
 * This class represents a response (1 type of response for each type)
 *
 * @param <C> the type of response code
 */
public class Response<C extends Response.Code> extends Data {

    public interface Code {

        int id();
    }

    private final RequestType type;
    private final C code;

    protected Response(final RequestType type,
                    final C code){
        this.type = type;
        this.code = code;
    }

    public RequestType type(){
        return type;
    }

    public C code(){
        return code;
    }
}
