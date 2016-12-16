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
import jvm.mohawk.chatter.server.message.types.SearchProfileByUser;
import jvm.mohawk.chatter.server.model.profile.Profile;
import jvm.mohawk.chatter.server.net.Opcode;
import jvm.mohawk.chatter.server.net.Packet;
import jvm.mohawk.chatter.server.net.client.Client;


import static jvm.mohawk.chatter.server.Chatter.database;
import static jvm.mohawk.chatter.server.model.profile.ProfileValidation.isUserValid;

public class GetProfileByUserListener implements ClientMessageListener<SearchProfileByUser> {

    private static final int INVALID_USER = 1;
    private static final int USER_NOT_FOUND = 2;
    private static final int CANT_SEARCH_YOURSELF = 3;

    private static final int SUCCESS = 100;
    private static final int ERROR = 101;

    @Override
    public void onMessage(final Client client, final SearchProfileByUser msg) throws Exception{
        if(!isUserValid(msg.searchUser())){
            client.writeResponse(Opcode.GET_PROFILE_BY_USER_RESPONSE, INVALID_USER);
            return;
        }
        if(msg.searchUser().equalsIgnoreCase(client.profile().user())){
            client.writeResponse(Opcode.GET_PROFILE_BY_USER_RESPONSE, CANT_SEARCH_YOURSELF);
            return;
        }
        final Profile searchProfile = database.profiles().forUser(msg.searchUser());
        if(searchProfile == null){
            client.writeResponse(Opcode.GET_PROFILE_BY_USER_RESPONSE, USER_NOT_FOUND);
            return;
        }
        client.write(
                new Packet(Opcode.GET_PROFILE_BY_USER_RESPONSE)
                    .writeByte(SUCCESS)
                    .writeBuffer(searchProfile.serialize())
        );
    }

    @Override
    public void onError(final Client client, final SearchProfileByUser msg, final Throwable err){
        client.writeResponse(Opcode.GET_PROFILE_BY_USER_RESPONSE, ERROR);
    }
}
