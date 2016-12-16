package jvm.mohawk.chatter.server.model.punishment;

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

import java.sql.Timestamp;
import jvm.mohawk.chatter.server.model.chatroom.Chatroom;
import jvm.mohawk.chatter.server.net.Opcode;
import jvm.mohawk.chatter.server.net.Packet;
import jvm.mohawk.chatter.server.net.client.Client;


import static jvm.mohawk.chatter.server.Chatter.activeChatrooms;
import static jvm.mohawk.chatter.server.Chatter.database;

public class ChatroomPunishment extends Punishment {

    private final int chatroomId;

    public ChatroomPunishment(final int id,
                              final Timestamp timestamp,
                              final int chatroomId,
                              final int punisherId,
                              final String punisherUser,
                              final int victimId,
                              final String victimUser,
                              final Type type,
                              final int durationInSeconds,
                              final String reason,
                              final boolean active){
        super(id, timestamp, punisherId, punisherUser, victimId, victimUser, type, durationInSeconds, reason, active);
        this.chatroomId = chatroomId;
    }

    public int chatroomId(){
        return chatroomId;
    }

    @Override
    public void apply(){
        final Chatroom chatroom = activeChatrooms.forNameOrId(chatroomId);
        if(chatroom == null)
            return;
        final Client victim = chatroom.clients().forIdOrUser(victimId());
        if(victim != null){
            switch(type()){
                case MUTE:
                    chatroom.mute(victimId(), true);
                    victim.write(new Packet(Opcode.ADD_COMMAND_MESSAGE).writeString("You have been muted inside chatroom: " + chatroom.name()));
                    break;
                case BAN:
                    chatroom.clients().remove(victim);
                    victim.chatrooms().remove(chatroom);
                    victim.write(new Packet(Opcode.REMOVE_CHATROOM).writeInt(chatroom.id()));
                    victim.write(new Packet(Opcode.ADD_COMMAND_MESSAGE).writeString("You have been banned inside chatroom: " + chatroom.name()));
                    chatroom.clients().list().forEach(
                            c -> {
                                c.write(
                                        new Packet(Opcode.REMOVE_CHATROOM_USER)
                                                .writeInt(chatroom.id())
                                                .writeInt(victim.profile().id())
                                );
                            }
                    );
                    break;
            }
        }
    }

    @Override
    public void unapply(){
        final Chatroom chatroom = activeChatrooms.forNameOrId(chatroomId);
        if(chatroom != null){
            if(type() == Type.MUTE)
                chatroom.mute(victimId(), false);
            final Client victim = chatroom.clients().forIdOrUser(victimId());
            if(victim != null){
                switch(type()){
                    case MUTE:
                        victim.write(new Packet(Opcode.ADD_COMMAND_MESSAGE).writeString("You are no longer muted in chatroom " + chatroom.name()));
                        break;
                    case BAN:
                        victim.write(new Packet(Opcode.ADD_COMMAND_MESSAGE).writeString("You are no longer banned in chatroom " + chatroom.name()));
                        break;
                }
            }
        }

        database.punishments().setChatroomPunishmentActive(id(), false);
        database.punishments().removeChatroomPunishment(id());
    }
}
