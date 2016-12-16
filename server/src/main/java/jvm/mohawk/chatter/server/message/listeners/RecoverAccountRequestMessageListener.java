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
import java.util.concurrent.ThreadLocalRandom;
import jvm.mohawk.chatter.server.message.MessageListener;
import jvm.mohawk.chatter.server.message.types.RecoverAccountRequestMessage;
import jvm.mohawk.chatter.server.model.profile.Profile;
import jvm.mohawk.chatter.server.model.security.SecurityQuestion;
import jvm.mohawk.chatter.server.net.Opcode;
import jvm.mohawk.chatter.server.net.Packet;


import static jvm.mohawk.chatter.server.Chatter.activeClients;
import static jvm.mohawk.chatter.server.Chatter.database;
import static jvm.mohawk.chatter.server.net.utils.NetUtils.write;
import static jvm.mohawk.chatter.server.net.utils.NetUtils.writeResponse;

public class RecoverAccountRequestMessageListener implements MessageListener<RecoverAccountRequestMessage> {

    private static final int USER_NOT_FOUND = 1;
    private static final int TARGET_USER_LOGGED_IN = 2;

    private static final int SUCCESS = 100;
    private static final int ERROR = 101;

    @Override
    public void onMessage(final ChannelHandlerContext ctx, final RecoverAccountRequestMessage msg) throws Exception{
        final Profile targetProfile = database.profiles().forUser(msg.targetUser());
        if(targetProfile == null){
            writeResponse(ctx, Opcode.RECOVER_ACCOUNT_REQUEST_RESPONSE, USER_NOT_FOUND);
            return;
        }
        if(activeClients.forProfile(targetProfile) != null){
            writeResponse(ctx, Opcode.RECOVER_ACCOUNT_REQUEST_RESPONSE, TARGET_USER_LOGGED_IN);
            return;
        }
        final Collection<SecurityQuestion> questions = database.profiles().getSecurityQuestions(targetProfile.id());
        final Packet pkt = new Packet(Opcode.RECOVER_ACCOUNT_REQUEST_RESPONSE)
                .writeByte(SUCCESS)
                .writeInt(targetProfile.id());
        if(!questions.isEmpty()){
            final SecurityQuestion sq = (SecurityQuestion)questions.toArray()[ThreadLocalRandom.current().nextInt(questions.size())];
            pkt.writeInt(sq.id())
                    .writeString(sq.text());
        }else{
            pkt.writeInt(-1)
                    .writeString("");
        }
        write(ctx, pkt);
    }

    @Override
    public void onError(final ChannelHandlerContext ctx, final RecoverAccountRequestMessage msg, final Throwable err){
        err.printStackTrace();
        writeResponse(ctx, Opcode.RECOVER_ACCOUNT_REQUEST_RESPONSE, ERROR);
    }
}
