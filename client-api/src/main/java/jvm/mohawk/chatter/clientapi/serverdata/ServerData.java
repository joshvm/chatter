package jvm.mohawk.chatter.clientapi.serverdata;

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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import jvm.mohawk.chatter.clientapi.ChatterClient;
import jvm.mohawk.chatter.clientapi.auth.impl.LoginData;

/**
 * This class isn't used anywhere but if there was extra time I was going to implement a "memory" feature
 * where you could save and connect to different chatter servers.
 * was going to manage all of the login data for a specific server.
 * still could be implemented...
 */
public class ServerData {

    private final ChatterClient.Config config;
    private final List<LoginData> loginData;

    public ServerData(final ChatterClient.Config config,
                      final List<LoginData> loginData){
        this.config = config;
        this.loginData = loginData;
    }

    public ChatterClient.Config config(){
        return config;
    }

    public List<LoginData> loginData(){
        return loginData;
    }

    public void write(final DataOutputStream out) throws IOException{
        out.writeUTF(config.name());
        out.writeUTF(config.host());
        out.writeInt(config.port());
        out.writeInt(loginData.size());
        for(final LoginData ld : loginData){
            out.writeUTF(ld.user());
            out.writeUTF(ld.pass());
            out.writeUTF(ld.securityPin());
        }
    }

    public static ServerData read(final DataInputStream in) throws IOException {
        final ChatterClient.Config config = new ChatterClient.Config(
                in.readUTF(),
                in.readUTF(),
                in.readInt()
        );
        final int count = in.readInt();
        final List<LoginData> loginData = new ArrayList<>(count);
        for(int n = 0; n < count; n++){
            loginData.add(
                    new LoginData(
                            in.readUTF(),
                            in.readUTF(),
                            in.readUTF()
                    )
            );
        }
        return new ServerData(config, loginData);
    }

    public static List<ServerData> load(final Path path) throws Exception {
        final List<ServerData> data = new ArrayList<>();
        try(final DataInputStream in = new DataInputStream(Files.newInputStream(path))){
            final int count = in.readInt();
            for(int n = 0; n < count; n++)
                data.add(read(in));
        }
        return data;
    }

    public static void dump(final Path path, final List<ServerData> data) throws IOException {
        try(final DataOutputStream out = new DataOutputStream(Files.newOutputStream(path))){
            out.writeInt(data.size());
            for(final ServerData d : data)
                d.write(out);
        }
    }
}
