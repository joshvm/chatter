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
import jvm.mohawk.chatter.server.net.client.Client;


import static jvm.mohawk.chatter.server.Chatter.database;

public class SetOptionCommand implements Command {

    @Override
    public String key(){
        return "set";
    }

    @Override
    public String usage(){
        return String.format("%s [option_name] = [value]", key());
    }

    @Override
    public String desc(){
        return "Sets an option that changes the dynamic of your account | Options: pin_login(whether or not you have to enter security pin when you login)[true/false]";
    }

    @Override
    public void execute(final Client client, final String text, final List<String> responseStrings){
        final String[] split = text.split("\\s*=\\s*");
        if(split.length != 2){
            responseStrings.add("Invalid syntax. set [option] = [value]");
            return;
        }
        final String key = split[0].trim();
        final String value = split[1].trim();
        switch(key){
            case "pin_login":
                final boolean pinLogin = Boolean.parseBoolean(value);
                database.profiles().setRequiresSecurityPinOnLogin(client.profile().id(), pinLogin);
                responseStrings.add("Requires security pin on login set to: " + pinLogin);
                break;
            default:
                responseStrings.add("Option not found: " + key);
        }
    }

}
