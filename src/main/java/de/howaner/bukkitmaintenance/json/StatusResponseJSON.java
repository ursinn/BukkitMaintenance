package de.howaner.bukkitmaintenance.json;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Status Response in 1.7
 *
 * @author franz
 */
public class StatusResponseJSON {

    @Getter
    @Setter
    private Version version;

    @Getter
    @Setter
    private Players players;

    @Getter
    @Setter
    private Description description;

    @Getter
    @Setter
    private String favicon;

    public static class Description {

        @Getter
        @Setter
        private String text;
    }

    public static class SamplePlayer {

        @Getter
        @Setter
        private String name;

        @Getter
        @Setter
        private String id;
    }

    public static class Players {

        @Getter
        @Setter
        private int max;

        @Getter
        @Setter
        private int online;

        @Getter
        @Setter
        private List<SamplePlayer> sample;
    }

    public static class Version {

        @Getter
        @Setter
        private String name;

        @Getter
        @Setter
        private int protocol;
    }
}