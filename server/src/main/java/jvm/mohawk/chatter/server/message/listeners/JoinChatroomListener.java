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
import jvm.mohawk.chatter.server.message.types.JoinChatroom;
import jvm.mohawk.chatter.server.model.chatroom.Chatroom;
import jvm.mohawk.chatter.server.model.chatroom.ChatroomRank;
import jvm.mohawk.chatter.server.model.punishment.Punishment;
import jvm.mohawk.chatter.server.net.Opcode;
import jvm.mohawk.chatter.server.net.Packet;
import jvm.mohawk.chatter.server.net.client.Client;


import static jvm.mohawk.chatter.server.Chatter.activeChatrooms;
import static jvm.mohawk.chatter.server.Chatter.database;

public class JoinChatroomListener implements ClientMessageListener<JoinChatroom> {

    private static final int CHATROOM_DOES_NOT_EXIST = 1;
    private static final int ALREADY_IN_CHATROOM = 2;
    private static final int BANNED = 3;

    private static final int SUCCESS = 100;
    private static final int ERROR = 101;

    @Override
    public void onMessage(final Client client, final JoinChatroom msg) throws Exception{
        final Chatroom chatroom = activeChatrooms.forNameOrId(msg.chatroomId(), database.chatrooms().forId(msg.chatroomId()));
        if(chatroom == null){
            client.writeResponse(Opcode.JOIN_CHATROOM_RESPONSE, CHATROOM_DOES_NOT_EXIST);
            return;
        }
        if(chatroom.clients().contains(client) || client.chatrooms().contains(chatroom)){
            client.writeResponse(Opcode.JOIN_CHATROOM_RESPONSE, ALREADY_IN_CHATROOM);
            return;
        }
        if(chatroom.punishments().forVictim(client.profile().id()).stream().anyMatch(p -> p.type() == Punishment.Type.BAN)){
            client.writeResponse(Opcode.JOIN_CHATROOM_RESPONSE, BANNED);
            return;
        }
        final ChatroomRank rank = database.chatrooms().rankIn(chatroom.id(), client.profile().id());
        if(rank != null)
            chatroom.rank(client.profile(), rank.rank());
        if(!activeChatrooms.contains(chatroom))
            activeChatrooms.add(chatroom);
        final int id = (int)database.logs().insert(client.profile().id(), String.format("Joined chatroom: %s ", chatroom.name()));
        client.write(new Packet(Opcode.ADD_GENERAL_LOG, database.logs().forId(id).serialize()));
        client.writeResponse(Opcode.JOIN_CHATROOM_RESPONSE, SUCCESS);
//        client.write(new Packet(Opcode.ADD_CHATROOM, chatroom.serialize()));
        client.write(
                new Packet(Opcode.ADD_CHATROOM_USER)
                        .writeInt(chatroom.id())
                        .writeBuffer(client.profile().serialize())
                        .writeByte(chatroom.rankFor(client.profile()).id())
        );
        chatroom.clients().list()
                .forEach(c -> {
                    c.write(
                            new Packet(Opcode.ADD_CHATROOM_USER)
                                .writeInt(chatroom.id())
                                .writeBuffer(client.profile().serialize())
                                .writeByte(chatroom.rankFor(client.profile()).id())
                    );
                    System.out.printf("Adding chatroom user %s to %s%n", client.profile().user(), c.profile().user());
                    client.write(
                            new Packet(Opcode.ADD_CHATROOM_USER)
                                .writeInt(chatroom.id())
                                .writeBuffer(c.profile().serialize())
                                .writeByte(chatroom.rankFor(c.profile()).id())
                    );
                    System.out.printf("Adding chatroom user %s to %s%n", c.profile().user(), client.profile().user());
                });
        client.chatrooms().add(chatroom);
        chatroom.clients().add(client);
    }

    @Override
    public void onError(final Client client, final JoinChatroom msg, final Throwable err){
        err.printStackTrace();
        client.writeResponse(Opcode.JOIN_CHATROOM_RESPONSE, ERROR);
    }
}
