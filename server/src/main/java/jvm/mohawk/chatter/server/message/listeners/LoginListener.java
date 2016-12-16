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
import java.util.Collection;
import java.util.List;
import jvm.mohawk.chatter.server.Chatter;
import jvm.mohawk.chatter.server.message.MessageListener;
import jvm.mohawk.chatter.server.message.types.Login;
import jvm.mohawk.chatter.server.model.friendship.FriendRequest;
import jvm.mohawk.chatter.server.model.friendship.Friendship;
import jvm.mohawk.chatter.server.model.profile.Profile;
import jvm.mohawk.chatter.server.model.profile.ProfileOptions;
import jvm.mohawk.chatter.server.model.punishment.Punishment;
import jvm.mohawk.chatter.server.net.Opcode;
import jvm.mohawk.chatter.server.net.Packet;
import jvm.mohawk.chatter.server.net.client.Client;


import static jvm.mohawk.chatter.server.Chatter.activeClients;
import static jvm.mohawk.chatter.server.Chatter.database;
import static jvm.mohawk.chatter.server.model.profile.ProfileValidation.isDeviceValid;
import static jvm.mohawk.chatter.server.model.profile.ProfileValidation.isPassValid;
import static jvm.mohawk.chatter.server.model.profile.ProfileValidation.isSecurityPinValid;
import static jvm.mohawk.chatter.server.model.profile.ProfileValidation.isUserValid;
import static jvm.mohawk.chatter.server.net.utils.NetUtils.ip;
import static jvm.mohawk.chatter.server.net.utils.NetUtils.writeResponse;
import static jvm.mohawk.chatter.server.utils.Utils.ipListContains;

public class LoginListener implements MessageListener<Login> {

    private static final int INVALID_DEVICE = 1;
    private static final int INVALID_USER = 2;
    private static final int INVALID_PASS = 3;

    private static final int USER_DOES_NOT_EXIST = 4;
    private static final int PASS_MISMATCH = 5;

    private static final int SECURITY_PIN_REQUIRED = 6;
    private static final int INVALID_SECURITY_PIN = 7;

    private static final int BLACK_LISTED = 8;

    private static final int ALREADY_LOGGED_IN = 9;

    private static final int BANNED = 10;

    private static final int SUCCESS = 100;
    private static final int ERROR = 101;

    @Override
    public void onMessage(final ChannelHandlerContext ctx, final Login msg){
        int response = SUCCESS;
        if(!isDeviceValid(msg.device()))
            response = INVALID_DEVICE;
        else if(!isUserValid(msg.user()))
            response = INVALID_USER;
        else if(!isPassValid(msg.pass()))
            response = INVALID_PASS;
        if(response != SUCCESS){
            writeResponse(ctx, Opcode.LOGIN_RESPONSE, response);
            return;
        }
        final Profile profile = database.profiles().forUser(msg.user());
        if(profile == null)
            response = USER_DOES_NOT_EXIST;
        else if(!profile.passEquals(msg.pass()))
            response = PASS_MISMATCH;
        else if(activeClients.forIdOrUser(profile.id()) != null)
            response = ALREADY_LOGGED_IN;
        if(response != SUCCESS){
            writeResponse(ctx, Opcode.LOGIN_RESPONSE, response);
            return;
        }
        final List<Punishment> punishments = Chatter.punishments.forVictim(profile.id());
        if(punishments.stream().anyMatch(p -> p.type() == Punishment.Type.BAN)){
            writeResponse(ctx, Opcode.LOGIN_RESPONSE, BANNED);
            return;
        }
        punishments.forEach(Punishment::apply);
        final String clientIp = ip(ctx);
        final ProfileOptions options = database.profiles().optionsForProfileId(profile.id());
        final Collection<String> blacklist = database.profiles().getBlacklist(profile.id());
        final Collection<String> whitelist = database.profiles().getWhitelist(profile.id());
        if(ipListContains(blacklist, clientIp) && !ipListContains(whitelist, clientIp))
            response = BLACK_LISTED;
        else if(options.requiresSecurityPinOnLogin()){
            if(isSecurityPinValid(msg.securityPin())){
                if(!profile.securityPinEquals(msg.securityPin()))
                    response = INVALID_SECURITY_PIN;
            }else{
                response = SECURITY_PIN_REQUIRED;
            }
        }
        if(response != SUCCESS){
            writeResponse(ctx, Opcode.LOGIN_RESPONSE, response);
            return;
        }
        database.logs().insertLogin(profile.id(), clientIp, msg.device());
        database.logs().insert(profile.id(), String.format("Login from %s (%s)", clientIp, msg.device()));
        final Collection<Friendship> friendships = database.friendships().involving(profile.id());
        final Collection<FriendRequest> friendRequests = database.friendships().requestsInvolving(profile.id());
        final Client client = Client.register(ctx, msg.device(), profile);
        activeClients.add(client);
        writeResponse(ctx, Opcode.LOGIN_RESPONSE, SUCCESS);
        client.write(new Packet(Opcode.INIT, profile.serialize()));
        database.logs().loginLogsForProfile(profile.id())
                .forEach(l -> client.write(new Packet(Opcode.ADD_LOGIN_LOG, l.serialize())));
        for(final Friendship friendship : friendships){
            final int targetProfileId = friendship.otherProfileId(client.profile().id());
            final Profile targetProfile = database.profiles().forId(targetProfileId);
            if(targetProfile == null){
                database.friendships().delete(friendship.id());
                continue;
            }
            client.write(new Packet(Opcode.ADD_FRIEND).writeInt(friendship.id()).writeBuffer(targetProfile.serialize()));
            final Client targetClient = activeClients.forProfile(targetProfile);
            if(targetClient != null)
                targetClient.write(new Packet(Opcode.SET_PROFILE_STATUS).writeInt(client.profile().id()).writeByte(client.profile().status().id()));
        }
        for(final FriendRequest friendRequest : friendRequests){
            Opcode opcode = Opcode.ADD_PENDING_FRIEND_REQUEST;
            int targetProfileId = friendRequest.targetProfileId();
            if(profile.id() == friendRequest.targetProfileId()){
                opcode = Opcode.ADD_ANSWERABLE_FRIEND_REQUEST;
                targetProfileId = friendRequest.requesterProfileId();
            }
            final Profile targetProfile = database.profiles().forId(targetProfileId);
            if(targetProfile == null){
                database.friendships().deleteRequest(friendRequest.id());
                continue;
            }
            client.write(new Packet(opcode).writeInt(friendRequest.id()).writeBuffer(targetProfile.serialize()));
        }
        database.logs().forProfile(profile.id())
                .forEach(l -> client.write(new Packet(Opcode.ADD_GENERAL_LOG, l.serialize())));
    }

    @Override
    public void onError(final ChannelHandlerContext ctx, final Login msg, final Throwable err){
        err.printStackTrace();
        ctx.writeAndFlush(new Packet(Opcode.LOGIN_RESPONSE).writeByte(ERROR));
        ctx.close();
    }
}
