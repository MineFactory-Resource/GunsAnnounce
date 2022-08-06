package teamuni.net.autoannounce;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CommandTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("autoannounce")) {
            List<String> tabCompleteList = new ArrayList<>();
            if (strings.length == 1) {
                tabCompleteList.add("reload");
            }
            return tabCompleteList;
        }
        return null;
    }
}
