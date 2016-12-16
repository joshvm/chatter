package jvm.mohawk.chatter.clientapi.model.conversation;

/*
  Capstone Project - Chatter
  
  An instant messenger that lets you connect with your friends 
  and meet new people with similar interests.
  
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

import java.sql.Timestamp;
import java.util.Map;
import jvm.mohawk.chatter.clientapi.model.profile.Profile;
import jvm.mohawk.chatter.clientapi.utils.Utils;

/**
 * representins a single chat message between the client and either a friendship or in a conversation
 */
public class ChatMessage {

    public enum Type {

        FRIENDSHIP(1),
        CHATROOM(2);

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
    private final Timestamp dateTime;
    private final Profile sender;
    private final String text;

    public ChatMessage(final int id,
                       final Timestamp dateTime,
                       final Profile sender,
                       final String text){
        this.id = id;
        this.dateTime = dateTime;
        this.sender = sender;
        this.text = text;
    }

    public int id(){
        return id;
    }

    public Timestamp dateTime(){
        return dateTime;
    }

    public Profile sender(){
        return sender;
    }

    public String text(){
        return text;
    }
}
