package jvm.mohawk.chatter.clientapi.model.profile;

/*
  Capstone Project - Chatter
  
  An instant messenger that lets you connect with your friends 
  and meet new people with similar interests.
  
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

import javafx.beans.binding.StringExpression;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import jvm.mohawk.chatter.clientapi.model.Rank;
import jvm.mohawk.chatter.clientapi.model.Status;
import jvm.mohawk.chatter.clientapi.net.Buffer;

/**
 * This is the profile class which holds all of the information related to a profile.
 * it was a design choice to use javafx properties here because it would be easier to bind
 * these data properties to javafx nodes.
 * using javafx properties might be problematic for using this client api in an android app...
 * a better approach might've been to make this class abstract and have different styles of the profile
 * but for my use case, it's fine
 */
public class Profile {

    private final ReadOnlyIntegerProperty id;
    private final SimpleObjectProperty<Rank> rank;

    private final ReadOnlyStringProperty user;

    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;

    private final StringExpression fullName;

    private final SimpleStringProperty picUrl;
    private final SimpleObjectProperty<Image> pic;
    private final SimpleObjectProperty<Status> status;

    public Profile(final int id,
                   final Rank rank,
                   final String user,
                   final String firstName,
                   final String lastName,
                   final String picUrl,
                   final Status status){
        this.id = new ReadOnlyIntegerWrapper(id);
        this.rank = new SimpleObjectProperty<>(rank);
        this.user = new ReadOnlyStringWrapper(user);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.status = new SimpleObjectProperty<>(status);
        this.picUrl = new SimpleStringProperty(picUrl);

        pic = new SimpleObjectProperty<>(new Image(picUrl, false));

        this.picUrl.addListener((ob, o, n) -> reloadPic());

        fullName = this.firstName.concat(" ").concat(this.lastName);
    }

    public ReadOnlyIntegerProperty idProperty(){
        return id;
    }

    public int id(){
        return id.get();
    }

    public SimpleObjectProperty<Rank> rankProperty(){
        return rank;
    }

    public Rank rank(){
        return rank.get();
    }

    public void rank(final Rank rank){
        this.rank.set(rank);
    }

    public ReadOnlyStringProperty userProperty(){
        return user;
    }

    public String user(){
        return user.get();
    }

    public SimpleStringProperty firstNameProperty(){
        return firstName;
    }

    public String firstName(){
        return firstName.get();
    }

    public void firstName(final String firstName){
        this.firstName.set(firstName);
    }

    public SimpleStringProperty lastNameProperty(){
        return lastName;
    }

    public String lastName(){
        return lastName.get();
    }

    public void lastName(final String lastName){
        this.lastName.set(lastName);
    }

    public StringExpression fullNameExpression(){
        return fullName;
    }

    public String fullName(){
        return fullName.get();
    }

    public SimpleObjectProperty<Status> statusProperty(){
        return status;
    }

    public Status status(){
        return status.get();
    }

    public void status(final Status status){
        this.status.set(status);
    }

    public SimpleStringProperty picUrlProperty(){
        return picUrl;
    }

    public String picUrl(){
        return picUrl.get();
    }

    public void picUrl(final String picUrl){
        this.picUrl.set(picUrl);
    }

    public SimpleObjectProperty<Image> picProperty(){
        return pic;
    }

    public Image pic(){
        return pic.get();
    }

    public void reloadPic(){
        pic.set(new Image(picUrl.get(), false));
    }

    @Override
    public boolean equals(final Object obj){
        if(!(obj instanceof Profile))
            return false;
        final Profile p = (Profile) obj;
        return id() == p.id();
    }

    public static Profile deserialize(final Buffer buf){
        return new Profile(
                buf.readInt(),
                Rank.forId(buf.readUnsignedByte()),
                buf.readString(),
                buf.readString(),
                buf.readString(),
                buf.readString(),
                Status.forId(buf.readUnsignedByte())
        );
    }
}
