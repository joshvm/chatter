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

import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import jvm.mohawk.chatter.clientapi.model.conversation.ChatMessage;
import jvm.mohawk.chatter.desktopclient.chat.ConversationPane;

public class FriendshipsPane extends BorderPane {

    private final SearchProfilePane searchProfilePane;

    private final FriendshipListPane listPane;
    private final ConversationPane convoPane;

    public FriendshipsPane(){
        searchProfilePane = new SearchProfilePane();

        listPane = new FriendshipListPane();
        listPane.setPrefWidth(200);

        final BorderPane leftPane = new BorderPane();
        leftPane.setTop(searchProfilePane);
        leftPane.setCenter(listPane);

        convoPane = new ConversationPane(ChatMessage.Type.FRIENDSHIP);

        listPane.list().getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (e, o, n) -> {
                            convoPane.refresh(n);
                        }
                );

        final SplitPane friendshipPane = new SplitPane();
        friendshipPane.setOrientation(Orientation.HORIZONTAL);
        friendshipPane.getItems().addAll(leftPane, convoPane);

        setCenter(friendshipPane);
    }

    public void refresh(){
        listPane.refresh();
        convoPane.refresh(null);
        searchProfilePane.refresh();
    }
}
