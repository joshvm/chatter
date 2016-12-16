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
import jvm.mohawk.chatter.server.message.types.SearchChatroomByName;
import jvm.mohawk.chatter.server.model.chatroom.Chatroom;
import jvm.mohawk.chatter.server.net.Opcode;
import jvm.mohawk.chatter.server.net.Packet;
import jvm.mohawk.chatter.server.net.client.Client;


import static jvm.mohawk.chatter.server.Chatter.activeChatrooms;
import static jvm.mohawk.chatter.server.Chatter.database;
import static jvm.mohawk.chatter.server.model.chatroom.ChatroomValidation.isValidName;

public class GetChatroomByNameListener implements ClientMessageListener<SearchChatroomByName> {

    private static final int INVALID_NAME = 1;
    private static final int CHATROOM_NOT_FOUND = 2;

    private static final int SUCCESS = 100;
    private static final int ERROR = 101;

    @Override
    public void onMessage(final Client client, final SearchChatroomByName msg) throws Exception{
        if(!isValidName(msg.searchName())){
            client.writeResponse(Opcode.GET_CHATROOM_BY_NAME_RESPONSE, INVALID_NAME);
            return;
        }
        final Chatroom searchChatroom = activeChatrooms.forNameOrId(msg.searchName(), database.chatrooms().forName(msg.searchName()));
        if(searchChatroom == null){
            client.writeResponse(Opcode.GET_CHATROOM_BY_NAME_RESPONSE, CHATROOM_NOT_FOUND);
            return;
        }
        client.write(
                new Packet(Opcode.GET_CHATROOM_BY_NAME_RESPONSE)
                    .writeByte(SUCCESS)
                    .writeBuffer(searchChatroom.serialize())
        );
    }

    @Override
    public void onError(final Client client, final SearchChatroomByName msg, final Throwable err){
        err.printStackTrace();
        client.writeResponse(Opcode.GET_CHATROOM_BY_NAME_RESPONSE, ERROR);
    }
}
