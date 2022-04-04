package rabbitown.bukkit.fourneau.common;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author Milkory
 */
public class Message {

    @Getter private static YamlConfiguration config;

    public static void load() {
        config = Config.loadConfig("message.yml");
    }

    public static String get(String path, Object... args) {
        String src = config.getString(path);
        if (src == null) {
            return "§c§l[Unknown Message]";
        }
        for (int i = 0; i < args.length; i++) {
            src = src.replace("{" + i + "}", args[i].toString());
        }
        return src.replaceAll("\\\\{0}&(.)", "§$1");
    }

    private static String translateColor(String str) {
        return str.replaceAll("\\\\{0}&(.)", "§$1");
    }

    public static void sendTo(CommandSender to, String path, Object... args) {
        if (checkIfEnabled(path)) sendTo0(to, path, args);
    }

    public static void sendTo0(CommandSender to, String path, Object... args) {
        to.sendMessage(get(path, args));
    }

    public static boolean checkIfEnabled(String path) {
        return !"::disabled::".equals(config.getString(path));
    }

}
