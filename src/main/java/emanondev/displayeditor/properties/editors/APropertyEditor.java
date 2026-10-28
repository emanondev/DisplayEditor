package emanondev.displayeditor.properties.editors;

import emanondev.displayeditor.DisplayEditor;
import emanondev.displayeditor.properties.Property;
import emanondev.displayeditor.properties.PropertyEditor;
import emanondev.displayeditor.utility.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class APropertyEditor<E, S> implements PropertyEditor {

    protected final Property<E, S> property;
    protected final Player player;
    protected final E entity;

    protected APropertyEditor(Property<E, S> property, Player player, E entity) {
        this.property = property;
        this.player = player;
        this.entity = entity;
    }

    @Override
    public boolean handleClick(InventoryClickEvent event) {
        return false;
    }

    @Override
    public ItemStack getEditorButton(Player player) {
        return new ItemBuilder(getMaterial())
                .setItemFlags(ItemFlag.values(), true)
                .setCustomModelData(getModelData())
                .setGuiDescription(getDescription(player))
                .build();
    }

    protected String getPath() {
        return "property." + property.getKey().getKey();
    }

    protected List<Component> getDescription(Player player) {
        return DisplayEditor.get().getTranslator().translateList(player,
                getPath() + ".description", getPlaceholders());
    }

    protected Material getMaterial() {
        return DisplayEditor.get().getConfig("gui.yml")
                .loadEnum(getPath() + ".material", Material.STONE, Material.class);
    }

    protected Boolean getGlow() {
        return DisplayEditor.get().getConfig("gui.yml")
                .loadBoolean(getPath() + ".glow", null);
    }

    protected Integer getModelData() {
        return DisplayEditor.get().getConfig("gui.yml")
                .loadInteger(getPath() + ".modeldata", null);
    }

    protected abstract String[] getPlaceholders();

}
