package jvm.mohawk.chatter.desktopclient.createchatroom;

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
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import jvm.mohawk.chatter.clientapi.data.request.Requests;
import jvm.mohawk.chatter.clientapi.model.chatroom.ChatroomValidation;


import static jvm.mohawk.chatter.desktopclient.DesktopClient.client;

public class CreateChatroomPane extends BorderPane {

    private final TextField nameBox;
    private final TextField descBox;

    private final Button createButton;
    private final Button clearButton;

    private Label responseLabel;

    public CreateChatroomPane(){
        final Label headingLabel = new Label("Create Chatroom");

        nameBox = new TextField();
        nameBox.setPromptText("Name");

        descBox = new TextField();
        descBox.setPromptText("Description");

        final VBox fieldsBox = new VBox();
        fieldsBox.setSpacing(2);
        fieldsBox.getChildren().addAll(nameBox, descBox);

        createButton = new Button("Create");
        createButton.setOnAction(e -> {
            final String name = nameBox.getText();
            final String desc = descBox.getText();
            if(!ChatroomValidation.isValidName(name)){
                responseLabel.setText("Invalid name. Letters+numbers only");
                return;
            }
            if(!ChatroomValidation.isValidDesc(desc)){
                responseLabel.setText("Invalid description.");
                return;
            }
            client.tryRequest(Requests.createChatroom(name, desc));
        });

        clearButton = new Button("Clear");
        clearButton.setOnAction(e -> {
            nameBox.setText("");
            descBox.setText("");
        });

        final HBox buttonsBox = new HBox();
        buttonsBox.setSpacing(2);
        buttonsBox.getChildren().addAll(createButton, clearButton);

        responseLabel = new Label();
        responseLabel.setTextAlignment(TextAlignment.CENTER);
        BorderPane.setAlignment(responseLabel, Pos.CENTER);

        final BorderPane centerPane = new BorderPane();
        centerPane.setTop(headingLabel);
        centerPane.setCenter(fieldsBox);
        centerPane.setBottom(buttonsBox);

        setCenter(centerPane);
        setBottom(responseLabel);
    }

    public void refresh(){
        client.responseHandler().onCreateChatroomResponse(
                (c, req, resp) -> {
                    Platform.runLater(() -> {
                        responseLabel.setText(resp.code().toString());
                    });
                }
        );
    }
}
