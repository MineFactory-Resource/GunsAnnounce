package teamuni.net.autoannounce;


import java.lang.String;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class AutoAnnounce extends JavaPlugin {
    private MessageManager messageManager;
    private boolean isUseRandom;
    private long delay;
    private long period;
    private int num = 0;
    private final Map<String, List<String>> messageMap = new HashMap<>();

    public void onEnable() {
        saveDefaultConfig();
        this.messageManager = new MessageManager(this);
        this.messageManager.createMessagesYml();
        this.delay = getConfig().getLong("delay");
        this.period = getConfig().getLong("period");
        this.messageMap.putAll(this.messageManager.getMessages());
        this.isUseRandom = getConfig().getBoolean("print_random");
        registerTask();
        getCommand("autoannounce").setTabCompleter(new CommandTabCompleter());
    }

    private void registerTask() {
        List<String> tipList = this.messageMap.get("announce_messages");

        if (tipList.size() == 0) {
            return;
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if (this.isUseRandom) {
                Random random = new Random();
                int randomNumber = random.nextInt(tipList.size());
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', tipList.get(randomNumber)));
            } else {
                if (this.num == tipList.size() - 1) {
                    this.num = 0;
                }
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', tipList.get(this.num)));
                this.num++;
            }
        }, delay, period);
    }


    @Override
    public void onDisable() {
        saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player player) {
            if (command.getName().equalsIgnoreCase("autoannounce") && player.hasPermission("autoannounce.manage")) {
                if (args.length > 0) {
                    switch (args[0]) {
                        case "reload" -> {
                            reloadConfig();
                            this.delay = getConfig().getLong("delay");
                            this.period = getConfig().getLong("period");
                            this.isUseRandom = getConfig().getBoolean("print_random");
                            this.messageManager.reload();
                            this.messageMap.putAll(this.messageManager.getMessages());
                            this.messageManager.sendTranslatedMessage(player, this.messageMap.get("reload_message"));
                        }
                        case "period" -> {
                            if (!args[1].matches("[0-9]+")) {
                                this.messageManager.sendTranslatedMessage(player, this.messageMap.get("syntax_error_message"));
                                return false;
                            }
                            long i = Long.parseLong(args[1]);
                            getConfig().set("period", i);
                            saveConfig();
                            this.period = i;
                            this.messageManager.sendTranslatedMessage(player, this.messageMap.get("set_period_message"));
                        }
                        default -> this.messageManager.sendTranslatedMessage(player, this.messageMap.get("not_available_command"));
                    }
                } else {
                    this.messageManager.sendTranslatedMessage(player, this.messageMap.get("guide_message"));
                }
            }
        }
        return false;
    }
}
