package emanondev.displayeditor.command.displayeditor;

import emanondev.displayeditor.command.AbstractCommand;
import emanondev.displayeditor.command.SubCmd;
import emanondev.displayeditor.selection.EditorMode;
import emanondev.displayeditor.selection.SelectionManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class Delete extends SubCmd {
    public Delete(@NotNull AbstractCommand cmd) {
        super("delete", cmd, true, false);
    }

    @Override
    public void onCommand(CommandSender sender, String alias, String[] args) {

        Player player = (Player) sender;
        //TODO check permissions
        Display sel = SelectionManager.getSelection(player);
        if (sel != null) {
            sel.remove();
            SelectionManager.deselect(player);
            EditorMode mode = SelectionManager.getEditorMode(player);
            if (mode != null)
                mode.setup((Player) sender);
            sendLanguageString("success", null, player);
        } else
            sendLanguageString("none-selected", null, player);
    }

    @Override
    public List<String> onComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
