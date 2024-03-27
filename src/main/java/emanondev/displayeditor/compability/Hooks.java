package emanondev.displayeditor.compability;

import emanondev.displayeditor.Util;
import org.bukkit.Bukkit;

public class Hooks {
    private static final MiniMessageUtil miniMessage = initMiniMessage();

    public static boolean isVault() {
        return isEnabled("Vault");
    }

    public static boolean isVanishEnabled() {
        return isEnabled("SuperVanish") || isEnabled("PremiumVanish");
    }

    /*public static boolean isNBTAPIEnabled() {
        return isEnabled("NBTAPI");
    }*/

    /*public static boolean isShopGuiPlusEnabled() {
        return isEnabled("ShopGuiPlus");
    }*/

    public static boolean isPAPIEnabled() {
        return isEnabled("PlaceholderAPI");
    }

    public static boolean isEnabled(String pluginName) {
        return Bukkit.getPluginManager().isPluginEnabled(pluginName);
    }

    public static boolean isMythicMobsEnabled() {
        return isEnabled("MythicMobs");
    }

    private static MiniMessageUtil initMiniMessage() {
        try {
            if (Util.hasPaperAPI() && Util.isVersionAfter(1, 16, 5))
                return MiniMessagePaper.getInstance();
        } catch (Throwable t) {

        }
        try {
            if (Hooks.isMythicMobsEnabled())
                return MiniMessageMM.getInstance();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean hasMiniMessage() {
        return miniMessage != null;
    }

    public static MiniMessageUtil getMiniMessageUtil() {
        return miniMessage;
    }

    /*

    public static boolean isItemBridgeEnabled() {
        return isEnabled("ItemBridge");
    }*/
}
