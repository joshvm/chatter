package jvm.mohawk.chatter.desktopclient.chatroom;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import jvm.mohawk.chatter.clientapi.data.request.Requests;
import jvm.mohawk.chatter.clientapi.data.response.types.JoinChatroomResponse;
import jvm.mohawk.chatter.desktopclient.ui.UI;


import static jvm.mohawk.chatter.desktopclient.DesktopClient.client;

public class JoinChatroomPane extends BorderPane {

    private final TextField searchNameBox;
    private final Button joinButton;

    public JoinChatroomPane(){
        searchNameBox = new TextField();
        searchNameBox.setPromptText("Chatroom Name");

        joinButton = new Button("Join");
        joinButton.setGraphic(new ImageView(UI.image("profiles_add_16.png")));
        joinButton.setOnAction(e -> {
            final String name = searchNameBox.getText();
            searchNameBox.setText("");
            client.tryRequest(Requests.getChatroomByName(name));
        });

        searchNameBox.setOnAction(e -> joinButton.fire());

        setCenter(searchNameBox);
        setRight(joinButton);
    }

    public void refresh(){
        client.responseHandler().onGetChatroomByNameResponse(
                (c, req, resp) -> {
                    Platform.runLater(() -> {
                        if(resp.resultChatroom() != null){
                            client.tryRequest(Requests.joinChatroom(resp.resultChatroom()));
                        }else{
                            UI.showMessageDialog(Alert.AlertType.INFORMATION, "Join Chatroom", "Response", resp.code().name());
                        }
                    });
                }
        );
        client.responseHandler().onJoinChatroomResponse(
                (c, req, resp) -> {
                    if(resp.code() == JoinChatroomResponse.Code.SUCCESS)
                        return;
                    Platform.runLater(() -> {
                        UI.showMessageDialog(Alert.AlertType.INFORMATION, "Join Chatroom", "Response", resp.code().name());
                    });
                }
        );
    }
}
