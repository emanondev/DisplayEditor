package emanondev.displayeditor;

import emanondev.displayeditor.command.DisplayEditorCommand;
import emanondev.displayeditor.command.DisplayEditorInfoCommand;
import emanondev.displayeditor.command.ReloadCommand;
import emanondev.displayeditor.gui.Gui;
import emanondev.displayeditor.gui.GuiHandler;
import emanondev.displayeditor.selection.EditorModeListener;
import emanondev.displayeditor.selection.SelectionManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

public final class DisplayEditor extends APlugin {
    private static DisplayEditor plugin = null;
    private final static Integer PROJECT_ID = 113254;
    private static final int BSTATS_PLUGIN_ID = 19646;

    public static @NotNull DisplayEditor get() {
        return plugin;
    }

    public void onLoad() {
        plugin = this;
    }

    @Override
    public @NotNull Integer getProjectId() {
        return PROJECT_ID;
    }

    @Override
    public void enable() {

        ConfigurationUpdater.update();
        C.reload();
        Bukkit.getPluginManager().registerEvents(new GuiHandler(), this);
        Bukkit.getPluginManager().registerEvents(new EditorModeListener(), this);

        registerCommand(new DisplayEditorCommand(), Collections.singletonList("de"));
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
        for (Player p: Bukkit.getOnlinePlayers())
            SelectionManager.setEditorMode(p,null);
        for (Player p : Bukkit.getOnlinePlayers())
            if (p.getOpenInventory().getTopInventory().getHolder() instanceof Gui)
                p.closeInventory();
    }
}
