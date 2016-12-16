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
import java.util.Map;
import jvm.mohawk.chatter.server.net.Opcode;
import jvm.mohawk.chatter.server.net.Packet;
import jvm.mohawk.chatter.server.net.client.Client;
import jvm.mohawk.chatter.server.utils.Utils;


import static jvm.mohawk.chatter.server.Chatter.activeClients;
import static jvm.mohawk.chatter.server.Chatter.database;

public class Punishment {

    public enum Type {

        KICK(1),
        MUTE(2),
        BAN(3);

        private static final Map<Integer, Type> MAP = Utils.map(values(), Type::id);

        private final int id;

        Type(final int id){
            this.id = id;
        }

        public int id(){
            return id;
        }

        public static Type forId(final int id){
            return MAP.get(id);
        }
    }

    private final int id;
    private final Timestamp timestamp;

    private final int punisherId;
    private final String punisherUser;

    private final int victimId;
    private final String victimUser;

    private final Type type;

    private final int durationInSeconds;
    private final String reason;

    private final boolean active;

    private final Timestamp endTimestamp;

    public Punishment(final int id,
                      final Timestamp timestamp,
                      final int punisherId,
                      final String punisherUser,
                      final int victimId,
                      final String victimUser,
                      final Type type,
                      final int durationInSeconds,
                      final String reason,
                      final boolean active){
        this.id = id;
        this.timestamp = timestamp;
        this.punisherId = punisherId;
        this.punisherUser = punisherUser;
        this.victimId = victimId;
        this.victimUser = victimUser;
        this.type = type;
        this.durationInSeconds = durationInSeconds;
        this.reason = reason;
        this.active = active;

        endTimestamp = new Timestamp(timestamp.getTime() + (durationInSeconds * 1000));
    }

    public int id(){
        return id;
    }

    public Timestamp timestamp(){
        return timestamp;
    }

    public Timestamp endTimestamp(){
        return endTimestamp;
    }

    public int punisherId(){
        return punisherId;
    }

    public String punisherUser(){
        return punisherUser;
    }

    public int victimId(){
        return victimId;
    }

    public String victimUser(){
        return victimUser;
    }

    public Type type(){
        return type;
    }

    public int durationInSeconds(){
        return durationInSeconds;
    }

    public String reason(){
        return reason;
    }

    public boolean active(){
        return active;
    }

    public void apply(){
        final Client victim = activeClients.forIdOrUser(victimId);
        if(victim != null){
            switch(type){
                case MUTE:
                    victim.muted(true);
                    victim.write(new Packet(Opcode.ADD_COMMAND_MESSAGE).writeString("You have been muted"));
                    break;
                case BAN:
                    victim.disconnect();
                    break;
            }
        }
    }

    public void unapply(){
        final Client victim = activeClients.forIdOrUser(victimId);
        if(victim != null){
            switch(type){
                case MUTE:
                    victim.muted(false);
                    victim.write(new Packet(Opcode.ADD_COMMAND_MESSAGE).writeString("You are no longer muted!"));
                    break;
            }
        }
        database.punishments().setActive(id, false);
        database.punishments().remove(id);
    }

    @Override
    public boolean equals(final Object obj){
        if(!(obj instanceof Punishment))
            return false;
        final Punishment p = (Punishment) obj;
        return p.id == id;
    }
}
