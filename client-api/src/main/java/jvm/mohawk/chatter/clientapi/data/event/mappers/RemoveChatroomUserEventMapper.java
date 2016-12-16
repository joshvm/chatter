package jvm.mohawk.chatter.clientapi.data.event.mappers;

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
import jvm.mohawk.chatter.clientapi.data.event.EventMapper;
import jvm.mohawk.chatter.clientapi.data.event.types.RemoveChatroomUserEvent;
import jvm.mohawk.chatter.clientapi.model.chatroom.Chatroom;
import jvm.mohawk.chatter.clientapi.model.chatroom.ChatroomUser;
import jvm.mohawk.chatter.clientapi.net.Packet;

public class RemoveChatroomUserEventMapper implements EventMapper<RemoveChatroomUserEvent> {

    @Override
    public RemoveChatroomUserEvent map(final ChatterClient client, final Packet pkt){
        final Chatroom chatroom = client.chatroomManager().forIdOrName(pkt.readInt());
        if(chatroom == null)
            return null;
        final ChatroomUser user = chatroom.users().forIdOrUser(pkt.readInt());
        if(user == null)
            return null;
        return new RemoveChatroomUserEvent(
                user
        );
    }
}
