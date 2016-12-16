package jvm.mohawk.chatter.clientapi.data.event.listeners.sync;

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
import jvm.mohawk.chatter.clientapi.data.event.listeners.SetProfileStatusEventListener;
import jvm.mohawk.chatter.clientapi.data.event.types.SetProfileStatusEvent;

public class ClientSyncSetProfileStatusEventListener implements SetProfileStatusEventListener {

    @Override
    public void onEvent(final ChatterClient client, final SetProfileStatusEvent e){
        if(e.profileId() == client.profile().id())
            client.profile().status(e.status());
        client.friendshipManager().list()
                .stream()
                .filter(f -> f.profile().id() == e.profileId())
                .forEach(f -> f.profile().status(e.status()));
        client.chatroomManager().list()
                .forEach(c -> {
                    c.users().list()
                            .stream()
                            .filter(u -> u.profile().id() == e.profileId())
                            .forEach(u -> u.profile().status(e.status()));
                });
    }
}
