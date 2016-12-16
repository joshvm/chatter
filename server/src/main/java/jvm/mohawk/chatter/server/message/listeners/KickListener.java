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

import io.netty.channel.ChannelHandlerContext;
import jvm.mohawk.chatter.server.message.MessageListener;
import jvm.mohawk.chatter.server.message.types.Kick;
import jvm.mohawk.chatter.server.model.profile.Profile;
import jvm.mohawk.chatter.server.net.Opcode;
import jvm.mohawk.chatter.server.net.Packet;
import jvm.mohawk.chatter.server.net.client.Client;
import jvm.mohawk.chatter.server.net.utils.NetUtils;


import static jvm.mohawk.chatter.server.Chatter.activeClients;
import static jvm.mohawk.chatter.server.Chatter.database;
import static jvm.mohawk.chatter.server.model.profile.ProfileValidation.isSecurityPinValid;
import static jvm.mohawk.chatter.server.model.profile.ProfileValidation.isUserValid;
import static jvm.mohawk.chatter.server.net.utils.NetUtils.writeResponse;

public class KickListener implements MessageListener<Kick> {

    private static final int INVALID_USER = 1;
    private static final int INVALID_SECURITY_PIN = 2;

    private static final int PROFILE_NOT_FOUND = 3;
    private static final int PIN_INCORRECT = 4;

    private static final int CLIENT_NOT_ONLINE = 5;

    private static final int SUCCESS = 100;
    private static final int ERROR = 101;

    @Override
    public void onMessage(final ChannelHandlerContext ctx, final Kick msg){
        int response = SUCCESS;
        if(!isUserValid(msg.user()))
            response = INVALID_USER;
        else if(!isSecurityPinValid(msg.securityPin()))
            response = INVALID_SECURITY_PIN;
        if(response != SUCCESS){
            writeResponse(ctx, Opcode.KICK_RESPONSE, response);
            return;
        }
        final Profile profile = database.profiles().forUser(msg.user());
        if(profile == null)
            response = PROFILE_NOT_FOUND;
        else if(!profile.securityPinEquals(msg.securityPin()))
            response = PIN_INCORRECT;
        if(response != SUCCESS){
            writeResponse(ctx, Opcode.KICK_RESPONSE, response);
            return;
        }
        final Client client = activeClients.forIdOrUser(profile.id());
        if(client == null){
            writeResponse(ctx, Opcode.KICK_RESPONSE, CLIENT_NOT_ONLINE);
            return;
        }
        final int id = (int)database.logs().insert(profile.id(), String.format("Kicked by: %s", NetUtils.ip(ctx)));
        client.write(new Packet(Opcode.ADD_GENERAL_LOG, database.logs().forId(id).serialize()));
        client.disconnect()
                .addListener(f -> writeResponse(ctx, Opcode.KICK_RESPONSE, SUCCESS));
    }

    @Override
    public void onError(final ChannelHandlerContext ctx, final Kick msg, final Throwable err){
        err.printStackTrace();
        ctx.writeAndFlush(new Packet(Opcode.KICK_RESPONSE).writeByte(ERROR));
    }
}
