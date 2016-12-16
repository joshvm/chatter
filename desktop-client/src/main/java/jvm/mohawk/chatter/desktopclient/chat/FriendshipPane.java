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

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import jvm.mohawk.chatter.clientapi.model.friendship.Friendship;
import jvm.mohawk.chatter.desktopclient.ui.UI;

public class FriendshipPane extends BorderPane {

    private final Friendship friendship;

    public FriendshipPane(final Friendship friendship){
        this.friendship = friendship;

        final Node profilePic = UI.profilePic(friendship.profile());

        final Label label = new Label(friendship.profile().fullName(), profilePic);
        label.setGraphicTextGap(10);

        setCenter(label);
    }
}
