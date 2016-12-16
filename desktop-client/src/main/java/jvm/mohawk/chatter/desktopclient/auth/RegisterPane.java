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

import java.io.File;
import java.time.LocalDate;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import jvm.mohawk.chatter.clientapi.ChatterClient;
import jvm.mohawk.chatter.clientapi.auth.impl.RegisterData;
import jvm.mohawk.chatter.clientapi.auth.impl.RegisterResponse;
import jvm.mohawk.chatter.clientapi.model.profile.ProfileValidation;
import jvm.mohawk.chatter.clientapi.upload.ChatterUploader;
import jvm.mohawk.chatter.clientapi.upload.UploadProfilePictureResponse;
import jvm.mohawk.chatter.desktopclient.DesktopClient;
import jvm.mohawk.chatter.desktopclient.ui.UI;


import static jvm.mohawk.chatter.desktopclient.DesktopClient.stage;

public class RegisterPane extends BorderPane {

    private final FileChooser imgChooser;
    private File picFile;
    private final ImageView picView;

    private final TextField firstNameBox;
    private final TextField lastNameBox;
    private final DatePicker birthDateBox;

    private final TextField userBox;
    private final PasswordField passBox;
    private final PasswordField securityPinBox;

    private final Label statusLabel;
    private final Button registerButton;

    public RegisterPane(){
        getStylesheets().add(UI.css("auth.css"));
        HBox.setHgrow(this, Priority.ALWAYS);

        final Label registerLabel = new Label("Register");
        registerLabel.getStyleClass().add("heading");

        final Label aboutYouLabel = new Label("About You");
        aboutYouLabel.getStyleClass().add("subheading");

        imgChooser = new FileChooser();
        imgChooser.setTitle("Select Profile Picture");
        imgChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pictures", "*.png", "*.jpeg", "*.jpg", "*.gif"));

        final Tooltip imgTip = new Tooltip();

        final Image defaultImage = UI.image("default_profile_pic_64.png");

        picView = new ImageView(UI.image("default_profile_pic_64.png"));
        picView.setFitHeight(64);
        picView.setFitHeight(64);
        picView.setOnMousePressed(
                e -> {
                    if(e.getButton() == MouseButton.PRIMARY){
                        final File file = imgChooser.showOpenDialog(stage);
                        if(file == null)
                            return;
                        try{
                            final String extension = file.getName().substring(file.getName().lastIndexOf('.')+1);
                            final File tmpFile = File.createTempFile(Long.toString(System.currentTimeMillis()), '.'+extension);
                            final Image img64 = new Image(file.toURI().toString(), 64, 64, true, true, false);
                            final Image img512 = new Image(file.toURI().toString(), 512, 512, true, true, false);
                            if(!ImageIO.write(SwingFXUtils.fromFXImage(img512, null), extension, tmpFile))
                                throw new Exception("Error writing image");
                            picFile = tmpFile;
                            picView.setImage(img64);
                            imgTip.setGraphic(new ImageView(img512));
                        }catch(Exception ex){
                            ex.printStackTrace();
                            picFile = null;
                            picView.setImage(defaultImage);
                        }

                    }else{
                        imgTip.show(picView, e.getScreenX(), e.getScreenY());
                    }

                }
        );
        picView.setOnMouseReleased(
                e -> {
                    if(e.getButton() != MouseButton.PRIMARY)
                        imgTip.hide();
                }
        );

        firstNameBox = new TextField();
        firstNameBox.setPromptText("Enter first name");

        lastNameBox = new TextField();
        lastNameBox.setPromptText("Enter last name");

        birthDateBox = new DatePicker(LocalDate.now().minusYears(ProfileValidation.MIN_AGE));

        final Label loginDetailsLabel = new Label("Login Details");
        loginDetailsLabel.getStyleClass().add("subheading");

        userBox = new TextField();
        userBox.setPromptText("Enter user name");

        passBox = new PasswordField();
        passBox.setPromptText("Enter password");

        final Label securityLabel = new Label("Security");
        securityLabel.getStyleClass().add("subheading");

        securityPinBox = new PasswordField();
        securityPinBox.setPromptText("Enter security pin");

        final VBox fieldsBox = new VBox();
        BorderPane.setMargin(fieldsBox, new Insets(0, 0, 5, 0));
        fieldsBox.setSpacing(5);
        fieldsBox.getChildren().addAll(aboutYouLabel, picView, firstNameBox, lastNameBox, birthDateBox, new Separator(Orientation.HORIZONTAL));
        fieldsBox.getChildren().addAll(loginDetailsLabel, userBox, passBox, new Separator(Orientation.HORIZONTAL));
        fieldsBox.getChildren().addAll(securityLabel, securityPinBox);

        statusLabel = new Label();
        statusLabel.setTextAlignment(TextAlignment.CENTER);

        registerButton = new Button("Register");
        registerButton.setOnAction(
                e -> {
                    statusLabel.setText("");
                    final LocalDate date = birthDateBox.getValue();
                    final String user = userBox.getText();
                    String pic = null;
                    if(ProfileValidation.isUserValid(user) && picFile != null){
                        final UploadProfilePictureResponse resp = ChatterUploader.tryUploadProfilePicture(
                                DesktopClient.CHATTER_CONFIG,
                                user,
                                picFile,
                                false
                        );
                        if(resp == null)
                            statusLabel.setText("Unable to upload profile picture");
                        else if(resp.url() == null)
                            statusLabel.setText("Bad profile picture: " + resp.code());
                        else
                            pic = resp.url();
                    }
                    System.out.println("pic: " + pic);
                    final RegisterData data = new RegisterData(
                            pic,
                            firstNameBox.getText(),
                            lastNameBox.getText(),
                            date.getYear(),
                            date.getMonthValue(),
                            date.getDayOfMonth(),
                            userBox.getText(),
                            passBox.getText(),
                            securityPinBox.getText()
                    );
                    try{
                        final RegisterResponse resp = ChatterClient.register(DesktopClient.CHATTER_CONFIG, data);
                        statusLabel.setText(resp.code().msg());
                    }catch(Exception ex){
                        statusLabel.setText("Error connecting to server");
                    }
                }
        );

        final BorderPane bottomPane = new BorderPane();
        bottomPane.setCenter(statusLabel);
        bottomPane.setRight(registerButton);

        setTop(registerLabel);
        setCenter(fieldsBox);
        setBottom(bottomPane);
    }

    public void clearForm(){
        statusLabel.setText("");
        firstNameBox.setText("");
        lastNameBox.setText("");
        birthDateBox.setValue(LocalDate.now().minusYears(ProfileValidation.MIN_AGE));
        userBox.setText("");
        passBox.setText("");
        securityPinBox.setText("");
    }
}
