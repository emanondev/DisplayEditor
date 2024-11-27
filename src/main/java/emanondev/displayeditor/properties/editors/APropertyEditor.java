package emanondev.displayeditor.properties.editors;

import emanondev.displayeditor.DMessage;
import emanondev.displayeditor.DisplayEditor;
import emanondev.displayeditor.ItemBuilder;
import emanondev.displayeditor.properties.Property;
import emanondev.displayeditor.properties.PropertyEditor;
import emanondev.displayeditor.properties.impl.BooleanProperty;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class APropertyEditor<E,S> implements PropertyEditor {

    protected final Property<E,S> property;
    protected final Player player;
    protected final E entity;

    protected APropertyEditor(Property<E, S> property, Player player, E entity){
        this.property = property;
        this.player = player;
        this.entity = entity;
    }

    @Override
    public boolean handleClick(InventoryClickEvent event) {
        return false;
    }

    @Override
    public ItemStack getEditorButton() {
        return new ItemBuilder(getMaterial()).setGuiProperty().glow(getGlow()).setCustomModelData(getModelData()).setDescription(
                getDescription()).build();
    }

    protected String getPath(){
        return "property." + property.getKey().getKey();
    }

    protected DMessage getDescription() {
        return new DMessage(DisplayEditor.get(),player).appendLang(
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
