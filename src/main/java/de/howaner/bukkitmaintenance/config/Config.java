package de.howaner.bukkitmaintenance.config;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import lombok.Cleanup;
import lombok.Getter;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {

    private Config() {
        throw new IllegalStateException("Utility class");
    }

    @Getter
    private static String bindAddress = "0.0.0.0";

    @Getter
    private static int bindPort = 25565;

    @Getter
    private static String kickMessage = "§4Wartung! Kommen sie später wieder!";

    @Getter
    private static String versionName = "Wartung";

    @Getter
    private static String motd = "Wir sind bald wieder für sie da!";

    @Getter
    private static String multilineMotd = "Wartungsmodus!\nBald können sie wieder rein!";

    public static void loadConfig() {
        try {
            @Cleanup YamlReader reader = new YamlReader(new FileReader("config.yml"));

            Map map = (Map) reader.read();
            bindAddress = (String) map.get("BindAddress");
            bindPort = Integer.parseInt((String) map.get("BindPort"));
            kickMessage = (String) map.get("KickMessage");
            versionName = (String) map.get("Version");
            motd = (String) map.get("Motd");
            multilineMotd = (String) map.get("MultilineMotd");
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, () -> "Error while loading the Config: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void saveConfig() {
        try {
            @Cleanup YamlWriter writer = new YamlWriter(new FileWriter("config.yml"));

            HashMap<String, String> map = new HashMap<>();
            map.put("BindAddress", bindAddress);
            map.put("BindPort", String.valueOf(bindPort));
            map.put("KickMessage", kickMessage);
            map.put("Version", versionName);
            map.put("Motd", motd);
            map.put("MultilineMotd", multilineMotd);

            writer.write(map);
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, () -> "Error while saving the Config: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
