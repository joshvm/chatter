package jvm.mohawk.chatter.clientapi.auth.impl;

/*
  Capstone Project - Chatter
  
  An instant messenger that lets you connect with your friends 
  and meet new people with similar interests.
  
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

import java.util.Map;
import jvm.mohawk.chatter.clientapi.ChatterClient;
import jvm.mohawk.chatter.clientapi.auth.AuthResponse;
import jvm.mohawk.chatter.clientapi.utils.Utils;

public class KickResponse extends AuthResponse<KickData, KickResponse.Code> {

    public enum Code implements AuthResponse.Code {

        INVALID_USER(1),
        INVALID_SECURITY_PIN(2),
        PROFILE_NOT_FOUND(3),
        PIN_INCORRECT(4),
        CLIENT_NOT_ONLINE(5),
        SUCCESS(100),
        ERROR(101),
        UNKNOWN(102);

        private static final Map<Integer, Code> MAP = Utils.map(values(), Code::id);

        private final int id;

        Code(final int id){
            this.id = id;
        }

        public int id(){
            return id;
        }

        public static Code forId(final int id){
            return MAP.getOrDefault(id, UNKNOWN);
        }
    }

    public KickResponse(final ChatterClient.Config config,
                        final KickData data,
                        final KickResponse.Code code){
        super(config, data, code);
    }
}
