package net.teamuni.autoannounce;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageManager {
    private final AutoAnnounce instance;
    private File file = null;
    private FileConfiguration messagesFile = null;

    public MessageManager(AutoAnnounce instance) {
        this.instance = instance;
    }

    public void createMessagesYml() {
        if (this.file == null) {
            this.file = new File(this.instance.getDataFolder(), "messages.yml");
        }
        if (!file.exists()) {
            this.instance.saveResource("messages.yml", false);
        }
        this.messagesFile = YamlConfiguration.loadConfiguration(file);
    }

    public void reload() {
        if (this.file == null) {
            this.file = new File(this.instance.getDataFolder(), "messages.yml");
        }
        this.messagesFile = YamlConfiguration.loadConfiguration(file);
    }

    public Map<String, List<String>> getMessages() {
        Map<String, List<String>> messageListMap = new HashMap<>();

        for (String key : this.messagesFile.getKeys(false)) {
            List<String> messages = this.messagesFile.getStringList(key);
            messageListMap.put(key, messages);
        }
        return messageListMap;
    }

    public void sendTranslatedMessage(Player player, List<String> msgList) {
        for (String msg : msgList) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }
    }
}
