package dev.jaegyu.QuickAlerts;

import java.util.Vector;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import static dev.jaegyu.QuickAlerts.QuickAlerts.logger;
import static dev.jaegyu.QuickAlerts.QuickAlerts.saveState;

@SuppressWarnings("SpellCheckingInspection")
public class NotifPlayer {
    private ResourceLocation NotifSound = new ResourceLocation("note.pling");
    public PingLocations PingLocs = new PingLocations();
    private boolean PingsEnabled = true;

    public boolean registerPingLocation(String name, double xPos, double yPos, double zPos, int radius) {
        if (PingLocs.containsName(name)) {
            return false;
        }

        if (PingLocs.contains(null)) {
            PingLocs.set(PingLocs.indexOf(null), new PingLocation(name, xPos, yPos, zPos, radius));
        } else {
            PingLocs.add(new PingLocation(name, xPos, yPos, zPos, radius));
        }

        return true;
    }

    public boolean unregisterPingLocation(String name) {
        return PingLocs.remove(name);
    }

    // This returns the new state of PingsEnabled
    public boolean togglePings() {
        PingsEnabled =!PingsEnabled;
        return PingsEnabled;
    }

    @SubscribeEvent @SuppressWarnings("unused")
    public void tickEvent(TickEvent.PlayerTickEvent e) {
        if(!PingsEnabled)
            return;

        for (PingLocation ping : PingLocs) {
            if (ping.ContainsBlock(e.player.getPosition())) {
                e.player.playSound(NotifSound.toString(), 1.0F, 1.0F);
                break;
            }
        }
    }

    @SubscribeEvent
    public void playerDisconnectFromServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent client) {
        logger.info("saving state");
        saveState();
    }

    // getters and setters for json rw
    public ResourceLocation getNotifSound() {
        return NotifSound;
    }

    public void setNotifSound(ResourceLocation notifSound) {
        // we might need to pass getResourcePath here
        NotifSound = notifSound;
    }

    public PingLocations getPingLocs() {
        return PingLocs;
    }

    public void setPingLocs(Vector<PingLocation> pingLocs) {
        PingLocs = (PingLocations) pingLocs;
    }

    public boolean isPingsEnabled() {
        return PingsEnabled;
    }

    public void setPingsEnabled(boolean pingsEnabled) {
        PingsEnabled = pingsEnabled;
    }
}