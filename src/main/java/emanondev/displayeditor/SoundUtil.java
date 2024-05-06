package emanondev.displayeditor;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class SoundUtil {

    public static void playSound(Player player, Sound sound, float volume, float pitch) {
        player.playSound(player.getLocation(), sound, SoundCategory.MASTER, volume, pitch);
    }

    public static void playSoundNo(Player player) {
        playSound(player, Sound.ENTITY_VILLAGER_NO, 1F, 2F);
    }

    public static void playSoundUIClick(Player player) {
        playSound(player, Sound.UI_BUTTON_CLICK, 0.5F, 2F);
    }

    public static void playSoundPageTurn(Player player) {
        playSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1F, 1F);
    }
}
