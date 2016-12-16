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

import java.util.Comparator;
import javafx.collections.ListChangeListener;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import jvm.mohawk.chatter.clientapi.data.request.Requests;
import jvm.mohawk.chatter.clientapi.model.conversation.ChatMessage;
import jvm.mohawk.chatter.clientapi.model.friendship.Friendship;
import jvm.mohawk.chatter.desktopclient.ui.UI;


import static jvm.mohawk.chatter.desktopclient.DesktopClient.client;

public class FriendshipListPane extends BorderPane {

    private static class FriendshipCell extends ListCell<Friendship> {

        @Override
        protected void updateItem(final Friendship f, final boolean empty){
            super.updateItem(f, empty);
            setText(null);
            if(f == null || empty){
                setGraphic(null);
                return;
            }
            final ContextMenu menu = new ContextMenu();

            final MenuItem deleteItem = new MenuItem("Delete");
            deleteItem.setGraphic(new ImageView(UI.image("minus_16.png")));
            deleteItem.setOnAction(e -> {
                client.tryRequest(Requests.removeFriend(f));
            });

            menu.getItems().add(deleteItem);

            setContextMenu(menu);

            final Label nameLabel = new Label(f.profile().fullName());
            nameLabel.setTooltip(new Tooltip(String.format("%s (%s)", f.profile().user(), f.profile().status())));

            final Label lastMsgLabel = new Label();
            lastMsgLabel.setTextOverrun(OverrunStyle.ELLIPSIS);
            f.conversation().messages().addListener((ListChangeListener<ChatMessage>) c -> {
               if(f.conversation().messages().isEmpty())
                   return;
                final ChatMessage lastMsg = f.conversation().messages().get(f.conversation().messages().size()-1);
                if(lastMsg.sender().equals(client.profile()))
                    lastMsgLabel.setText("You: " + lastMsg.text());
                else
                    lastMsgLabel.setText(lastMsg.text());
            });

            final BorderPane content = new BorderPane();
            content.setLeft(UI.profilePic(f.profile()));
            content.setCenter(nameLabel);
            content.setBottom(lastMsgLabel);

            setGraphic(content);
        }
    }

    private static final Comparator<Friendship> SORTING = Comparator.<Friendship>comparingInt(f -> f.profile().status().id())
            .thenComparingInt(f -> f.profile().rank().id())
            .thenComparing(f -> f.profile().fullName());

    private final ListView<Friendship> list;

    public FriendshipListPane(){
        list = new ListView<>();
        list.setCellFactory(e -> new FriendshipCell());

        setCenter(list);
    }

    public ListView<Friendship> list(){
        return list;
    }

    public void refresh(){
        list.setItems(client.friendshipManager().list().sorted(SORTING));
    }
}
