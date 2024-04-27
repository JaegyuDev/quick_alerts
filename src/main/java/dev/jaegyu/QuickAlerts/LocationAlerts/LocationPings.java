package dev.jaegyu.QuickAlerts.LocationAlerts;

import com.google.gson.JsonArray;

import java.util.Vector;

public class LocationPings extends Vector<LocationPing> {
    public LocationPings() {}
    public LocationPings(JsonArray json) {
        for (int i = 0; i < json.size(); i++) {
            this.add(new LocationPing(json.get(i).getAsJsonObject()));
        }
    }

    public boolean containsName(String name) {
        for (LocationPing ping : this) {
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
