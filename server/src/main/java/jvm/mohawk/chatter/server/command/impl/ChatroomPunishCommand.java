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
import jvm.mohawk.chatter.server.model.Rank;
import jvm.mohawk.chatter.server.model.chatroom.Chatroom;
import jvm.mohawk.chatter.server.model.profile.Profile;
import jvm.mohawk.chatter.server.model.punishment.ChatroomPunishment;
import jvm.mohawk.chatter.server.model.punishment.Punishment;
import jvm.mohawk.chatter.server.net.client.Client;


import static jvm.mohawk.chatter.server.Chatter.database;

public class ChatroomPunishCommand implements Command {

    private static final Pattern ARGS_PATTERN = Pattern.compile("([\\w\\d]{1,20}) ([\\w\\d._]{1,30}) (\\d{1,9}) (.+)");

    private final String key;
    private final Punishment.Type type;
    private final Rank requiredRank;

    public ChatroomPunishCommand(final String key,
                                 final Punishment.Type type,
                                 final Rank requiredRank){
        this.key = key;
        this.type = type;
        this.requiredRank = requiredRank;
    }

    @Override
    public String key(){
        return key;
    }

    @Override
    public String usage(){
        return String.format("%s [chatroom_name] [target_username] [time_in_seconds] [reason]", key());
    }

    @Override
    public String desc(){
        return String.format(
                "%s a chatroom user",
                type
        );
    }

    @Override
    public void execute(final Client client, final String text, final List<String> responseStrings){
        final Matcher matcher = ARGS_PATTERN.matcher(text);
        if(!matcher.find()){
            responseStrings.add("Invalid format. Args: [chatroom_name] [username] [time_in_seconds] [reason]");
            return;
        }
        final String chatroomName = matcher.group(1);
        final Chatroom chatroom = client.chatrooms().forNameOrId(chatroomName);
        if(chatroom == null){
            responseStrings.add("Chatroom not found: " + chatroomName);
            return;
        }
        if(chatroom.rankFor(client.profile()).id() < requiredRank.id()){
            responseStrings.add(String.format("You must be %s to use this command", requiredRank));
            return;
        }
        final String targetUser = matcher.group(2);
        final int durationInSeconds = Integer.parseInt(matcher.group(3));
        final String reason = matcher.group(4);
        final Profile victimProfile = database.profiles().forUser(targetUser);
        if(victimProfile == null){
            responseStrings.add("Target profile not found: " + targetUser);
            return;
        }
        if(chatroom.rankFor(victimProfile).id() >= chatroom.rankFor(client.profile()).id()){
            responseStrings.add("Cannot punish someone with the same or higher rank as you");
            return;
        }
        final Punishment existingPunishment = chatroom.punishments().forVictim(victimProfile.id(), type);
        if(existingPunishment != null){
            responseStrings.add("Punishment already exists... Delete the existing one before adding a new one");
            return;
        }
        final int id = (int)database.punishments().insertChatroomPunishment(
                chatroom.id(),
                client.profile().id(),
                client.profile().user(),
                victimProfile.id(),
                victimProfile.user(),
                type,
                durationInSeconds,
                reason,
                true);
        if(id < 0){
            responseStrings.add("Error adding punishment");
            return;
        }
        final ChatroomPunishment p = database.punishments().chatroomPunishmentForId(id);
        p.apply();
        chatroom.punishments().add(p);
        responseStrings.add(String.format("chatroom %s %s for %s: Success", chatroom.name(), type, targetUser));
    }
}
