package jvm.mohawk.chatter.desktopclient.welcome;

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

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import jvm.mohawk.chatter.desktopclient.DesktopClient;
import jvm.mohawk.chatter.desktopclient.ui.UI;


import static jvm.mohawk.chatter.desktopclient.DesktopClient.client;

public class WelcomePane extends BorderPane {

    private final Label welcomeLabel;

    private final ImageView profilePicView;

    private final LoginLogListPane loginLogListPane;

    private final Button logoutButton;
    private final Button startButton;

    public WelcomePane(){
        getStylesheets().add(UI.css("welcome.css"));

        welcomeLabel = new Label();
        welcomeLabel.getStyleClass().add("welcomeLabel");
        BorderPane.setAlignment(welcomeLabel, Pos.CENTER);

        logoutButton = new Button("Logout");
        logoutButton.setOnAction(
                e -> {
                    client.disconnect();
                    client = null;
                    Platform.runLater(DesktopClient::toRegisterLoginScene);
                }
        );

        startButton = new Button("Start Chatting!");
        startButton.setOnAction(
                e -> {
//                    client.start();
                    DesktopClient.toMainScene();
                }
        );

        final HBox buttons = new HBox();
        buttons.setAlignment(Pos.CENTER_RIGHT);
        buttons.setSpacing(5);
        buttons.getChildren().addAll(logoutButton, startButton);

        profilePicView = new ImageView();
        profilePicView.setFitWidth(256);
        profilePicView.setFitHeight(256);
        BorderPane.setAlignment(profilePicView, Pos.CENTER);

        loginLogListPane = new LoginLogListPane();

        final Label loginListLabel = new Label("Login History");
        BorderPane.setAlignment(loginListLabel, Pos.CENTER);

        final BorderPane loginPane = new BorderPane();
        loginPane.setTop(loginListLabel);
        loginPane.setCenter(loginLogListPane);

        final BorderPane center = new BorderPane();
        center.setLeft(profilePicView);
        center.setCenter(loginPane);

        setTop(welcomeLabel);
        setCenter(center);
        setBottom(buttons);
    }

    public void refresh(){
        welcomeLabel.setText(String.format("Welcome, %s!", client.profile().firstName()));
        profilePicView.setImage(client.profile().pic());
        loginLogListPane.refresh();
    }
}
