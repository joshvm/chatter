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
import jvm.mohawk.chatter.server.database.profile.ProfileDao;
import jvm.mohawk.chatter.server.message.MessageListener;
import jvm.mohawk.chatter.server.message.types.Register;
import jvm.mohawk.chatter.server.net.Opcode;
import jvm.mohawk.chatter.server.net.Packet;
import org.mindrot.jbcrypt.BCrypt;


import static jvm.mohawk.chatter.server.Chatter.config;
import static jvm.mohawk.chatter.server.Chatter.database;
import static jvm.mohawk.chatter.server.model.profile.ProfileValidation.isBirthDateValid;
import static jvm.mohawk.chatter.server.model.profile.ProfileValidation.isDeviceValid;
import static jvm.mohawk.chatter.server.model.profile.ProfileValidation.isFirstNameValid;
import static jvm.mohawk.chatter.server.model.profile.ProfileValidation.isLastNameValid;
import static jvm.mohawk.chatter.server.model.profile.ProfileValidation.isPassValid;
import static jvm.mohawk.chatter.server.model.profile.ProfileValidation.isSecurityPinValid;
import static jvm.mohawk.chatter.server.model.profile.ProfileValidation.isUserValid;
import static jvm.mohawk.chatter.server.net.utils.NetUtils.ip;
import static jvm.mohawk.chatter.server.net.utils.NetUtils.writeResponse;

public class RegisterListener implements MessageListener<Register> {

    private static final int INVALID_DEVICE = 1;
    private static final int INVALID_FIRST_NAME = 2;
    private static final int INVALID_LAST_NAME = 3;
    private static final int INVALID_BIRTH_DATE = 4;
    private static final int INVALID_USER = 5;
    private static final int INVALID_PASS = 6;
    private static final int INVALID_SECURITY_PIN = 7;

    private static final int USER_ALREADY_EXISTS = 8;

    private static final int SUCCESS = 100;
    private static final int ERROR = 101;

    @Override
    public void onMessage(final ChannelHandlerContext ctx, final Register msg){
        int response = SUCCESS;
        if(!isDeviceValid(msg.device()))
            response = INVALID_DEVICE;
        else if(!isFirstNameValid(msg.firstName()))
            response = INVALID_FIRST_NAME;
        else if(!isLastNameValid(msg.lastName()))
            response = INVALID_LAST_NAME;
        else if(!isBirthDateValid(msg.birthYear(), msg.birthMonth(), msg.birthDay()))
            response = INVALID_BIRTH_DATE;
        else if(!isUserValid(msg.user()))
            response = INVALID_USER;
        else if(!isPassValid(msg.pass()))
            response = INVALID_PASS;
        else if(!isSecurityPinValid(msg.securityPin()))
            response = INVALID_SECURITY_PIN;
        else if(database.profiles().forUser(msg.user()) != null)
            response = USER_ALREADY_EXISTS;
        if(response != SUCCESS){
            writeResponse(ctx, Opcode.REGISTER_RESPONSE, response);
            return;
        }
        final ProfileDao dao = database.openProfiles();
        try{
            dao.begin();
            final int profileId = (int) dao.add(
                    config.startingRank(),
                    msg.user(),
                    BCrypt.hashpw(msg.pass(), BCrypt.gensalt()),
                    msg.firstName(),
                    msg.lastName(),
                    msg.birthDate(),
                    config.startingStatus(),
                    msg.pic(),
                    BCrypt.hashpw(msg.securityPin(), BCrypt.gensalt())
            );
            dao.addRegistrationDetails(profileId, ip(ctx), msg.device());
            dao.addOptions(profileId, config.requiresSecurityPinOnLogin());
            dao.commit();
            writeResponse(ctx, Opcode.REGISTER_RESPONSE, SUCCESS);
        }catch(Exception ex){
            ex.printStackTrace();
            dao.rollback();
            throw ex;
        }finally{
            dao.close();
        }
    }

    @Override
    public void onError(final ChannelHandlerContext ctx, final Register msg, final Throwable err){
        err.printStackTrace();
        ctx.writeAndFlush(new Packet(Opcode.REGISTER_RESPONSE).writeByte(ERROR));
    }
}
