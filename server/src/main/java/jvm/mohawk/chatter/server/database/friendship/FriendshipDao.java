package jvm.mohawk.chatter.server.database.friendship;

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
import jvm.mohawk.chatter.server.model.friendship.FriendRequest;
import jvm.mohawk.chatter.server.model.friendship.Friendship;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

public interface FriendshipDao extends Dao, Transactional<FriendshipDao> {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS friendships (" +
            "timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
            "id INT NOT NULL AUTO_INCREMENT," +
            "profile1_id INT NOT NULL, " +
            "profile2_id INT NOT NULL, " +
            "PRIMARY KEY (id))")
    void init();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS friend_requests (" +
            "timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
            "id INT NOT NULL AUTO_INCREMENT, " +
            "requester_profile_id INT NOT NULL, " +
            "target_profile_id INT NOT NULL, " +
            "PRIMARY KEY (id))")
    void initRequests();

    @SqlQuery("SELECT * FROM friend_requests WHERE " +
            "requester_profile_id = :requester_profile_id AND " +
            "target_profile_id = :target_profile_id")
    @Mapper(FriendRequestMapper.class)
    FriendRequest requestWith(@Bind("requester_profile_id") final int requesterProfileId,
                              @Bind("target_profile_id") final int targetProfileId);

    @SqlUpdate("INSERT INTO friend_requests " +
            "(requester_profile_id, target_profile_id) VALUES " +
            "(:requester_profile_id, :target_profile_id)")
    @GetGeneratedKeys
    long addRequest(@Bind("requester_profile_id") final int requesterProfileId,
                   @Bind("target_profile_id") final int targetProfileId);

    @SqlUpdate("INSERT INTO friendships " +
            "(profile1_id, profile2_id) VALUES " +
            "(:profile1_id, :profile2_id)")
    @GetGeneratedKeys
    long add(@Bind("profile1_id") final int profile1Id,
            @Bind("profile2_id") final int profile2Id);

    @SqlQuery("SELECT * FROM friend_requests WHERE id = :id")
    @Mapper(FriendRequestMapper.class)
    FriendRequest requestForId(@Bind("id") final int id);

    @SqlQuery("SELECT * FROM friendships WHERE id = :id")
    @Mapper(FriendshipMapper.class)
    Friendship forId(@Bind("id") final int id);

    @SqlUpdate("DELETE FROM friend_requests WHERE id = :id")
    int deleteRequest(@Bind("id") final int id);

    @SqlUpdate("DELETE FROM friendships WHERE id = :id")
    int delete(@Bind("id") final int id);

    @SqlQuery("SELECT * FROM friendships WHERE " +
            "profile1_id IN (:profile1_id, :profile2_id) AND " +
            "profile2_id IN (:profile1_id, :profile2_id)")
    @Mapper(FriendshipMapper.class)
    Friendship between(@Bind("profile1_id") final int profile1Id,
                       @Bind("profile2_id") final int profile2Id);

    @SqlQuery("SELECT * FROM friendships WHERE " +
            ":profile_id IN (profile1_id, profile2_id)")
    @Mapper(FriendshipMapper.class)
    Collection<Friendship> involving(@Bind("profile_id") final int profileId);

    @SqlQuery("SELECT * FROM friend_requests WHERE " +
            ":profile_id IN (requester_profile_id, target_profile_id)")
    @Mapper(FriendRequestMapper.class)
    Collection<FriendRequest> requestsInvolving(@Bind("profile_id") final int profileId);

    @SqlQuery("SELECT * FROM friend_requests WHERE requester_profile_id = :profile_id")
    @Mapper(FriendRequestMapper.class)
    Collection<FriendRequest> pendingRequestsForProfileId(@Bind("profile_id") final int profileId);

    @SqlQuery("SELECT * FROM friend_requests WHERE target_profile_id = :profile_id")
    @Mapper(FriendRequestMapper.class)
    Collection<FriendRequest> answerableRequestsForProfileId(@Bind("profile_id") final int profileId);
}
