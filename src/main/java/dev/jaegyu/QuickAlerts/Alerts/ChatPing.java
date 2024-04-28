package dev.jaegyu.QuickAlerts.Alerts;

import com.google.gson.JsonObject;
import dev.jaegyu.QuickAlerts.BasePing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatPing extends BasePing {
    private final Pattern pattern;
    private final Boolean isRaw;
    public ChatPing(String name, String pattern, Boolean isRaw) {
        super(name);
        this.pattern = Pattern.compile(pattern);
        this.isRaw = isRaw;
    }
    public ChatPing(JsonObject json) {
        super(json);
        this.isRaw = json.get("isRaw").getAsBoolean();

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

    public boolean isRaw() {
        return isRaw;
    }
}
