package rabbitown.bukkit.fourneau.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import rabbitown.bukkit.fourneau.FurnaceManager;

import java.util.List;
import java.util.Optional;

/**
 * @author Milkory
 */
public class Util {

    @Nullable public static ItemStack getSmeltResult(Material mat) {
        ItemStack result = null;
        var iter = Bukkit.recipeIterator();
        while (iter.hasNext()) {
            var recipe = iter.next();
            if (recipe instanceof FurnaceRecipe) {
                if (((FurnaceRecipe) recipe).getInput().getType() == mat) {
                    result = recipe.getResult();
                }
            }
        }
        return result;
    }

    /** Check whether the inventory belongs to a fast furnace. */
    public static boolean isFastFurnace(Inventory inv) {
        return inv instanceof FurnaceInventory && inv.getItem(1).isSimilar(FurnaceManager.FurnaceInstance.FUEL);
    }

    public static Optional<FurnaceManager.FurnaceInstance> getFurnaceFrom(Inventory inv) {
        var holder = inv.getHolder();
        if (holder instanceof Furnace) {
            return FurnaceManager.getInstance().get(((Furnace) holder).getBlock());
        }
        return Optional.empty();
    }


    private static final List<ClickType> transferClicks = List.of(
            ClickType.NUMBER_KEY, ClickType.SHIFT_LEFT, ClickType.SHIFT_RIGHT
    );

    public static boolean doClickTransItem(ClickType click) {
        return transferClicks.contains(click);
    }

    public static Location align(Location loc) {
        return loc.add(0.5,0,0.5);
    }

    public static Location alignUpper(Location loc) {
        return loc.add(0.5,0.5,0.5);
    }



}
