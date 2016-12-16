package jvm.mohawk.chatter.desktopclient.ui;

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

import java.io.File;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import jvm.mohawk.chatter.clientapi.data.request.Requests;
import jvm.mohawk.chatter.clientapi.model.Rank;
import jvm.mohawk.chatter.clientapi.model.Status;
import jvm.mohawk.chatter.clientapi.model.profile.Profile;


import static jvm.mohawk.chatter.desktopclient.DesktopClient.client;
import static jvm.mohawk.chatter.desktopclient.DesktopClient.stage;

public final class UI {

    private UI(){}

    public static File file(final String dir,
                              final String name){
        return new File(UI.class.getResource(String.format("%s/%s", dir, name)).toExternalForm());
    }

    public static Image image(final String name){
        return new Image(UI.class.getResourceAsStream("images/"+name));
    }

    public static Font font(final double size){
        return Font.font(size);
    }

    public static String css(final String name){
        return UI.class.getResource("css/"+name).toExternalForm();
    }

    public static double screenX(final Node c, final boolean addWidth){
        return c.getScene().getWindow().getX()
                + c.getScene().getX()
                + c.localToScene(0, 0).getX()
                + (addWidth ? (c instanceof Control ? ((Control)c).getWidth() : 0) : 0);
    }

    public static double screenY(final Node c, final boolean addHeight){
        return c.getScene().getWindow().getY()
                + c.getScene().getY()
                + c.localToScene(0, 0).getY()
                + (addHeight ? (c instanceof Control ? ((Control)c).getHeight() : 0) : 0);
    }

    public static void showMessageDialog(final Alert.AlertType type,
                                         final String title,
                                         final String header,
                                         final String msg){
        final Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static String textInputDialog(final String title,
                                         final String header,
                                         final String prompt){
        final TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(prompt);
        return dialog.showAndWait().orElse(null);
    }

    public static Node profilePic(final Profile profile,
                                  final boolean editStatus){
        return profilePic(profile, profile.rankProperty(), editStatus);
    }

    public static Node profilePic(final Profile profile){
        return profilePic(profile, false);
    }

    public static Node profilePic(final Profile profile,
                                  final ObjectProperty<Rank> rankProperty,
                                  final boolean editStatus){
        final ImageView img = new ImageView(profile.pic());
        img.setFitWidth(64);
        img.setFitHeight(64);

        final Circle imgCircle = new Circle(32);
        imgCircle.setFill(new ImagePattern(img.getImage()));

        final ContextMenu popup = new ContextMenu();
        for(final Status status : Status.values()){
            final MenuItem item = new MenuItem(status.name());
            item.setOnAction(e -> {
                client.tryRequest(Requests.updateProfileStatus(status));
            });
            popup.getItems().add(item);
        }

        final Circle circle = new Circle(12);
        circle.getStyleClass().add(profile.status().name().toLowerCase());
        circle.setTranslateX(26);
        circle.setTranslateY(24);
        if(editStatus)
            circle.setOnMousePressed(e -> popup.show(stage, e.getScreenX(), e.getScreenY()));
        profile.statusProperty().addListener(
                (ob, o, n) -> {
                    circle.getStyleClass().clear();
                    circle.getStyleClass().add(n.name().toLowerCase());
                }
        );

        final StackPane pane = new StackPane();
        pane.getStylesheets().add(UI.css("status.css"));
        pane.getChildren().addAll(imgCircle, circle);

        String rankImgRes;

        switch(rankProperty.get()){
            case HELPER:
                rankImgRes = "info_24.png";
                break;
            case MODERATOR:
                rankImgRes = "silver_medal_24.png";
                break;
            case ADMINISTRATOR:
                rankImgRes = "gold_medal_24.png";
                break;
            case OWNER:
                rankImgRes = "gold_crown_24.png";
                break;
            default:
                rankImgRes = null;
        }

        if(rankImgRes != null){
            final ImageView rankImg = new ImageView(UI.image(rankImgRes));
            rankImg.setTranslateX(26);
            rankImg.setTranslateY(-24);
            pane.getChildren().add(rankImg);
        }

        BorderPane.setMargin(pane, new Insets(5, 10, 5, 5));

        return pane;
    }

    public static Tab tab(final String text,
                          final Image img,
                          final Node content){
        final Tab tab = new Tab(text, content);
        tab.setGraphic(new ImageView(img));
        return tab;
    }
}
