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
import jvm.mohawk.chatter.server.message.types.TryRecoverAccountMessage;
import jvm.mohawk.chatter.server.model.profile.Profile;
import jvm.mohawk.chatter.server.model.profile.ProfileValidation;
import jvm.mohawk.chatter.server.model.security.SecurityQuestion;
import jvm.mohawk.chatter.server.net.Opcode;
import org.mindrot.jbcrypt.BCrypt;


import static jvm.mohawk.chatter.server.Chatter.database;
import static jvm.mohawk.chatter.server.net.utils.NetUtils.writeResponse;

public class TryRecoverAccountMessageListener implements MessageListener<TryRecoverAccountMessage> {

    private static final int INVALID_PROFILE = 1;
    private static final int INVALID_SECURITY_PIN = 2;
    private static final int INVALID_NEW_PASS = 3;
    private static final int INVALID_SECURITY_QUESTION = 4;
    private static final int SECURITY_PIN_MISMATCH = 5;
    private static final int SECURITY_QUESTION_ANSWER_WRONG = 6;

    private static final int SUCCESS = 100;
    private static final int ERROR = 101;

    @Override
    public void onMessage(final ChannelHandlerContext ctx, final TryRecoverAccountMessage msg) throws Exception{
        if(!ProfileValidation.isSecurityPinValid(msg.securityPin())){
            writeResponse(ctx, Opcode.TRY_RECOVER_ACCOUNT_RESPONSE, INVALID_SECURITY_PIN);
            return;
        }
        if(!ProfileValidation.isPassValid(msg.newPass())){
            writeResponse(ctx, Opcode.TRY_RECOVER_ACCOUNT_RESPONSE, INVALID_NEW_PASS);
            return;
        }
        final Profile profile = database.profiles().forId(msg.profileId());
        if(profile == null){
            writeResponse(ctx, Opcode.TRY_RECOVER_ACCOUNT_RESPONSE, INVALID_PROFILE);
            return;
        }
        if(!profile.securityPinEquals(msg.securityPin())){
            writeResponse(ctx, Opcode.TRY_RECOVER_ACCOUNT_RESPONSE, SECURITY_PIN_MISMATCH);
            return;
        }
        if(msg.securityQuestionId() > 0){
            final SecurityQuestion sq = database.profiles().securityQuestionForId(msg.securityQuestionId());
            if(sq == null || sq.profileId() != profile.id()){
                writeResponse(ctx, Opcode.TRY_RECOVER_ACCOUNT_RESPONSE, INVALID_SECURITY_QUESTION);
                return;
            }
            if(!sq.answer().equals(msg.securityQuestionAnswer())){
                writeResponse(ctx, Opcode.TRY_RECOVER_ACCOUNT_RESPONSE, SECURITY_QUESTION_ANSWER_WRONG);
                return;
            }
        }
        database.profiles().setPassword(profile.id(), BCrypt.hashpw(msg.newPass(), BCrypt.gensalt()));
        writeResponse(ctx, Opcode.TRY_RECOVER_ACCOUNT_RESPONSE, SUCCESS);
    }

    @Override
    public void onError(final ChannelHandlerContext ctx, final TryRecoverAccountMessage msg, final Throwable err){
        err.printStackTrace();
        writeResponse(ctx, Opcode.TRY_RECOVER_ACCOUNT_RESPONSE, ERROR);
    }
}
