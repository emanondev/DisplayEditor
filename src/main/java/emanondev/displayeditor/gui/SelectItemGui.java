package emanondev.displayeditor.gui;

import emanondev.displayeditor.DisplayEditor;
import emanondev.displayeditor.SoundUtil;
import emanondev.displayeditor.selection.EditorMode;
import emanondev.displayeditor.selection.SelectionManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SelectItemGui implements Gui {

    private final Inventory inv;
    private final Player player;

    public SelectItemGui(@NotNull Player player) {
        inv = Bukkit.createInventory(getTargetPlayer(), 5 * 9, getLanguageMessage("gui.select_item.title"));
        this.player = player;
    }

    @Override
    public void onClose(@NotNull InventoryCloseEvent event) {

    }

    @Override
    public void onClick(@NotNull InventoryClickEvent event) {
        if (event.getClickedInventory() != inv) {
            SoundUtil.playSoundNo(player);
            return;
        }
        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType().isAir()) {
            SoundUtil.playSoundNo(player);
            return;
        }
        Display display = SelectionManager.getSelection(player);
        if (display == null) {
            player.closeInventory();
            SoundUtil.playSoundNo(player);
            return;
        }
        if (display instanceof ItemDisplay itemDisplay)
            itemDisplay.setItemStack(item);
        else if (display instanceof BlockDisplay blockDisplay && item.getType().isBlock()) {
            blockDisplay.setBlock(item.getType().createBlockData());
        } else {
            SoundUtil.playSoundNo(player);
            return;
        }
        SoundUtil.playSoundUIClick(player);
        EditorMode mode = SelectionManager.getEditorMode(player);
        if (mode != null)
            mode.setup(player);
        event.getWhoClicked().closeInventory();
    }

    @Override
    public void onDrag(@NotNull InventoryDragEvent event) {

    }

    @Override
    public void onOpen(@NotNull InventoryOpenEvent event) {
        ItemStack[] items = SelectionManager.getInventoryBackup((Player) event.getPlayer());
        if (items != null)
            for (int i = 0; i < items.length; i++)
                inv.setItem(i, items[i]);
        inv.setItem(40, this.loadLanguageDescription(
                getGuiItem("select_item.info", Material.PAPER), "gui.select_item.info"));
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }

    @NotNull
    @Override
    public Player getTargetPlayer() {
        return player;
    }

    @NotNull
    @Override
    public DisplayEditor getPlugin() {
        return DisplayEditor.get();
    }
}
