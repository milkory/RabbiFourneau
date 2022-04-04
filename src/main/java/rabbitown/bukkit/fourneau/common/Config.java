package rabbitown.bukkit.fourneau.common;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import rabbitown.bukkit.fourneau.RabbiFourneau;

/**
 * @author Milkory
 */
public class Config {

    private static final RabbiFourneau plugin = RabbiFourneau.getInstance();

    @Getter private static YamlConfiguration config;

    public static void load() {
        config = loadConfig("config.yml");
        Message.load();
    }

    /** Note: Only compatible with the Bingo plugin. */
    public static YamlConfiguration loadConfig(String path) {
        var file = plugin.saveResource(path);
        var config = YamlConfiguration.loadConfiguration(file);
        var defaults = YamlConfiguration.loadConfiguration(plugin.getResourceReader(path));
        config.setDefaults(defaults);
        return config;
    }

}
