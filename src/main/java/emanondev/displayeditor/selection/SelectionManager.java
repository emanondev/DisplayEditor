package emanondev.displayeditor.selection;

import emanondev.displayeditor.DisplayEditor;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class SelectionManager {

    private static final HashMap<Player, Display> selections = new HashMap<>();
    /* TODO coming soon
    private static final HashMap<Player, BoundingBox> copyArea = new HashMap<>();
    private static final HashMap<Player, Vector> copyOffset = new HashMap<>();
    private static final HashMap<Player, World> copyWorld = new HashMap<>();
    private static final HashMap<Player, List<Display>> copyDisplays = new HashMap<>();*/
    private static final HashMap<Player, EditorMode> editorMode = new HashMap<>();
    private static final HashMap<Player, ItemStack[]> inventoryBackup = new HashMap<>();
    private static final HashMap<Player, ItemStack[]> offhandBackup = new HashMap<>();


    public static void select(@NotNull Player player, @NotNull Display display) {
        selections.put(player, display);
        new BukkitRunnable(){
            @Override
            public void run() {
                if (!display.isValid() || i>10)
                    this.cancel();
                if (i%2!=0)
                    player.showEntity(DisplayEditor.get(),display);
                else
                    player.hideEntity(DisplayEditor.get(),display);

                i++;
            }

            int i = 0;

        }.runTaskTimer(DisplayEditor.get(),2L,2L);
    }

    public static boolean deselect(@NotNull Player player) {
        return selections.remove(player) != null;
    }

    public static @Nullable Display getSelection(@NotNull Player player) {
        return selections.get(player);
    }

    public static boolean hasSelection(@NotNull Player player) {
        return getSelection(player) != null;
    }

    public static boolean isOnEditorMode(@NotNull Player player) {
        return getEditorMode(player)!=null;
    }

    public static void setEditorMode(@NotNull Player player, @Nullable EditorMode mode) {
        EditorMode now = getEditorMode(player);
        if (now == mode)
            return;

        if (!editorMode.containsKey(player)) {
            inventoryBackup.put(player, player.getInventory().getStorageContents());
            offhandBackup.put(player, player.getInventory().getExtraContents());
            player.getInventory().clear();
        }

        if (mode == null) {
            editorMode.remove(player);
            player.getInventory().setStorageContents(inventoryBackup.remove(player));
            player.getInventory().setExtraContents(offhandBackup.remove(player));
        } else {
            editorMode.put(player, mode);
            mode.setup(player);
        }
    }

    public static @Nullable EditorMode getEditorMode(@NotNull Player player) {
        return editorMode.get(player);
    }


    public static void swapEditorMode(@NotNull Player player) {
        swapEditorMode(player,1);
    }

    public static void swapEditorMode(@NotNull Player player,int amount) {
        EditorMode mode = getEditorMode(player);
        if (mode == null)
            setEditorMode(player, EditorMode.POSITION);
        else {
            setEditorMode(player, EditorMode.values()[(EditorMode.values().length+ mode.ordinal() + amount) % EditorMode.values().length]);
        }
    }


}
