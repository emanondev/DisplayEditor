package emanondev.displayeditor.command.displayeditor;

import emanondev.displayeditor.command.AbstractCommand;
import emanondev.displayeditor.command.SubCmd;
import emanondev.displayeditor.selection.SelectionManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class Deselect extends SubCmd {
    public Deselect(@NotNull AbstractCommand cmd) {
        super("deselect", cmd, true, false);
    }

    @Override
    public void onCommand(CommandSender sender, String alias, String[] args) {
        if (SelectionManager.deselect((Player) sender)) {
            SelectionManager.setEditorMode((Player) sender, null);
            sendLanguageString("success", null, sender);
        }
        sendLanguageString("none-selected", null, sender);
    }

    @Override
    public List<String> onComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
