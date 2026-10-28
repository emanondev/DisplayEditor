package emanondev.displayeditor.command.displayeditor;

import emanondev.displayeditor.command.AbstractCommand;
import emanondev.displayeditor.command.SubCmd;
import emanondev.displayeditor.selection.EditorMode;
import emanondev.displayeditor.selection.SelectionManager;
import emanondev.displayeditor.utility.CompleteUtility;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Editormode extends SubCmd {
    public Editormode(@NotNull AbstractCommand cmd) {
        super("editormode", cmd, true, false);
    }

    @Override
    public void onCommand(@NonNull CommandSender sender, @NonNull String alias, String[] args) {
        EditorMode target;
        if (args.length == 1) {
            target = SelectionManager.isOnEditorMode((Player) sender) ? null : EditorMode.POSITION;
        } else {
            try {
                target = EditorMode.valueOf(args[1].toUpperCase(Locale.ENGLISH));
            } catch (Exception e) {
                sendLanguageString("wrong-type", sender);
                return;
            }
        }
        SelectionManager.setEditorMode((Player) sender, target);
        if (target == null)
            sendLanguageString("success-disabled", sender);
        else
            sendLanguageString("success-enabled", sender);
    }

    @Override
    public List<String> onComplete(@NonNull CommandSender sender, String[] args) {
        return (args.length == 2) ? CompleteUtility.complete(args[1], EditorMode.class) : Collections.emptyList();
    }
}
