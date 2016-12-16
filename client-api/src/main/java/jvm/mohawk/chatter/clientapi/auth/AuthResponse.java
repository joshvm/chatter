package jvm.mohawk.chatter.clientapi.auth;

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

import jvm.mohawk.chatter.clientapi.ChatterClient;

/**
 *
 * auth responses aren't part of an active session with the chatter client.
 * these are responses that are yielded when you don't have a chatter client instance...
 * generally for authentication purposes (registering, logging in, etc)
 *
 * @param <D> the auth data
 * @param <C> the response code
 */
public class AuthResponse<D extends AuthData, C extends AuthResponse.Code> {

    public interface Code {

        int id();
    }

    private final ChatterClient.Config config;
    private final D data;
    private final C code;

    public AuthResponse(final ChatterClient.Config config,
                        final D data,
                        final C code){
        this.config = config;
        this.data = data;
        this.code = code;
    }

    public ChatterClient.Config config(){
        return config;
    }

    public D data(){
        return data;
    }

    public C code(){
        return code;
    }
}
