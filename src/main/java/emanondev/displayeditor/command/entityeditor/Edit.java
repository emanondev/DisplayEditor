package emanondev.displayeditor.command.entityeditor;

import emanondev.displayeditor.command.EntityEditorCommand;
import emanondev.displayeditor.command.SubCmd;
import emanondev.displayeditor.gui.EntityEditorGui;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import java.util.List;

public class Edit extends SubCmd {
    public Edit(EntityEditorCommand cmd) {
        super("edit", cmd, true, false);
    }

    @Override
    public void onCommand(CommandSender sender, String alias, String[] args) {
        Player p = (Player) sender;
        RayTraceResult ray = p.getWorld().rayTraceEntities(p.getEyeLocation(), p.getLocation().getDirection(), 10, 0.4, (en) -> en.getType().isSpawnable());
        if (ray != null)
            p.openInventory(new EntityEditorGui(p, ray.getHitEntity()).getInventory());

    }

    @Override
    public List<String> onComplete(CommandSender sender, String[] args) {
        return List.of();
    }
}
