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
import jvm.mohawk.chatter.server.model.security.SecurityQuestion;
import jvm.mohawk.chatter.server.net.client.Client;


import static jvm.mohawk.chatter.server.Chatter.database;

public class RemoveSecurityQuestionCommand implements Command {

    @Override
    public String key(){
        return "deletesecurityquestion";
    }

    @Override
    public String usage(){
        return String.format("%s [security_question_id]", key());
    }

    @Override
    public String desc(){
        return "Removes a security question from your account";
    }

    @Override
    public void execute(final Client client, final String text, final List<String> responseStrings){
        if(!text.matches("\\d{1,8}")){
            responseStrings.add("Invalid syntax. Args: Security question id");
            return;
        }
        final int id = Integer.parseInt(text);
        final SecurityQuestion sq = database.profiles().securityQuestionForId(id);
        if(sq == null){
            responseStrings.add("Security question not found. Type \"viewsecurityquestions\" to see ids");
            return;
        }
        if(database.profiles().deleteSecurityQuestion(id) > 0)
            responseStrings.add(String.format("Successfully removed security question: %d %s", id, sq.text()));
        else
            responseStrings.add(String.format("Error removing security question: %d %s", id, sq.text()));
    }
}
