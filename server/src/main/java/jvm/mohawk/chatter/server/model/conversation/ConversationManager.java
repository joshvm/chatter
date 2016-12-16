package jvm.mohawk.chatter.server.model.conversation;

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
import jvm.mohawk.chatter.server.net.Opcode;
import jvm.mohawk.chatter.server.net.Packet;
import jvm.mohawk.chatter.server.net.client.Client;


import static jvm.mohawk.chatter.server.Chatter.database;

public class ConversationManager {

    private final Client client;
    private final ChatMessageType type;

    private final Map<Integer, Boolean> loaded;

    public ConversationManager(final Client client,
                               final ChatMessageType type){
        this.client = client;
        this.type = type;

        loaded = new HashMap<>();
    }

    public Client client(){
        return client;
    }

    public ChatMessageType type(){
        return type;
    }

    public void load(final int id){
        if(loaded.getOrDefault(id, false))
            return;
        database.conversations().get(type, id)
                .forEach(c -> client.write(new Packet(Opcode.ADD_CHAT_MESSAGE, c.serialize())));
        loaded.put(id, true);
    }
}
