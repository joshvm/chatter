package jvm.mohawk.chatter.server.net.client;

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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jvm.mohawk.chatter.server.model.profile.Profile;
import jvm.mohawk.chatter.server.net.Packet;

public class ClientManager {

    private final Map<Object, Client> map;
    private final List<Client> list;

    public ClientManager(){
        map = new LinkedHashMap<>();
        list = new ArrayList<>();
    }

    public List<Client> list(){
        return list;
    }

    public void add(final Client client){
        map.put(client.profile().id(), client);
        map.put(client.profile().user(), client);
        list.add(client);
    }

    public void remove(final Client client){
        map.remove(client.profile().id());
        map.remove(client.profile().user());
        list.remove(client);
    }

    public Client forIdOrUser(final Object profileIdOrUser){
        return map.get(profileIdOrUser);
    }

    public Client forProfile(final Profile profile){
        return map.get(profile.id());
    }

    public boolean contains(final Client client){
        return forIdOrUser(client.profile().id()) != null;
    }

    public boolean contains(final Profile profile){
        return forProfile(profile) != null;
    }

    public Set<Client> set(){
        return new LinkedHashSet<>(map.values());
    }

    public void writeLater(final Packet pkt){
        set().forEach(c -> c.writeLater(pkt));
    }

    public void write(final Packet pkt){
        set().forEach(c -> c.write(pkt));
    }
}
