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
import jvm.mohawk.chatter.server.database.chatroom.ChatroomDao;
import jvm.mohawk.chatter.server.message.ClientMessageListener;
import jvm.mohawk.chatter.server.message.types.CreateChatroom;
import jvm.mohawk.chatter.server.model.Rank;
import jvm.mohawk.chatter.server.net.Opcode;
import jvm.mohawk.chatter.server.net.Packet;
import jvm.mohawk.chatter.server.net.client.Client;


import static jvm.mohawk.chatter.server.Chatter.database;
import static jvm.mohawk.chatter.server.model.chatroom.ChatroomValidation.isValidDesc;
import static jvm.mohawk.chatter.server.model.chatroom.ChatroomValidation.isValidName;

public class CreateChatroomListener implements ClientMessageListener<CreateChatroom> {

    private static final int INVALID_NAME = 1;
    private static final int INVALID_DESC = 2;

    private static final int NAME_TAKEN = 3;

    private static final int SUCCESS = 100;
    private static final int ERROR = 101;

    @Override
    public void onMessage(final Client client, final CreateChatroom msg) throws Exception{
        int response = SUCCESS;
        if(!isValidName(msg.name()))
            response = INVALID_NAME;
        else if(!isValidDesc(msg.desc()))
            response = INVALID_DESC;
        else if(database.chatrooms().forName(msg.name()) != null)
            response = NAME_TAKEN;
        if(response != SUCCESS){
            client.writeResponse(Opcode.CREATE_CHATROOM_RESPONSE, response);
            return;
        }
        final ChatroomDao dao = database.openChatrooms();
        try{
            dao.begin();
            final int id = (int) dao.add(msg.name(), msg.desc());
            if(id < 0)
                throw new SQLException("Error adding chatroom");
            if(dao.addRegistrationDetails(id, client.profile().id()) < 1)
                throw new SQLException("Error adding chatroom registration details");
            if(dao.addRank(id, client.profile().id(), Rank.OWNER) < 1)
                throw new SQLException("Error adding chatroom rank");
            dao.commit();
            client.writeResponse(Opcode.CREATE_CHATROOM_RESPONSE, SUCCESS);
            //presumably they would want to join the chatroom now maybe... no auto join for now
            final int lid = (int)database.logs().insert(client.profile().id(), String.format("Created chatroom: %s", msg.name()));
            client.write(new Packet(Opcode.ADD_GENERAL_LOG, database.logs().forId(lid).serialize()));
        }catch(Exception ex){
            dao.rollback();
            throw ex;
        }finally{
            dao.close();
        }
    }

    @Override
    public void onError(final Client client, final CreateChatroom msg, final Throwable err){
        err.printStackTrace();
        client.writeResponse(Opcode.CREATE_CHATROOM_RESPONSE, ERROR);
    }
}
