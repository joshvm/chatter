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
import jvm.mohawk.chatter.clientapi.data.event.types.AddChatroomUserEvent;
import jvm.mohawk.chatter.clientapi.model.Rank;
import jvm.mohawk.chatter.clientapi.model.chatroom.Chatroom;
import jvm.mohawk.chatter.clientapi.model.chatroom.ChatroomUser;
import jvm.mohawk.chatter.clientapi.model.profile.Profile;
import jvm.mohawk.chatter.clientapi.net.Packet;

public class AddChatroomUserEventMapper implements EventMapper<AddChatroomUserEvent> {

    @Override
    public AddChatroomUserEvent map(final ChatterClient client, final Packet pkt){
        final int id = pkt.readInt();
        final Profile profile = Profile.deserialize(pkt);
        final Rank rank = Rank.forId(pkt.readUnsignedByte());
        final Chatroom chatroom = client.chatroomManager().forIdOrName(id);
        System.out.printf("chatroom_id: %d | chatroom: %s%n", id, chatroom);
        if(chatroom == null)
            return null;
        return new AddChatroomUserEvent(
                new ChatroomUser(chatroom, profile, rank)
        );
    }
}
