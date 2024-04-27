package dev.jaegyu.QuickAlerts.commands;

import dev.jaegyu.QuickAlerts.NotifPlayer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

public class register extends CommandBase {
    private final NotifPlayer notifPlayer;
    public register(NotifPlayer notifPlayer) {
        this.notifPlayer = notifPlayer;
    }

    @Override
    public String getCommandName() {
        return "jsm-register";
    }
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Usage: /jsm-register <name> <x> <y> <z> <radius>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
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
            sender.addChatMessage(new ChatComponentText("§aRegistered " + name + "at" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ()));
        }

    }

    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }
}
