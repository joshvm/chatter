package jvm.mohawk.chatter.clientapi.data.response.listeners;

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

import jvm.mohawk.chatter.clientapi.data.request.types.CancelFriendRequest;
import jvm.mohawk.chatter.clientapi.data.response.ResponseListener;
import jvm.mohawk.chatter.clientapi.data.response.types.CancelFriendRequestResponse;

public interface CancelFriendRequestResponseListener extends ResponseListener<CancelFriendRequest, CancelFriendRequestResponse> {
}
