package de.howaner.bukkitmaintenance;

import com.google.gson.Gson;
import de.howaner.bukkitmaintenance.config.Config;
import de.howaner.bukkitmaintenance.util.ImageUtils;
import de.howaner.bukkitmaintenance.util.PacketListener;
import lombok.Cleanup;
import lombok.Getter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainServer {

    public static MainServer instance;

    @Getter
    private Gson gson;

    @Getter
    private String icon = "";

    public static void main(String[] args) {
        Logger.getGlobal().log(Level.INFO, "Started BukkitMaintenance v1.2!");
        instance = new MainServer();
        instance.load();
    }

    public void load() {
        this.extractLibs();
        this.gson = new Gson();

        if (!new File("config.yml").exists()) Config.saveConfig();
        Config.loadConfig();

        //Servericon
        if (new File("server-icon.png").exists()) {
            try {
                BufferedImage image = ImageIO.read(new File("server-icon.png"));
                this.icon = "data:image/png;base64," + ImageUtils.encodeToString(image, "png");
                Logger.getGlobal().log(Level.INFO, "Found Server-Icon.");
            } catch (Exception e) {
                this.icon = "";
                Logger.getGlobal().log(Level.SEVERE, () -> "Can't load Server-Icon: " + e.getMessage());
                e.printStackTrace();
            }
        }

        Thread thread = new PacketListener(this);
        thread.start();
    }

    public void extractLibs() {
        if (!new File("lib/gson-2.2.4.jar").exists()) {
            this.extractLib("/lib/gson-2.2.4.jar", new File("lib/gson-2.2.4.jar"));
        }
        if (!new File("lib/yamlbeans-1.06.jar").exists()) {
            this.extractLib("/lib/yamlbeans-1.06.jar", new File("lib/yamlbeans-1.06.jar"));
        }
    }

    public void extractLib(String path, File to) {
        try {
            @Cleanup InputStream stream = this.getClass().getResourceAsStream(path);
            if (stream == null) {
                return;
            }

            int readBytes;
            byte[] buffer = new byte[4096];

            if (!to.getParentFile().exists()) {
                to.getParentFile().mkdirs();
            }

            if (to.exists()) {
                to.delete();
            }

            to.createNewFile();

            try (OutputStream out = new FileOutputStream(to)) {
                while ((readBytes = stream.read(buffer)) > 0) {
                    out.write(buffer, 0, readBytes);
                }
            }
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, () -> "Can't extract Library " + to.getName() + "!");
            e.printStackTrace();
        }
    }
}
