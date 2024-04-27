package dev.jaegyu.QuickAlerts.commands;

import dev.jaegyu.QuickAlerts.NotifPlayer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

// I hate this shit sometimes
@SuppressWarnings("SpellCheckingInspection")
public class togglePings extends CommandBase {

    private final NotifPlayer notifPlayer;

    public togglePings(NotifPlayer notifPlayer) {
        this.notifPlayer = notifPlayer;
    }

    @Override
    public String getCommandName() {
        return "jqa-togglepings";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/jqa-togglepings";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] strings) {
        if (notifPlayer.togglePings())
            sender.addChatMessage(new ChatComponentText("§aPings enabled!"));
        else
            sender.addChatMessage(new ChatComponentText("§cPings disabled!"));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }
}
