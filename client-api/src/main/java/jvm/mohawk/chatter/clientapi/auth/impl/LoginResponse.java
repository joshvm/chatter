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

public class LoginResponse extends AuthResponse<LoginData, LoginResponse.Code> {

    public enum Code implements AuthResponse.Code {

        INVALID_DEVICE(1, "Invalid Device"),
        INVALID_USER(2, "Invalid username"),
        INVALID_PASS(3, "Invalid password"),
        USER_DOES_NOT_EXIST(4, "Username doesn't exist"),
        PASS_MISMATCH(5, "Password mismatch"),
        SECURITY_PIN_REQUIRED(6, "Security pin is required"),
        INVALID_SECURITY_PIN(7, "Security pin mismatch"),
        BLACK_LISTED(8, "Blacklisted"),
        ALREADY_LOGGED_IN(9, "Already logged in"),
        BANNED(10, "You are banned. Try again later."),
        SUCCESS(100, "Success"),
        ERROR(101, "Error logging in"),
        UNKNOWN(102, "Unknown login code");

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

    private final ChatterClient client;

    public LoginResponse(final ChatterClient.Config config,
                         final LoginData data,
                         final Code code,
                         final ChatterClient client){
        super(config, data, code);
        this.client = client;
    }

    public ChatterClient client(){
        return client;
    }
}
