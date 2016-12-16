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

import java.util.Objects;
import jvm.mohawk.chatter.server.message.ClientMessageListener;
import jvm.mohawk.chatter.server.message.types.UpdateProfileStatusMessage;
import jvm.mohawk.chatter.server.model.Status;
import jvm.mohawk.chatter.server.net.Opcode;
import jvm.mohawk.chatter.server.net.Packet;
import jvm.mohawk.chatter.server.net.client.Client;


import static jvm.mohawk.chatter.server.Chatter.activeChatrooms;
import static jvm.mohawk.chatter.server.Chatter.activeClients;
import static jvm.mohawk.chatter.server.Chatter.database;

public class UpdateProfileStatusMessageListener implements ClientMessageListener<UpdateProfileStatusMessage> {

    private static final int INVALID_STATUS = 1;
    private static final int STATUS_ALREADY_SET = 2;

    private static final int SUCCESS = 100;
    private static final int ERROR = 101;

    @Override
    public void onMessage(final Client client, final UpdateProfileStatusMessage msg) throws Exception{
        final Status status = Status.forId(msg.statusId());
        if(status == null){
            client.writeResponse(Opcode.UPDATE_PROFILE_STATUS_RESPONSE, INVALID_STATUS);
            return;
        }
        if(client.profile().status() == status){
            client.writeResponse(Opcode.UPDATE_PROFILE_STATUS_RESPONSE, STATUS_ALREADY_SET);
            return;
        }
        client.profile().status(status);
        database.profiles().setStatus(client.profile().id(), status);
        final int id = (int)database.logs().insert(client.profile().id(), String.format("Updated status: %s", status));
        client.write(new Packet(Opcode.ADD_GENERAL_LOG, database.logs().forId(id).serialize()));
        client.writeResponse(Opcode.UPDATE_PROFILE_STATUS_RESPONSE, SUCCESS);
        database.friendships().involving(client.profile().id())
                .stream()
                .map(f -> f.otherProfileId(client.profile().id()))
                .map(activeClients::forIdOrUser)
                .filter(Objects::nonNull)
                .forEach(c -> c.write(new Packet(Opcode.SET_PROFILE_STATUS).writeInt(client.profile().id()).writeByte(status.id())));
        activeChatrooms.list()
                .stream()
                .filter(c -> c.clients().forIdOrUser(client.profile().id()) != null)
                .flatMap(c -> c.clients().list().stream().filter(cl -> cl.profile().id() != client.profile().id()))
                .forEach(c -> c.write(new Packet(Opcode.SET_PROFILE_STATUS).writeInt(client.profile().id()).writeByte(status.id())));
    }

    @Override
    public void onError(final Client client, final UpdateProfileStatusMessage msg, final Throwable err){
        err.printStackTrace();
        client.writeResponse(Opcode.UPDATE_PROFILE_STATUS_RESPONSE, ERROR);
    }
}
