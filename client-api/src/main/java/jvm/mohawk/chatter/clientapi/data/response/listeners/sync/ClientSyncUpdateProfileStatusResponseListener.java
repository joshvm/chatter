package jvm.mohawk.chatter.clientapi.data.response.listeners.sync;

/*
  Capstone Project - Chatter
  
  An instant messenger that lets you connect with your friends 
  and meet new people with similar interests.
  
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: desktop-client
  
  Developed By: Josh Maione (000320309)
*/

import jvm.mohawk.chatter.clientapi.ChatterClient;
import jvm.mohawk.chatter.clientapi.data.event.types.SetProfileStatusEvent;
import jvm.mohawk.chatter.clientapi.data.request.types.UpdateProfileStatusRequest;
import jvm.mohawk.chatter.clientapi.data.response.listeners.UpdateProfileStatusResponseListener;
import jvm.mohawk.chatter.clientapi.data.response.types.UpdateProfileStatusResponse;

public class ClientSyncUpdateProfileStatusResponseListener implements UpdateProfileStatusResponseListener {

    @Override
    public void onResponse(final ChatterClient client, final UpdateProfileStatusRequest req, final UpdateProfileStatusResponse resp){
        if(resp.code() == UpdateProfileStatusResponse.Code.SUCCESS)
            client.eventHandler().fireAll(client, new SetProfileStatusEvent(client.profile().id(), req.status()));
    }
}
