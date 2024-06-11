package emanondev.displayeditor.gui;

import emanondev.displayeditor.DisplayEditor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class GuiHandler implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    private static void onOpen(InventoryOpenEvent event) {
        DisplayEditor.get().log("InventoryOpenEvent");
        if (!(event.getView().getTopInventory().getHolder() instanceof Gui holder))
            return;
        if (!(event.getPlayer() instanceof Player))
            return;
        holder.onOpen(event);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    private static void onClose(InventoryCloseEvent event) {
        if (!(event.getView().getTopInventory().getHolder() instanceof Gui holder))
            return;
        if (!(event.getPlayer() instanceof Player))
            return;
        holder.onClose(event);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private static void onClick(InventoryClickEvent event) {
        if (!(event.getView().getTopInventory().getHolder() instanceof Gui holder))
            return;
        event.setCancelled(true);
        if (event.getClickedInventory() != null && event.getClickedInventory().equals(event.getView().getTopInventory()))
            if (event.getWhoClicked() instanceof Player)
                holder.onClick(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private static void onDrag(InventoryDragEvent event) {
        if (event.getView().getTopInventory().getHolder() instanceof Gui holder) {
            event.setCancelled(true);
            holder.onDrag(event);
        }
    }
}