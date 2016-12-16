package jvm.mohawk.chatter.server.database.punishment;

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
import jvm.mohawk.chatter.server.model.punishment.ChatroomPunishment;
import jvm.mohawk.chatter.server.model.punishment.Punishment;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

public interface PunishmentDao extends Dao {

    @Override
    @SqlUpdate("CREATE TABLE IF NOT EXISTS punishments (" +
            "id INT NOT NULL AUTO_INCREMENT, " +
            "timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
            "punisher_id INT NOT NULL, " +
            "punisher_user TEXT NOT NULL, " +
            "victim_id INT NOT NULL, " +
            "victim_user TEXT NOT NULL, " +
            "type_id INT NOT NULL, " +
            "duration_in_seconds INT NOT NULL, " +
            "reason TEXT NOT NULL, " +
            "active BOOLEAN NOT NULL, " +
            "PRIMARY KEY (id))")
    void init();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS chatroom_punishments (" +
            "id INT NOT NULL AUTO_INCREMENT, " +
            "timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
            "chatroom_id INT NOT NULL, " +
            "punisher_id INT NOT NULL, " +
            "punisher_user TEXT NOT NULL, " +
            "victim_id INT NOT NULL, " +
            "victim_user TEXT NOT NULL, " +
            "type_id INT NOT NULL, " +
            "duration_in_seconds INT NOT NULL, " +
            "reason TEXT NOT NULL, " +
            "active BOOLEAN NOT NULL, " +
            "PRIMARY KEY (id))")
    void initChatroomPunishments();

    @SqlUpdate("INSERT INTO punishments " +
            "(punisher_id, punisher_user,  victim_id, victim_user, type_id, duration_in_seconds, reason, active) VALUES " +
            "(:punisher_id, :punisher_user, :victim_id, :victim_user, :type_id, :duration_in_seconds, :reason, :active)")
    @GetGeneratedKeys
    long insert(@Bind("punisher_id") final int punisherId,
                @Bind("punisher_user") final String punisherUser,
                @Bind("victim_id") final int victimId,
                @Bind("victim_user") final String victimUser,
                @BindType("type_id") final Punishment.Type type,
                @Bind("duration_in_seconds") final int durationInSeconds,
                @Bind("reason") final String reason,
                @Bind("active") final boolean active);

    @SqlUpdate("INSERT INTO chatroom_punishments " +
            "(chatroom_id, punisher_id, punisher_user,  victim_id, victim_user, type_id, duration_in_seconds, reason, active) VALUES " +
            "(:chatroom_id, :punisher_id, :punisher_user, :victim_id, :victim_user, :type_id, :duration_in_seconds, :reason, :active)")
    @GetGeneratedKeys
    long insertChatroomPunishment(@Bind("chatroom_id") final int chatroomId,
                                  @Bind("punisher_id") final int punisherId,
                                  @Bind("punisher_user") final String punisherUser,
                                  @Bind("victim_id") final int victimId,
                                  @Bind("victim_user") final String victimUser,
                                  @BindType("type_id") final Punishment.Type type,
                                  @Bind("duration_in_seconds") final int durationInSeconds,
                                  @Bind("reason") final String reason,
                                  @Bind("active") final boolean active);

    @SqlQuery("SELECT * FROM punishments WHERE id = :id")
    @Mapper(PunishmentMapper.class)
    Punishment forId(@Bind("id") final int id);

    @SqlQuery("SELECT * FROM chatroom_punishments WHERE id = :id")
    @Mapper(ChatroomPunishmentMapper.class)
    ChatroomPunishment chatroomPunishmentForId(@Bind("id") final int id);

    @SqlUpdate("UPDATE punishments SET active = :active WHERE id = :id")
    int setActive(@Bind("id") final int id,
                  @Bind("active") final boolean active);

    @SqlUpdate("UPDATE chatroom_punishments SET active = :active WHERE id = :id")
    int setChatroomPunishmentActive(@Bind("id") final int id,
                                    @Bind("active") final boolean active);

    @SqlUpdate("DELETE FROM punishments WHERE id = :id")
    int remove(@Bind("id") final int id);

    @SqlUpdate("DELETE FROM chatroom_punishments WHERE id = :id")
    int removeChatroomPunishment(@Bind("id") final int id);

    @SqlQuery("SELECT * FROM punishments")
    @Mapper(PunishmentMapper.class)
    Collection<Punishment> all();

    @SqlQuery("SELECT * FROM chatroom_punishments WHERE chatroom_id = :chatroom_id")
    @Mapper(ChatroomPunishmentMapper.class)
    Collection<ChatroomPunishment> forChatroom(@Bind("chatroom_id") final int chatroomId);


}
