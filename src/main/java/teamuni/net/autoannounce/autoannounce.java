package teamuni.net.autoannounce;


import java.lang.String;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
                String msgSplit = (msg.replace("\\n","\n"));
                String msgBlack = (msgSplit.replace("&0", ChatColor.BLACK + ""));
                String msgDarkBlue = (msgBlack.replace("&1", ChatColor.DARK_BLUE + ""));
                String msgDarkGreen = (msgDarkBlue.replace("&2", ChatColor.DARK_GREEN + ""));
                String msgDarkAqua = (msgDarkGreen.replace("&3", ChatColor.DARK_AQUA + ""));
                String msgDarkRed = (msgDarkAqua.replace("&4", ChatColor.DARK_RED + ""));
                String msgDarkPurple = (msgDarkRed.replace("&5", ChatColor.DARK_PURPLE + ""));
                String msgGold = (msgDarkPurple.replace("&6", ChatColor.GOLD + ""));
                String msgGray = (msgGold.replace("&7", ChatColor.GRAY + ""));
                String msgDarkGray = (msgGray.replace("&8", ChatColor.DARK_GRAY + ""));
                String msgBlue = (msgDarkGray.replace("&9", ChatColor.BLUE + ""));
                String msgGreen = (msgBlue.replace("&a", ChatColor.GREEN + ""));
                String msgAqua = (msgGreen.replace("&b", ChatColor.AQUA + ""));
                String msgRed = (msgAqua.replace("&c", ChatColor.RED + ""));
                String msgLightPurple = (msgRed.replace("&d", ChatColor.LIGHT_PURPLE + ""));
                String msgYellow = (msgLightPurple.replace("&e", ChatColor.YELLOW + ""));
                String msgWhite = (msgYellow.replace("&f", ChatColor.WHITE + ""));
                String msgMagic = (msgWhite.replace("&k", ChatColor.MAGIC + ""));
                String msgBold = (msgMagic.replace("&l", ChatColor.BOLD + ""));
                String msgStrikeThrough = (msgBold.replace("&m", ChatColor.STRIKETHROUGH + ""));
                String msgUnderLine = (msgStrikeThrough.replace("&n", ChatColor.UNDERLINE + ""));
                String msgItalic = (msgUnderLine.replace("&o", ChatColor.ITALIC + ""));
                String msgReset = (msgItalic.replace("&r", ChatColor.RESET + ""));
                Bukkit.broadcastMessage(msgReset);
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
                player.sendMessage("/autoannounce message [공지] - 공지를 수정합니다.");
                break;
            }
        }
        return true;
    }
}
