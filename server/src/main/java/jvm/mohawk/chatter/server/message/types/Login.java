package jvm.mohawk.chatter.server.message.types;

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

import java.sql.Timestamp;
import jvm.mohawk.chatter.server.message.Message;


import static jvm.mohawk.chatter.server.message.MessageType.LOGIN;

public class Login extends Message {

    private final String device;

    private final String user;
    private final String pass;

    private final String securityPin;

    public Login(final Timestamp timestamp,
                 final String device,
                 final String user,
                 final String pass,
                 final String securityPin){
        super(LOGIN, timestamp);
        this.device = device;
        this.user = user;
        this.pass = pass;
        this.securityPin = securityPin;
    }

    public String device(){
        return device;
    }

    public String user(){
        return user;
    }

    public String pass(){
        return pass;
    }

    public String securityPin(){
        return securityPin;
    }
}
