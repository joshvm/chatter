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

import jvm.mohawk.chatter.clientapi.auth.AuthData;

public class KickData implements AuthData {

    private final String profileUser;
    private final String securityPin;

    public KickData(final String profileUser,
                    final String securityPin){
        this.profileUser = profileUser;
        this.securityPin = securityPin;
    }

    public String profileUser(){
        return profileUser;
    }

    public String securityPin(){
        return securityPin;
    }
}
