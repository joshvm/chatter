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

public class TryRecoverAccountData implements AuthData {

    private final int profileId;
    private final String securityPin;
    private final int securityQuestionId;
    private final String securityQuestionAnswer;
    private final String newPass;

    public TryRecoverAccountData(final int profileId,
                                 final String securityPin,
                                 final int securityQuestionId,
                                 final String securityQuestionAnswer,
                                 final String newPass){
        this.profileId = profileId;
        this.securityPin = securityPin;
        this.securityQuestionId = securityQuestionId;
        this.securityQuestionAnswer = securityQuestionAnswer;
        this.newPass = newPass;
    }

    public int profileId(){
        return profileId;
    }

    public String securityPin(){
        return securityPin;
    }

    public int securityQuestionId(){
        return securityQuestionId;
    }

    public String securityQuestionAnswer(){
        return securityQuestionAnswer;
    }

    public String newPass(){
        return newPass;
    }
}
