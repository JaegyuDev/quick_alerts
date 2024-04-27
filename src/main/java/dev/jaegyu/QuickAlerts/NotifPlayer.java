package dev.jaegyu.QuickAlerts;

import java.util.Vector;

import dev.jaegyu.QuickAlerts.LocationAlerts.LocationPing;
import dev.jaegyu.QuickAlerts.LocationAlerts.LocationPings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import static dev.jaegyu.QuickAlerts.QuickAlerts.logger;
import static dev.jaegyu.QuickAlerts.QuickAlerts.saveState;

@SuppressWarnings("SpellCheckingInspection")
public class NotifPlayer {
    private ResourceLocation NotifSound = new ResourceLocation("note.pling");
    public LocationPings PingLocs = new LocationPings();
    private boolean PingsEnabled = true;

    public boolean registerPingLocation(String name, double xPos, double yPos, double zPos, int radius) {
        if (PingLocs.containsName(name)) {
            return false;
        }

        if (PingLocs.contains(null)) {
            PingLocs.set(PingLocs.indexOf(null), new LocationPing(name, xPos, yPos, zPos, radius));
        } else {
            PingLocs.add(new LocationPing(name, xPos, yPos, zPos, radius));
        }

        return true;
    }

    public boolean unregisterPingLocation(String name) {
        return PingLocs.remove(name);
    }

    // This returns the new state of PingsEnabled
    public boolean togglePings() {
        PingsEnabled =! PingsEnabled;
        return PingsEnabled;
    }

    @SubscribeEvent
    public void tickEvent(TickEvent.PlayerTickEvent e) {
        if(!PingsEnabled)
            return;

        for (LocationPing ping : PingLocs) {
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

    public LocationPings getPingLocs() {
        return PingLocs;
    }

    public void setPingLocs(Vector<LocationPing> pingLocs) {
        PingLocs = (LocationPings) pingLocs;
    }

    public boolean isPingsEnabled() {
        return PingsEnabled;
    }

    public void setPingsEnabled(boolean pingsEnabled) {
        PingsEnabled = pingsEnabled;
    }
}