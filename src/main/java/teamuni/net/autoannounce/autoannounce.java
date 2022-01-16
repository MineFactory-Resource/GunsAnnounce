package teamuni.net.autoannounce;


import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class autoannounce extends JavaPlugin {
    Timer timerForAnnouncement = new Timer();

    public void onEnable() {
        Bukkit.getLogger().info("auto announce Enable. made by fade");
        saveConfig();
        File cfile = new File(getDataFolder(), "config.yml");
        if (cfile.length() == 0) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
        Long times = this.getConfig().getLong("times");
        String msg = this.getConfig().getString("Messages.1");

        timerForAnnouncement.schedule(new TimerTask() {
            @Override
            public void run() {
                getServer().broadcastMessage(msg);
            }
        }, 5000, times);
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("auto announce Disable. made by fade");
    }
}