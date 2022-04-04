package rabbitown.bukkit.fourneau.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.scheduler.BukkitRunnable;
import rabbitown.bukkit.fourneau.FurnaceManager;
import rabbitown.bukkit.fourneau.RabbiFourneau;
import rabbitown.bukkit.fourneau.util.Util;

/**
 * @author Milkory
 */
public class PlayerListener implements Listener {

    private static final RabbiFourneau plugin = RabbiFourneau.getInstance();
    private static final FurnaceManager manager = FurnaceManager.getInstance();

    @EventHandler
    public void handle(BlockPlaceEvent event) {
        var item = event.getItemInHand();
        if (event.getBlockPlaced().getType() == Material.FURNACE) {
            new BukkitRunnable() {
                @Override public void run() {
                    if (!event.isCancelled()) {
                        manager.push(item, event.getBlock());
                    }
                }
            }.runTask(plugin);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void handle(BlockBreakEvent event) {
        var block = event.getBlock();
        manager.remove(block);
    }

    @EventHandler
    public void handle(InventoryMoveItemEvent event) {
        var inv = event.getDestination();
        var furnace = Util.getFurnaceFrom(inv);
        furnace.ifPresent(furnaceInstance -> new BukkitRunnable() {
            @Override public void run() {
                if (!event.isCancelled()) {
                    furnaceInstance.burn();
                }
            }
        }.runTask(plugin));

    }

    @EventHandler
    public void handle(InventoryClickEvent event) {
        var inv = event.getInventory();
        var furnace = Util.getFurnaceFrom(inv);
        if (furnace.isPresent()) {
            if (event.getRawSlot() == 1) {
                event.setCancelled(true);
            } else if (event.getRawSlot() == 0 || Util.doClickTransItem(event.getClick())) {
                new BukkitRunnable() {
                    @Override public void run() {
                        if (!event.isCancelled()) {
                            furnace.get().burn();
                        }
                    }
                }.runTask(plugin);
            }
        }
    }

}
