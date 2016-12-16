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

import jvm.mohawk.chatter.clientapi.auth.AuthData;

public class RecoverAccountRequestData implements AuthData {

    private final String user;

    public RecoverAccountRequestData(final String user){
        this.user = user;
    }

    public String user(){
        return user;
    }
}
