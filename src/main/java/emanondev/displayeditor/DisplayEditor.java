package emanondev.displayeditor;

import emanondev.displayeditor.command.DisplayEditorCommand;
import emanondev.displayeditor.command.DisplayEditorInfoCommand;
import emanondev.displayeditor.command.EntityEditorCommand;
import emanondev.displayeditor.command.ReloadCommand;
import emanondev.displayeditor.gui.Gui;
import emanondev.displayeditor.gui.GuiHandler;
import emanondev.displayeditor.gui.SelectItemGui;
import emanondev.displayeditor.selection.EditorModeListener;
import emanondev.displayeditor.selection.SelectionManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public final class DisplayEditor extends APlugin {
    private final static Integer PROJECT_ID = 113254;
    private static final int BSTATS_PLUGIN_ID = 19646;
    private static DisplayEditor plugin = null;

    @NotNull
    public static DisplayEditor get() {
        return plugin;
    }

    public void onLoad() {
        plugin = this;
    }

    @Override
    @NotNull
    public Integer getProjectId() {
        return PROJECT_ID;
    }

    @Override
    public void enable() {

        ConfigurationUpdater.update();
        C.reload();
        Bukkit.getPluginManager().registerEvents(new GuiHandler(), this);
        Bukkit.getPluginManager().registerEvents(new EditorModeListener(), this);

        registerCommand(new DisplayEditorCommand(), Collections.singletonList("de"));
        registerCommand(new EntityEditorCommand(), Collections.singletonList("ee"));
        new DisplayEditorInfoCommand().register();
        new ReloadCommand(this).register();

        registerMetrics(BSTATS_PLUGIN_ID);
    }

    @Override
    public void reload() {
        C.reload();
        DisplayEditorCommand.get().reload();
    }

    @Override
    public void disable() {
        for (Player p : Bukkit.getOnlinePlayers())
            SelectionManager.setEditorMode(p, null);
        for (Player p : Bukkit.getOnlinePlayers())
            if (p.getOpenInventory().getTopInventory().getHolder() instanceof Gui)
                p.closeInventory();
    }
}
