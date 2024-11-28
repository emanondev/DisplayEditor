package emanondev.displayeditor.command.entityeditor;

import emanondev.displayeditor.command.EntityEditorCommand;
import emanondev.displayeditor.command.SubCmd;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

public class SpawnAll extends SubCmd {
    public SpawnAll(EntityEditorCommand cmd) {
        super("spawnall", cmd, true, false);
    }

    @Override
    public void onCommand(CommandSender sender, String alias, String[] args) {
        Player p = ((Player) sender);
        Location loc = p.getLocation();
        for (EntityType type : EntityType.values()) {
            if (!type.isSpawnable()) {
                getPlugin().log(type.name() + " is not spawnable");
                continue;
            }
            try {
                Entity t = loc.getWorld().spawnEntity(loc, type);
                loc.add(0,0,2);
                if (t instanceof LivingEntity liv) {
                    liv.setAI(false);
                }
                t.setInvulnerable(true);
                t.setPersistent(true);
                t.setGravity(false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<String> onComplete(CommandSender sender, String[] args) {
        return List.of();
    }
}
