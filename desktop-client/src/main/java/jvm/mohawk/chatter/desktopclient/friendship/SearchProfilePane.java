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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import jvm.mohawk.chatter.clientapi.data.request.Requests;
import jvm.mohawk.chatter.desktopclient.ui.UI;


import static jvm.mohawk.chatter.desktopclient.DesktopClient.client;

public class SearchProfilePane extends BorderPane {

    private final TextField userBox;
    private final Button searchButton;

    public SearchProfilePane(){
        userBox = new TextField();
        userBox.setPromptText("Profile User");

        searchButton = new Button("Search");
        searchButton.setGraphic(new ImageView(UI.image("search_16.png")));
        searchButton.setOnAction(e -> {
            final String user = userBox.getText();
            client.tryRequest(Requests.getProfileByUser(user));
        });

        userBox.setOnAction(e -> searchButton.fire());

        final BorderPane searchPane = new BorderPane();
        searchPane.setCenter(userBox);
        searchPane.setRight(searchButton);

        setTop(searchPane);
    }

    public void reset(){
        setCenter(null);
        userBox.setText("");
    }

    public void refresh(){
        client.responseHandler().onGetProfileByUserResponse(
                (c, req, resp) -> {
                    Platform.runLater(() -> {
                        if(resp.resultProfile() != null){
                            setCenter(new SearchedProfilePane(this, resp.resultProfile()));
                        }else{
                            UI.showMessageDialog(Alert.AlertType.INFORMATION, "Search Profile", "Response", resp.code().toString());
                        }
                    });

                }
        );
    }
}
