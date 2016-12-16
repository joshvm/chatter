package jvm.mohawk.chatter.desktopclient.friendrequest;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;
import jvm.mohawk.chatter.clientapi.data.request.types.AnswerFriendRequest;
import jvm.mohawk.chatter.clientapi.data.request.types.CancelFriendRequest;
import jvm.mohawk.chatter.clientapi.model.friendship.FriendRequest;
import jvm.mohawk.chatter.desktopclient.ui.UI;


import static jvm.mohawk.chatter.desktopclient.DesktopClient.client;

public class FriendRequestListPane extends BorderPane {

    private static class AnswerableFriendRequestListCell extends ListCell<FriendRequest> {

        @Override
        protected void updateItem(final FriendRequest req, final boolean empty){
            super.updateItem(req, empty);
            if(req == null)
                return;
            final BorderPane pane = new BorderPane();
            pane.setLeft(UI.profilePic(req.targetProfile()));
            pane.setCenter(new Label(req.targetProfile().user()));
            final HBox buttons = new HBox();
            buttons.setSpacing(2);
            final Button acceptButton = new Button("Accept");
            acceptButton.setOnAction(e -> {
                client.tryRequest(new AnswerFriendRequest(req, FriendRequest.Answer.ACCEPT));
            });
            final Button declineButton = new Button("Decline");
            declineButton.setOnAction(e -> {
                client.tryRequest(new AnswerFriendRequest(req, FriendRequest.Answer.DECLINE));
            });
            buttons.getChildren().addAll(acceptButton, declineButton);
            pane.setRight(buttons);
            setGraphic(pane);
        }
    }

    private static class PendingFriendRequestListCell extends ListCell<FriendRequest> {

        @Override
        protected void updateItem(final FriendRequest req, final boolean empty){
            super.updateItem(req, empty);
            if(req == null)
                return;
            final BorderPane pane = new BorderPane();
            pane.setLeft(UI.profilePic(req.targetProfile()));
            pane.setCenter(new Label(req.targetProfile().user()));
            final Button cancelButton = new Button("Cancel");
            cancelButton.setOnAction(e -> {
                client.tryRequest(new CancelFriendRequest(req));
            });
            pane.setRight(cancelButton);
            setGraphic(pane);
        }
    }

    private final ListView<FriendRequest> pendingList;
    private final ListView<FriendRequest> answerableList;

    public FriendRequestListPane(){
        pendingList = new ListView<>();
        pendingList.setCellFactory(param -> new PendingFriendRequestListCell());

        final Label pendingLabel = new Label("Pending Friend Requests");
        pendingLabel.setAlignment(Pos.CENTER);
        pendingLabel.setTextAlignment(TextAlignment.CENTER);

        final BorderPane pendingPane = new BorderPane();
        pendingPane.setTop(pendingLabel);
        pendingPane.setCenter(pendingList);

        answerableList = new ListView<>();
        answerableList.setCellFactory(param -> new AnswerableFriendRequestListCell());

        final Label answerableLabel = new Label("Answerable Friend Requests");
        answerableLabel.setAlignment(Pos.CENTER);
        answerableLabel.setTextAlignment(TextAlignment.CENTER);

        final BorderPane answerablePane = new BorderPane();
        answerablePane.setTop(answerableLabel);
        answerablePane.setCenter(answerableList);

        final SplitPane split = new SplitPane();
        split.setOrientation(Orientation.HORIZONTAL);
        split.getItems().addAll(pendingPane, answerablePane);

        setCenter(split);
    }

    public void refresh(){
        pendingList.setItems(client.friendRequestManager().list(FriendRequest.Type.PENDING));
        answerableList.setItems(client.friendRequestManager().list(FriendRequest.Type.ANSWERABLE));
    }
}
