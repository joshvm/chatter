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
import jvm.mohawk.chatter.clientapi.data.event.types.AddChatroomEvent;
import jvm.mohawk.chatter.clientapi.data.request.types.JoinChatroom;
import jvm.mohawk.chatter.clientapi.data.response.listeners.JoinChatroomResponseListener;
import jvm.mohawk.chatter.clientapi.data.response.types.JoinChatroomResponse;

public class ClientSyncJoinChatroomResponseListener implements JoinChatroomResponseListener{

    @Override
    public void onResponse(final ChatterClient client, final JoinChatroom req, final JoinChatroomResponse resp){
        if(resp.code() == JoinChatroomResponse.Code.SUCCESS)
            client.eventHandler().fireAll(client, new AddChatroomEvent(req.chatroom()));
    }
}
