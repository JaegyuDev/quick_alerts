package dev.jaegyu.QuickAlerts.Alerts;

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

        JsonObject patternObject = json.get("pattern").getAsJsonObject();

        this.pattern = Pattern.compile(patternObject.get("pattern").getAsString(),
                patternObject.get("flags").getAsInt());
    }

    public boolean PatternIn(String s) {
        Matcher m = pattern.matcher(s);
        return m.find();
    }

    public String getPatternAsString() {
        return pattern.pattern();
    }
}
