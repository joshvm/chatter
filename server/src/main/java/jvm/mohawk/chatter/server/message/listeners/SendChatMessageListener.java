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

import jvm.mohawk.chatter.server.message.ClientMessageListener;
import jvm.mohawk.chatter.server.message.types.SendChatMessage;
import jvm.mohawk.chatter.server.model.chatroom.Chatroom;
import jvm.mohawk.chatter.server.model.conversation.ChatMessage;
import jvm.mohawk.chatter.server.model.conversation.ChatMessageType;
import jvm.mohawk.chatter.server.model.friendship.Friendship;
import jvm.mohawk.chatter.server.model.profile.Profile;
import jvm.mohawk.chatter.server.net.Opcode;
import jvm.mohawk.chatter.server.net.Packet;
import jvm.mohawk.chatter.server.net.client.Client;


import static jvm.mohawk.chatter.server.Chatter.activeChatrooms;
import static jvm.mohawk.chatter.server.Chatter.activeClients;
import static jvm.mohawk.chatter.server.Chatter.database;

public class SendChatMessageListener implements ClientMessageListener<SendChatMessage> {

    private static final int BAD_TYPE = 1;
    private static final int BAD_MESSAGE_TEXT = 2;

    private static final int INVALID_FRIENDSHIP = 3;
    private static final int BAD_FRIENDSHIP = 4;

    private static final int INVALID_CHATROOM = 3;
    private static final int NOT_IN_CHATROOM = 4;

    private static final int MUTED = 5;

    private static final int SUCCESS = 100;
    private static final int ERROR = 101;

    @Override
    public void onMessage(final Client client, final SendChatMessage msg) throws Exception {
        final ChatMessageType type = ChatMessageType.forId(msg.typeId());
        if(type == null){
            client.writeResponse(Opcode.SEND_CHAT_MESSAGE_RESPONSE, BAD_TYPE);
            return;
        }
        if(msg.text().isEmpty()){
            client.writeResponse(Opcode.SEND_CHAT_MESSAGE_RESPONSE, BAD_MESSAGE_TEXT);
            return;
        }
        switch(type){
            case FRIEND:
                sendFriendMessage(client, msg);
                break;
            case CHATROOM:
                sendChatroomMessage(client, msg);
                break;
        }
    }

    private void sendFriendMessage(final Client client,
                                   final SendChatMessage msg){
        final Friendship friendship = database.friendships().forId(msg.conversableId());
        if(friendship == null){
            client.writeResponse(Opcode.SEND_CHAT_MESSAGE_RESPONSE, INVALID_FRIENDSHIP);
            return;
        }
        if(client.profile().id() != friendship.profile1Id() && client.profile().id() != friendship.profile2Id()){
            client.writeResponse(Opcode.SEND_CHAT_MESSAGE_RESPONSE, BAD_FRIENDSHIP);
            return;
        }
        if(client.muted()){
            client.writeResponse(Opcode.SEND_CHAT_MESSAGE_RESPONSE, MUTED);
            return;
        }
        client.friendshipConvoManager().load(friendship.id());
        final int id = (int) database.conversations().insert(
                ChatMessageType.FRIEND, friendship.id(), client.profile().id(), msg.text()
        );
        final ChatMessage chatMsg = database.conversations().forId(id);
        client.writeResponse(Opcode.SEND_CHAT_MESSAGE_RESPONSE, SUCCESS);
        client.write(new Packet(Opcode.ADD_CHAT_MESSAGE, chatMsg.serialize()));
        final Profile targetProfile = database.profiles().forId(friendship.otherProfileId(client.profile().id()));
        final int cid = (int)database.logs().insert(client.profile().id(), String.format("Sent message to friend %s: %s", targetProfile.user(), msg.text()));
        client.write(new Packet(Opcode.ADD_GENERAL_LOG, database.logs().forId(cid).serialize()));
        final int tid = (int)database.logs().insert(targetProfile.id(), String.format("Received message from friend %s: %s", client.profile().user(), msg.text()));
        final Client targetClient = activeClients.forProfile(targetProfile);
        if(targetClient != null){
            targetClient.write(new Packet(Opcode.ADD_CHAT_MESSAGE, chatMsg.serialize()));
            targetClient.write(new Packet(Opcode.ADD_GENERAL_LOG, database.logs().forId(tid).serialize()));
        }
    }

    private void sendChatroomMessage(final Client client,
                                     final SendChatMessage msg){
        final Chatroom chatroom = activeChatrooms.forNameOrId(msg.conversableId());
        if(chatroom == null){
            client.writeResponse(Opcode.SEND_CHAT_MESSAGE_RESPONSE, INVALID_CHATROOM);
            return;
        }
        if(!chatroom.clients().contains(client) || !client.chatrooms().contains(chatroom)){
            client.writeResponse(Opcode.SEND_CHAT_MESSAGE_RESPONSE, NOT_IN_CHATROOM);
            return;
        }
        if(chatroom.muted(client.profile().id())){
            client.writeResponse(Opcode.SEND_CHAT_MESSAGE_RESPONSE, MUTED);
            return;
        }
        // TODO: 2016-12-02 add the missing profile sender ids to the chatroom so we can see full convo instead of just our msgs...
        client.chatroomConvoManager().load(chatroom.id());
        final int id = (int) database.conversations().insert(ChatMessageType.CHATROOM, chatroom.id(), client.profile().id(), msg.text());
        final ChatMessage chatMsg = database.conversations().forId(id);
        client.writeResponse(Opcode.SEND_CHAT_MESSAGE_RESPONSE, SUCCESS);
        client.write(new Packet(Opcode.ADD_CHAT_MESSAGE, chatMsg.serialize()));
        chatroom.clients().list().stream()
                .filter(c -> !c.equals(client))
                .forEach(c -> {
                    System.out.println("sending chatroom msg to: " + c.profile().user());
                    c.write(new Packet(Opcode.ADD_CHAT_MESSAGE, chatMsg.serialize()));
                });
    }

    @Override
    public void onError(final Client client, final SendChatMessage msg, final Throwable err){
        err.printStackTrace();
        client.writeResponse(Opcode.SEND_CHAT_MESSAGE_RESPONSE, ERROR);
    }
}
