package dev.jaegyu.QuickAlerts.commands;

import dev.jaegyu.QuickAlerts.Alerts.ChatPing;
import dev.jaegyu.QuickAlerts.Alerts.LocationPing;
import dev.jaegyu.QuickAlerts.NotifPlayer;
import dev.jaegyu.QuickAlerts.PingVec;
import dev.jaegyu.QuickAlerts.Utils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

public class chatCommands extends CommandBase{
    public final NotifPlayer notifPlayer;
    public chatCommands(NotifPlayer notifPlayer) {
        this.notifPlayer = notifPlayer;
    }

    @Override
    public String getCommandName() {
        return "jqa-chat";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Usage: /jqa-chat <subcommand> args...\n" +
                "Subcommands:\n" +
                " register <name> <pattern> - registers a sound cue that will pop whenever there " +
                    "is a message in chat that matches the regex\n" +
                " unregister <name> - unregisters the chat ping\n" +
                " list - lists all of your registered chat pings";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.addChatMessage(new ChatComponentText("§c" + getCommandUsage(sender)));
            return;
        }

        switch(args[0]) {
            case "register":
                register(sender, Utils.dropFirstItem(args));
                break;

            case "unregister":
                unregister(sender, Utils.dropFirstItem(args));
                break;

            case "list":
                list(sender, Utils.dropFirstItem(args));
                break;

            default: {
                sender.addChatMessage(new ChatComponentText("§c" + getCommandUsage(sender)));
                return;
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        List<String> options = new ArrayList<>();

        if (args.length == 1) {
            options.add("register");
            options.add("unregister");
            options.add("list");
        }

        if (args.length > 1) {
            String subCommand = args[0];
            switch (subCommand) {
                case "register":
                    // we can't auto complete here since it is a regex pattern.
                    break;

                case "unregister":
                    PingVec<ChatPing> chatPings = notifPlayer.getChatPings();
                    for (ChatPing chatPing : chatPings) {
                        options.add(chatPing.getName());
                    }
                    break;

                default:
                    break;
            }
        }

        return options;
    }


    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }

    private void register(ICommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.addChatMessage(new ChatComponentText("§c" + getCommandUsage(sender)));
            return;
        }

        // maybe there is a better way to do this?
        String name = args[0];
        String pattern = args[1];

        if (notifPlayer.registerChatPing(name, pattern)) {
            sender.addChatMessage(new ChatComponentText("§aRegistered " + name + " for " + pattern));
        }
    }

    private void unregister(ICommandSender sender, String[] args) {
        if (args.length!= 1) {
            sender.addChatMessage(new ChatComponentText("§c" + getCommandUsage(sender)));
            return;
        }

        if(notifPlayer.unregisterLocationPing(args[0])) {
            if (args[0].equalsIgnoreCase("all")) {
                notifPlayer.setLocPings(new PingVec<>());
                sender.addChatMessage(new ChatComponentText("Unregistered all chat pings"));
            }

            sender.addChatMessage(new ChatComponentText("Unregistered all pings"));
        } else if (notifPlayer.unregisterChatPing(args[0])) {
            sender.addChatMessage(new ChatComponentText("Unregistered " + args[0]));
            return;
        }
            sender.addChatMessage(new ChatComponentText("Could not find " + args[0] + " to unregister"));
    }

    private void list(ICommandSender sender, String[] args) {
        PingVec<ChatPing> chatPings = notifPlayer.getChatPings();
        StringBuilder chatPingsResponse = new StringBuilder();

        chatPingsResponse
                .append(Utils.Color.GOLD)
                .append("ChatPing Names:\n");

        // Could just replace this with 2 color characters, however for now ill leave it as is.
        for (ChatPing chatPing : chatPings) {
            chatPingsResponse
                    .append(Utils.Color.GOLD)
                    .append(" - ")
                    .append(chatPing.getName())
                    .append(Utils.Color.RESET)
                    .append("\n");
        }

        // removes last newline character.
        if (chatPingsResponse.length() > 0) {
            chatPingsResponse.deleteCharAt(chatPingsResponse.length() - 1);
        }

        sender.addChatMessage(new ChatComponentText(chatPingsResponse.toString()));
    }
}
