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

public class Spawn extends SubCmd {
    public Spawn(EntityEditorCommand cmd) {
        super("copy",cmd,true,false);
    }

    @Override
    public void onCommand(CommandSender sender, String alias, String[] args) {
        YMLConfig config = getPlugin().getConfig("test/data.yml");
        ArrayList<SerializedEntity> values = new ArrayList<>((List<SerializedEntity>) config.get("entities" ));
        for (SerializedEntity ser:values){
            Entity e = ser.getEntitySnapshot().createEntity(((Player) sender).getLocation());
            ser.applyToEntity(e);
        }
    }

    @Override
    public List<String> onComplete(CommandSender sender, String[] args) {
        return List.of();
    }
}
