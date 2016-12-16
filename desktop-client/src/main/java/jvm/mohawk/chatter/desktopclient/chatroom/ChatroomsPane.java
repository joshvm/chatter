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

import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import jvm.mohawk.chatter.clientapi.model.conversation.ChatMessage;
import jvm.mohawk.chatter.desktopclient.chat.ConversationPane;

public class ChatroomsPane extends BorderPane {

    private final JoinChatroomPane joinChatroomPane;
    private final ChatroomListPane chatroomListPane;
    private final ConversationPane convoPane;
    private final ChatroomUserListPane chatroomUserListPane;

    public ChatroomsPane(){

        joinChatroomPane = new JoinChatroomPane();

        chatroomListPane = new ChatroomListPane();

        final BorderPane leftPane = new BorderPane();
        leftPane.setTop(joinChatroomPane);
        leftPane.setCenter(chatroomListPane);

        convoPane = new ConversationPane(ChatMessage.Type.CHATROOM);

        chatroomUserListPane = new ChatroomUserListPane();

        chatroomListPane.list().getSelectionModel()
                .selectedItemProperty()
                .addListener((ob, o, n) -> {
                    convoPane.refresh(n);
                    chatroomUserListPane.refresh(n);
                });

        final SplitPane split = new SplitPane();
        split.setOrientation(Orientation.HORIZONTAL);
        split.getItems().addAll(leftPane, convoPane, chatroomUserListPane);

        setCenter(split);
    }

    public void refresh(){
        chatroomListPane.refresh();
        convoPane.refresh(null);
        chatroomUserListPane.refresh(null);
        joinChatroomPane.refresh();
    }
}
