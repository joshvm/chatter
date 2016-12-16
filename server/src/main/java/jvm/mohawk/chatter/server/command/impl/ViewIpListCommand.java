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

import java.util.Collection;
import java.util.List;
import jvm.mohawk.chatter.server.command.Command;
import jvm.mohawk.chatter.server.net.client.Client;


import static jvm.mohawk.chatter.server.Chatter.database;

public class ViewIpListCommand implements Command {

    private final String key;
    private final boolean white;

    public ViewIpListCommand(final String key,
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
        return key();
    }

    @Override
    public String desc(){
        if(white)
            return "View a list of all of the ips that have access to your account (whitelist)";
        else
            return "View a list of all of the ips that do not have access ot your account (blacklist)";
    }

    @Override
    public void execute(final Client client, final String text, final List<String> responseStrings){
        final Collection<String> ips = white
                ? database.profiles().getWhitelist(client.profile().id())
                : database.profiles().getBlacklist(client.profile().id());
        final String ipStr = String.join(" ", ips);
        responseStrings.add(String.format("IP %s: %s", white ? "Whitelist" : "Blacklist", ipStr));
    }
}
