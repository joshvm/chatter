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

import jvm.mohawk.chatter.clientapi.ChatterClient;
import jvm.mohawk.chatter.clientapi.data.request.Request;

/**
 *
 * this is the interface/callback that is used when you want to listen for specific responses
 *
 * @param <R1> the type of request
 * @param <R2> the type of response
 */
public interface ResponseListener<R1 extends Request, R2 extends Response> {

    void onResponse(final ChatterClient client,
                    final R1 request,
                    final R2 response);
}
