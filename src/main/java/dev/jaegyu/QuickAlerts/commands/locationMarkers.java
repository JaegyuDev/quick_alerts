package dev.jaegyu.QuickAlerts.commands;

import dev.jaegyu.QuickAlerts.NotifPlayer;
import dev.jaegyu.QuickAlerts.PingVec;
import dev.jaegyu.QuickAlerts.Utils;
import dev.jaegyu.QuickAlerts.LocationAlerts.LocationPing;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

public class locationMarkers extends CommandBase {
    public final NotifPlayer notifPlayer;
    public locationMarkers(NotifPlayer notifPlayer) {
        this.notifPlayer = notifPlayer;
    }

    @Override
    public String getCommandName() {
        return "jqa-location";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Usage: /jqa-location <subcommand> args...\n" +
                "Subcommands:\n" +
                " register <name> <x> <y> <z> <radius> - registers a sound cue at the location given with " + "a specified radius\n" +
                " registerAtMe <name> - registers a sound cue at your current position\n" +
                " unregister <name> - unregisters the location ping\n" +
                " list - lists all of your registered location pings";
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

            case "registerAtMe":
                registerAtMe(sender, Utils.dropFirstItem(args));
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
            options.add("registerAtMe");
            options.add("unregister");
            options.add("list");
        }

        if (args.length > 1) {
            String subCommand = args[0];
            switch (subCommand) {
                case "register":
                    // we skip args[2] because that's a user specified name.
                    // we also just use the defaults of registerAtMe.
                    switch (args.length) {
                        case 3:
                            options.add(String.valueOf(pos.getX()));
                            break;

                        case 4:
                            options.add(String.valueOf(pos.getY()));
                            break;

                        case 5:
                            options.add(String.valueOf(pos.getZ()));
                            break;

                        case 6:
                            options.add(String.valueOf(3));
                            break;
                    }
                    break;

                case "unregister":
                    PingVec<LocationPing> locations = notifPlayer.getLocPings();
                    for (LocationPing location : locations) {
                        options.add(location.getName());
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
        if (args.length < 5) {
            sender.addChatMessage(new ChatComponentText("§c" + getCommandUsage(sender)));
            return;
        }

        // maybe there is a better way to do this?
        String name;
        BlockPos pos;
        int radius;
        try {
            name = args[0];
            /*
             The decompiler doesn't give property names, but from sheer guess
             arg1 -> sender
             arg2 -> args array
             arg3 -> index the blockpos starts at
             arg4 -> ?????
            */
            pos = CommandBase.parseBlockPos(sender, args, 1, true);
            radius = CommandBase.parseInt(args[4]);

        } catch (NumberFormatException e) {
            sender.addChatMessage(new ChatComponentText("§c" + getCommandUsage(sender)));
            return;

            // no multicatch :(
        } catch (NumberInvalidException e) {
            sender.addChatMessage(new ChatComponentText("§c" + getCommandUsage(sender)));
            return;
        }


        if (notifPlayer.registerPingLocation(name, pos.getX(), pos.getY(), pos.getZ(), radius)) {
            sender.addChatMessage(new ChatComponentText("§aRegistered " + name + " at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ()));
        }
    }

    private void registerAtMe(ICommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.addChatMessage(new ChatComponentText("§c" + getCommandUsage(sender)));
            return;
        }
        EntityPlayer entity = (EntityPlayer) sender;

        // register the ping location
        if (notifPlayer.registerPingLocation(args[0], entity.posX, entity.posY, entity.posZ, 3)) {
            // TODO: use chat components instead of strings
            sender.addChatMessage(new ChatComponentText("§aRegistered " + args[0] + " at " + entity.posX + ", " + entity.posY + ", " + entity.posZ));
        }
    }

    private void unregister(ICommandSender sender, String[] args) {
        if (args.length!= 1) {
            sender.addChatMessage(new ChatComponentText("§c" + getCommandUsage(sender)));
            return;
        }

        if(notifPlayer.unregisterPingLocation(args[0])) {
            if (args[0].equalsIgnoreCase("all")) {
                notifPlayer.setLocPings(new PingVec<>());
                sender.addChatMessage(new ChatComponentText("Unregistered all locations"));
            }

            sender.addChatMessage(new ChatComponentText("Unregistered " + args[0]));
        }
        else
            sender.addChatMessage(new ChatComponentText("Could not find " + args[0] + " to unregister"));
    }

    private void list(ICommandSender sender, String[] args) {
        PingVec<LocationPing> locations = notifPlayer.getLocPings();
        StringBuilder locationResponse = new StringBuilder();

        locationResponse
                .append(Utils.Color.GOLD)
                .append("Location Names:\n");

        // Could just replace this with 2 color characters, however for now ill leave it as is.
        for (LocationPing location : locations) {
            locationResponse
                    .append(Utils.Color.GOLD)
                    .append(" - ")
                    .append(location.getName())
                    .append(Utils.Color.RESET)
                    .append("\n");
        }

        // removes last newline character.
        if (locationResponse.length() > 0) {
            locationResponse.deleteCharAt(locationResponse.length() - 1);
        }

        sender.addChatMessage(new ChatComponentText(locationResponse.toString()));
    }
}
