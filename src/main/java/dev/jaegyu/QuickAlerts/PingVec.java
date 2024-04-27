package dev.jaegyu.QuickAlerts;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Vector;

public class PingVec<T extends BasePing> extends Vector<T> {
    public PingVec() {}

    public PingVec(JsonArray json, Class<T> clazz) {
        for (int i = 0; i < json.size(); i++) {
            try {
                this.add(clazz.getConstructor(JsonObject.class).newInstance(json.get(i).getAsJsonObject()));
            } catch (Exception e) {
                //TODO: change this lol
                e.printStackTrace();
            }
        }
    }

    public boolean containsName(String name) {
        for (T ping : this) {
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
