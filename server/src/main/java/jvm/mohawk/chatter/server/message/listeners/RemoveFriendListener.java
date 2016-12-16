package jvm.mohawk.chatter.server.message.listeners;

/*
  Capstone Project - Chatter
  
  An instant messenger that lets you connect with your friends 
  and meet new people with similar interests.
  
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import java.sql.SQLException;
import jvm.mohawk.chatter.server.message.ClientMessageListener;
import jvm.mohawk.chatter.server.message.types.RemoveFriend;
import jvm.mohawk.chatter.server.model.friendship.Friendship;
import jvm.mohawk.chatter.server.model.profile.Profile;
import jvm.mohawk.chatter.server.net.Opcode;
import jvm.mohawk.chatter.server.net.Packet;
import jvm.mohawk.chatter.server.net.client.Client;


import static jvm.mohawk.chatter.server.Chatter.activeClients;
import static jvm.mohawk.chatter.server.Chatter.database;

public class RemoveFriendListener implements ClientMessageListener<RemoveFriend> {

    private static final int BAD_FRIENDSHIP_ID = 1;
    private static final int NOT_OWNER_FRIENDSHIP = 2;

    private static final int SUCCESS = 100;
    private static final int ERROR = 101;

    @Override
    public void onMessage(final Client client, final RemoveFriend msg) throws Exception{
        final Friendship friendship = database.friendships().forId(msg.friendshipId());
        if(friendship == null){
            client.writeResponse(Opcode.REMOVE_FRIEND_RESPONSE, BAD_FRIENDSHIP_ID);
            return;
        }
        if(friendship.profile1Id() != client.profile().id()
                && friendship.profile2Id() != client.profile().id()){
            client.writeResponse(Opcode.REMOVE_FRIEND_RESPONSE, NOT_OWNER_FRIENDSHIP);
            return;
        }
        if(database.friendships().delete(friendship.id()) < 1)
            throw new SQLException("Error deleting friendship");
        client.writeResponse(Opcode.REMOVE_FRIEND_RESPONSE, SUCCESS);
        client.write(new Packet(Opcode.REMOVE_FRIEND).writeInt(msg.friendshipId()));
        final Profile targetProfile = database.profiles().forId(friendship.otherProfileId(client.profile().id()));
        final int id = (int)database.logs().insert(client.profile().id(), String.format("Deleted friend: %s", targetProfile.user()));
        client.write(new Packet(Opcode.ADD_GENERAL_LOG, database.logs().forId(id).serialize()));
        final int tid = (int)database.logs().insert(targetProfile.id(), String.format("%s deleted you from their friend's list", client.profile().user()));
        final Client targetClient = activeClients.forProfile(targetProfile);
        if(targetClient != null){
            targetClient.write(new Packet(Opcode.REMOVE_FRIEND).writeInt(msg.friendshipId()));
            targetClient.write(new Packet(Opcode.ADD_GENERAL_LOG, database.logs().forId(tid).serialize()));
        }
    }

    @Override
    public void onError(final Client client, final RemoveFriend msg, final Throwable err){
        err.printStackTrace();
        client.writeResponse(Opcode.REMOVE_FRIEND_RESPONSE, ERROR);
    }
}
