package jvm.mohawk.chatter.clientapi.model.chatroom;

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

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleStringProperty;
import jvm.mohawk.chatter.clientapi.model.conversation.ChatMessage;
import jvm.mohawk.chatter.clientapi.model.conversation.Conversable;
import jvm.mohawk.chatter.clientapi.model.conversation.Conversation;
import jvm.mohawk.chatter.clientapi.net.Buffer;

/**
 * this class represents a chatroom.
 */
public class Chatroom implements Conversable {

    private final ReadOnlyIntegerProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty description;

    private final ChatroomUserManager users;
    private final Conversation conversation;

    public Chatroom(final int id,
                    final String name,
                    final String description){
        this.id = new ReadOnlyIntegerWrapper(id);
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);

        users = new ChatroomUserManager(this);

        conversation = new Conversation();
    }

    public ReadOnlyIntegerProperty idProperty(){
        return id;
    }

    public int id(){
        return id.get();
    }

    public SimpleStringProperty nameProperty(){
        return name;
    }

    public String name(){
        return name.get();
    }

    public void name(final String name){
        this.name.set(name);
    }

    public SimpleStringProperty descriptionProperty(){
        return description;
    }

    public String description(){
        return description.get();
    }

    public void description(final String description){
        this.description.set(description);
    }

    public ChatroomUserManager users(){
        return users;
    }

    @Override
    public ChatMessage.Type messageType(){
        return ChatMessage.Type.CHATROOM;
    }

    @Override
    public Conversation conversation(){
        return conversation;
    }

    @Override
    public boolean equals(final Object obj){
        if(!(obj instanceof Chatroom))
            return false;
        final Chatroom c = (Chatroom) obj;
        return id() == c.id();
    }

    public static Chatroom deserialize(final Buffer buffer){
        return new Chatroom(
                buffer.readInt(),
                buffer.readString(),
                buffer.readString()
        );
    }
}
