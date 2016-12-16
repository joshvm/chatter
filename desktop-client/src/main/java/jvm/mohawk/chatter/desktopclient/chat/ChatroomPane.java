package jvm.mohawk.chatter.desktopclient.chat;

/*
  Capstone Project - Chatter
  
  An instant messenger that lets you connect with your friends 
  and meet new people with similar interests.
  
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: desktop-client
  
  Developed By: Josh Maione (000320309)
*/

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import jvm.mohawk.chatter.clientapi.model.chatroom.Chatroom;

public class ChatroomPane extends BorderPane {

    private final Chatroom chatroom;

    public ChatroomPane(final Chatroom chatroom){
        this.chatroom = chatroom;

        final Label nameLabel = new Label(chatroom.name());
        nameLabel.getStyleClass().add("h1");
        nameLabel.setTooltip(new Tooltip(chatroom.description()));
        BorderPane.setAlignment(nameLabel, Pos.CENTER);
        nameLabel.setTextAlignment(TextAlignment.CENTER);

        setCenter(nameLabel);
    }
}
