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

import java.text.SimpleDateFormat;
import java.util.Comparator;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import jvm.mohawk.chatter.clientapi.data.request.Requests;
import jvm.mohawk.chatter.clientapi.model.chatroom.Chatroom;
import jvm.mohawk.chatter.clientapi.model.conversation.ChatMessage;
import jvm.mohawk.chatter.clientapi.model.conversation.Conversable;
import jvm.mohawk.chatter.clientapi.model.friendship.Friendship;
import jvm.mohawk.chatter.desktopclient.ui.UI;


import static jvm.mohawk.chatter.desktopclient.DesktopClient.client;

public class ConversationPane extends BorderPane {

    private static final SimpleDateFormat TIMESTAMP = new SimpleDateFormat("'['MMM dd yyyy '@' hh:mm:ss a']'");

    private class ChatMessageCell extends ListCell<ChatMessage> {

        @Override
        protected void updateItem(final ChatMessage msg, final boolean empty){
            super.updateItem(msg, empty);
            if(msg == null || empty){
                setGraphic(null);
                return;
            }

            final Label msgLabel = new Label(String.format("%s: %s", client.profile().equals(msg.sender()) ? "You" : msg.sender().firstName(), msg.text()));
            msgLabel.getStyleClass().add(client.profile().equals(msg.sender()) ? "grey" : "white");
            msgLabel.setTextAlignment(TextAlignment.LEFT);
            msgLabel.setWrapText(true);
            msgLabel.setTooltip(new Tooltip(TIMESTAMP.format(msg.dateTime())));

            final BorderPane contentPane = new BorderPane();
            BorderPane.setAlignment(msgLabel, Pos.CENTER_LEFT);
            contentPane.getStyleClass().add(client.profile().equals(msg.sender()) ? "grey" : "white");
            contentPane.setCenter(msgLabel);

            getStyleClass().add(client.profile().equals(msg.sender()) ? "grey" : "white");
            setGraphic(contentPane);
        }
    }

    private final ChatMessage.Type type;
    private Conversable conversable;

    private final ListView<ChatMessage> list;

    private final TextField inputBox;
    private final Button sendButton;

    public ConversationPane(final ChatMessage.Type type){
        this.type = type;
        getStylesheets().add(UI.css("convo.css"));

        list = new ListView<>();
        list.setCellFactory(e -> new ChatMessageCell());

        inputBox = new TextField();
        inputBox.setPromptText("Enter Text To Send Message");

        sendButton = new Button();
        sendButton.setGraphic(new ImageView(UI.image("send_message_16.png")));
        sendButton.setOnAction(e -> {
            final String text = inputBox.getText();
            if(type == ChatMessage.Type.FRIENDSHIP)
                client.tryRequest(Requests.sendFriendMessage((Friendship)conversable, text));
            else if(type == ChatMessage.Type.CHATROOM)
                client.tryRequest(Requests.sendChatroomMessage((Chatroom)conversable, text));
            inputBox.setText("");
        });

        inputBox.setOnAction(e -> {
            sendButton.fire();
        });

        final BorderPane inputPane = new BorderPane();
        inputPane.setCenter(inputBox);
        inputPane.setRight(sendButton);

        setCenter(list);
        setBottom(inputPane);
    }

    public void refresh(final Conversable conversable){
        this.conversable = conversable;
        if(conversable == null){
            setTop(null);
            list.setItems(null);
            return;
        }
        BorderPane top = null;
        if(conversable instanceof Friendship)
            top = new FriendshipPane((Friendship)conversable);
        else if(conversable instanceof Chatroom)
            top = new ChatroomPane((Chatroom)conversable);
        BorderPane.setAlignment(top, Pos.CENTER);
        setTop(top);
        list.setItems(conversable.conversation().messages().sorted(Comparator.comparing(ChatMessage::dateTime)));
        list.getItems().addListener((ListChangeListener<ChatMessage>) c -> {
            list.scrollTo(list.getItems().size()-1);
        });
    }
}
