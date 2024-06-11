package emanondev.displayeditor.command.displayeditor;

import emanondev.displayeditor.C;
import emanondev.displayeditor.Util;
import emanondev.displayeditor.command.AbstractCommand;
import emanondev.displayeditor.command.SubCmd;
import emanondev.displayeditor.selection.EditorMode;
import emanondev.displayeditor.selection.SelectionManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
                try {
                    UUID uuid = UUID.fromString(args[1]);
                    List<Entity> entities = player.getNearbyEntities(48,800,48);
                    Display target = null;
                    for (Entity entity:entities)
                        if (entity instanceof Display disp && entity.getUniqueId().equals(uuid)){
                            target = disp;
                            break;
                        }
                    if (target==null){
                        sendLanguageString("entity-uuid-not-found", null, sender);
                        return;
                    }
                    SelectionManager.select(player,target);
                    EditorMode mode = SelectionManager.getEditorMode(player);
                    if (mode == null)
                        SelectionManager.setEditorMode(player, EditorMode.POSITION);
                    else
                        mode.setup(player);
                    sendLanguageString("success", null, sender);
                    return;
                } catch (Exception e2) {
                    sendLanguageString("invalid-radius-or-uuid", null, sender);
                    return;
                }
            }
        }

        Display target = null;
        for (Entity en : player.getNearbyEntities(Math.min(200,radius), Math.min(200,radius), Math.min(200,radius))) {
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
        EditorMode mode = SelectionManager.getEditorMode(player);
        if (mode == null)
            SelectionManager.setEditorMode(player, EditorMode.POSITION);
        else
            mode.setup(player);
        sendLanguageString("success", null, sender);
    }

    @Override
    public List<String> onComplete(CommandSender sender, String[] args) {
        if (args.length==2 && sender instanceof Player p){
            Collection<Entity> entities = p.getWorld().getNearbyEntities(p.getLocation(),5,8,5,(entity -> entity instanceof Display));
            return Util.complete(args[1],entities,entity -> entity.getUniqueId().toString(),entity -> true);
        }
        return Collections.emptyList();
    }
}
