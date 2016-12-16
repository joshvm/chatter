package jvm.mohawk.chatter.desktopclient.auth;

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

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import jvm.mohawk.chatter.clientapi.ChatterClient;
import jvm.mohawk.chatter.clientapi.auth.impl.LoginData;
import jvm.mohawk.chatter.clientapi.auth.impl.LoginResponse;
import jvm.mohawk.chatter.clientapi.auth.impl.RecoverAccountRequestData;
import jvm.mohawk.chatter.clientapi.auth.impl.RecoverAccountRequestResponse;
import jvm.mohawk.chatter.clientapi.model.profile.ProfileValidation;
import jvm.mohawk.chatter.desktopclient.DesktopClient;
import jvm.mohawk.chatter.desktopclient.recover.TryRecoverPane;
import jvm.mohawk.chatter.desktopclient.ui.UI;

public class LoginPane extends BorderPane {

    private final TextField userBox;
    private final PasswordField passBox;

    private final Label statusLabel;
    private final Button loginButton;

    private final Button recoverButton;

    public LoginPane(){
        getStylesheets().add(UI.css("auth.css"));
        HBox.setHgrow(this, Priority.ALWAYS);

        final Label loginLabel = new Label("Login");
        loginLabel.getStyleClass().addAll("heading");

        final Image defaultPic = UI.image("default_profile_128.png");

        final ImageView imageView = new ImageView(defaultPic);
        imageView.setFitWidth(256);
        imageView.setFitHeight(256);
        VBox.setVgrow(imageView, Priority.ALWAYS);
        HBox.setHgrow(imageView, Priority.ALWAYS);

        final Label detailsLabel = new Label("Details");
        detailsLabel.getStyleClass().add("subheading");

        final Tooltip userErrorTip = new Tooltip("Invalid username");
        userErrorTip.getStyleClass().add("errorBox");

        userBox = new TextField();
        userBox.setPromptText("Enter your username");
        userBox.setTooltip(new Tooltip("Login username"));
        userBox.textProperty().addListener(
                (e, o, n) -> {
                    if(n.isEmpty() || ProfileValidation.isUserValid(n)){
                        userBox.getStyleClass().remove("errorBox");
                        userErrorTip.hide();
                        return;
                    }

                    if(userBox.getStyleClass().contains("errorBox"))
                        return;
                    userBox.getStyleClass().add("errorBox");
                    userErrorTip.show(userBox, UI.screenX(userBox, false) + n.length() * 2, UI.screenY(userBox, false) - userBox.getHeight());
                }
        );
        userBox.focusedProperty().addListener(
                (e, o, n) -> {
                    if(!n){
                        try{
                            final String user = userBox.getText();
                            if(user.isEmpty())
                                throw new Exception();
                            final String url = String.format("https://csunix.mohawkcollege.ca/~000320309/chatter/uploads/profile_pictures/%s.jpg", user);
                            final Image img = new Image(url, true);
                            img.errorProperty().addListener(
                                    (ob, eo, en) -> {
                                        if(en)
                                            imageView.setImage(defaultPic);
                                    }
                            );
                            imageView.setImage(img);
                        }catch(Exception ex){
                            imageView.setImage(defaultPic);
                        }
                    }
                }
        );

        final Tooltip passErrorTip = new Tooltip("Invalid password");
        passErrorTip.getStyleClass().add("errorBox");

        passBox = new PasswordField();
        passBox.setPromptText("Enter your password");
        passBox.setTooltip(new Tooltip("Login password"));
        passBox.textProperty().addListener(
                (e, o, n) -> {
                    if(n.isEmpty() || ProfileValidation.isPassValid(n)){
                        passBox.getStyleClass().remove("errorBox");
                        passErrorTip.hide();
                        return;
                    }
                    if(passBox.getStyleClass().contains("errorBox"))
                        return;
                    passBox.getStyleClass().add("errorBox");
                    passErrorTip.show(passBox, UI.screenX(passBox, false) + n.length() * 2, UI.screenY(passBox, false) - passBox.getHeight());
                }
        );

        final VBox fieldsBox = new VBox();
        fieldsBox.setSpacing(5);
        fieldsBox.getChildren().addAll(detailsLabel, userBox, passBox);

        final BorderPane centerPane = new BorderPane();
        BorderPane.setMargin(centerPane, new Insets(0, 0, 5, 0));
        centerPane.setCenter(imageView);
        centerPane.setBottom(fieldsBox);

        statusLabel = new Label();
        statusLabel.setTextAlignment(TextAlignment.CENTER);

        loginButton = new Button("Login");
        loginButton.disableProperty().bind(userErrorTip.showingProperty().or(passErrorTip.showingProperty()));
        loginButton.setOnAction(
                e -> {
                    statusLabel.setText("Logging in...");
                    try{
                        LoginData data = new LoginData(userBox.getText(), passBox.getText());
                        LoginResponse resp = ChatterClient.login(DesktopClient.CHATTER_CONFIG, data);
                        if(resp.code() == LoginResponse.Code.SECURITY_PIN_REQUIRED){
                            final String securityPin = UI.textInputDialog(
                                    "Security Pin Required",
                                    "Security Pin for " + data.user(),
                                    "Security Pin:"
                            );
                            if(securityPin == null || !ProfileValidation.isSecurityPinValid(securityPin)){
                                UI.showMessageDialog(Alert.AlertType.ERROR, "Error", "Invalid Security Pin", "Security pin must be an integer value!");
                                return;
                            }
                            data = new LoginData(userBox.getText(), passBox.getText(), securityPin);
                            resp = ChatterClient.login(DesktopClient.CHATTER_CONFIG, data);
                        }
                        statusLabel.setText(resp.code().msg());
                        if(resp.code() == LoginResponse.Code.SUCCESS){
                            DesktopClient.client = resp.client();
                            DesktopClient.toWelcomeScene();
                        }
                    }catch(Exception ex){
                        statusLabel.setText("Error connecting to server");
                    }
                }
        );

        recoverButton = new Button("Recover");
        recoverButton.setOnAction(e -> {
            final String user = userBox.getText();
            final RecoverAccountRequestData data = new RecoverAccountRequestData(user);
            try{
                final RecoverAccountRequestResponse resp = ChatterClient.recoverAccountRequest(
                        DesktopClient.CHATTER_CONFIG, data
                );
                if(resp.code() != RecoverAccountRequestResponse.Code.SUCCESS){
                    UI.showMessageDialog(Alert.AlertType.ERROR, "Error", "Error Creating Recovery Request", resp.code().name());
                    return;
                }
                final Stage stage = new Stage();
                final TryRecoverPane pane = new TryRecoverPane(resp.profileId(), user, resp.securityQuestionId(), resp.securityQuestion());
                stage.setScene(new Scene(pane, 400, 150));
                stage.setTitle("Recover Acount: " + user);
                stage.showAndWait();
            }catch(Exception ex){
                statusLabel.setText("Error connecting to server");
            }
        });

        final BorderPane bottomPane = new BorderPane();
        BorderPane.setMargin(bottomPane, new Insets(0, 0, 5, 0));
        bottomPane.setCenter(statusLabel);
        bottomPane.setRight(loginButton);
        bottomPane.setLeft(recoverButton);

        setTop(loginLabel);
        setCenter(centerPane);
        setBottom(bottomPane);
    }

    public void clearForm(){
        statusLabel.setText("");
        userBox.setText("");
        passBox.setText("");
    }
}
