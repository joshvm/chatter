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
import jvm.mohawk.chatter.clientapi.data.event.types.AddFriendRequestEvent;
import jvm.mohawk.chatter.clientapi.model.friendship.FriendRequest;
import jvm.mohawk.chatter.clientapi.model.profile.Profile;
import jvm.mohawk.chatter.clientapi.net.Packet;

public class AddFriendRequestEventMapper implements EventMapper<AddFriendRequestEvent> {

    private final FriendRequest.Type type;

    public AddFriendRequestEventMapper(final FriendRequest.Type type){
        this.type = type;
    }

    @Override
    public AddFriendRequestEvent map(final ChatterClient client, final Packet pkt){
        final int friendRequestId = pkt.readInt();
        final Profile profile = Profile.deserialize(pkt);
        return new AddFriendRequestEvent(
            new FriendRequest(friendRequestId, type, profile)
        );
    }
}
