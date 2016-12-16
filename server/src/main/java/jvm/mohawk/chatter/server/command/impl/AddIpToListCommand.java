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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jvm.mohawk.chatter.server.command.Command;
import jvm.mohawk.chatter.server.net.client.Client;


import static jvm.mohawk.chatter.server.Chatter.database;

public class AddIpToListCommand implements Command {

    private static final Pattern PATTERN = Pattern.compile("(\\d|\\*){1,3}\\.(\\d|\\*){1,3}\\.(\\d|\\*){1,3}\\.(\\d|\\*){1,3}");

    private final String key;
    private final boolean white;

    public AddIpToListCommand(final String key,
                              final boolean white){
        this.key = key;
        this.white = white;
    }

    @Override
    public String key(){
        return key;
    }

    @Override
    public String usage(){
        return String.format("%s [ip]", key());
    }

    @Override
    public String desc(){
        if(white)
            return "Adds an ip pattern to the whitelist (ips that are allowed to access to this account)";
        else
            return "Adds an ip pattern to the blacklist (ips that are NOT allowed to access this account)";
    }

    @Override
    public void execute(final Client client, final String text, final List<String> responseStrings){
        final Matcher m = PATTERN.matcher(text);
        if(!m.find()){
            responseStrings.add("Invalid format. Args: [ip_part1].[ip_part2].[ip_part3].[ip_part4]");
            return;
        }
        final String ipPattern = m.group();
        if(white){
            database.profiles().addIpToWhitelist(client.profile().id(), ipPattern);
            responseStrings.add("Added ip pattern to whitelist: " + ipPattern);
        }else{
            database.profiles().addIpToBlacklist(client.profile().id(), ipPattern);
            responseStrings.add("Added ip pattern to blacklist: " + ipPattern);
        }
    }

}
