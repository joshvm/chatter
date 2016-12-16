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
import jvm.mohawk.chatter.server.message.types.CreateFriendRequest;
import jvm.mohawk.chatter.server.model.profile.Profile;
import jvm.mohawk.chatter.server.net.Opcode;
import jvm.mohawk.chatter.server.net.Packet;
import jvm.mohawk.chatter.server.net.client.Client;


import static jvm.mohawk.chatter.server.Chatter.activeClients;
import static jvm.mohawk.chatter.server.Chatter.config;
import static jvm.mohawk.chatter.server.Chatter.database;

public class CreateFriendRequestListener implements ClientMessageListener<CreateFriendRequest> {

    private static final int TARGET_PROFILE_NOT_FOUND = 1;
    private static final int ALREADY_REQUESTED_WITH_TARGET_PROFILE = 2;
    private static final int CANT_ADD_YOURSELF = 3;
    private static final int ALREADY_FRIENDS_WITH_TARGET_PROFILE = 4;

    private static final int SUCCESS = 100;
    private static final int ERROR = 101;

    @Override
    public void onMessage(final Client client, final CreateFriendRequest msg) throws Exception{
        if(msg.targetProfileId() == client.profile().id()){
            client.writeResponse(Opcode.CREATE_FRIEND_REQUEST_RESPONSE, CANT_ADD_YOURSELF);
            return;
        }
        final Profile targetProfile = database.profiles().forId(msg.targetProfileId());
        if(targetProfile == null){
            client.writeResponse(Opcode.CREATE_FRIEND_REQUEST_RESPONSE, TARGET_PROFILE_NOT_FOUND);
            return;
        }
        if(database.friendships().between(client.profile().id(), targetProfile.id()) != null){
            client.writeResponse(Opcode.CREATE_FRIEND_REQUEST_RESPONSE, ALREADY_FRIENDS_WITH_TARGET_PROFILE);
            return;
        }
        final Client targetClient = activeClients.forIdOrUser(targetProfile.id());
        if(config.autoAcceptFriendRequests()){
            final int friendshipId = (int)database.friendships().add(client.profile().id(), targetProfile.id());
            if(friendshipId < 0)
                throw new SQLException("Error adding friendship");
            client.writeResponse(Opcode.CREATE_FRIEND_REQUEST_RESPONSE, SUCCESS);
            client.write(new Packet(Opcode.ADD_FRIEND).writeInt(friendshipId).writeBuffer(targetProfile.serialize()));
            if(targetClient != null)
                targetClient.write(new Packet(Opcode.ADD_FRIEND).writeInt(friendshipId).writeBuffer(client.profile().serialize()));
            final int cid = (int)database.logs().insert(client.profile().id(), String.format("Sent friend request to %s - auto accepted", targetProfile.user()));
            client.write(new Packet(Opcode.ADD_GENERAL_LOG, database.logs().forId(cid).serialize()));
            final int tid = (int)database.logs().insert(targetProfile.id(), String.format("Friend request received from %s - auto accepted", client.profile().user()));
            if(targetClient != null)
                targetClient.write(new Packet(Opcode.ADD_GENERAL_LOG, database.logs().forId(tid).serialize()));
        }else{
            if(database.friendships().requestWith(client.profile().id(), targetProfile.id()) != null){
                client.writeResponse(Opcode.CREATE_FRIEND_REQUEST_RESPONSE, ALREADY_REQUESTED_WITH_TARGET_PROFILE);
                return;
            }
            final int requestId = (int)database.friendships().addRequest(client.profile().id(), targetProfile.id());
            if(requestId < 0)
                throw new SQLException("Error adding friend request");
            client.writeResponse(Opcode.CREATE_FRIEND_REQUEST_RESPONSE, SUCCESS);
            client.write(new Packet(Opcode.ADD_PENDING_FRIEND_REQUEST).writeInt(requestId).writeBuffer(targetProfile.serialize()));
            if(targetClient != null)
                targetClient.write(new Packet(Opcode.ADD_ANSWERABLE_FRIEND_REQUEST).writeInt(requestId).writeBuffer(client.profile().serialize()));
            final int cid = (int)database.logs().insert(client.profile().id(), String.format("Sent friend request to %s", targetProfile.user()));
            client.write(new Packet(Opcode.ADD_GENERAL_LOG, database.logs().forId(cid).serialize()));
            final int lid = (int)database.logs().insert(targetProfile.id(), String.format("Friend request received from %s", client.profile().user()));
            if(targetClient != null)
                targetClient.write(new Packet(Opcode.ADD_GENERAL_LOG, database.logs().forId(lid).serialize()));
        }
    }

    @Override
    public void onError(final Client client, final CreateFriendRequest msg, final Throwable err){
        err.printStackTrace();
        client.writeResponse(Opcode.CREATE_FRIEND_REQUEST_RESPONSE, ERROR);
    }
}
