package me.blueslime.supervotes.modules.utils.reflect;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class Reflect {
    private static Reflect INSTANCE = null;

    private boolean handMethod = true;

    private static Reflect get() {
        if (INSTANCE == null) {
            INSTANCE = new Reflect();
        }
        return INSTANCE;
    }

    public static ItemStack itemHand(Player player) {
        return get().hand(player);
    }

    @SuppressWarnings("deprecation")
    private ItemStack hand(Player player) {
        if (handMethod) {
            try {
                return player.getInventory().getItemInMainHand();
            } catch (Exception ignored) {
                handMethod = false;
            }
        }
        return player.getItemInHand();
    }

    private boolean isAirMaterial(Material material) {
        return material == Material.AIR;
    }

    public static boolean isAir(ItemStack itemStack) {
        return isAir(itemStack.getType());
    }

    public static boolean isAir(Material material) {
        return get().isAirMaterial(material);
    }
}
