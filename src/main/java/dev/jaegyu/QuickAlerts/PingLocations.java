package dev.jaegyu.QuickAlerts;

import com.google.gson.JsonArray;

import java.util.Vector;

public class PingLocations extends Vector<PingLocation> {
    public PingLocations() {}
    public PingLocations(JsonArray json) {
        for (int i = 0; i < json.size(); i++) {
            this.add(new PingLocation(json.get(i).getAsJsonObject()));
        }
    }

    public boolean containsName(String name) {
        for (PingLocation ping : this) {
            if (ping.getName().contains(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean remove(String string) {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getName().equals(string)) {
                this.remove(i);
                return true;
            }
        }
        return false;
    }

}
