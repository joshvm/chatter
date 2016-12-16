package jvm.mohawk.chatter.server.command;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jvm.mohawk.chatter.server.command.impl.AddIpToListCommand;
import jvm.mohawk.chatter.server.command.impl.AddSecurityQuestionCommand;
import jvm.mohawk.chatter.server.command.impl.ChatroomPunishCommand;
import jvm.mohawk.chatter.server.command.impl.GetOptionCommand;
import jvm.mohawk.chatter.server.command.impl.HelpCommand;
import jvm.mohawk.chatter.server.command.impl.PunishCommand;
import jvm.mohawk.chatter.server.command.impl.RemoveIpFromListCommand;
import jvm.mohawk.chatter.server.command.impl.RemoveSecurityQuestionCommand;
import jvm.mohawk.chatter.server.command.impl.SetOptionCommand;
import jvm.mohawk.chatter.server.command.impl.ViewIpListCommand;
import jvm.mohawk.chatter.server.command.impl.ViewSecurityQuestionsCommand;
import jvm.mohawk.chatter.server.model.Rank;
import jvm.mohawk.chatter.server.model.punishment.Punishment;
import jvm.mohawk.chatter.server.net.Opcode;
import jvm.mohawk.chatter.server.net.Packet;
import jvm.mohawk.chatter.server.net.client.Client;


import static jvm.mohawk.chatter.server.Chatter.database;

public final class CommandHandler {

   private static final Map<String, Command> COMMANDS = new HashMap<>();

    static{
        add(new PunishCommand("mute", Punishment.Type.MUTE, Rank.MODERATOR));
        add(new PunishCommand("ban", Punishment.Type.BAN, Rank.ADMINISTRATOR));
        add(new ChatroomPunishCommand("cmute", Punishment.Type.MUTE, Rank.NONE));
        add(new ChatroomPunishCommand("cban", Punishment.Type.BAN, Rank.NONE));
        add(new HelpCommand());
        add(new GetOptionCommand());
        add(new SetOptionCommand());
        add(new AddIpToListCommand("whitelist", true));
        add(new AddIpToListCommand("blacklist", false));
        add(new RemoveIpFromListCommand("unwhitelist", true));
        add(new RemoveIpFromListCommand("unblacklist", false));
        add(new ViewIpListCommand("viewwhitelist", true));
        add(new ViewIpListCommand("viewblacklist", false));
        add(new AddSecurityQuestionCommand());
        add(new RemoveSecurityQuestionCommand());
        add(new ViewSecurityQuestionsCommand());
    }

    private CommandHandler(){}

    public static boolean execute(final Client client,
                                  final String cmdStr){
        String text = cmdStr.replaceAll("\\s+", " ").toLowerCase().trim();
        final String key = text.split(" ")[0];
        final Command cmd = COMMANDS.get(key);
        if(cmd == null){
            client.write(new Packet(Opcode.ADD_COMMAND_MESSAGE).writeString("Command not found"));
            return false;
        }
        text = text.substring(key.length()).trim();
        final List<String> responses = new ArrayList<>();
        responses.add(String.format("***** %s Response *****", cmd.key()));
        boolean success;
        try{
            cmd.execute(client, text, responses);
            success = true;
        }catch(Exception ex){
            ex.printStackTrace();
            cmd.onError(client, text, responses, ex);
            success = false;
        }
        responses.add(String.format("***** End Of %s Response *****", cmd.key()));
        final int id = (int)database.logs().insert(client.profile().id(), String.format("Executed command: %s - %s", cmdStr, success ? "Success" : "Fail"));
        client.write(new Packet(Opcode.ADD_GENERAL_LOG, database.logs().forId(id).serialize()));
        if(!responses.isEmpty()){
            responses.forEach(r -> client.writeLater(new Packet(Opcode.ADD_COMMAND_MESSAGE).writeString(r)));
            client.flush();
        }
        return true;
    }

    public static void add(final Command cmd){
        COMMANDS.put(cmd.key(), cmd);
    }

    public static Collection<Command> commands(){
        return COMMANDS.values();
    }
}
