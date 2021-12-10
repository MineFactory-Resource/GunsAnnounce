package teamuni.net.autoannounce;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public final class Main extends JavaPlugin implements Listener {
    FileConfiguration config = getConfig();
    Logger logger = getServer().getLogger();
    Timer timerAnnouncement = new Timer();

    @Override
    public void onEnable() {
        getLogger().info("Auto announce Enable. made by fade");
        this.saveDefaultConfig();
        super.onEnable();
        String msg = this.getConfig().getString("Message");
        timerAnnouncement.schedule(new TimerTask() {
            @Override
            public void run() {
                getServer().broadcastMessage(msg);
            }
        }, 1000, 30000);
    }

    @Override
    public void onDisable() {
        getLogger().info("Auto announce Disable. made by fade");
    }
}
