package jvm.mohawk.chatter.server.database.chatroom;

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

import java.util.Collection;
import jvm.mohawk.chatter.server.database.Dao;
import jvm.mohawk.chatter.server.database.util.BindRank;
import jvm.mohawk.chatter.server.model.Rank;
import jvm.mohawk.chatter.server.model.chatroom.Chatroom;
import jvm.mohawk.chatter.server.model.chatroom.ChatroomRank;
import jvm.mohawk.chatter.server.model.chatroom.ChatroomRegistrationDetails;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

public interface ChatroomDao extends Dao, Transactional<ChatroomDao> {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS chatrooms (" +
            "id INT NOT NULL AUTO_INCREMENT, " +
            "name TEXT NOT NULL, " +
            "description TEXT NOT NULL, " +
            "PRIMARY KEY (id))")
    void init();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS chatroom_registration_details (" +
            "chatroom_id INT NOT NULL, " +
            "timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
            "owner_profile_id INT NOT NULL)")
    void initRegistrationDetails();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS chatroom_ranks (" +
            "chatroom_id INT NOT NULL, " +
            "profile_id INT NOT NULL, " +
            "rank_id int NOT NULL)")
    void initChatroomRanks();

    @SqlUpdate("INSERT INTO chatrooms " +
            "(name, description) VALUES " +
            "(:name, :description)")
    @GetGeneratedKeys
    long add(@Bind("name") final String name,
            @Bind("description") final String description);

    @SqlUpdate("INSERT INTO chatroom_registration_details " +
            "(chatroom_id, owner_profile_id) VALUES " +
            "(:chatroom_id, :owner_profile_id)")
    int addRegistrationDetails(@Bind("chatroom_id") final int chatroomId,
                               @Bind("owner_profile_id") final int ownerProfileId);

    @SqlQuery("SELECT * FROM chatrooms WHERE id = :chatroom_id")
    @Mapper(ChatroomMapper.class)
    Chatroom forId(@Bind("chatroom_id") final int chatroomId);

    @SqlQuery("SELECT * FROM chatrooms WHERE name = :chatroom_name")
    @Mapper(ChatroomMapper.class)
    Chatroom forName(@Bind("chatroom_name") final String chatroomName);

    @SqlQuery("SELECT * FROM chatroom_registration_details WHERE " +
            "chatroom_id = :chatroom_id")
    @Mapper(ChatroomRegistrationDetailsMapper.class)
    ChatroomRegistrationDetails registrationDetails(@Bind("chatroom_id") final int chatroomId);

    @SqlUpdate("INSERT INTO chatroom_ranks " +
            "(chatroom_id, profile_id, rank_id) VALUES " +
            "(:chatroom_id, :profile_id, :rank_id)")
    int addRank(@Bind("chatroom_id") final int chatroomId,
                @Bind("profile_id") final int profileId,
                @BindRank("rank_id") final Rank rank);

    @SqlQuery("SELECT * FROM chatroom_ranks WHERE " +
            "chatroom_id = :chatroom_id AND " +
            "profile_id = :profile_id")
    @Mapper(ChatroomRankMapper.class)
    ChatroomRank rankIn(@Bind("chatroom_id") final int chatroomId,
                        @Bind("profile_id") final int profileId);

    @SqlQuery("SELECT * FROM chatrooms ")
    @Mapper(ChatroomRankMapper.class)
    Collection<ChatroomRank> ranks(@Bind("chatroom_id") final int chatroomId);

    @SqlUpdate("UPDATE chatroom_ranks SET " +
            "rank_id = :rank_id WHERE profile_id = :profile_id")
    int updateRank(@Bind("chatroom_id") final int chatroomId,
                @Bind("profile_id") final int profileId,
                @BindRank("rank_id") final Rank rank);
}
