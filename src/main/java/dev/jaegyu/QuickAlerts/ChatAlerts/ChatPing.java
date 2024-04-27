package dev.jaegyu.QuickAlerts.ChatAlerts;

import com.google.gson.JsonObject;
import dev.jaegyu.QuickAlerts.BasePing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatPing extends BasePing {
    private final Pattern pattern;
    public ChatPing(String name, String pattern) {
        super(name);
        this.pattern = Pattern.compile(pattern);
    }
    public ChatPing(JsonObject json) {
        super(json);
        this.pattern = Pattern.compile(json.get("pattern").getAsString());
    }

    public boolean ContainsPattern(String s) {
        Matcher m = pattern.matcher(s);
        return m.find();
    }
}
