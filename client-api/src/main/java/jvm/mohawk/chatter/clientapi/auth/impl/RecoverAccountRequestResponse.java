package jvm.mohawk.chatter.clientapi.auth.impl;

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

import java.util.Map;
import jvm.mohawk.chatter.clientapi.ChatterClient;
import jvm.mohawk.chatter.clientapi.auth.AuthResponse;
import jvm.mohawk.chatter.clientapi.utils.Utils;

public class RecoverAccountRequestResponse extends AuthResponse<RecoverAccountRequestData, RecoverAccountRequestResponse.Code> {

    public enum Code implements AuthResponse.Code {

        USER_NOT_FOUND(1),
        TARGET_USER_LOGGED_IN(2),
        SUCCESS(100),
        ERROR(101),
        UNKNOWN(102);

        private static final Map<Integer, Code> MAP = Utils.map(values(), Code::id);

        private final int id;

        Code(final int id){
            this.id = id;
        }

        @Override
        public int id(){
            return id;
        }

        public static Code forId(final int id){
            return MAP.getOrDefault(id, UNKNOWN);
        }
    }

    private final int profileId;
    private final int securityQuestionId;
    private final String securityQuestion;

    public RecoverAccountRequestResponse(final ChatterClient.Config config,
                                         final RecoverAccountRequestData data,
                                         final Code code,
                                         final int profileId,
                                         final int securityQuestionId,
                                         final String securityQuestion){
        super(config, data, code);
        this.profileId = profileId;
        this.securityQuestionId = securityQuestionId;
        this.securityQuestion = securityQuestion;
    }

    public RecoverAccountRequestResponse(final ChatterClient.Config config,
                                         final RecoverAccountRequestData data,
                                         final Code code){
        this(config, data, code, -1, -1, null);
    }

    public int profileId(){
        return profileId;
    }

    public int securityQuestionId(){
        return securityQuestionId;
    }

    public String securityQuestion(){
        return securityQuestion;
    }
}
