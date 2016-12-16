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

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jvm.mohawk.chatter.server.model.profile.Profile;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@BindingAnnotation(BindProfile.ProfileBinderFactory.class)
public @interface BindProfile {

    class ProfileBinderFactory implements BinderFactory {

        @Override
        public Binder<BindProfile, Profile> build(final Annotation annotation){
            return (q, bind, p) -> {
                q.bind("id", p.id());
                q.bind("rank_id", p.rank().id());
                q.bind("user", p.user());
                q.bind("pass", p.pass());
                q.bind("first_name", p.firstName());
                q.bind("last_name", p.lastName());
                q.bind("birth_date", p.birthDate());
                q.bind("security_pin", p.securityPin());
            };
        }
    }
}
