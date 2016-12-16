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

import java.sql.Date;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import jvm.mohawk.chatter.server.message.Message;
import lombok.ToString;


import static jvm.mohawk.chatter.server.message.MessageType.REGISTER;

@ToString(callSuper = true)
public class Register extends Message {

    private final String device;

    private final String pic;

    private final String firstName;
    private final String lastName;

    private final int birthYear;
    private final int birthMonth;
    private final int birthDay;

    private final String user;
    private final String pass;

    private final String securityPin;

    public Register(final Timestamp timestamp,
                    final String device,
                    final String pic,
                    final String firstName,
                    final String lastName,
                    final int birthYear,
                    final int birthMonth,
                    final int birthDay,
                    final String user,
                    final String pass,
                    final String securityPin){
        super(REGISTER, timestamp);
        this.device = device;
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

    public String device(){
        return device;
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

    public Date birthDate(){
        return new Date(new GregorianCalendar(birthYear, birthMonth, birthDay).getTimeInMillis());
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
