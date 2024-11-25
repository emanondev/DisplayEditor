package emanondev.displayeditor.properties;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import java.util.HashMap;
import java.util.Map;

public class EntitySerializer {


    public Map<String, Object> toMap(Entity e) {
        HashMap<String, Object> map = new HashMap<>();
        String snapshot = e.createSnapshot().getAsString();

        Bukkit.getEntityFactory().createEntitySnapshot(snapshot);
        return map;
    }
}
