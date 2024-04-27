package dev.jaegyu.QuickAlerts;

import com.google.gson.JsonObject;
import net.minecraft.util.BlockPos;


public class PingLocation {
    private String name;
    private double xPos, yPos, zPos;
    private int radius;
    public PingLocation(String name, double xPos, double yPos, double zPos, int radius) {
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
        this.radius = radius;
    }

    public PingLocation(JsonObject json) {
        this.name = json.get("name").getAsString();
        this.xPos = json.get("xPos").getAsDouble();
        this.yPos = json.get("yPos").getAsDouble();
        this.zPos = json.get("zPos").getAsDouble();
        this.radius = json.get("radius").getAsInt();
    }

    // This isn't really a true radius, but more of an approximation.
    public boolean ContainsBlock(BlockPos b) {
        return b.distanceSqToCenter(xPos, yPos, zPos) <  (radius * radius);
    }

    public String getName() {
        return name;
    }

}
