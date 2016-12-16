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
import jvm.mohawk.chatter.server.database.friendship.FriendshipDao;
import jvm.mohawk.chatter.server.message.ClientMessageListener;
import jvm.mohawk.chatter.server.message.types.AnswerFriendRequest;
import jvm.mohawk.chatter.server.model.friendship.FriendRequest;
import jvm.mohawk.chatter.server.model.profile.Profile;
import jvm.mohawk.chatter.server.net.Opcode;
import jvm.mohawk.chatter.server.net.Packet;
import jvm.mohawk.chatter.server.net.client.Client;


import static jvm.mohawk.chatter.server.Chatter.activeClients;
import static jvm.mohawk.chatter.server.Chatter.database;

public class AnswerFriendRequestListener implements ClientMessageListener<AnswerFriendRequest> {

    private static final int FRIEND_REQUEST_NOT_FOUND = 1;
    private static final int INVALID_ANSWER_ID = 2;

    private static final int NOT_TARGET_PROFILE = 3;

    private static final int SUCCESS = 100;
    private static final int ERROR = 101;

    @Override
    public void onMessage(final Client client, final AnswerFriendRequest msg) throws Exception{
        final AnswerFriendRequest.Answer answer = AnswerFriendRequest.Answer.forId(msg.answerId());
        if(answer == null){
            client.writeResponse(Opcode.ANSWER_FRIEND_REQUEST_RESPONSE, INVALID_ANSWER_ID);
            return;
        }
        final FriendRequest request = database.friendships().requestForId(msg.friendRequestId());
        if(request == null){
            client.writeResponse(Opcode.ANSWER_FRIEND_REQUEST_RESPONSE, FRIEND_REQUEST_NOT_FOUND);
            return;
        }
        if(request.targetProfileId() != client.profile().id()){
            client.writeResponse(Opcode.ANSWER_FRIEND_REQUEST_RESPONSE, NOT_TARGET_PROFILE);
            return;
        }
        final FriendshipDao dao = database.openFriendships();
        try{
            dao.begin();
            if(dao.deleteRequest(request.id()) < 1)
                throw new SQLException("Error deleting friend request");
            final Profile requesterProfile = database.profiles().forId(request.requesterProfileId());
            final Client requesterClient = activeClients.forIdOrUser(requesterProfile.id());
            if(answer == AnswerFriendRequest.Answer.ACCEPT){
                final int id = (int)dao.add(request.requesterProfileId(), request.targetProfileId());
                if(id < 0)
                    throw new SQLException("Error adding friend");
                dao.commit();
                client.writeResponse(Opcode.ANSWER_FRIEND_REQUEST_RESPONSE, SUCCESS);
                client.write(new Packet(Opcode.ADD_FRIEND).writeInt(id).writeBuffer(requesterProfile.serialize()));
                if(requesterClient != null)
                    requesterClient.write(new Packet(Opcode.ADD_FRIEND).writeInt(id).writeBuffer(client.profile().serialize()));
            } else dao.commit();
            client.writeResponse(Opcode.ANSWER_FRIEND_REQUEST_RESPONSE, SUCCESS);
            client.write(new Packet(Opcode.REMOVE_ANSWERABLE_FRIEND_REQUEST).writeInt(request.id()));
            if(requesterClient != null)
                requesterClient.write(new Packet(Opcode.REMOVE_PENDING_FRIEND_REQUEST).writeInt(request.id()));
            final int sid = (int)database.logs().insert(client.profile().id(), String.format("Answered %s's friend request: %s", requesterProfile.user(), answer));
            client.write(new Packet(Opcode.ADD_GENERAL_LOG, database.logs().forId(sid).serialize()));
            final int rid = (int)database.logs().insert(requesterProfile.id(), String.format("%s responded to your friend request: %s", client.profile().user(), answer));
            if(requesterClient != null)
                requesterClient.write(new Packet(Opcode.ADD_GENERAL_LOG, database.logs().forId(rid).serialize()));
        }catch(Exception ex){
            dao.rollback();
            throw ex;
        }finally{
            dao.close();
        }
    }

    @Override
    public void onError(final Client client, final AnswerFriendRequest msg, final Throwable err){
        err.printStackTrace();
        client.writeResponse(Opcode.ANSWER_FRIEND_REQUEST_RESPONSE, ERROR);
    }
}
