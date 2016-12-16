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
import jvm.mohawk.chatter.server.model.security.SecurityQuestion;
import jvm.mohawk.chatter.server.net.client.Client;


import static jvm.mohawk.chatter.server.Chatter.database;

public class ViewSecurityQuestionsCommand implements Command {

    @Override
    public String key(){
        return "viewsecurityquestions";
    }

    @Override
    public String usage(){
        return key();
    }

    @Override
    public String desc(){
        return "Views all of the security questions on your account";
    }

    @Override
    public void execute(final Client client, final String text, final List<String> responseStrings){
        final Collection<SecurityQuestion> questions = database.profiles().getSecurityQuestions(client.profile().id());
        if(questions.isEmpty()){
            responseStrings.add("You do not have any security questions. Use the addsecurityquestion command to add some");
            return;
        }
        for(final SecurityQuestion sq : questions)
            responseStrings.add(String.format("%d. %s", sq.id(), sq.text()));
    }
}
