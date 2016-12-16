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
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import jvm.mohawk.chatter.clientapi.model.chatroom.Chatroom;
import jvm.mohawk.chatter.clientapi.model.chatroom.ChatroomUser;
import jvm.mohawk.chatter.desktopclient.ui.UI;

public class ChatroomUserListPane extends BorderPane {

    private static class ChatroomUserListCell extends ListCell<ChatroomUser> {

        @Override
        protected void updateItem(final ChatroomUser user, final boolean empty){
            super.updateItem(user, empty);
            if(user == null){
                setGraphic(null);
                return;
            }

            final Node profilePic = UI.profilePic(user.profile(), user.rankProperty(), false);

            final Label nameLabel = new Label(user.profile().fullName());
            nameLabel.setTooltip(new Tooltip(user.profile().user()));

            final BorderPane content = new BorderPane();
            content.setLeft(profilePic);
            content.setCenter(nameLabel);

            setGraphic(content);
        }
    }

    private static final Comparator<ChatroomUser> SORTING = Comparator.<ChatroomUser>comparingInt(u -> u.profile().status().id())
            .thenComparingInt(u -> u.profile().rank().id())
            .thenComparing(u -> u.profile().fullName());

    private Chatroom chatroom;

    private final ListView<ChatroomUser> list;

    public ChatroomUserListPane(){
        list = new ListView<>();
        list.setCellFactory(param -> new ChatroomUserListCell());

        setCenter(list);
    }

    public void refresh(final Chatroom chatroom){
        this.chatroom = chatroom;
        if(chatroom != null){
            list.setItems(chatroom.users().list().sorted(SORTING));
        }else{
            list.setItems(null);
        }
    }
}
