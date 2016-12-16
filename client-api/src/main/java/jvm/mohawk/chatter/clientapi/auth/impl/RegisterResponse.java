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

public class RegisterResponse extends AuthResponse<RegisterData, RegisterResponse.Code> {

    public enum Code implements AuthResponse.Code {

        INVALID_DEVICE(1, "Invalid device"),
        INVALID_FIRST_NAME(2, "Invalid first name"),
        INVALID_LAST_NAME(3, "Invalid last name"),
        INVALID_BIRTH_DATE(4, "Invalid birth date"),
        INVALID_USER(5, "Invalid username"),
        INVALID_PASS(6, "Invalid password"),
        INVALID_SECURITY_PIN(7, "Invalid security pin"),
        USER_ALREADY_EXISTS(8, "Username is taken"),
        SUCCESS(100, "Success"),
        ERROR(101, "Error registering account"),
        UNKNOWN(102, "Unknown registration code");

        private static final Map<Integer, Code> MAP = Utils.map(values(), Code::id);

        private final int id;
        private final String msg;

        Code(final int id,
             final String msg){
            this.id = id;
            this.msg = msg;
        }

        public int id(){
            return id;
        }

        public String msg(){
            return msg;
        }

        public static Code forId(final int id){
            return MAP.getOrDefault(id, UNKNOWN);
        }
    }

    public RegisterResponse(final ChatterClient.Config config,
                            final RegisterData data,
                            final Code code){
        super(config, data, code);
    }
}
