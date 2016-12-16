package jvm.mohawk.chatter.clientapi.model.profile;

/*
  Capstone Project - Chatter
  
  An instant messenger that lets you connect with your friends 
  and meet new people with similar interests.
  
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

import java.awt.image.BufferedImage;
import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDate;
import javax.imageio.ImageIO;
import jvm.mohawk.chatter.clientapi.utils.Utils;

/**
 * This class contains various helper methods for validating profile information
 * while this is client-side validation, server-side validation still exists
 * it's just to prevent some of the requests that could be validated client-side (optimization)
 */
public final class ProfileValidation {

    public static final int MIN_FIRST_NAME_LENGTH = 1;
    public static final int MAX_FIRST_NAME_LENGTH = 20;

    public static final int MIN_LAST_NAME_LENGTH = 1;
    public static final int MAX_LAST_NAME_LENGTH = 30;

    public static final int MIN_AGE = 10;
    public static final int MAX_AGE = 150;

    public static final int MIN_USER_LENGTH = 1;
    public static final int MAX_USER_LENGTH = 30;
    public static final String USER_ALLOWED_SYMBOLS = "._";

    public static final int MIN_PASS_LENGTH = 5;
    public static final int MAX_PASS_LENGTH = 50;
    public static final String PASS_ALLOWED_SYMBOLS = "._!@#$%^&*()_+-=`:;,<>/?";

    public static final String PIN_REGEX = "\\d{1,30}";

    private ProfileValidation(){}

    public static boolean isFirstNameValid(final String firstName){
        return Utils.between(firstName.length(), MIN_FIRST_NAME_LENGTH, MAX_FIRST_NAME_LENGTH)
                && firstName.chars()
                .allMatch(Character::isLetter);
    }

    public static boolean isLastNameValid(final String lastName){
        return Utils.between(lastName.length(), MIN_LAST_NAME_LENGTH, MAX_LAST_NAME_LENGTH)
                && lastName.chars()
                .allMatch(Character::isLetter);
    }

    public static boolean isPicValid(final String pic){
        try{
            final BufferedImage img = ImageIO.read(new URL(pic));
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean isBirthDateValid(final int year,
                                           final int month,
                                           final int day){
        try{
            final LocalDate bday = LocalDate.of(year, month, day);
            final LocalDate now = LocalDate.now();
            final int age = now.getYear() - bday.getYear();
            return Utils.between(age, MIN_AGE, MAX_AGE);
        }catch(DateTimeException ex){
            return false;
        }
    }

    public static boolean isUserValid(final String user){
        return Utils.between(user.length(), MIN_USER_LENGTH, MAX_USER_LENGTH)
                && user.chars()
                .noneMatch(c -> !Character.isLetterOrDigit(c)
                        && USER_ALLOWED_SYMBOLS.indexOf(c) < 0
                );
    }

    public static boolean isPassValid(final String pass){
        return Utils.between(pass.length(), MIN_PASS_LENGTH, MAX_PASS_LENGTH)
                && pass.chars()
                .noneMatch(c -> !Character.isLetterOrDigit(c)
                        && PASS_ALLOWED_SYMBOLS.indexOf(c) < 0
                );
    }

    public static boolean isSecurityPinValid(final String pin){
        return pin.matches(PIN_REGEX);
    }
}
