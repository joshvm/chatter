package jvm.mohawk.chatter.clientapi.test;

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
import jvm.mohawk.chatter.clientapi.auth.impl.LoginData;

public class ClientTest {

    public static void main(String[] args) throws Exception {
        final LoginData joshData = new LoginData("josh", "nigga123", null);
        final LoginData amyData = new LoginData("amy", "nigga123", null);
        final ChatterClient.Config config = new ChatterClient.Config("local", "localhost", 7495);
        final ChatterClient client = ChatterClient.login(config, joshData).client();
        client.start();
    }

}
