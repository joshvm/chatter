package jvm.mohawk.chatter.desktopclient.friendship;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import jvm.mohawk.chatter.clientapi.data.request.Requests;
import jvm.mohawk.chatter.clientapi.model.profile.Profile;
import jvm.mohawk.chatter.desktopclient.ui.UI;


import static jvm.mohawk.chatter.desktopclient.DesktopClient.client;

public class SearchedProfilePane extends BorderPane {

    private final Label userLabel;
    private final Label fullNameLabel;

    private final Button addFriendButton;

    public SearchedProfilePane(final SearchProfilePane searchProfilePane,
                               final Profile profile){
        userLabel = new Label(profile.user());

        fullNameLabel = new Label(profile.fullName());

        final VBox fields = new VBox();
        fields.setSpacing(2);
        fields.getChildren().addAll(userLabel, fullNameLabel);

        addFriendButton = new Button("Add Friend");
        addFriendButton.setGraphic(new ImageView(UI.image("profile_add_32.png")));
        addFriendButton.setTextAlignment(TextAlignment.CENTER);
        addFriendButton.setContentDisplay(ContentDisplay.TOP);
        addFriendButton.setOnAction(
                e -> {
                    client.tryRequest(Requests.createFriendRequest(profile));
                }
        );

        final HBox buttons = new HBox();
        buttons.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(buttons, Pos.CENTER);
        buttons.setSpacing(5);
        buttons.getChildren().addAll(addFriendButton);

        client.responseHandler().onCreateFriendRequestResponse(
                (c, req, resp) -> {
                    Platform.runLater(() -> {
                        UI.showMessageDialog(Alert.AlertType.INFORMATION, "Send Friend Request", "Response", resp.code().toString());
                    });
                }
        );

        final BorderPane profilePane = new BorderPane();
        profilePane.setLeft(UI.profilePic(profile));
        profilePane.setCenter(fields);

        final BorderPane centerPane = new BorderPane();
        centerPane.setLeft(profilePane);
        centerPane.setCenter(buttons);

        final Button closeButton = new Button();
        closeButton.setGraphic(new ImageView(UI.image("x_16.png")));
        BorderPane.setAlignment(closeButton, Pos.TOP_CENTER);
        closeButton.setOnAction(e -> searchProfilePane.reset());

        setCenter(centerPane);
        setRight(closeButton);
    }

}
