package dev.jaegyu.QuickAlerts;

import com.google.gson.JsonObject;

public class BasePing {
    protected final String name;

    protected BasePing(String name) {
        this.name = name;
    }

    protected BasePing(JsonObject json) {
        this.name = json.get("name").getAsString();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
