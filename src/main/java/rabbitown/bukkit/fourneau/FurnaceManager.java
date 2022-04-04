package rabbitown.bukkit.fourneau;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import rabbitown.bukkit.fourneau.util.Holo;
import rabbitown.bukkit.fourneau.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Milkory
 */
public class FurnaceManager {

    private static FurnaceManager instance;

    public static FurnaceManager getInstance() {
        if (instance == null) instance = new FurnaceManager();
        return instance;
    }

    private final List<FurnaceInstance> furnaces = new ArrayList<>();

    public void push(ItemStack item, Block block) {
        if (block.getType() == Material.FURNACE && item.hasItemMeta()) {
            if (item.getItemMeta().getPersistentDataContainer()
                    .get(new NamespacedKey(RabbiFourneau.getInstance(), "remain"), PersistentDataType.INTEGER) != null) {
                furnaces.add(FurnaceInstance.adaptItem(item, (Furnace) block.getState()));
            }
        }
    }

    public boolean is(Block block) {
        return furnaces.stream().anyMatch(it -> it.getBlock().getLocation().equals(block.getLocation()));
    }

    public void remove(Block block) {
        furnaces.removeIf(it -> {
            var bool = it.getBlock().getLocation().equals(block.getLocation());
            if (bool) {
                it.destroy();
            }
            return bool;
        });
    }

    public void removeAll() {
        for (FurnaceInstance furnace : furnaces) {
            furnace.getBlock().getInventory().setFuel(null);
            furnace.getHolograph().remove();
        }
    }

    public Optional<FurnaceInstance> get(Block block) {
        return furnaces.stream().filter(it -> it.getBlock().getLocation().equals(block.getLocation()))
                .findFirst();
    }

    @Getter
    public static class FurnaceInstance {
        private static final String HOLO_PREFIX = "§a剩余兔能. ";
        public static final ItemStack FUEL = new ItemStack(Material.RABBIT_SPAWN_EGG);
        public static final ItemStack TEMPLATE_ITEM = new ItemStack(Material.FURNACE);

        static {
            {
                var meta = FUEL.getItemMeta();
                meta.setDisplayName("§d兔能");
                FUEL.setItemMeta(meta);
            }

            {
                var meta = TEMPLATE_ITEM.getItemMeta();
                meta.setDisplayName("§6兔子熔炉");
                meta.setLore(List.of("§7以兔能驱动.", "§7有着兔子级速度", "§7的超级熔炉.", ""));
                meta.setCustomModelData(-17200000);
                TEMPLATE_ITEM.setItemMeta(meta);
            }
        }

        private final Furnace block;
        private int remain;

        private ArmorStand holograph;

        FurnaceInstance(Furnace block, int remain) {
            this.block = block;
            this.remain = remain;
            update();
            block.getInventory().setFuel(FUEL);
        }

        public void update() {
            if (holograph != null) {
                Holo.edit(holograph, HOLO_PREFIX + remain);
            } else {
                holograph = Holo.placeOn(block.getBlock(), HOLO_PREFIX + remain);
            }
        }

        public void burn() {
            var inv = block.getInventory();
            var smelting = inv.getSmelting();
            if (smelting == null) return;
            var result = Util.getSmeltResult(smelting.getType());
            if (result != null) {
                var amount = Math.min(remain, smelting.getAmount());
                var resultSlot = inv.getResult();
                if (resultSlot == null) {
                    resultSlot = result.clone();
                    resultSlot.setAmount(amount);
                    inv.setResult(resultSlot);
                } else if (result.isSimilar(resultSlot)) {
                    amount = Math.min(amount, resultSlot.getMaxStackSize() - resultSlot.getAmount());
                    resultSlot.setAmount(resultSlot.getAmount() + amount);
                } else return;
                if (amount >= 1) {
                    smelting.setAmount(smelting.getAmount() - amount);
                    remain -= amount;
                    effect();
                    update();
                }
                if (remain <= 0) {
                    FurnaceManager.getInstance().remove(block.getBlock());
                }
            }
        }

        public void effect() {
            var loc = block.getLocation();
            loc.getWorld().spawnParticle(Particle.LAVA, Util.alignUpper(loc), 5);
            loc.getWorld().playSound(loc, Sound.BLOCK_LAVA_POP, 1, 1);
        }

        public void destroy() {
            var loc = block.getLocation();
            var inv = block.getInventory();
            var drops = Lists.newArrayList(inv.getSmelting(), inv.getResult());
            if (remain <= 0) {
                loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, Util.alignUpper(loc), 15, 0.2, 0.1, 0.2, 0);
                loc.getWorld().playSound(loc, Sound.BLOCK_ANVIL_PLACE, 1, 0.8F);
            } else drops.add(toItem());
            block.getBlock().setType(Material.AIR);
            for (ItemStack drop : drops) {
                if (drop != null) {
                    loc.getWorld().dropItemNaturally(loc, drop);
                }
            }
            holograph.remove();
        }

        public ItemStack toItem() {
            return newItem(remain);
        }

        public static ItemStack newItem(int remain) {
            var item = TEMPLATE_ITEM.clone();
            var meta = item.getItemMeta();
            var lore = meta.getLore();
            lore.add(HOLO_PREFIX + remain);
            meta.setLore(lore);
            meta.getPersistentDataContainer().set(new NamespacedKey(RabbiFourneau.getInstance(), "remain")
                    , PersistentDataType.INTEGER, remain);
            item.setItemMeta(meta);
            return item;
        }

        public static FurnaceInstance adaptItem(ItemStack item, Furnace block) {
            var remain = item.getItemMeta().getPersistentDataContainer()
                    .get(new NamespacedKey(RabbiFourneau.getInstance(), "remain"), PersistentDataType.INTEGER);
            if (remain == null) remain = 10;
            return new FurnaceInstance(block, remain);
        }
    }

}
