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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import jvm.mohawk.chatter.server.net.client.Client;
import jvm.mohawk.chatter.server.utils.Utils;


import static jvm.mohawk.chatter.server.Chatter.activeClients;
import static jvm.mohawk.chatter.server.Chatter.service;

public final class PunishmentManager<P extends Punishment> {

    private final List<P> list;

    public PunishmentManager(){
        list = new ArrayList<>();
    }

    public void start(){
        service.scheduleAtFixedRate(() -> {
            if(list.isEmpty())
                return;
            final Iterator<P> itr = list.iterator();
            while(itr.hasNext()){
                final P p = itr.next();
                if(Utils.timestamp().after(p.endTimestamp())){
                    itr.remove();
                    final Client victim = activeClients.forIdOrUser(p.victimId());
                    if(victim != null)
                        p.unapply();
                }
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    public void add(final P p){
        if(!p.active())
            return;
        list.add(p);
    }

    public void remove(final P p){
        list.remove(p);
    }

    public List<Punishment> forVictim(final Object idOrUser){
        return list.stream()
                .filter(p -> Objects.equals(idOrUser, p.victimId()) || Objects.equals(idOrUser, p.victimUser()))
                .collect(Collectors.toList());
    }

    public Punishment forVictim(final Object idOrUser,
                                final Punishment.Type type){
        return list.stream()
                .filter(p -> Objects.equals(idOrUser, p.victimId()) || Objects.equals(idOrUser, p.victimUser()))
                .filter(p -> p.type() == type)
                .findFirst()
                .orElse(null);
    }

}
