package jvm.mohawk.chatter.desktopclient.log;

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
import jvm.mohawk.chatter.clientapi.model.log.GeneralLog;
import jvm.mohawk.chatter.desktopclient.DesktopClient;

public class GeneralLogListPane extends BorderPane {

    private static class GeneralLogListCell extends ListCell<GeneralLog> {

        @Override
        protected void updateItem(final GeneralLog log, final boolean empty){
            super.updateItem(log, empty);
            if(log == null){
                setGraphic(null);
                return;
            }
            final Label label = new Label(String.format("[%s] %s", log.timestamp(), log.data()));

            setGraphic(label);
        }
    }

    private final ListView<GeneralLog> list;

    public GeneralLogListPane(){
        list = new ListView<>();
        list.setCellFactory(param -> new GeneralLogListCell());

        setCenter(list);
    }

    public void refresh(){
        list.setItems(DesktopClient.client.generalLogs().sorted(Comparator.comparing(GeneralLog::timestamp)));
        list.getItems().addListener((ListChangeListener<GeneralLog>) c -> {
            list.scrollTo(list.getItems().size()-1);
        });
    }

}
