package net.teamuni.gunsannounce;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CommandTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("gunsannounce") && sender.hasPermission("gunsannounce.manage")) {
            List<String> tabCompleteList = new ArrayList<>();
            if (args.length == 1) {
                tabCompleteList.add("reload");
                tabCompleteList.add("period");
            }
            return tabCompleteList;
        }
        return null;
    }
}
