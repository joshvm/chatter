package jvm.mohawk.chatter.server.command.impl;

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

import java.util.List;
import jvm.mohawk.chatter.server.command.Command;
import jvm.mohawk.chatter.server.model.profile.ProfileOptions;
import jvm.mohawk.chatter.server.net.client.Client;


import static jvm.mohawk.chatter.server.Chatter.database;

public class GetOptionCommand implements Command {

    @Override
    public String key(){
        return "get";
    }

    @Override
    public String desc(){
        return "Gets the value of an option that changes the dynamic of your account";
    }

    @Override
    public String usage(){
        return String.format("%s [option_name] | Available options: pin_login (whether or not you have to enter your pin on login)", key());
    }

    @Override
    public void execute(final Client client, final String option, final List<String> responseStrings){
        final ProfileOptions options = database.profiles().optionsForProfileId(client.profile().id());
        if(options == null){
            responseStrings.add("Error fetching options... Try again later");
            return;
        }
        switch(option){
            case "pin_login":
                responseStrings.add("Requires security pin on login: " + options.requiresSecurityPinOnLogin());
                break;
            default:
                responseStrings.add("Option not found:" + option);
        }
    }
}
