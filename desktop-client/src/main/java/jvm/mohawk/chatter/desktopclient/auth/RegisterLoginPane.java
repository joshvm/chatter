package jvm.mohawk.chatter.desktopclient.auth;

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

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import jvm.mohawk.chatter.desktopclient.ui.UI;

public class RegisterLoginPane extends BorderPane {

    private final LoginPane loginPane;
    private final RegisterPane registerPane;

    public RegisterLoginPane(){
        getStylesheets().add(UI.css("auth.css"));

        final Label titleLabel = new Label("Chatter");
        titleLabel.getStyleClass().addAll("title");
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        BorderPane.setMargin(titleLabel, new Insets(5, 5, 0, 5));

        loginPane = new LoginPane();
        registerPane = new RegisterPane();

        final HBox centerPane = new HBox();
        BorderPane.setMargin(centerPane, new Insets(5));
        centerPane.setSpacing(5);
        centerPane.getChildren().addAll(loginPane, new Separator(Orientation.VERTICAL), registerPane);

        setTop(titleLabel);
        setCenter(centerPane);
    }

    public LoginPane loginPane(){
        return loginPane;
    }

    public RegisterPane registerPane(){
        return registerPane;
    }

    public void clearForms(){
        loginPane.clearForm();
        registerPane.clearForm();
    }
}
