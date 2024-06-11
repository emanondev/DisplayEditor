package emanondev.displayeditor.gui;

import emanondev.displayeditor.DisplayEditor;
import emanondev.displayeditor.SoundUtil;
import emanondev.displayeditor.selection.SelectionManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class SelectMobGui implements PagedGui {

    private final Inventory inv;
    private final Player player;
    private int page = 1;
    private final List<Entity> entities;
    private final List<Entity> selected;
    private final boolean copy;

    public SelectMobGui(@NotNull Player player, Collection<Entity> entities, boolean copy) {
        inv = Bukkit.createInventory(this, 6 * 9, getLanguageMessage("gui.select_entity.title"));
        this.player = player;
        this.entities = new ArrayList<>(entities);
        if (copy)
            selected = new ArrayList<>(entities);
        else {
            entities.removeIf(en -> !(en instanceof Display));
            selected = new ArrayList<>();
        }
        this.copy = copy;
        //updateInventory();
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
        if (event.getSlot() == 46) { //page change
            page = Math.max(1, page - 1);
            updateInventory();
            return;
        }
        if (event.getSlot() == 52) { //page change
            page = Math.min(page + 1, 1 + entities.size() / 45 + (entities.size() % 45 == 0 ? -1 : 0));
            updateInventory();
            return;
        }
        Entity entity = entities.get(event.getSlot() + (page - 1) * 45);
        if (copy) {
            if (!selected.remove(entity))
                selected.add(entity);
            SelectionManager.getCopyPasteOption(player).copy(selected, player.getLocation());
        } else {
            selected.clear();
            selected.add(entity);
            SelectionManager.select(player, (Display) entity);
        }
        updateInventory();
        SoundUtil.playSoundUIClick(player);
        //if (entity instanceof Display display)
        //    SelectionManager.select((Player) event.getWhoClicked(), display);
    }

    private void updateInventory() {
        inv.setItem(46, page > 1 ? getPreviousPageItem() : null);
        inv.setItem(52, page < entities.size() / 45 + (entities.size() % 45 == 0 ? 0 : 1) ? getNextPageItem() : null);
        for (int i = 0; i < 45; i++) {
            if (entities.size() <= i+(page-1)*45) {
                inv.setItem(i, null);
                continue;
            }
            Entity entity = entities.get(i+(page-1)*45);
            String[] holders = new String[8 /*+ (entities.get(i) instanceof Display ? 2 : 0)*/];
            int j = 0;
            holders[j++] = "%type%";
            holders[j++] = entity.getType().name();
            holders[j++] = "%uuid%";
            holders[j++] = entity.getUniqueId().toString();
            holders[j++] = "%customname%";
            holders[j++] = entity.getCustomName() == null ? "null" : entity.getCustomName();
            holders[j++] = "%selected%";
            holders[j++] = String.valueOf(selected.contains(entity));
                /*TODO add more lines
                if (entities.get(i) instanceof ItemDisplay itemDisplay)
                    holders[j++] = ;
                */
            ItemStack item = this.loadLanguageDescription(getGuiItem("select_entity.info_" +
                            entity.getType().toString().toLowerCase(Locale.ENGLISH), Material.ARMOR_STAND),
                    copy ? "gui.select_entity.info_copy" : "gui.select_entity.info_select", holders);
            ItemMeta meta = item.getItemMeta();
            if (selected.contains(entity))
                meta.addEnchant(Enchantment.LURE, 1, true); //TODO use glint override

            meta.addItemFlags(ItemFlag.values());
            item.setItemMeta(meta);
            inv.setItem(i, item);
        }
    }

    @Override
    public void onDrag(@NotNull InventoryDragEvent event) {

    }

    @Override
    public void onOpen(@NotNull InventoryOpenEvent event) {
        updateInventory();
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

    @Override
    public int getPage() {
        return page;
    }
}
