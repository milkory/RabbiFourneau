package rabbitown.bukkit.fourneau;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import rabbitown.bukkit.fourneau.common.Config;
import rabbitown.bukkit.fourneau.common.Logger;
import rabbitown.bukkit.fourneau.listener.PlayerListener;

import java.io.File;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * @author Milkory
 */
@SuppressWarnings("unused")
public class RabbiFourneau extends JavaPlugin {

    @Getter private static RabbiFourneau instance;

    public RabbiFourneau() {
        instance = this;
    }

    @Override public void onLoad() {
        if (!Bukkit.getServer().getMotd().contains("RabbiTown")) {
            Logger.severe("ONLY RabbiTown SERVER COULD USE THIS PLUGIN.");
            Bukkit.shutdown();
        }
        Config.load();
        if (!Config.getConfig().getBoolean("doAPJ20Fans")) {
            Logger.severe("我的B站20粉丝啦，哇！");
            Bukkit.shutdown();
        }
        getCommand("rabbi-fourneau").setExecutor(this);
    }

    @Override public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Logger.logPopTeamEpic();
    }

    @Override public void onDisable() {
        FurnaceManager.getInstance().removeAll();
    }

    public InputStreamReader getResourceReader(String file) {
        return new InputStreamReader(Objects.requireNonNull(this.getResource(file)));
    }

    public File saveResource(String path) {
        var file = new File(getDataFolder(), path);
        if (!file.exists()) saveResource(path, false);
        return file;
    }

    @Override public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("rabbitown.fourneau.give")) {
            if (sender instanceof Player) {
                var player = (Player) sender;
                var loc = player.getLocation();
                if (args.length == 1) {
                    try {
                        var item = loc.getWorld().dropItem(loc, FurnaceManager.FurnaceInstance.newItem(Integer.parseInt(args[0])));
                        item.setPickupDelay(0);
                    } catch (NumberFormatException e) {
                        sender.sendMessage("连 APJ 都知道自己粉丝数，你连数字都认不得喵！");
                    }
                } else if (args.length == 0) {
                    var item = loc.getWorld().dropItem(loc, FurnaceManager.FurnaceInstance.newItem(10));
                    item.setPickupDelay(0);
                } else {
                    sender.sendMessage("悠悠听不懂喵，关注永雏塔菲喵.");
                }
            } else {
                sender.sendMessage("关注永雏塔菲喵，关注永雏塔菲谢谢喵！");
            }
        }
        return true;
    }

}
