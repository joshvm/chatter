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

public class RemoveIpFromListCommand implements Command {

    private static final Pattern PATTERN = Pattern.compile("(\\d|\\*){1,3}\\.(\\d|\\*){1,3}\\.(\\d|\\*){1,3}\\.(\\d|\\*){1,3}");

    private final String key;
    private final boolean white;

    public RemoveIpFromListCommand(final String key,
                                   final boolean white){
        this.key = key;
        this.white = white;
    }

    public String key(){
        return key;
    }

    @Override
    public String usage(){
        return String.format("%s [ip]", key);
    }

    @Override
    public String desc(){
        if(white)
            return "Removes an ip from the whitelist (ips that have access to this account)";
        else
            return "Removes an ip from the blacklist (ips that don't have access to this account)";
    }

    public boolean white(){
        return white;
    }

    @Override
    public void execute(final Client client, final String text, final List<String> responseStrings){
        final Matcher m = PATTERN.matcher(text);
        if(!m.find()){
            responseStrings.add("Invalid Syntax. Args: [ip_pattern]");
            return;
        }
        final String ipPattern = m.group();
        if(white){
            if(database.profiles().deleteIpFromWhitelist(client.profile().id(), ipPattern) > 0)
                responseStrings.add("Removed ip pattern from whitelist: " + ipPattern);
            else
                responseStrings.add("ip pattern not found on the whitelist: " + ipPattern);
        }else{
            if(database.profiles().deleteIpFromBlacklist(client.profile().id(), ipPattern) > 0)
                responseStrings.add("Removed ip pattern from blacklist: " + ipPattern);
            else
                responseStrings.add("ip pattern not found on the blacklist: " + ipPattern);
        }
    }
}
