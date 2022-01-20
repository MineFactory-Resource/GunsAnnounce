package teamuni.net.autoannounce;


import java.io.File;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import jdk.javadoc.internal.doclets.toolkit.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
        saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        String arg0 = args[0];
        String arg0lowercase = arg0.toLowerCase();
        if (player.isOp()) {
            if (args.length == 0)
                return false;
            switch (arg0lowercase) {
                case "times": {
                    player.sendMessage("시간을 설정하였습니다!");
                    getConfig().set("times", args[1]);
                    saveConfig();
                    break;
                }
                case "message": {
                    player.sendMessage("공지를 설정하였습니다!");
                    getConfig().set("Messages.1", args[1]);
                    saveConfig();
                    break;
                }
                default: {
                    player.sendMessage("/autoannounce times [시간] - 시간을 설정합니다.");
                    player.sendMessage("/autoannounce message [공지] - 공지를 설정합니다.");
                    break;
                }
            }
        } else {
            player.sendMessage("명령어를 사용할 권한이 없습니다!");
        }
        return true;
    }
}
