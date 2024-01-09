package emanondev.displayeditor.command.displayeditor;

import emanondev.displayeditor.Util;
import emanondev.displayeditor.command.AbstractCommand;
import emanondev.displayeditor.command.SubCmd;
import emanondev.displayeditor.selection.EditorMode;
import emanondev.displayeditor.selection.SelectionManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Editormode extends SubCmd {
    public Editormode(@NotNull AbstractCommand cmd) {
        super("editormode", cmd, true, false);
    }

    @Override
    public void onCommand(CommandSender sender, String alias, String[] args) {
        EditorMode target;
        if (args.length == 1) {
            target = SelectionManager.isOnEditorMode((Player) sender) ? null : EditorMode.POSITION;
        } else {
            try {
                target = EditorMode.valueOf(args[1].toUpperCase(Locale.ENGLISH));
            } catch (Exception e) {
                sendLanguageString("wrong-type", null, sender);
                return;
            }
        }
        SelectionManager.setEditorMode((Player) sender, target);
        if (target == null)
            sendLanguageString("success-disabled", null, sender);
        else
            sendLanguageString("success-enabled", null, sender);
    }

    @Override
    public List<String> onComplete(CommandSender sender, String[] args) {
        return (args.length == 2) ? Util.complete(args[1], EditorMode.class) : Collections.emptyList();
    }
}
