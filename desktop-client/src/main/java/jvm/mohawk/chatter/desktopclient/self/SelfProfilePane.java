package jvm.mohawk.chatter.desktopclient.self;

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

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import jvm.mohawk.chatter.desktopclient.ui.UI;


import static jvm.mohawk.chatter.desktopclient.DesktopClient.client;

public class SelfProfilePane extends BorderPane {

    private final Label userLabel;
    private final Label fullNameLabel;

    public SelfProfilePane(){
        userLabel = new Label();

        fullNameLabel = new Label();

        final VBox fields = new VBox();
        fields.setSpacing(2);
        fields.getChildren().addAll(userLabel, fullNameLabel);

        setCenter(fields);
    }

    public void refresh(){
        setLeft(UI.profilePic(client.profile(), true));
        userLabel.textProperty().bind(client.profile().userProperty());
        fullNameLabel.textProperty().bind(client.profile().fullNameExpression());
    }
}
