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

import java.sql.ResultSet;
import java.sql.SQLException;
import jvm.mohawk.chatter.server.model.log.LoginLog;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class LoginLogMapper implements ResultSetMapper<LoginLog> {

    @Override
    public LoginLog map(final int index, final ResultSet rs, final StatementContext ctx) throws SQLException{
        return new LoginLog(
                rs.getInt("id"),
                rs.getTimestamp("timestamp"),
                rs.getInt("profile_id"),
                rs.getString("ip"),
                rs.getString("device")
        );
    }
}
