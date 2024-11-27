package emanondev.displayeditor.gui;

import emanondev.displayeditor.DisplayEditor;
import emanondev.displayeditor.properties.Property;
import emanondev.displayeditor.properties.PropertyEditor;
import emanondev.displayeditor.properties.Registries;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EntityEditorGui implements PagedGui {

    private final Inventory inv;
    private final Player player;
    private int page = 1;
    private final List<PropertyEditor> props = new ArrayList<>();

    public EntityEditorGui(@NotNull Player player, @NotNull Entity entity) {
        inv = Bukkit.createInventory(this, 6 * 9, getLanguageMessage("gui.entity_editor.title"));
        this.player = player;
        List<Property<?, ?>> props = Registries.PROPERTIES.getAllByEntity(entity.getClass());
        for (Property<?, ?> prop : props) {
            PropertyEditor value = prop.getPropertyEditorRaw(entity, player);
            if (value != null)
                this.props.add(value);
        }
    }

    @Override
    public void onClose(@NotNull InventoryCloseEvent event) {

    }

    @Override
    public void onClick(@NotNull InventoryClickEvent event) {
        if (event.getClickedInventory() != event.getView().getTopInventory())
            return;
        if (event.getSlot() > 45) {
            switch (event.getSlot()) {
                case 46 -> {
                    page = Math.max(1, page - 1); //TODO optimize
                    updateInventory();
                }
                case 52 -> {
                    page = Math.min(props.size() / 45 + (props.size() % 45 == 0 ? 0 : 1), page + 1); //TODO optimize
                    updateInventory();
                }
            }
            return;
        }
        PropertyEditor prop = props.get(event.getSlot() + (page - 1) * 45);
        if (prop.handleClick(event))
            updateInventory();
    }

    private void updateInventory() {
        inv.setItem(46, page > 1 ? getPreviousPageItem() : null);
        inv.setItem(52, page < props.size() / 45 + (props.size() % 45 == 0 ? 0 : 1) ? getNextPageItem() : null);
        for (int i = 0; i < 45; i++) {
            if (props.size() <= i + (page - 1) * 45) {
                inv.setItem(i, null);
            } else {
                PropertyEditor prop = props.get(i + (page - 1) * 45);
                inv.setItem(i, prop.getEditorButton());
            }
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
