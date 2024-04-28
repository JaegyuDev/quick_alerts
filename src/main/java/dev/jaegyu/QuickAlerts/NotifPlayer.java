package dev.jaegyu.QuickAlerts;

import dev.jaegyu.QuickAlerts.Alerts.ChatPing;
import dev.jaegyu.QuickAlerts.Alerts.LocationPing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.Stack;

import static dev.jaegyu.QuickAlerts.QuickAlerts.logger;
import static dev.jaegyu.QuickAlerts.QuickAlerts.saveState;

@SuppressWarnings("SpellCheckingInspection")
public class NotifPlayer {
    private ResourceLocation NotifSound = new ResourceLocation("note.pling");

    public PingVec<LocationPing> locPings = new PingVec<>();
    public PingVec<ChatPing> chatPings = new PingVec<>();

    private final Stack<ResourceLocation> pingQueue = new Stack<>();
    private boolean PingsEnabled = true;
    private final boolean DebugTextWithFormat = true;

    @SubscribeEvent
    public void tickEvent(TickEvent.PlayerTickEvent e) {
        if (!PingsEnabled)
            return;

        for (LocationPing ping : locPings) {
            if (ping.ContainsBlock(e.player.getPosition())) {
                e.player.playSound(NotifSound.toString(), 1.0F, 1.0F);
                break;
            }
        }

        // Pop an element off of the pingQueue. again this is really hacky and
        // ill have to find a solution for this later.
        if(!pingQueue.isEmpty())
            e.player.playSound(pingQueue.pop().toString(), 1.0F, 1.0F);
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent e) {
        if (!PingsEnabled)
            return;

        if (DebugTextWithFormat)
            logger.info(e.message.getFormattedText());

        // This is really fucking hacky, and I'll definitely need to change this.
        String unformatted_text = Utils.Color.unformatString(e.message.getFormattedText());

        for (ChatPing ping : chatPings) {
            if (ping.PatternIn(unformatted_text)) {
                if (!(pingQueue.size() > 25)){
                    pingQueue.push(NotifSound);
                }
            }
        }
    }

    @SubscribeEvent
    public void playerDisconnectFromServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent client) {
        logger.info("saving state");
        saveState();
    }

    // This returns the new state of PingsEnabled
    public boolean togglePings() {
        PingsEnabled =! PingsEnabled;
        return PingsEnabled;
    }

    public boolean registerLocationPing(String name, double xPos, double yPos, double zPos, int radius) {
        if (locPings.containsName(name)) {
            return false;
        }

        if (locPings.contains(null)) {
            locPings.set(locPings.indexOf(null), new LocationPing(name, xPos, yPos, zPos, radius));
        } else {
            locPings.add(new LocationPing(name, xPos, yPos, zPos, radius));
        }

        return true;
    }

    public boolean unregisterLocationPing(String name) {
        return locPings.remove(name);
    }

    // pretty boilerplate, not sure how to fix it?
    public boolean registerChatPing(String name, String pattern, Boolean isRaw) {
        if(chatPings.containsName(name)) {
            return false;
        }
        if (chatPings.contains(null)) {
            chatPings.set(chatPings.indexOf(null), new ChatPing(name, pattern, isRaw));
        } else {
            chatPings.add(new ChatPing(name, pattern, isRaw));
        }

        return true;
    }

    public boolean unregisterChatPing(String name) {
        return chatPings.remove(name);
    }

    // getters and setters for json rw
    public ResourceLocation getNotifSound() {
        return NotifSound;
    }

    public void setNotifSound(ResourceLocation notifSound) {
        // we might need to pass getResourcePath here
        NotifSound = notifSound;
    }

    public PingVec<LocationPing> getLocPings() {
        return this.locPings;
    }

    public void setLocPings(PingVec<LocationPing> locPings) {
        this.locPings = locPings;
    }

    public PingVec<ChatPing> getChatPings() {
        return this.chatPings;
    }

    public void setChatPings(PingVec<ChatPing> chatPings) {
        this.chatPings = chatPings;
    }

    public boolean isPingsEnabled() {
        return PingsEnabled;
    }

    public void setPingsEnabled(boolean pingsEnabled) {
        PingsEnabled = pingsEnabled;
    }


}