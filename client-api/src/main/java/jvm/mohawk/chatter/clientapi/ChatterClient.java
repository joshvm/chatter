package jvm.mohawk.chatter.clientapi;

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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jvm.mohawk.chatter.clientapi.auth.impl.KickData;
import jvm.mohawk.chatter.clientapi.auth.impl.KickResponse;
import jvm.mohawk.chatter.clientapi.auth.impl.LoginData;
import jvm.mohawk.chatter.clientapi.auth.impl.LoginResponse;
import jvm.mohawk.chatter.clientapi.auth.impl.RecoverAccountRequestData;
import jvm.mohawk.chatter.clientapi.auth.impl.RecoverAccountRequestResponse;
import jvm.mohawk.chatter.clientapi.auth.impl.RegisterData;
import jvm.mohawk.chatter.clientapi.auth.impl.RegisterResponse;
import jvm.mohawk.chatter.clientapi.auth.impl.TryRecoverAccountData;
import jvm.mohawk.chatter.clientapi.auth.impl.TryRecoverAccountResponse;
import jvm.mohawk.chatter.clientapi.data.Data;
import jvm.mohawk.chatter.clientapi.data.event.Event;
import jvm.mohawk.chatter.clientapi.data.event.EventHandler;
import jvm.mohawk.chatter.clientapi.data.message.Message;
import jvm.mohawk.chatter.clientapi.data.request.Request;
import jvm.mohawk.chatter.clientapi.data.request.RequestType;
import jvm.mohawk.chatter.clientapi.data.response.Response;
import jvm.mohawk.chatter.clientapi.data.response.ResponseHandler;
import jvm.mohawk.chatter.clientapi.model.chatroom.ChatroomManager;
import jvm.mohawk.chatter.clientapi.model.friendship.FriendRequestManager;
import jvm.mohawk.chatter.clientapi.model.friendship.FriendshipManager;
import jvm.mohawk.chatter.clientapi.model.log.GeneralLog;
import jvm.mohawk.chatter.clientapi.model.log.LoginLog;
import jvm.mohawk.chatter.clientapi.model.profile.Profile;
import jvm.mohawk.chatter.clientapi.model.profile.ProfileValidation;
import jvm.mohawk.chatter.clientapi.net.Opcode;
import jvm.mohawk.chatter.clientapi.net.Packet;
import jvm.mohawk.chatter.clientapi.utils.Utils;

/**
 * This is the main class that the end user of this API must use in order to establish and communicate with the server.
 * This class responsible for managing the connection and registering event/response listeners.
 * an instance of this class means that you have an actual connection to the server (and are logged in as a valid profile).
 * there are various static methods in this class which are used to communicate with the server without being logged in a valid profile.
 * methods that are used to register, login, and recover accounts.
 */
public final class ChatterClient {

    /**
     * The config class (holds details for connecting to the server)
     */
    public static class Config {

        //the point of the name variable was to allow the client to manage multiple configs
        //was going to introduce a "server selector" where all of the chatter server communicates
        //with the server selector, and then you get to pick a server you want to connect to
        //but did not get implemented, so right now, name is essentially pointless
        private final String name;
        private final String host;
        private final int port;

        private final String uploadProfilePicScript;

        public Config(final String name,
                      final String host,
                      final int port){
            this.name = name;
            this.host = host;
            this.port = port;

            //the script used to upload profile pics
            //don't really like how this is hardcoded as i believe the server should send the url
            //(as profile pic uploading happens client side to reduce server stress)
            //but this is fine...
            uploadProfilePicScript = "https://csunix.mohawkcollege.ca/~000320309/chatter/upload_profile_picture.php";
//            uploadProfilePicScript = "http://localhost/chatter/upload_profile_picture.php";
        }

        public String name(){
            return name;
        }

        public String host(){
            return host;
        }

        public int port(){
            return port;
        }

        public String uploadProfilePicScript(){
            return uploadProfilePicScript;
        }
    }

    /**
     * the listener used to listen for when the connection is lost between the server
     */
    public interface DisconnectListener {

        void onDisconnect(final ChatterClient client);
    }

    public static boolean debug = false;

    private final Config config;
    private final Connection con;

    private final Profile profile; //this is the user's profile that gets logged in with

    //considering requests yield responses, the request is "cached" so it can be used
    //in the response listener (you have access to the request which yielded the response)
    //this is mostly just used for client sync stuff (to ensure the client stays in sync)
    //for example - there is a client sync listener for sending chat messages
    //if the response yields success, the server doesn't need to send the chat message back to the client
    //considering the client already has the message it sent, so it's an optimization
    private final Map<RequestType, Request> pendingRequestMap;

    //the response handler (contains callbacks for listening for various different responses)
    private ResponseHandler responseHandler;

    //the event handler (contains callbacks for listening for various different events)
    private EventHandler eventHandler;

    //the disconnection callback (used when the connection is lost)
    private DisconnectListener disconnectListener;

    //manages friend requests (gets populated automatically by client sync listeners)
    private final FriendRequestManager friendRequestManager;
    //manages friends (gets populated automatically by client sync listeners)
    private final FriendshipManager friendshipManager;

    //manages chatrooms you are currently in (gets populated automatically by client sync listeners)
    private final ChatroomManager chatroomManager;

    //the command/console response messages (gets populated automatically by client sync listeners)
    private final ObservableList<String> commandMessages;

    //the logs list (gets populated automatically by client sync listeners)
    private final ObservableList<GeneralLog> generalLogs;
    //the login logs list (gets populated automatically by the client sync listeners)
    private final ObservableList<LoginLog> loginLogs;

    /**
     *
     * private instantiation as we don't want the user to directly instantiate this class.
     * this constructor is invoked in the static login method.
     *
     * @param config the configuration (used to connect to the server)
     * @param con the connection used
     * @param profile the profile that you are logged in with
     */
    private ChatterClient(final Config config,
                          final Connection con,
                          final Profile profile){
        this.config = config;
        this.con = con;
        this.profile = profile;

        //initialize everything...
        pendingRequestMap = new HashMap<>();
        responseHandler = new ResponseHandler();

        eventHandler = new EventHandler();

        friendRequestManager = new FriendRequestManager();
        friendshipManager = new FriendshipManager();

        chatroomManager = new ChatroomManager();

        commandMessages = FXCollections.observableArrayList();

        generalLogs = FXCollections.observableArrayList();
        loginLogs = FXCollections.observableArrayList();
    }

    /**
     * initializes this chatter client instance
     * basically adds listeners to fire the response/event listeners (and disconnection listener)
     */
    private void init(){
        con.listener = new Connection.Listener() {
            @Override
            public void onRead(final Data data){
                if(data instanceof Response){
                    final Response response = (Response) data;
                    final Request pendingRequest = pendingRequestMap.remove(response.type());
                    //pending request shouldn't be null but should probably ignore anyway...
                    if(pendingRequest == null)
                        return;
                    debug("Read response: %s (%s)", response.type(), response.getClass().getName());
                    responseHandler.fireAll(ChatterClient.this, pendingRequest, response);
                }else if(data instanceof Event){
                    final Event event = (Event) data;
                    debug("Read event: %s (%s)", event.type(), event.getClass().getName());
                    eventHandler.fireAll(ChatterClient.this, event);
                }
            }

            @Override
            public void onDisconnect(){
                if(disconnectListener != null)
                    disconnectListener.onDisconnect(ChatterClient.this);
            }
        };
    }

    /**
     *
     * @return the configuration used to connect to the server
     */
    public Config config(){
        return config;
    }

    /**
     *
     * @return the profile that is currently associated with this chatter client instance (logged in user)
     */
    public Profile profile(){
        return profile;
    }

    /**
     * closes the connection to the server
     */
    public void disconnect(){
        con.disconnect();
    }

    /**
     *
     * @return true if the client is connected to the server, false otherwise
     */
    public boolean connected(){
        return con.connected();
    }

    /**
     * this method must be called by the end user when they are ready to start receiving packets.
     * this is kind of a glory method which allows the user to add listeners to the event/response managers
     * before the packets are being received.
     * note: the connection is already established at this point, it just doesn't start reading until this method is called
     */
    public void start(){
        if(con.started)
            return;
        init();
        con.startThread();
    }

    /**
     *
     * @return the disconnection listener, if any
     */
    public DisconnectListener disconnectListener(){
        return disconnectListener;
    }

    /**
     *
     * sets the disconnection listener
     *
     * @param disconnectListener the disconnection listener
     */
    public void onDisconnect(final DisconnectListener disconnectListener){
        this.disconnectListener = disconnectListener;
    }

    /**
     *
     * @return the current response handler
     */
    public ResponseHandler responseHandler(){
        return responseHandler;
    }

    /**
     *
     * sets the respone handler
     *
     * @param responseHandler the response handler
     */
    public void responseHandler(final ResponseHandler responseHandler){
        this.responseHandler = responseHandler;
    }

    /**
     *
     * @param type the request type
     * @return the pending request for that type, if any
     */
    public Request pendingRequest(final RequestType type){
        return pendingRequestMap.get(type);
    }

    /**
     *
     * considering every request yields a single response, you can only send one request of a certain type a time.
     * when you send a request of a certain type you have to wait until the response is read until
     * you can send another request of that certain type again.
     *
     * @param request the request
     * @return true if the client can send that request
     */
    public boolean canRequest(final Request request){
        return pendingRequest(request.type()) == null;
    }

    /**
     *
     * attempts to send the request to the server
     *
     * @param request the request to be sent
     * @return true if the request was successfully sent
     * @throws IOException if there is an error sending the request
     */
    public boolean request(final Request request) throws IOException {
        if(!canRequest(request)) //can't send the request because response is pending
            return false;
        con.send(request.serialize());
        pendingRequestMap.put(request.type(), request); //put the request in the pending map
        return true;
    }

    /**
     *
     * @param request the request to be sent
     * @return true if the request was successfully sent, false if otherwise (or if an error was thrown)
     */
    public boolean tryRequest(final Request request){
        try{
            return request(request);
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @return the current event handler
     */
    public EventHandler eventHandler(){
        return eventHandler;
    }

    /**
     *
     * sets the new event handler
     *
     * @param eventHandler the new event handler
     */
    public void eventHandler(final EventHandler eventHandler){
        this.eventHandler = eventHandler;
    }

    /**
     *
     * attempts to send the message to the server
     * messages don't yield responses so they can be sent however frequent the user desires
     *
     * @param msg the message to be sent
     * @throws IOException if an error was thrown while trying to send the message
     */
    public void message(final Message msg) throws IOException {
        con.send(msg.serialize());
    }

    /**
     *
     * @param msg the message to be sent
     * @return true if successfully sent the message, false otherwise or if an error was thrown
     */
    public boolean tryMessage(final Message msg){
        try{
            message(msg);
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @return the friend request manager (auto populated by client sync)
     */
    public FriendRequestManager friendRequestManager(){
        return friendRequestManager;
    }

    /**
     *
     * @return the current friend manager (auto populated by client sync)
     */
    public FriendshipManager friendshipManager(){
        return friendshipManager;
    }

    /**
     *
     * @return the chatroom manager (auto populated by client sync)
     */
    public ChatroomManager chatroomManager(){
        return chatroomManager;
    }

    /**
     *
     * @return the command response messages (auto populated by client sync)
     */
    public ObservableList<String> commandMessages(){
        return commandMessages;
    }

    /**
     *
     * @return the log manager (auto populated by client sync)
     */
    public ObservableList<GeneralLog> generalLogs(){
        return generalLogs;
    }

    /**
     *
     * @return the login log manager (auto populated by client sync)
     */
    public ObservableList<LoginLog> loginLogs(){
        return loginLogs;
    }

    /**
     *
     * static method used to register an account
     *
     * @param config the configuration used to connect to the server
     * @param data the registration data used
     * @return a register response
     * @throws IOException if an error is thrown
     */
    public static RegisterResponse register(final Config config,
                                            final RegisterData data) throws IOException {
        RegisterResponse.Code preValidCode = RegisterResponse.Code.SUCCESS;
        if(!ProfileValidation.isFirstNameValid(data.firstName()))
            preValidCode = RegisterResponse.Code.INVALID_FIRST_NAME;
        else if(!ProfileValidation.isLastNameValid(data.lastName()))
            preValidCode = RegisterResponse.Code.INVALID_LAST_NAME;
        else if(!ProfileValidation.isBirthDateValid(data.birthYear(), data.birthMonth(), data.birthDay()))
            preValidCode = RegisterResponse.Code.INVALID_BIRTH_DATE;
        else if(!ProfileValidation.isUserValid(data.user()))
            preValidCode = RegisterResponse.Code.INVALID_USER;
        else if(!ProfileValidation.isPassValid(data.pass()))
            preValidCode = RegisterResponse.Code.INVALID_PASS;
        else if(!ProfileValidation.isSecurityPinValid(data.securityPin()))
            preValidCode = RegisterResponse.Code.INVALID_SECURITY_PIN;
        //some pre validation code which checks the validity of the data before sending it off to the server
        //this can be seen as a premature optimization but there's no need to send data if we already know
        //it isn't going to be valid
        if(preValidCode != RegisterResponse.Code.SUCCESS)
            return new RegisterResponse(config, data, preValidCode);
        final Connection con = new Connection(config.host, config.port); //create connection to server
        final Packet registerPacket = new Packet(Opcode.REGISTER) //build the register packet
                .writeString(Utils.device())
                .writeString(data.pic() != null ? data.pic() : "")
                .writeString(data.firstName())
                .writeString(data.lastName())
                .writeShort(data.birthYear())
                .writeByte(data.birthMonth())
                .writeByte(data.birthDay())
                .writeString(data.user())
                .writeString(data.pass())
                .writeString(data.securityPin());
        con.send(registerPacket); //send the register packet
        final int response = con.readForOpcode(Opcode.REGISTER_RESPONSE)
                .readUnsignedByte(); //read the response code for the register response
        con.disconnect();
        return new RegisterResponse(config, data, RegisterResponse.Code.forId(response));
    }

    /**
     *
     * static method used for logging in to an account
     *
     * @param config the configuration used to connect to the server
     * @param data the login data
     * @return a login response
     * @throws IOException if an error is thrown
     */
    public static LoginResponse login(final Config config,
                                      final LoginData data) throws IOException {
        //some pre validation to check the validity of the credentials before sending the packet
        LoginResponse.Code preValidCode = LoginResponse.Code.SUCCESS;
        if(!ProfileValidation.isUserValid(data.user()))
            preValidCode = LoginResponse.Code.INVALID_USER;
        else if(!ProfileValidation.isPassValid(data.pass()))
            preValidCode = LoginResponse.Code.INVALID_PASS;
        if(preValidCode != LoginResponse.Code.SUCCESS) //we know the data isn't valid at this point
            return new LoginResponse(config, data, preValidCode, null);
        final Connection con = new Connection(config.host, config.port); //establish a connection
        final Packet loginPacket = new Packet(Opcode.LOGIN)
                .writeString(Utils.device())
                .writeString(data.user())
                .writeString(data.pass())
                .writeString(data.securityPin()); //build the login packet
        con.send(loginPacket); //send the packet
        final int response = con.readForOpcode(Opcode.LOGIN_RESPONSE)
                .readUnsignedByte(); //read the response code
        final LoginResponse.Code code = LoginResponse.Code.forId(response);
        if(code != LoginResponse.Code.SUCCESS){
            con.disconnect(); //if there was troubles logging in, close the connection
            return new LoginResponse(config, data, code, null);
        }
        //at this point, they have successfully logged in but the server sends an initialization packet
        //which just holds the profile you logged in with
        final Packet initPacket = con.readForOpcode(Opcode.INIT);
        final Profile profile = Profile.deserialize(initPacket); //decode that packet into a readable model class
        final ChatterClient client = new ChatterClient(config, con, profile); //at this point an instance of chatter client can be instantiated as we have a valid profile
        con.client = client; //associate the client with the connection
        return new LoginResponse(config, data, code, client);
    }

    /**
     *
     * this method is used to kick a user off an account assuming you provide the correct security pin
     *
     * @param config the configuration used to connect to the server
     * @param data the data
     * @return a kick response
     * @throws IOException if an error is thrown
     */
    public static KickResponse kick(final Config config,
                                    final KickData data) throws IOException {
        //pre validation code to check validity of the data
        //no point in sending the packet if we already know the data isn't valid
        KickResponse.Code preValidateCode = KickResponse.Code.SUCCESS;
        if(!ProfileValidation.isUserValid(data.profileUser()))
            preValidateCode = KickResponse.Code.INVALID_USER;
        else if(!ProfileValidation.isSecurityPinValid(data.securityPin()))
            preValidateCode = KickResponse.Code.INVALID_SECURITY_PIN;
        if(preValidateCode != KickResponse.Code.SUCCESS)
            return new KickResponse(config, data, preValidateCode);
        final Connection con = new Connection(config.host, config.port); //establish connection
        con.send(
                new Packet(Opcode.KICK)
                    .writeString(data.profileUser())
                    .writeString(data.securityPin())
        ); //send the kick packet
        final int codeId = con.readForOpcode(Opcode.KICK_RESPONSE)
                .readUnsignedByte(); //read the response
        con.disconnect();
        final KickResponse.Code code = KickResponse.Code.forId(codeId);
        return new KickResponse(config, data, code);
    }

    /**
     *
     * recovery is a 2 stage request; this is the first stage
     * the first stage is letting the server know you're trying to recover an account
     * some additional information is sent with this request such as any security questions the profile might've set
     *
     * @param config the configuration used to connect to the server
     * @param data the data
     * @return a recover account request response
     * @throws IOException if an error is thrown
     */
    public static RecoverAccountRequestResponse recoverAccountRequest(final Config config,
                                                                      final RecoverAccountRequestData data) throws IOException {
        if(!ProfileValidation.isUserValid(data.user())) //pre validation
            return new RecoverAccountRequestResponse(config, data, RecoverAccountRequestResponse.Code.USER_NOT_FOUND);
        final Connection con = new Connection(config.host, config.port); //establish a connection
        con.send(
                new Packet(Opcode.RECOVER_ACCOUNT_REQUEST)
                    .writeString(data.user())
        ); //send the request packet
        final Packet responsePkt = con.readForOpcode(Opcode.RECOVER_ACCOUNT_REQUEST_RESPONSE); //read the response packet
        con.disconnect(); //close the connection as we don't need to keep it open
        final RecoverAccountRequestResponse.Code code = RecoverAccountRequestResponse.Code.forId(responsePkt.readUnsignedByte());
        if(code != RecoverAccountRequestResponse.Code.SUCCESS)
            return new RecoverAccountRequestResponse(config, data, code);
        //this is the extra data the server sends
        final int profileId = responsePkt.readInt();
        final int securityQuestionId = responsePkt.readInt();
        final String securityQuestion = responsePkt.readString();
        return new RecoverAccountRequestResponse(config, data, code, profileId, securityQuestionId, securityQuestion); //return the response
    }

    /**
     *
     * recovery is a 2 stage process; this is the second stage.
     * considering we've received information from the first stage,
     * in this method we actually send the data that will actually try to recover the account
     *
     * @param config the configuration used to connect to the server
     * @param data the data
     * @return a try recover account response
     * @throws IOException if an error is thrown
     */
    public static TryRecoverAccountResponse tryRecoverAccount(final Config config,
                                                              final TryRecoverAccountData data) throws IOException {
        //a little bit of prevalidation
        if(!ProfileValidation.isSecurityPinValid(data.securityPin()))
            return new TryRecoverAccountResponse(config, data, TryRecoverAccountResponse.Code.INVALID_SECURITY_PIN);
        if(!ProfileValidation.isPassValid(data.newPass()))
            return new TryRecoverAccountResponse(config, data, TryRecoverAccountResponse.Code.INVALID_NEW_PASS);
        final Connection con = new Connection(config.host, config.port); //create a connection
        con.send(
                new Packet(Opcode.TRY_RECOVER_ACCOUNT)
                    .writeInt(data.profileId())
                    .writeString(data.securityPin())
                    .writeInt(data.securityQuestionId())
                    .writeString(data.securityQuestionAnswer() != null ? data.securityQuestionAnswer() : "")
                    .writeString(data.newPass())
        ); //write the packet
        final Packet responsePkt = con.readForOpcode(Opcode.TRY_RECOVER_ACCOUNT_RESPONSE); //read the response
        con.disconnect(); //close the connection as there's no reason to keep it alive
        final TryRecoverAccountResponse.Code code = TryRecoverAccountResponse.Code.forId(responsePkt.readUnsignedByte());
        return new TryRecoverAccountResponse(config, data, code); //return the response
    }

    public static void debug(final String fmt, final Object... args){
        if(debug)
            System.out.println(String.format(fmt, args));
    }
}
