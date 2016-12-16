package jvm.mohawk.chatter.server.model.profile;

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
import jvm.mohawk.chatter.server.Chatter;
import jvm.mohawk.chatter.server.model.Rank;
import jvm.mohawk.chatter.server.model.Status;
import jvm.mohawk.chatter.server.net.Buffer;
import org.mindrot.jbcrypt.BCrypt;

public class Profile {

    private final int id;
    private Rank rank;

    private final String user;
    private String pass;

    private String firstName;
    private String lastName;

    private final Date birthDate;

    private Status status;
    private String pic;

    private final String securityPin;

    public Profile(final int id,
                   final Rank rank,
                   final String user,
                   final String pass,
                   final String firstName,
                   final String lastName,
                   final Date birthDate,
                   final Status status,
                   final String pic,
                   final String securityPin){
        this.id = id;
        this.rank = rank;
        this.user = user;
        this.pass = pass;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.status = status;
        this.pic = pic;
        this.securityPin = securityPin;
    }

    public int id(){
        return id;
    }

    public Rank rank(){
        return rank;
    }

    public void rank(final Rank rank){
        this.rank = rank;
    }

    public String user(){
        return user;
    }

    public String pass(){
        return pass;
    }

    public void pass(final String pass){
        this.pass = pass;
    }

    public boolean passEquals(final String pass){
        return BCrypt.checkpw(pass, this.pass);
    }

    public String firstName(){
        return firstName;
    }

    public void firstName(final String firstName){
        this.firstName = firstName;
    }

    public String lastName(){
        return lastName;
    }

    public void lastName(final String lastName){
        this.lastName = lastName;
    }

    public Date birthDate(){
        return birthDate;
    }

    public String pic(){
        return pic;
    }

    public void pic(final String pic){
        this.pic = pic;
    }

    public Status status(){
        return status;
    }

    public void status(final Status status){
        this.status = status;
    }

    public String securityPin(){
        return securityPin;
    }

    public boolean securityPinEquals(final String securityPin){
        return BCrypt.checkpw(securityPin, this.securityPin);
    }

    public Buffer serialize(){
        return new Buffer()
                .writeInt(id)
                .writeByte(rank.id())
                .writeString(user)
                .writeString(firstName)
                .writeString(lastName)
                .writeString(pic)
                .writeByte(Chatter.activeClients.forProfile(this) != null ? status.id() : Status.OFFLINE.id());
    }
}
