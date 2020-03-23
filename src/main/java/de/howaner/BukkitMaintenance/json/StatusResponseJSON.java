package de.howaner.BukkitMaintenance.json;

import java.util.List;

/**
 * Status Response in 1.7
 *
 * @author franz
 */
public class StatusResponseJSON {
    private Version version;
    private Players players;
    private Description description;
    private String favicon;

    public Version getVersion() {
        return this.version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public Players getPlayers() {
        return this.players;
    }

    public void setPlayers(Players players) {
        this.players = players;
    }

    public Description getDescription() {
        return this.description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public String getFavIcon() {
        return this.favicon;
    }

    public void setFavIcon(String favicon) {
        this.favicon = favicon;
    }


    public static class Description {
        private String text;

        public String getText() {
            return this.text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class SamplePlayer {
        private String name;
        private String id;

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUUID() {
            return this.id;
        }

        public void setUUID(String uuid) {
            this.id = uuid;
        }
    }

    public static class Players {
        private int max;
        private int online;
        private List<SamplePlayer> sample;

        public int getMax() {
            return this.max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public int getOnline() {
            return this.online;
        }

        public void setOnline(int online) {
            this.online = online;
        }

        public List<SamplePlayer> getSample() {
            return this.sample;
        }

        public void setSample(List<SamplePlayer> sample) {
            this.sample = sample;
        }
    }

    public static class Version {
        private String name;
        private int protocol;

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getProtocol() {
            return this.protocol;
        }

        public void setProtocol(int protocol) {
            this.protocol = protocol;
        }
    }

}