package dev.jaegyu.QuickAlerts.commands;

import dev.jaegyu.QuickAlerts.NotifPlayer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

@SuppressWarnings("SpellCheckingInspection")
public class registerAtMe extends CommandBase {
    private final NotifPlayer notifPlayer;

    public registerAtMe(NotifPlayer notifPlayer) {
        this.notifPlayer = notifPlayer;
    }

    @Override
    public String getCommandName() {
        return "jsm-registeratme";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/jsm-registeratme <name>";
    }

    // Technically not necessary but might catch a bug somewhere sometime lol
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return sender instanceof EntityPlayer;
    }

    public void processCommand(ICommandSender sender, String[] args) {
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

    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }
}
