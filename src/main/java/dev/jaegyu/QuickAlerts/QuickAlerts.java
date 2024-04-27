package dev.jaegyu.QuickAlerts;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import dev.jaegyu.QuickAlerts.Alerts.ChatPing;
import dev.jaegyu.QuickAlerts.Alerts.LocationPing;
import dev.jaegyu.QuickAlerts.commands.chatCommands;
import dev.jaegyu.QuickAlerts.commands.locationCommands;
import dev.jaegyu.QuickAlerts.commands.togglePings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

@Mod(modid = QuickAlerts.MODID, useMetadata=true)
public class QuickAlerts {
    static final String MODID = "quickalerts";

    private static final NotifPlayer notifPlayer = new NotifPlayer();
    public static final Logger logger = LogManager.getLogger(MODID);
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(notifPlayer);
        MinecraftForge.EVENT_BUS.register(this);

        ClientCommandHandler.instance.registerCommand(new locationCommands(notifPlayer));
        ClientCommandHandler.instance.registerCommand(new chatCommands(notifPlayer));
        ClientCommandHandler.instance.registerCommand(new togglePings(notifPlayer));

        loadState();
    }

    // sadly this only uses one state for every world. For my use case this is fine, however if
    // anyone else wants to use this they should keep it in mind. PR's are welcome :)
    public static void saveState() {
        try {
            File file = new File("QuickAlerts", "state.json");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            JsonWriter writer = new JsonWriter(new FileWriter(file, false));
            writeJson(writer);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void loadState(){
        try {
            File file = new File("QuickAlerts", "state.json");
            if (file.exists())
                readJson(file);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    // sadly these two are generated with ChatGPT. I couldn't figure out how to do it after about 6 hours and
    // i want to use this already lol.
    private static void readJson(File file) {
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(reader).getAsJsonObject();

            QuickAlerts.notifPlayer.setPingsEnabled(json.get("pingsEnabled").getAsBoolean());
            QuickAlerts.notifPlayer.setNotifSound(new ResourceLocation(json.get("notifSound").getAsString()));

            JsonArray locPings = json.get("locPings").getAsJsonArray();
            QuickAlerts.notifPlayer.setLocPings(new PingVec<>(locPings, LocationPing.class));

            JsonArray chatPings = json.get("chatPings").getAsJsonArray();
            QuickAlerts.notifPlayer.setChatPings(new PingVec<>(chatPings, ChatPing.class));

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (JsonSyntaxException e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
        } catch (Throwable e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println("Error closing FileReader: " + e.getMessage());
                }
            }
        }
    }

    private static void writeJson(JsonWriter writer) throws IOException {
        try {
            Gson gson = new Gson();
            writer.setIndent("    ");
            writer.beginObject();
            writer.name("formatVersion").value(1);
            writer.name("pingsEnabled").value(notifPlayer.isPingsEnabled());
            writer.name("notifSound").value(notifPlayer.getNotifSound().toString());

            // Serialize pingLocs to a JsonArray
            JsonArray locPingsArray = gson.toJsonTree(notifPlayer.getLocPings()).getAsJsonArray();

            writer.name("locPings");
            writer.beginArray();
            for (JsonElement element : locPingsArray) {
                gson.toJson(element, writer);
            }
            writer.endArray();

            // Serialize chatPings to a JsonArray
            JsonArray chatPingsArray = gson.toJsonTree(notifPlayer.getChatPings()).getAsJsonArray();

            writer.name("chatPings");
            writer.beginArray();
            for (JsonElement element : chatPingsArray) {
                gson.toJson(element, writer);
            }
            writer.endArray();

            writer.endObject();
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause().toString());
        }
    }

}

