package jvm.mohawk.chatter.server.model.security;

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

public class SecurityQuestion {

    private final int id;
    private final int profileId;
    private final String text;
    private final String answer;

    public SecurityQuestion(final int id,
                            final int profileId,
                            final String text,
                            final String answer){
        this.id = id;
        this.profileId = profileId;
        this.text = text;
        this.answer = answer;
    }

    public int id(){
        return id;
    }

    public int profileId(){
        return profileId;
    }

    public String text(){
        return text;
    }

    public String answer(){
        return answer;
    }
}
