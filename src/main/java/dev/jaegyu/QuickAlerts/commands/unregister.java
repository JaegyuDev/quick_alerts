package dev.jaegyu.QuickAlerts.commands;

import dev.jaegyu.QuickAlerts.NotifPlayer;
import dev.jaegyu.QuickAlerts.PingLocations;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class unregister extends CommandBase {

    private final NotifPlayer notifPlayer;

    public unregister(NotifPlayer notifPlayer) {
        this.notifPlayer = notifPlayer;
    }

    @Override
    public String getCommandName() {
        return "jsm-unregister";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/jsm-unregister <name>";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }


    //
    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length!= 1) {
            sender.addChatMessage(new ChatComponentText("§c" + getCommandUsage(sender)));
            return;
        }

        if(notifPlayer.unregisterPingLocation(args[0])) {
            if (args[0].equalsIgnoreCase("all")) {
                notifPlayer.setPingLocs(new PingLocations());
                sender.addChatMessage(new ChatComponentText("Unregistered all locations"));
            }

            sender.addChatMessage(new ChatComponentText("Unregistered " + args[0]));
        }
        else
            sender.addChatMessage(new ChatComponentText("Could not find " + args[0] + " to unregister"));
    }
}
