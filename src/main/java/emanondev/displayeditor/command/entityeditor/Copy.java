package emanondev.displayeditor.command.entityeditor;

import emanondev.displayeditor.YMLConfig;
import emanondev.displayeditor.command.EntityEditorCommand;
import emanondev.displayeditor.command.SubCmd;
import emanondev.displayeditor.properties.SerializedEntity;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Copy extends SubCmd {
    public Copy(EntityEditorCommand cmd) {
        super("copy",cmd,true,false);
    }

    @Override
    public void onCommand(CommandSender sender, String alias, String[] args) {
        List<Entity> en = ((Player) sender).getNearbyEntities(5, 5, 5);
        YMLConfig config = getPlugin().getConfig("test/data.yml");
        ArrayList<SerializedEntity> values = new ArrayList<>();
        for (Entity e : en){
            if (e.getType().isSpawnable())
                values.add(new SerializedEntity(e));
        }
        config.set("entities" , values);
    }

    @Override
    public List<String> onComplete(CommandSender sender, String[] args) {
        return List.of();
    }
}
