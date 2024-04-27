package dev.jaegyu.QuickAlerts.Alerts;

import com.google.gson.JsonObject;
import dev.jaegyu.QuickAlerts.BasePing;
import net.minecraft.util.BlockPos;


public class LocationPing extends BasePing {
    private final double xPos, yPos, zPos;
    private final int radius;
    public LocationPing(String name, double xPos, double yPos, double zPos, int radius) {
        super(name);
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
        this.radius = radius;
    }

    public LocationPing(JsonObject json) {
        super(json);
        this.xPos = json.get("xPos").getAsDouble();
        this.yPos = json.get("yPos").getAsDouble();
        this.zPos = json.get("zPos").getAsDouble();
        this.radius = json.get("radius").getAsInt();
    }

    // This isn't really a true radius, but more of an approximation.
    public boolean ContainsBlock(BlockPos b) {
        return b.distanceSqToCenter(xPos, yPos, zPos) <  (radius * radius);
    }

}
