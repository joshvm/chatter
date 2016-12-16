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

import jvm.mohawk.chatter.clientapi.ChatterClient;
import jvm.mohawk.chatter.clientapi.net.Packet;

/**
 * data mapper (1 for events, 1 for responses)
 * @param <D> the type of data (event/response)
 */
public interface DataMapper<D extends Data> {

    D map(final ChatterClient client, final Packet pkt);
}
