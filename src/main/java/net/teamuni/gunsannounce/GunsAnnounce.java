package net.teamuni.gunsannounce;

import java.lang.String;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import net.teamuni.gunscore.api.GunsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class GunsAnnounce extends JavaPlugin {
    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    private MessageManager messageManager;
    private boolean isUseRandom;
    private long period;
    private int taskID = 0;
    private int num = 0;
    private final Map<String, List<String>> messageMap = new HashMap<>();

    public void onEnable() {
        saveDefaultConfig();
        this.messageManager = new MessageManager(this);
        this.messageManager.createMessagesYml();
        this.period = getConfig().getLong("period");
        this.messageMap.putAll(this.messageManager.getMessages());
        this.isUseRandom = getConfig().getBoolean("print_random");
        registerTask();
        getCommand("공지").setTabCompleter(new CommandTabCompleter());
    }

    private void registerTask() {
        List<String> tipList = this.messageMap.get("announce_messages");

        if (tipList.size() == 0) {
            return;
        }

        if (this.taskID != 0) {
            Bukkit.getScheduler().cancelTask(this.taskID);
        }

        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            Set<Player> players = Bukkit.getOnlinePlayers().stream().filter(player -> GunsAPI.getGame(player) == null).collect(
                Collectors.toSet());
            if (this.isUseRandom) {
                int randomNumber = RANDOM.nextInt(tipList.size());
                players.forEach(player -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', tipList.get(randomNumber))));
            } else {
                if (this.num == tipList.size()) {
                    this.num = 0;
                }
                players.forEach(player -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', tipList.get(this.num))));
                this.num++;
            }
        }, 0L, period);
    }


    @Override
    public void onDisable() {
        saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player player) {
            if (command.getName().equalsIgnoreCase("공지") && player.hasPermission("gunsannounce.manage")) {
                if (args.length > 0) {
                    switch (args[0]) {
                        case "리로드","reload" -> {
                            if (args.length != 1) {
                                this.messageManager.sendTranslatedMessage(player, this.messageMap.get("not_available_command"));
                                return false;
                            }

                            reloadConfig();
                            this.period = getConfig().getLong("period");
                            this.isUseRandom = getConfig().getBoolean("print_random");
                            this.messageManager.reload();
                            this.messageMap.putAll(this.messageManager.getMessages());
                            this.messageManager.sendTranslatedMessage(player, this.messageMap.get("reload_message"));
                            registerTask();
                        }
                        case "주기","period" -> {
                            if (args.length != 2) {
                                this.messageManager.sendTranslatedMessage(player, this.messageMap.get("not_available_command"));
                                return false;
                            }

                            if (!args[1].matches("[0-9]+")) {
                                this.messageManager.sendTranslatedMessage(player, this.messageMap.get("syntax_error_message"));
                                return false;
                            }

                            long i = Long.parseLong(args[1]);
                            this.period = i;
                            this.messageManager.sendTranslatedMessage(player, this.messageMap.get("set_period_message"));
                            getConfig().set("period", i);
                            saveConfig();
                            registerTask();
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
