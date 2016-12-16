package jvm.mohawk.chatter.server.database.profile;

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

import java.sql.Date;
import java.util.Collection;
import jvm.mohawk.chatter.server.database.Dao;
import jvm.mohawk.chatter.server.database.util.BindRank;
import jvm.mohawk.chatter.server.database.util.BindStatus;
import jvm.mohawk.chatter.server.model.Rank;
import jvm.mohawk.chatter.server.model.Status;
import jvm.mohawk.chatter.server.model.profile.Profile;
import jvm.mohawk.chatter.server.model.profile.ProfileOptions;
import jvm.mohawk.chatter.server.model.profile.ProfileRegistrationDetails;
import jvm.mohawk.chatter.server.model.security.SecurityQuestion;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

public interface ProfileDao extends Dao, Transactional<ProfileDao> {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS profiles (" +
            "id INT NOT NULL AUTO_INCREMENT, " +
            "rank_id INT NOT NULL, " +
            "user TEXT NOT NULL, " +
            "pass TEXT NOT NULL, " +
            "first_name TEXT NOT NULL, " +
            "last_name TEXT NOT NULL, " +
            "birth_date TEXT NOT NULL, " +
            "status_id INT NOT NULL, " +
            "pic TEXT NOT NULL, " +
            "security_pin TEXT NOT NULL, " +
            "PRIMARY KEY (id))")
    void init();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS profile_registration_details (" +
            "profile_id INT NOT NULL, " +
            "timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
            "ip TEXT NOT NULL, " +
            "device TEXT NOT NULL, " +
            "PRIMARY KEY (profile_id))")
    void initRegistrationDetails();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS profile_options (" +
            "profile_id INT NOT NULL, " +
            "requires_security_pin_on_login BOOLEAN NOT NULL, " +
            "PRIMARY KEY (profile_id))")
    void initOptions();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS profile_whitelist (" +
            "profile_id INT NOT NULL, " +
            "ip TEXT NOT NULL)")
    void initWhitelist();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS profile_blacklist (" +
            "profile_id INT NOT NULL, " +
            "ip TEXT NOT NULL)")
    void initBlacklist();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS profile_security_questions (" +
            "id INT NOT NULL AUTO_INCREMENT, " +
            "profile_id INT NOT NULL, " +
            "question TEXT NOT NULL, " +
            "answer TEXT NOT NULL, " +
            "PRIMARY KEY(id))")
    void initSecurityQuestions();

    @SqlQuery("SELECT * FROM profiles WHERE id = :id")
    @Mapper(ProfileMapper.class)
    Profile forId(@Bind("id") final int id);

    @SqlQuery("SELECT * FROM profile_registration_details WHERE profile_id = :profile_id")
    @Mapper(ProfileRegistrationDetailsMapper.class)
    ProfileRegistrationDetails registrationDetailsForProfileId(@Bind("profile_id") final int profileId);

    @SqlQuery("SELECT * FROM profile_options WHERE profile_id = :profile_id")
    @Mapper(ProfileOptionsMapper.class)
    ProfileOptions optionsForProfileId(@Bind("profile_id") final int profileId);

    @SqlQuery("SELECT * FROM profiles WHERE user = :user")
    @Mapper(ProfileMapper.class)
    Profile forUser(@Bind("user") final String user);

    @SqlUpdate("INSERT INTO profiles " +
            "(rank_id, user, pass, first_name, last_name, birth_date, status_id, pic, security_pin) VALUES " +
            "(:rank_id, :user, :pass, :first_name, :last_name, :birth_date, :status_id, :pic, :security_pin)")
    @Mapper(ProfileMapper.class)
    @GetGeneratedKeys
    long add(@BindRank("rank_id") final Rank rank,
            @Bind("user") final String user,
            @Bind("pass") final String pass,
            @Bind("first_name") final String firstName,
            @Bind("last_name") final String lastName,
            @Bind("birth_date") final Date birthDate,
            @BindStatus("status_id") final Status status,
            @Bind("pic") final String pic,
            @Bind("security_pin") final String securityPin);

    @SqlUpdate("UPDATE profiles SET pass = :pass WHERE id = :id")
    int setPassword(@Bind("id") final int id,
                    @Bind("pass") final String pass);

    @SqlUpdate("INSERT INTO profile_registration_details " +
            "(profile_id, ip, device) VALUES " +
            "(:profile_id, :ip, :device)")
    int addRegistrationDetails(@Bind("profile_id") final int profileId,
                               @Bind("ip") final String ip,
                               @Bind("device") final String device);

    @SqlUpdate("INSERT INTO profile_options " +
            "(profile_id, requires_security_pin_on_login) VALUES " +
            "(:profile_id, :requires_security_pin_on_login)")
    int addOptions(@Bind("profile_id") final int profileId,
                   @Bind("requires_security_pin_on_login") final boolean requiresSecurityPinOnLogin);

    @SqlUpdate("UPDATE profile_options SET requires_security_pin_on_login = :requires_security_pin_on_login " +
            "WHERE profile_id = :profile_id")
    int setRequiresSecurityPinOnLogin(@Bind("profile_id") final int profileId,
                                      @Bind("requires_security_pin_on_login") final boolean requiresSecurityPinOnLogin);

    @SqlUpdate("DELETE FROM profiles WHERE id = :id")
    int delete(@Bind("id") final int id);

    @SqlUpdate("DELETE FROM profile_registration_details WHERE profile_id = :profile_id")
    int deleteRegistrationDetails(@Bind("profile_id") final int profileId);

    @SqlUpdate("DELETE FROM profile_options WHERE profile_id = :profile_id")
    int deleteOptions(@Bind("profile_id") final int profileId);

    @SqlUpdate("UPDATE profiles SET status_id = :status_id WHERE id = :id")
    int setStatus(@Bind("id") final int profileId,
                  @BindStatus("status_id") final Status status);

    @SqlUpdate("INSERT INTO profile_whitelist " +
            "(profile_id, ip) VALUES " +
            "(:profile_id, :ip)")
    int addIpToWhitelist(@Bind("profile_id") final int profileId,
                         @Bind("ip") final String ip);

    @SqlUpdate("DELETE FROM profile_whitelist WHERE profile_id = :profile_id AND " +
            "ip = :ip")
    int deleteIpFromWhitelist(@Bind("profile_id") final int profileId,
                              @Bind("ip") final String ip);

    @SqlUpdate("INSERT INTO profile_blacklist " +
            "(profile_id, ip) VALUES " +
            "(:profile_id, :ip)")
    int addIpToBlacklist(@Bind("profile_id") final int profileId,
                         @Bind("ip") final String ip);

    @SqlUpdate("DELETE FROM profile_blacklist WHERE profile_id = :profile_id AND " +
            "ip = :ip")
    int deleteIpFromBlacklist(@Bind("profile_id") final int profileId,
                              @Bind("ip") final String ip);

    @SqlQuery("SELECT ip FROM profile_blacklist WHERE profile_id = :profile_id")
    Collection<String> getBlacklist(@Bind("profile_id") final int profileId);

    @SqlQuery("SELECT ip FROM profile_whitelist WHERE profile_id = :profile_id")
    Collection<String> getWhitelist(@Bind("profile_id") final int profileId);

    @SqlUpdate("INSERT INTO profile_security_questions " +
            "(profile_id, question, answer) VALUES " +
            "(:profile_id, :question, :answer)")
    int addSecurityQuestion(@Bind("profile_id") final int profileId,
                            @Bind("question") final String question,
                            @Bind("answer") final String answer);

    @SqlQuery("SELECT * FROM profile_security_questions WHERE profile_id = :profile_id")
    @Mapper(SecurityQuestionMapper.class)
    Collection<SecurityQuestion> getSecurityQuestions(@Bind("profile_id") final int profileId);

    @SqlQuery("SELECT * FROM profile_security_questions WHERE id = :id")
    @Mapper(SecurityQuestionMapper.class)
    SecurityQuestion securityQuestionForId(@Bind("id") final int id);

    @SqlUpdate("DELETE FROM profile_security_questions WHERE id = :id")
    int deleteSecurityQuestion(@Bind("id") final int id);
}
