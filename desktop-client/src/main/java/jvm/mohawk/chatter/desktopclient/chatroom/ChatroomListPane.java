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

import java.util.Comparator;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import jvm.mohawk.chatter.clientapi.data.request.Requests;
import jvm.mohawk.chatter.clientapi.model.chatroom.Chatroom;
import jvm.mohawk.chatter.desktopclient.ui.UI;


import static jvm.mohawk.chatter.desktopclient.DesktopClient.client;

public class ChatroomListPane extends BorderPane {

    private static class ChatroomListCell extends ListCell<Chatroom> {

        @Override
        protected void updateItem(final Chatroom chatroom, final boolean empty){
            super.updateItem(chatroom, empty);
            if(chatroom == null || empty){
                setGraphic(null);
                return;
            }

            final ContextMenu menu = new ContextMenu();

            final MenuItem leaveItem = new MenuItem("Leave");
            leaveItem.setGraphic(new ImageView(UI.image("minus_16.png")));
            leaveItem.setOnAction(e -> {
                client.tryRequest(Requests.leaveChatroom(chatroom));
            });
            menu.getItems().add(leaveItem);

            setContextMenu(menu);

            final Label nameLabel = new Label(chatroom.name());
            nameLabel.setGraphic(new ImageView(UI.image("profiles_16.png")));

            final BorderPane content = new BorderPane();
            content.setCenter(nameLabel);

            setGraphic(content);
            setTooltip(new Tooltip(chatroom.description()));
        }
    }

    private final ListView<Chatroom> list;

    public ChatroomListPane(){
        list = new ListView<>();
        list.setCellFactory(param -> new ChatroomListCell());

        setCenter(list);
    }

    public ListView<Chatroom> list(){
        return list;
    }

    public void refresh(){
        list.setItems(client.chatroomManager().list().sorted(Comparator.comparing(Chatroom::name)));
    }
}
