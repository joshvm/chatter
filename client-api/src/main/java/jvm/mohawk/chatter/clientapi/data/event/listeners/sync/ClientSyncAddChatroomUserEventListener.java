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
import jvm.mohawk.chatter.clientapi.data.event.listeners.AddChatroomUserEventListener;
import jvm.mohawk.chatter.clientapi.data.event.types.AddChatroomUserEvent;

public class ClientSyncAddChatroomUserEventListener implements AddChatroomUserEventListener {

    @Override
    public void onEvent(final ChatterClient client, final AddChatroomUserEvent e){
        e.user().chatroom().users().add(e.user());
    }
}
