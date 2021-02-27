package de.howaner.bukkitmaintenance.util;

public class PingUtil {

    public static String createPingString(int protocolVersion, String version, String motd, int players, int slots) {
        return "§1" +
                "\0" + protocolVersion +
                "\0" + version +
                "\0" + motd +
                "\0" + players +
                "\0" + slots;
    }

}
