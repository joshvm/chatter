package jvm.mohawk.chatter.desktopclient.recover;

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

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import jvm.mohawk.chatter.clientapi.ChatterClient;
import jvm.mohawk.chatter.clientapi.auth.impl.TryRecoverAccountData;
import jvm.mohawk.chatter.clientapi.auth.impl.TryRecoverAccountResponse;
import jvm.mohawk.chatter.desktopclient.DesktopClient;
import jvm.mohawk.chatter.desktopclient.ui.UI;

public class TryRecoverPane extends BorderPane {

    public TryRecoverPane(final int profileId,
                          final String profileUser,
                          final int securityQuestionId,
                          final String securityQuestion){
        getStylesheets().add(UI.css("recovery.css"));

        final Label headingLabel = new Label("Recovery For: " + profileUser);
        headingLabel.getStyleClass().add("heading");
        BorderPane.setAlignment(headingLabel, Pos.CENTER);

        final BorderPane securityPane = new BorderPane();

        final Label questionLabel = new Label();

        final TextField answerBox = new TextField();

        if(securityQuestionId != -1){
            questionLabel.setText(securityQuestion);
            answerBox.setPromptText("Answer");
            securityPane.setCenter(questionLabel);
            securityPane.setRight(answerBox);
        }else{
            questionLabel.setText("No security question found");
            securityPane.setCenter(questionLabel);
        }

        final Label securityPinLabel = new Label("Enter Security Pin");

        final TextField securityPinBox = new TextField();

        final BorderPane pinPane = new BorderPane();
        pinPane.setCenter(securityPinLabel);
        pinPane.setRight(securityPinBox);

        final Label passLabel = new Label("Enter New Pass");

        final PasswordField passBox = new PasswordField();

        final BorderPane passPane = new BorderPane();
        passPane.setCenter(passLabel);
        passPane.setRight(passBox);

        final BorderPane centerPane = new BorderPane();
        centerPane.setTop(securityPane);
        centerPane.setCenter(pinPane);
        centerPane.setBottom(passPane);

        final Label responseLabel = new Label();

        final Button recoverButton = new Button("Recover");
        recoverButton.setOnAction(e -> {
            final String answer = answerBox.getText();
            final String pin = securityPinBox.getText();
            final String pass = passBox.getText();
            final TryRecoverAccountData data = new TryRecoverAccountData(
                    profileId,
                    pin,
                    securityQuestionId,
                    answer,
                    pass
            );
            try{
                final TryRecoverAccountResponse resp = ChatterClient.tryRecoverAccount(
                        DesktopClient.CHATTER_CONFIG, data
                );
                responseLabel.setText(resp.code().name());
            }catch(Exception ex){
                ex.printStackTrace();
                responseLabel.setText("Error recovering account");
            }
        });

        final BorderPane bottomPane = new BorderPane();
        bottomPane.setCenter(responseLabel);
        bottomPane.setRight(recoverButton);

        setTop(headingLabel);
        setCenter(centerPane);
        setBottom(bottomPane);
    }
}
