package emanondev.displayeditor.properties;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface PropertyEditor {

    boolean handleClick(InventoryClickEvent event);

    ItemStack getEditorButton(Player player);

}
