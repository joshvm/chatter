package jvm.mohawk.chatter.desktopclient.welcome;

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
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import jvm.mohawk.chatter.clientapi.model.log.LoginLog;
import jvm.mohawk.chatter.desktopclient.DesktopClient;

public class LoginLogListPane extends BorderPane {

    private static class LoginLogListCell extends ListCell<LoginLog> {

        @Override
        protected void updateItem(final LoginLog log, final boolean empty){
            super.updateItem(log, empty);
            if(log == null){
                setGraphic(null);
                return;
            }
            final Label timestampLabel = new Label(log.timestamp().toString());

            final Label ipLabel = new Label("IP: " + log.ip());

            final Label deviceLabel = new Label("Device: " + log.device());

            final VBox pane = new VBox();
            pane.setSpacing(2);
            pane.getChildren().addAll(timestampLabel, ipLabel, deviceLabel);

            setGraphic(pane);
        }
    }

    private final ListView<LoginLog> list;

    public LoginLogListPane(){
        list = new ListView<>();
        list.setCellFactory(param -> new LoginLogListCell());

        setCenter(list);
    }

    public void refresh(){
        list.setItems(DesktopClient.client.loginLogs().sorted(Comparator.comparing(LoginLog::timestamp)));
        list.getItems().addListener((ListChangeListener<LoginLog>) c -> {
            list.scrollTo(list.getItems().size()-1);
        });
    }
}
