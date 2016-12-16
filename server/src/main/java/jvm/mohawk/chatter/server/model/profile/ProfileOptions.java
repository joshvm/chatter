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

public class ProfileOptions {

    private final int profileId;

    private boolean requiresSecurityPinOnLogin;

    public ProfileOptions(final int profileId,
                          final boolean requiresSecurityPinOnLogin){
        this.profileId = profileId;
        this.requiresSecurityPinOnLogin = requiresSecurityPinOnLogin;
    }

    public int profileId(){
        return profileId;
    }

    public boolean requiresSecurityPinOnLogin(){
        return requiresSecurityPinOnLogin;
    }

}
