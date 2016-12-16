package jvm.mohawk.chatter.desktopclient.command;

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
import javafx.collections.ListChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import jvm.mohawk.chatter.clientapi.data.message.Messages;


import static jvm.mohawk.chatter.desktopclient.DesktopClient.client;

public class CommandPane extends BorderPane {

    private final ListView<String> list;

    private final TextField commandBox;
    private Button executeButton;

    public CommandPane(){
        list = new ListView<>();
        list.setPlaceholder(new Label("Type \"help\" to see commands"));

        commandBox = new TextField();
        commandBox.setPromptText("Enter Command Here");
        commandBox.setOnAction(e -> executeButton.fire());

        executeButton = new Button("Execute");
        executeButton.setOnAction(e -> {
            final String cmd = commandBox.getText();
            if(!client.tryMessage(Messages.executeCommand(cmd)))
                client.commandMessages().add("Error executing command");
            commandBox.setText("");
        });

        final BorderPane bottomPane = new BorderPane();
        bottomPane.setCenter(commandBox);
        bottomPane.setRight(executeButton);

        setCenter(list);
        setBottom(bottomPane);
    }

    public void refresh(){
        list.setItems(client.commandMessages());
        list.getItems().addListener((ListChangeListener<String>) c -> {
            list.scrollTo(list.getItems().size()-1);
        });
        client.responseHandler().onGlobalResponse(
                (c, req, resp) -> {
                    Platform.runLater(() -> {
                        client.commandMessages().add(String.format("Request: %s -> %s", req.getClass().getSimpleName(), resp.code()));
                    });

                }
        );
    }
}
