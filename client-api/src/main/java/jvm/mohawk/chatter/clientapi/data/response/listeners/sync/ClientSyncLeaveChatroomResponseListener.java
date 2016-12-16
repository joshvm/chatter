package jvm.mohawk.chatter.clientapi.data.response.listeners.sync;

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
import jvm.mohawk.chatter.clientapi.data.event.types.RemoveChatroomEvent;
import jvm.mohawk.chatter.clientapi.data.request.types.LeaveChatroom;
import jvm.mohawk.chatter.clientapi.data.response.listeners.LeaveChatroomResponseListener;
import jvm.mohawk.chatter.clientapi.data.response.types.LeaveChatroomResponse;

public class ClientSyncLeaveChatroomResponseListener implements LeaveChatroomResponseListener {

    @Override
    public void onResponse(final ChatterClient client, final LeaveChatroom req, final LeaveChatroomResponse resp){
        if(resp.code() == LeaveChatroomResponse.Code.SUCCESS)
            client.eventHandler().fire(client, new RemoveChatroomEvent(req.chatroom()));
    }
}
