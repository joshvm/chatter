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
import jvm.mohawk.chatter.server.message.types.CancelPendingFriendRequest;
import jvm.mohawk.chatter.server.model.friendship.FriendRequest;
import jvm.mohawk.chatter.server.model.profile.Profile;
import jvm.mohawk.chatter.server.net.Opcode;
import jvm.mohawk.chatter.server.net.Packet;
import jvm.mohawk.chatter.server.net.client.Client;


import static jvm.mohawk.chatter.server.Chatter.activeClients;
import static jvm.mohawk.chatter.server.Chatter.database;

public class CancelPendingFriendRequestListener implements ClientMessageListener<CancelPendingFriendRequest> {

    private static final int FRIEND_REQUEST_NOT_FOUND = 1;
    private static final int NOT_REQUESTED_BY_CLIENT = 2;

    private static final int SUCCESS = 100;
    private static final int ERROR = 101;

    @Override
    public void onMessage(final Client client, final CancelPendingFriendRequest msg) throws Exception{
        final FriendRequest request = database.friendships().requestForId(msg.friendRequestId());
        if(request == null){
            client.writeResponse(Opcode.CANCEL_PENDING_FRIEND_REQUEST_RESPONSE, FRIEND_REQUEST_NOT_FOUND);
            return;
        }
        if(request.requesterProfileId() != client.profile().id()){
            client.writeResponse(Opcode.CANCEL_PENDING_FRIEND_REQUEST_RESPONSE, NOT_REQUESTED_BY_CLIENT);
            return;
        }
        if(database.friendships().deleteRequest(request.id()) < 1)
            throw new SQLException("Error deleting request");
        client.writeResponse(Opcode.CANCEL_PENDING_FRIEND_REQUEST_RESPONSE, SUCCESS);
        client.write(new Packet(Opcode.REMOVE_PENDING_FRIEND_REQUEST).writeInt(request.id()));
        final Profile targetProfile = database.profiles().forId(request.targetProfileId());
        final Client targetClient = activeClients.forProfile(targetProfile);
        if(targetClient != null)
            targetClient.write(new Packet(Opcode.REMOVE_ANSWERABLE_FRIEND_REQUEST).writeInt(request.id()));
        final int id = (int)database.logs().insert(client.profile().id(), String.format("Cancelled friend request to %s", targetProfile.user()));
        client.write(new Packet(Opcode.ADD_GENERAL_LOG, database.logs().forId(id).serialize()));
        final int tid = (int)database.logs().insert(targetProfile.id(), String.format("%s cancelled their friend request to you", client.profile().user()));
        if(targetClient != null)
            targetClient.write(new Packet(Opcode.ADD_GENERAL_LOG, database.logs().forId(tid).serialize()));
    }

    @Override
    public void onError(final Client client, final CancelPendingFriendRequest msg, final Throwable err){
        err.printStackTrace();
        client.writeResponse(Opcode.CANCEL_PENDING_FRIEND_REQUEST_RESPONSE, ERROR);
    }
}
