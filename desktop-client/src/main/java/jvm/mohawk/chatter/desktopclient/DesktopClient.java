package jvm.mohawk.chatter.desktopclient;

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

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import jvm.mohawk.chatter.clientapi.ChatterClient;
import jvm.mohawk.chatter.clientapi.data.event.EventType;
import jvm.mohawk.chatter.desktopclient.auth.RegisterLoginPane;
import jvm.mohawk.chatter.desktopclient.main.MainPane;
import jvm.mohawk.chatter.desktopclient.ui.UI;
import jvm.mohawk.chatter.desktopclient.welcome.WelcomePane;

public class DesktopClient extends Application {

    public static final ChatterClient.Config CHATTER_CONFIG = new ChatterClient.Config("Default", "localhost", 7495);

    public static boolean closing;
    public static ChatterClient client;

    public static Stage stage;

    private static RegisterLoginPane registerLoginPane;
    private static Scene registerLoginScene;

    private static WelcomePane welcomePane;
    private static Scene welcomeScene;

    private static MainPane mainPane;
    private static Scene mainScene;

    @Override
    public void start(final Stage primaryStage) throws Exception{
        stage = primaryStage;
        stage.setOnCloseRequest(
                e -> {
                    closing = true;
                    if(client != null)
                        client.disconnect();
                }
        );

        registerLoginPane = new RegisterLoginPane();
        registerLoginScene = new Scene(registerLoginPane, 700, 512);

        welcomePane = new WelcomePane();
        welcomeScene = new Scene(welcomePane, 700, 400);

        mainPane = new MainPane();
        mainScene = new Scene(mainPane, 1000, 800);

        toRegisterLoginScene();

        stage.show();
    }

    public static void toRegisterLoginScene(){
        registerLoginPane.clearForms();
        stage.setTitle("Chatter - Register / Login");
        stage.setScene(registerLoginScene);
    }

    public static boolean isAtRegisterLoginScene(){
        return stage.getScene().equals(registerLoginScene);
    }

    public static void toWelcomeScene(){
        client.eventHandler().clientSyncWorker(EventType.ADD_FRIEND, Platform::runLater);
        client.eventHandler().clientSyncWorker(EventType.ADD_FRIEND_REQUEST, Platform::runLater);
        client.eventHandler().clientSyncWorker(EventType.FRIEND_MESSAGE, Platform::runLater);
        client.eventHandler().clientSyncWorker(EventType.CHATROOM_MESSAGE, Platform::runLater);
        client.eventHandler().clientSyncWorker(EventType.ADD_CHATROOM_USER, Platform::runLater);
        client.eventHandler().clientSyncWorker(EventType.REMOVE_CHATROOM_USER, Platform::runLater);
        client.eventHandler().clientSyncWorker(EventType.ADD_CHATROOM, Platform::runLater);
        client.eventHandler().clientSyncWorker(EventType.ADD_COMMAND_MESSAGE, Platform::runLater);
        client.eventHandler().clientSyncWorker(EventType.SET_PROFILE_STATUS, Platform::runLater);
        client.eventHandler().clientSyncWorker(EventType.ADD_GENERAL_LOG, Platform::runLater);
        client.eventHandler().clientSyncWorker(EventType.ADD_LOGIN_LOG, Platform::runLater);
        client.eventHandler().clientSyncWorker(EventType.REMOVE_FRIEND, Platform::runLater);
        client.eventHandler().clientSyncWorker(EventType.REMOVE_FRIEND_REQUEST, Platform::runLater);
        client.eventHandler().clientSyncWorker(EventType.REMOVE_CHATROOM, Platform::runLater);
        client.responseHandler().onGlobalResponse((c, req, resp) -> {
            System.out.printf("%s -> %s: %s%n", req.type(), resp.getClass().getSimpleName(), resp.code());
        });
        client.onDisconnect(de -> {
            client.disconnect();
            if(!DesktopClient.closing){
                Platform.runLater(() -> {
                    DesktopClient.toRegisterLoginScene();
                    mainPane = new MainPane();
                    mainScene.setRoot(mainPane);
                    UI.showMessageDialog(Alert.AlertType.ERROR, "Disconnected", "You have been disconnected from the server!", "Please log back in!");
                });
            }

        });
        welcomePane.refresh();
        stage.setTitle(String.format("Chatter - %s (%s)", client.profile().user(), client.profile().fullName()));
        stage.setScene(welcomeScene);
        client.start();
    }

    public static boolean isAtWelcomeScene(){
        return stage.getScene().equals(welcomeScene);
    }

    public static void toMainScene(){
        mainPane.refresh();
        stage.setScene(mainScene);
    }

    public static boolean isAtMainScreen(){
        return stage.getScene().equals(mainScene);
    }

    public static void main(String[] args){
        launch(DesktopClient.class, args);
    }
}
