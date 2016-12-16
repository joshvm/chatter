package jvm.mohawk.chatter.clientapi.upload;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

import java.io.File;
import java.util.Map;
import jvm.mohawk.chatter.clientapi.ChatterClient;
import jvm.mohawk.chatter.clientapi.utils.Utils;

public class UploadProfilePictureResponse {

    public enum Code {

        IMAGE_NOT_FOUND(1),
        IMAGE_WRONG_FORMAT(2),
        IMAGE_TOO_BIG(3),
        INVALID_USER(4),
        SUCCESS(100),
        ERROR(101);

        private static final Map<Integer, Code> MAP = Utils.map(values(), Code::id);

        private final int id;

        Code(final int id){
            this.id = id;
        }

        public int id(){
            return id;
        }

        public static Code forId(final int id){
            return MAP.getOrDefault(id, ERROR);
        }
    }

    private final ChatterClient.Config config;
    private final String user;
    private final File pic;
    private final Code code;

    private final String url;

    public UploadProfilePictureResponse(final ChatterClient.Config config,
                                        final String user,
                                        final File pic,
                                        final Code code,
                                        final String url){
        this.config = config;
        this.user = user;
        this.pic = pic;
        this.code = code;
        this.url = url;
    }

    public UploadProfilePictureResponse(final ChatterClient.Config config,
                                        final String user,
                                        final File pic,
                                        final Code code){
        this(config, user, pic, code, null);
    }

    public ChatterClient.Config config(){
        return config;
    }

    public String user(){
        return user;
    }

    public File pic(){
        return pic;
    }

    public Code code(){
        return code;
    }

    public String url(){
        return url;
    }
}
