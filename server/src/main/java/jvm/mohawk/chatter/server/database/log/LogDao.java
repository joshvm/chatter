package jvm.mohawk.chatter.server.database.log;

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
import jvm.mohawk.chatter.server.model.log.GeneralLog;
import jvm.mohawk.chatter.server.model.log.LoginLog;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

public interface LogDao extends Dao {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS logs (" +
            "id INT NOT NULL AUTO_INCREMENT, " +
            "timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
            "profile_id INT NOT NULL, " +
            "data TEXT NOT NULL, " +
            "PRIMARY KEY(id))")
    void init();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS login_logs (" +
            "id INT NOT NULL AUTO_INCREMENT, " +
            "timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
            "profile_id INT NOT NULL, " +
            "ip TEXT NOT NULL, " +
            "device TEXT NOT NULL, " +
            "PRIMARY KEY(id))")
    void initLogin();

    @SqlUpdate("INSERT INTO logs " +
            "(profile_id, data) VALUES " +
            "(:profile_id, :data)")
    @GetGeneratedKeys
    long insert(@Bind("profile_id") final int profileId,
               @Bind("data") final String data);

    @SqlQuery("SELECT * FROM logs WHERE profile_id = :profile_id ORDER BY timestamp")
    @Mapper(GeneralLogMapper.class)
    Collection<GeneralLog> forProfile(@Bind("profile_id") final int profileId);

    @SqlQuery("SELECT * FROM logs WHERE id = :id")
    @Mapper(GeneralLogMapper.class)
    GeneralLog forId(@Bind("id") final int id);

    @SqlUpdate("INSERT INTO login_logs " +
            "(profile_id, ip, device) VALUES " +
            "(:profile_id, :ip, :device)")
    @GetGeneratedKeys
    long insertLogin(@Bind("profile_id") final int profileId,
                    @Bind("ip") final String ip,
                    @Bind("device") final String device);

    @SqlQuery("SELECT * FROM login_logs WHERE profile_id = :profile_id ORDER BY timestamp")
    @Mapper(LoginLogMapper.class)
    Collection<LoginLog> loginLogsForProfile(@Bind("profile_id") final int profileId);
}
