package teamuni.net.autoannounce;


import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class autoannounce extends JavaPlugin {
    BukkitRunnable runnable;
    String msg;
    ArrayList<String> tipList;
    Random random;
    long delay;
    long period;

    public void onEnable() {
        saveDefaultConfig();
        try {
            delay = this.getConfig().getLong("delay");
            period = this.getConfig().getLong("period");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        tipList = new ArrayList<>(getConfig().getStringList("Messages"));
        random = new Random();
        updateRunnable();
        getCommand("autoannounce").setTabCompleter(new CommandTabCompleter());
    }

    private void updateRunnable() {
        if (runnable != null)
            runnable.cancel();
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                int randomNumber = random.nextInt(tipList.size());
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', tipList.get(randomNumber)));
            }
        };
        runnable.runTaskTimer(this, delay, period);
    }


    @Override
    public void onDisable() {
        saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("autoannounce")) {
            if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("autoannounce.reload")) {
                reloadConfig();
                saveConfig();
                try {
                    delay = getConfig().getLong("delay");
                    period = getConfig().getLong("period");
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                tipList = new ArrayList<>(getConfig().getStringList("Messages"));
                sender.sendMessage(ChatColor.GREEN + "Autoannounce has been reloaded!");
            }
            return true;
        }
        if (args.length == 1) {
            sender.sendMessage(ChatColor.BLUE + "[" + ChatColor.WHITE + " AutoAnnounce " + ChatColor.BLUE + "]" + ChatColor.GOLD + " /autoannounce delay [딜레이]" + ChatColor.GRAY + " - 딜레이를 설정합니다. [20 = 1초]");
            sender.sendMessage(ChatColor.BLUE + "[" + ChatColor.WHITE + " AutoAnnounce " + ChatColor.BLUE + "]" + ChatColor.GOLD + " /autoannounce message [공지]" + ChatColor.GRAY + " - 공지를 수정합니다.");
            return false;
        }
        String arg0 = args[0].toLowerCase();
        String arg1 = args[1];
        switch (arg0) {
            case "delay": {
                long i;
                try {
                    i = Long.parseLong(arg1);
                } catch (Exception e) {
                    sender.sendMessage(ChatColor.BLUE + "[" + ChatColor.WHITE + " AutoAnnounce " + ChatColor.BLUE + "]" + ChatColor.RED + " 잘못된 숫자입니다!");
                    return true;
                }
                getConfig().set("delay", i);
                saveConfig();
                delay = i;
                updateRunnable();
                sender.sendMessage(ChatColor.BLUE + "[" + ChatColor.WHITE + " AutoAnnounce " + ChatColor.BLUE + "]" + ChatColor.GOLD + " 딜레이를 설정했습니다!");
                break;
            }
            case "message": {
                String str = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                getConfig().set("message", str);
                saveConfig();
                msg = str;
                sender.sendMessage(ChatColor.BLUE + "[" + ChatColor.WHITE + " AutoAnnounce " + ChatColor.BLUE + "]" + ChatColor.GOLD + " 공지를 수정했습니다!");
                break;
            }
        }
        return true;
    }
}
