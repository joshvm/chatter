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

public class RegisterData implements AuthData {

    private final String pic;
    private final String firstName;
    private final String lastName;

    private final int birthYear;
    private final int birthMonth;
    private final int birthDay;

    private final String user;
    private final String pass;

    private final String securityPin;

    public RegisterData(final String pic,
                        final String firstName,
                        final String lastName,
                        final int birthYear,
                        final int birthMonth,
                        final int birthDay,
                        final String user,
                        final String pass,
                        final String securityPin){
        this.pic = pic;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthYear = birthYear;
        this.birthMonth = birthMonth;
        this.birthDay = birthDay;
        this.user = user;
        this.pass = pass;
        this.securityPin = securityPin;
    }

    public String pic(){
        return pic;
    }

    public String firstName(){
        return firstName;
    }

    public String lastName(){
        return lastName;
    }

    public int birthYear(){
        return birthYear;
    }

    public int birthMonth(){
        return birthMonth;
    }

    public int birthDay(){
        return birthDay;
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
