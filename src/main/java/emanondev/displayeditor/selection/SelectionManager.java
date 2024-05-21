package emanondev.displayeditor.selection;

import emanondev.displayeditor.DisplayEditor;
import emanondev.displayeditor.Util;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class SelectionManager {

    private static final HashMap<Player, Display> selections = new HashMap<>();
    private static final HashMap<Player, CopyPasteOption> pasteOptions = new HashMap<>();
    private static final HashMap<Player, EditorMode> editorMode = new HashMap<>();
    private static final HashMap<Player, ItemStack[]> inventoryBackup = new HashMap<>();
    private static final HashMap<Player, ItemStack[]> offhandBackup = new HashMap<>();
    private static final HashMap<Player, ItemStack[]> equipmentBackup = new HashMap<>();
    private static BukkitTask cornerFlash;

    @Nullable
    public static ItemStack[] getInventoryBackup(@NotNull Player player) {
        return inventoryBackup.get(player);
    }

    public static void select(@NotNull Player player, @NotNull Display display) {
        selections.put(player, display);
        EditorMode mode = SelectionManager.getEditorMode(player);
        if (mode != null)
            mode.setup(player);
        player.playSound(player, Sound.UI_BUTTON_CLICK, 1F, 0.9F);
        Util.flashEntities(player, display);
    }

    public static boolean deselect(@NotNull Player player) {
        if (selections.containsKey(player))
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1F, 0.1F);
        if (selections.remove(player) != null) {
            EditorMode mode = SelectionManager.getEditorMode(player);
            if (mode != null)
                mode.setup(player);
            return true;
        }
        return false;
    }

    @Nullable
    public static Display getSelection(@NotNull Player player) {
        return selections.get(player);
    }

    public static boolean hasSelection(@NotNull Player player) {
        return getSelection(player) != null;
    }

    public static boolean isOnEditorMode(@NotNull Player player) {
        return getEditorMode(player) != null;
    }

    public static void setEditorMode(@NotNull Player player, @Nullable EditorMode mode) {
        EditorMode now = getEditorMode(player);
        if (now == mode)
            return;

        if (!editorMode.containsKey(player)) {
            inventoryBackup.put(player, player.getInventory().getStorageContents());
            offhandBackup.put(player, player.getInventory().getExtraContents());
            equipmentBackup.put(player, player.getInventory().getArmorContents());
            player.getInventory().clear();
            if (!selections.containsKey(player))
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1F, 0.9F);
        }

        if (mode == null) {
            editorMode.remove(player);
            selections.remove(player);
            player.getInventory().setStorageContents(inventoryBackup.remove(player));
            player.getInventory().setExtraContents(offhandBackup.remove(player));
            player.getInventory().setArmorContents(equipmentBackup.remove(player));
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1F, 0.1F);
            if (DisplayEditor.get().getConfig().loadBoolean("editor.actionbar_reminder", true))
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder().create());
        } else {
            editorMode.put(player, mode);
            mode.setup(player);
        }


        if (editorMode.isEmpty() && cornerFlash != null) {
            cornerFlash.cancel();
            cornerFlash = null;
        }
        if (!editorMode.isEmpty() && cornerFlash == null) {
            cornerFlash = new BukkitRunnable() {
                long counter = 0;

                @Override
                public void run() {
                    counter++;
                    editorMode.keySet().forEach(p -> {
                        if (!(p.isValid() && p.isOnline()))
                            return;
                        if (DisplayEditor.get().getConfig().loadBoolean("editor.actionbar_reminder", true) && counter % 5 == 0)
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(
                                    DisplayEditor.get().getLanguageConfig(p).getMessage("editor.reminder",
                                            "&6You are on (Display) Editor Mode")).create());
                    });
                    //if (counter % 3 == 0)
                    selections.forEach((p, disp) -> {
                        if (!(p.isValid() && p.isOnline()))
                            return;
                        if (disp.isValid() && p.getWorld().equals(disp.getWorld()))
                            p.spawnParticle(Util.isVersionAfter(1,20,5)?
                                            Particle.DUST:Particle.valueOf("REDSTONE"), disp.getLocation(),
                                    1, 0, 0, 0, 0,
                                    new Particle.DustOptions(Color.RED, 0.5F));
                    });
                    /*pastePreview.forEach((p, disp) -> {
                                if (counter % 5 != 0)
                                    disp.forEach((d) -> player.showEntity(DisplayEditor.get(), d));
                                else
                                    disp.forEach((d) -> player.hideEntity(DisplayEditor.get(), d));
                            }
                    );*/
                }
            }.runTaskTimer(DisplayEditor.get(), 2L, 2L);
        }

    }

    @Nullable
    public static EditorMode getEditorMode(@NotNull Player player) {
        return editorMode.get(player);
    }


    public static void swapEditorMode(@NotNull Player player) {
        swapEditorMode(player, 1);
    }

    public static void swapEditorMode(@NotNull Player player, int amount) {
        EditorMode mode = getEditorMode(player);
        if (mode == null)
            setEditorMode(player, EditorMode.POSITION);
        else
            setEditorMode(player, EditorMode.values()[(EditorMode.values().length + mode.ordinal() + amount) % EditorMode.values().length]);
    }

    public static CopyPasteOption getCopyPasteOption(@NotNull Player player) {
        pasteOptions.putIfAbsent(player, new CopyPasteOption());
        return pasteOptions.get(player);
    }

}
