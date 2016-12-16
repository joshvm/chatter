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
import jvm.mohawk.chatter.server.Chatter;
import jvm.mohawk.chatter.server.command.Command;
import jvm.mohawk.chatter.server.net.client.Client;

public class AddSecurityQuestionCommand implements Command {

    @Override
    public String key(){
        return "addsecurityquestion";
    }

    @Override
    public String usage(){
        return String.format(
                "%s [question] = [answer]",
                key()
        );
    }

    @Override
    public String desc(){
        return "Adds a security question (used for recovery information)";
    }

    @Override
    public void execute(final Client client, final String text, final List<String> responseStrings){
        final String[] parts = text.split("\\s*=\\s*");
        if(parts.length != 2){
            responseStrings.add("Invalid syntax. Args: [question] = [answer]");
            return;
        }
        final String question = parts[0].trim();
        final String answer = parts[1].trim();
        if(question.isEmpty()){
            responseStrings.add("Security question cannot be empty");
            return;
        }
        if(answer.isEmpty()){
            responseStrings.add("Security question answer cannot be empty");
            return;
        }
        if(Chatter.database.profiles().addSecurityQuestion(client.profile().id(), question, answer) > 0)
            responseStrings.add(String.format("Added security question: %s = %s", question, answer));
        else
            responseStrings.add(String.format("Error adding security question: %s = %s", question, answer));
    }
}
