package jvm.mohawk.chatter.desktopclient.main;

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

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jvm.mohawk.chatter.desktopclient.DesktopClient;
import jvm.mohawk.chatter.desktopclient.chatroom.ChatroomsPane;
import jvm.mohawk.chatter.desktopclient.command.CommandPane;
import jvm.mohawk.chatter.desktopclient.createchatroom.CreateChatroomPane;
import jvm.mohawk.chatter.desktopclient.friendrequest.FriendRequestListPane;
import jvm.mohawk.chatter.desktopclient.friendship.FriendshipsPane;
import jvm.mohawk.chatter.desktopclient.log.GeneralLogListPane;
import jvm.mohawk.chatter.desktopclient.self.SelfProfilePane;
import jvm.mohawk.chatter.desktopclient.ui.UI;
import jvm.mohawk.chatter.desktopclient.welcome.LoginLogListPane;


import static jvm.mohawk.chatter.desktopclient.DesktopClient.client;

public class MainPane extends BorderPane {

    private final CommandPane commandPane;

    private final SelfProfilePane selfProfilePane;
    private final FriendshipsPane friendshipsPane;

    private final FriendRequestListPane friendRequestListPane;

    private final ChatroomsPane chatroomsPane;

    private final CreateChatroomPane createChatroomPane;

    private final GeneralLogListPane generalLogListPane;
    private final LoginLogListPane loginLogListPane;

    public MainPane(){
        getStylesheets().add(UI.css("main.css"));

        selfProfilePane = new SelfProfilePane();
        BorderPane.setAlignment(selfProfilePane, Pos.CENTER);

        createChatroomPane = new CreateChatroomPane();

        final Stage createChatroomStage = new Stage();
        createChatroomStage.setTitle("Create Chatroom");
        createChatroomStage.initOwner(DesktopClient.stage);
        createChatroomStage.initModality(Modality.APPLICATION_MODAL);
        createChatroomStage.setScene(new Scene(createChatroomPane, 400, 300));

        commandPane = new CommandPane();
        commandPane.setPrefHeight(300);

        friendshipsPane = new FriendshipsPane();

        friendRequestListPane = new FriendRequestListPane();

        generalLogListPane = new GeneralLogListPane();

        loginLogListPane = new LoginLogListPane();

        chatroomsPane = new ChatroomsPane();

        final Button createChatroomButton = new Button("Create Chatroom");
        createChatroomButton.setTextAlignment(TextAlignment.CENTER);
        createChatroomButton.setContentDisplay(ContentDisplay.TOP);
        createChatroomButton.setGraphic(new ImageView(UI.image("profiles_add_32.png")));
        createChatroomButton.setOnAction(e -> {
            createChatroomStage.showAndWait();
        });

        final Button logoutButton = new Button("Logout");
        logoutButton.setTextAlignment(TextAlignment.CENTER);
        logoutButton.setContentDisplay(ContentDisplay.TOP);
        logoutButton.setGraphic(new ImageView(UI.image("logout_32.png")));
        logoutButton.setOnAction(e -> client.disconnect());

        final ToggleButton consoleButton = new ToggleButton("Console");
        consoleButton.setTextAlignment(TextAlignment.CENTER);
        consoleButton.setContentDisplay(ContentDisplay.TOP);
        consoleButton.setGraphic(new ImageView(UI.image("console_32.png")));

        final HBox buttons = new HBox();
        buttons.setSpacing(5);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        buttons.getChildren().add(createChatroomButton);
        buttons.getChildren().add(consoleButton);
        buttons.getChildren().add(logoutButton);

        final BorderPane top = new BorderPane();
        top.setCenter(selfProfilePane);
        top.setRight(buttons);

        final TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.getTabs().add(UI.tab("Friends", UI.image("profile_16.png"), friendshipsPane));
        tabs.getTabs().add(UI.tab("Friend Requests", UI.image("profile_checkmark_16.png"), friendRequestListPane));
        tabs.getTabs().add(UI.tab("Chatrooms", UI.image("profiles_16.png"), chatroomsPane));
        tabs.getTabs().add(UI.tab("Logs", UI.image("clock_16.png"), generalLogListPane));
        tabs.getTabs().add(UI.tab("Login History", UI.image("clock_16.png"), loginLogListPane));

        final BorderPane center = new BorderPane();
        center.setTop(top);
        center.setCenter(tabs);

        consoleButton.selectedProperty()
                .addListener((ob, o, n) -> {
                    if(n){
                        final SplitPane split = new SplitPane();
                        split.setOrientation(Orientation.VERTICAL);
                        split.getItems().addAll(center, commandPane);
                        setCenter(split);
                    }else{
                        setCenter(center);
                    }
                });

        setCenter(center);
    }

    public void refresh(){
        commandPane.refresh();
        selfProfilePane.refresh();
        friendshipsPane.refresh();
        friendRequestListPane.refresh();
        chatroomsPane.refresh();
        createChatroomPane.refresh();
        generalLogListPane.refresh();
        loginLogListPane.refresh();
    }
}
