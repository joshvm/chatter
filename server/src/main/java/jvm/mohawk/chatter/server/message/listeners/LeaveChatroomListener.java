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
import jvm.mohawk.chatter.server.message.types.LeaveChatroom;
import jvm.mohawk.chatter.server.model.chatroom.Chatroom;
import jvm.mohawk.chatter.server.net.Opcode;
import jvm.mohawk.chatter.server.net.Packet;
import jvm.mohawk.chatter.server.net.client.Client;


import static jvm.mohawk.chatter.server.Chatter.activeChatrooms;
import static jvm.mohawk.chatter.server.Chatter.database;

public class LeaveChatroomListener implements ClientMessageListener<LeaveChatroom> {

    private static final int CHATROOM_NOT_FOUND = 1;
    private static final int NOT_IN_CHATROOM = 2;

    private static final int SUCCESS = 100;
    private static final int ERROR = 101;

    @Override
    public void onMessage(final Client client, final LeaveChatroom msg) throws Exception{
        final Chatroom chatroom = activeChatrooms.forNameOrId(msg.chatroomId());
        if(chatroom == null){
            client.writeResponse(Opcode.LEAVE_CHATROOM_RESPONSE, CHATROOM_NOT_FOUND);
            return;
        }
        if(!chatroom.clients().contains(client) || !client.chatrooms().contains(chatroom)){
            client.writeResponse(Opcode.LEAVE_CHATROOM_RESPONSE, NOT_IN_CHATROOM);
            return;
        }
        final int id = (int)database.logs().insert(client.profile().id(), String.format("Left chatroom: %s", chatroom.name()));
        client.write(new Packet(Opcode.ADD_GENERAL_LOG, database.logs().forId(id).serialize()));
        client.writeResponse(Opcode.LEAVE_CHATROOM_RESPONSE, SUCCESS);
        client.write(new Packet(Opcode.REMOVE_CHATROOM).writeInt(chatroom.id()));
        client.chatrooms().remove(chatroom);
        chatroom.clients().remove(client);
        chatroom.clients().list().forEach(
                c -> {
                    c.write(
                            new Packet(Opcode.REMOVE_CHATROOM_USER)
                                .writeInt(chatroom.id())
                                .writeInt(client.profile().id())
                    );
                }
        );
    }

    @Override
    public void onError(final Client client, final LeaveChatroom msg, final Throwable err){
        err.printStackTrace();
        client.writeResponse(Opcode.LEAVE_CHATROOM_RESPONSE, ERROR);
    }
}
