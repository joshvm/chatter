package jvm.mohawk.chatter.server.database;

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

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import java.io.IOException;
import java.nio.file.Path;
import jvm.mohawk.chatter.server.database.chatroom.ChatroomDao;
import jvm.mohawk.chatter.server.database.conversation.ConversationDao;
import jvm.mohawk.chatter.server.database.friendship.FriendshipDao;
import jvm.mohawk.chatter.server.database.log.LogDao;
import jvm.mohawk.chatter.server.database.profile.ProfileDao;
import jvm.mohawk.chatter.server.database.punishment.PunishmentDao;
import jvm.mohawk.chatter.server.utils.Props;
import jvm.mohawk.chatter.server.utils.Utils;
import org.skife.jdbi.v2.DBI;

public class Database {

    public static class Config {

        private final String url;
        private final String user;
        private final String pass;

        public Config(final String url,
                      final String user,
                      final String pass){
            this.url = url;
            this.user = user;
            this.pass = pass;
        }

        public String url(){
            return url;
        }

        public String user(){
            return user;
        }

        public String pass(){
            return pass;
        }

        public static Config parse(final Path path) throws IOException{
            final Props props = Utils.props(path);
            return new Config(
                    props.string("url"),
                    props.string("user"),
                    props.string("pass")
            );
        }
    }

    private final Config config;

    private DBI dbi;

    private ProfileDao profiles;
    private FriendshipDao friendships;

    private ChatroomDao chatrooms;

    private PunishmentDao punishments;

    private ConversationDao conversations;

    private LogDao logs;

    public Database(final Config config){
        this.config = config;
    }

    public Config config(){
        return config;
    }

    public void init(){
        final MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
        ds.setUrl(config.url);
        ds.setUser(config.user);
        ds.setPassword(config.pass);
        dbi = new DBI(ds);

        profiles = init(ProfileDao.class);
        profiles.initRegistrationDetails();
        profiles.initOptions();
        profiles.initWhitelist();
        profiles.initBlacklist();
        profiles.initSecurityQuestions();

        friendships = init(FriendshipDao.class);
        friendships.initRequests();

        chatrooms = init(ChatroomDao.class);
        chatrooms.initRegistrationDetails();
        chatrooms.initChatroomRanks();

        punishments = init(PunishmentDao.class);
        punishments.initChatroomPunishments();

        conversations = init(ConversationDao.class);

        logs = init(LogDao.class);
        logs.initLogin();
    }

    public void start(){

    }

    public ProfileDao profiles(){
        return profiles;
    }

    public ProfileDao openProfiles(){
        return dbi.open(ProfileDao.class);
    }

    public FriendshipDao friendships(){
        return friendships;
    }

    public FriendshipDao openFriendships(){
        return dbi.open(FriendshipDao.class);
    }

    public ChatroomDao chatrooms(){
        return chatrooms;
    }

    public ChatroomDao openChatrooms(){
        return dbi.open(ChatroomDao.class);
    }

    public PunishmentDao punishments(){
        return punishments;
    }

    public PunishmentDao openPunishments(){
        return dbi.open(PunishmentDao.class);
    }

    public ConversationDao conversations(){
        return conversations;
    }

    public ConversationDao openConversations(){
        return dbi.open(ConversationDao.class);
    }

    public LogDao logs(){
        return logs;
    }

    public LogDao openLogs(){
        return dbi.open(LogDao.class);
    }

    private <T extends Dao> T init(final Class<T> clazz){
        final T dao = dbi.onDemand(clazz);
        dao.init();
        return dao;
    }
}
