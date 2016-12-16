package jvm.mohawk.chatter.server.model.chatroom;

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

import java.util.HashMap;
import java.util.Map;
import jvm.mohawk.chatter.server.model.Rank;
import jvm.mohawk.chatter.server.model.profile.Profile;
import jvm.mohawk.chatter.server.model.punishment.ChatroomPunishment;
import jvm.mohawk.chatter.server.model.punishment.PunishmentManager;
import jvm.mohawk.chatter.server.net.Buffer;
import jvm.mohawk.chatter.server.net.client.ClientManager;


import static jvm.mohawk.chatter.server.Chatter.database;

public class Chatroom {

    private final int id;
    private final String name;
    private final String description;

    private final Map<Integer, Rank> ranks;

    private final ClientManager clients;

    private final Map<Integer, Boolean> mutedMap;

    private final PunishmentManager<ChatroomPunishment> punishments;

    public Chatroom(final int id,
                    final String name,
                    final String description){
        this.id = id;
        this.name = name;
        this.description = description;

        ranks = new HashMap<>();

        clients = new ClientManager();

        mutedMap = new HashMap<>();

        punishments = new PunishmentManager<>();
        database.punishments().forChatroom(id).forEach(punishments::add);
        punishments.start();
    }

    public int id(){
        return id;
    }

    public String name(){
        return name;
    }

    public String description(){
        return description;
    }

    public void mute(final int profileId, final boolean muted){
        mutedMap.put(profileId, muted);
    }

    public boolean muted(final int profileId){
        return mutedMap.getOrDefault(profileId, false);
    }

    public Rank rankFor(final Profile profile){
        return ranks.getOrDefault(profile.id(), Rank.NONE);
    }

    public void rank(final Profile profile, final Rank rank){
        ranks.put(profile.id(), rank);
    }

    public ClientManager clients(){
        return clients;
    }

    public PunishmentManager<ChatroomPunishment> punishments(){
        return punishments;
    }

    public Buffer serialize(){
        return new Buffer()
                .writeInt(id)
                .writeString(name)
                .writeString(description);
    }
}
