package emanondev.displayeditor.command.displayeditor;

import emanondev.displayeditor.C;
import emanondev.displayeditor.command.AbstractCommand;
import emanondev.displayeditor.command.SubCmd;
import emanondev.displayeditor.selection.EditorMode;
import emanondev.displayeditor.selection.SelectionManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class Select extends SubCmd {
    public Select(@NotNull AbstractCommand cmd) {
        super("select", cmd, true, false);
    }

    @Override
    public void onCommand(CommandSender sender, String alias, String[] args) {
        double radius = C.DEFAULT_SELECT_RADIUS;
        Player player = ((Player) sender);
        if (args.length > 1) {
            try {
                radius = Double.parseDouble(args[1]);
                if (radius <= 0) {
                    sendLanguageString("invalid-radius", null, sender);
                    return;
                }
            } catch (Exception e) {
                sendLanguageString("invalid-radius", null, sender);
                return;
            }
        }

        Display target = null;
        for (Entity en : player.getNearbyEntities(radius, radius, radius)) {
            if (en instanceof Display) {
                if (target == null)
                    target = (Display) en;
                else //select closest  //TODO check if is owner or may select/edit not owned
                    target = target.getLocation().distanceSquared(player.getLocation()) > en.getLocation().distanceSquared(player.getLocation()) ?
                            (Display) en : target;
            }
        }
        if (target == null) {
            sendLanguageString("none-found", null, sender);
            return;
        }

        SelectionManager.select(player, target);
        @Nullable EditorMode mode = SelectionManager.getEditorMode(player);
        if (mode==null)
            SelectionManager.setEditorMode(player, EditorMode.POSITION);
        else
            mode.setup(player);
        sendLanguageString("success", null, sender);
    }

    @Override
    public List<String> onComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
