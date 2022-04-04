package rabbitown.bukkit.fourneau.util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

/**
 * Holographic display utils.
 *
 * @author Milkory
 */
public class Holo {

    private static final EulerAngle IDENTIFIER = new EulerAngle(-8.10497477, -1.129757578, -1.217247375);

//    private static final List<ArmorStand> holoList = new ArrayList<>();

    public static boolean identify(ArmorStand holo) {
        return holo.getBodyPose() == IDENTIFIER;
    }

    public static ArmorStand place(Location loc, String text) {
        var holo = loc.getWorld().spawn(loc, ArmorStand.class);
        holo.setVisible(false);
        holo.setMarker(true);
        holo.setRemoveWhenFarAway(false); // needless maybe
        holo.setHeadPose(IDENTIFIER);
        holo.setCustomName(text);
        holo.setCustomNameVisible(true);
//        holoList.add(holo);
        return holo;
    }

    public static ArmorStand placeOn(Block block, String text) {
        return place(block.getLocation().add(0.5, 1.0, 0.5), text);
    }

    public static void edit(ArmorStand holo, String text) {
        holo.setCustomName(text);
    }

//    public static void removeAt(Location loc) {
//        var entities = loc.getWorld().getNearbyEntities(loc, 1, 1, 1);
//        for (var entity : entities) {
//            if (entity instanceof ArmorStand) {
//                var holo = (ArmorStand) entity;
//                if (identify(holo)) {
//                    entity.remove();
//                    holoList.remove(holo);
//                }
//            }
//        }
//    }

//    public static void removeAll() {
//        for (ArmorStand holo : holoList) {
//            holo.remove();
//        }
//        holoList.clear();
//    }

}
