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

public class TryRecoverAccountResponse extends AuthResponse<TryRecoverAccountData, TryRecoverAccountResponse.Code> {

    public enum Code implements AuthResponse.Code {

        INVALID_PROFILE(1),
        INVALID_SECURITY_PIN(2),
        INVALID_NEW_PASS(3),
        INVALID_SECURITY_QUESTION(4),
        SECURITY_PIN_MISMATCH(5),
        SECURITY_QUESTION_ANSWER_WRONG(6),
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

    public TryRecoverAccountResponse(final ChatterClient.Config config,
                                     final TryRecoverAccountData data,
                                     final Code code){
        super(config, data, code);
    }
}
