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
import jvm.mohawk.chatter.server.model.profile.ProfileOptions;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@BindingAnnotation(BindProfileOptions.ProfileOptionsBinderFactory.class)
public @interface BindProfileOptions {

    class ProfileOptionsBinderFactory implements BinderFactory {

        @Override
        public Binder<BindProfileOptions, ProfileOptions> build(final Annotation annotation){
            return (q, bind, po) -> {
                q.bind("profile_id", po.profileId());
                q.bind("requires_security_pin_on_login", po.requiresSecurityPinOnLogin());
            };
        }
    }
}
