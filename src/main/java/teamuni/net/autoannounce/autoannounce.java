package teamuni.net.autoannounce;


import java.lang.String;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class autoannounce extends JavaPlugin {
    BukkitRunnable runnable;
    String msg;
    long delay;

    public void onEnable() {
        Bukkit.getLogger().info("auto announce Enable. made by fade");
        saveDefaultConfig();
        delay = this.getConfig().getLong("delay", 1200);
        msg = this.getConfig().getString("message", "message");
        updateRunnable();
    }

    private void updateRunnable() {
        if(runnable != null)
            runnable.cancel();
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(msg.replace("\\n", "\n"));
            }
        };
        runnable.runTaskTimer(this, delay, delay);
    }


    @Override
    public void onDisable() {
        Bukkit.getLogger().info("auto announce Disable. made by fade");
        saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        if (args.length < 2) return true;
        String arg0 = args[0].toLowerCase();
        String arg1 = args[1];
        switch (arg0) {
            case "delay": {
                long i;
                try {
                    i = Long.parseLong(arg1);
                } catch (Exception e) {
                    sender.sendMessage("잘못된 숫자입니다");
                    return true;
                }
                getConfig().set("delay", i);
                saveConfig();
                delay = i;
                updateRunnable();
                player.sendMessage("딜레이를 설정하였습니다!");
                break;
            }
            case "message": {
                String str = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                getConfig().set("message", str);
                saveConfig();
                msg = str;
                sender.sendMessage("공지를 수정했습니다!");
            }
            default: {
                player.sendMessage("/autoannounce delay [딜레이] - 딜레이를 설정합니다. [20 = 1초]");
                player.sendMessage("/autoannounce message [공지] - 공지를 수정합니다. [줄바꿈 \n ]");
                break;
            }
        }
        return true;
    }
}
