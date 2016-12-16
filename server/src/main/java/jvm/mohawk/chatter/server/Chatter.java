package jvm.mohawk.chatter.server;

/*
  Capstone Project - Chatter
  
  An instant messenger that lets you connect with your friends 
  and meet new people with similar interests.
  
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import jvm.mohawk.chatter.server.database.Database;
import jvm.mohawk.chatter.server.model.Rank;
import jvm.mohawk.chatter.server.model.Status;
import jvm.mohawk.chatter.server.model.chatroom.ChatroomManager;
import jvm.mohawk.chatter.server.model.punishment.PunishmentManager;
import jvm.mohawk.chatter.server.net.client.ClientManager;
import jvm.mohawk.chatter.server.utils.Props;
import jvm.mohawk.chatter.server.utils.Utils;

public class Chatter {

    public static class Config {

        public static final Rank STARTING_RANK = Rank.NONE;
        public static final Status STARTING_STATUS = Status.ONLINE;
        public static final boolean REQUIRES_SECURITY_PIN_ON_LOGIN = false;
        public static final boolean AUTO_ACCEPT_FRIEND_REQUESTS = false;

        private final Rank startingRank;
        private final Status startingStatus;

        private final boolean requiresSecurityPinOnLogin;

        private final boolean autoAcceptFriendRequests;

        public Config(final Rank startingRank,
                      final Status startingStatus,
                      final boolean requiresSecurityPinOnLogin,
                      final boolean autoAcceptFriendRequests){
            this.startingRank = startingRank;
            this.startingStatus = startingStatus;
            this.requiresSecurityPinOnLogin = requiresSecurityPinOnLogin;
            this.autoAcceptFriendRequests = autoAcceptFriendRequests;
        }

        public Rank startingRank(){
            return startingRank;
        }

        public Status startingStatus(){
            return startingStatus;
        }

        public boolean requiresSecurityPinOnLogin(){
            return requiresSecurityPinOnLogin;
        }

        public boolean autoAcceptFriendRequests(){
            return autoAcceptFriendRequests;
        }

        public static Config parse(final Path path) throws IOException {
            final Props props = Utils.props(path);
            return new Config(
                    props.object(Rank::valueOf, "starting_rank", STARTING_RANK),
                    props.object(Status::valueOf, "starting_status", STARTING_STATUS),
                    props.bool("requires_security_pin_on_login", REQUIRES_SECURITY_PIN_ON_LOGIN),
                    props.bool("auto_accept_friend_requests", AUTO_ACCEPT_FRIEND_REQUESTS)
            );
        }
    }

    public static Config config;

    public static Server.Config serverConfig;
    public static Database.Config databaseConfig;

    public static Server server;
    public static Database database;

    public static PunishmentManager punishments;

    public static ClientManager activeClients;
    public static ChatroomManager activeChatrooms;

    public static final ScheduledExecutorService service = Executors.newScheduledThreadPool(5);

    public static void start(final Config config,
                             final Server.Config serverConfig,
                             final Database.Config databaseConfig) throws Exception{

        Chatter.config = config;
        Chatter.serverConfig = serverConfig;
        Chatter.databaseConfig = databaseConfig;

        activeClients = new ClientManager();
        activeChatrooms = new ChatroomManager();

        punishments = new PunishmentManager();

        database = new Database(databaseConfig);
        database.init();
        database.start();

        database.punishments().all().forEach(punishments::add);
        punishments.start();

        server = new Server(serverConfig);
        server.init();
        server.start();
    }

    public static void main(String[] args) throws Exception {
        Path chatterConfigPath = Paths.get("chatter.properties");
        Path serverConfigPath = Paths.get("server.properties");
        Path databaseConfigPath = Paths.get("database.properties");
        if(args.length == 3){
            chatterConfigPath = Paths.get(args[0]);
            serverConfigPath = Paths.get(args[1]);
            databaseConfigPath = Paths.get(args[2]);
        }
        Chatter.Config config = Chatter.Config.parse(chatterConfigPath);
        Server.Config serverConfig = Server.Config.parse(serverConfigPath);
        Database.Config databaseConfig = Database.Config.parse(databaseConfigPath);

        Chatter.start(config, serverConfig, databaseConfig);
    }
}
