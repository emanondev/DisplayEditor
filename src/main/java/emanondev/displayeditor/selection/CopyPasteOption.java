package emanondev.displayeditor.selection;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntitySnapshot;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CopyPasteOption {

    private static final List<List<Entity>> lastPasted = new ArrayList<>();
    private static final int MAX_HISTORY = 50; //TODO
    private static final int MAX_COPY = 200; //TODO
    private final List<EntitySnapshot> copiedEntities = new ArrayList<>();
    private final List<Location> copiedEntitiesOffsets = new ArrayList<>();
    private int yRotation = 0;
    private int copyRadius = 5;
    private Location copyPos;

    public int getYRotation() {
        return yRotation;
    }

    public void setYRotation(int yRotation) {
        this.yRotation = yRotation % 360;
        if (this.yRotation < 0)
            this.yRotation += 360;
    }

    public void addYRotation(int yRotation) {
        setYRotation(this.yRotation + yRotation);
    }

    public int getCopyRadius() {
        return copyRadius;
    }

    public boolean copy(@NotNull Collection<Entity> entities, @NotNull Location from) {
        if (entities.size() == 0 || entities.size() > MAX_COPY)
            return false;
        copiedEntities.clear();
        copiedEntitiesOffsets.clear();
        entities.forEach((en) -> {
            this.copiedEntities.add(en.createSnapshot());
            Location loc = en.getLocation();
            loc.setWorld(null);
            this.copiedEntitiesOffsets.add(loc);
        });
        from = from.clone();
        from.setWorld(null);
        copyPos = from;
        setYRotation(0);
        return true;
    }

    public boolean paste(@NotNull Location loc, boolean round, double rotationDegreesY) {
        if (copiedEntities.isEmpty())
            return false;
        World w = loc.getWorld();
        if (w == null)
            return false;
        Location from = copyPos.clone();
        if (round) {
            from.setX(from.getBlockX());
            from.setY(from.getBlockY());
            from.setZ(from.getBlockZ());
            loc.setX(loc.getBlockX());
            loc.setY(loc.getBlockY());
            loc.setZ(loc.getBlockZ());
        }
        loc.setWorld(null);
        List<Location> locations = new ArrayList<>();
        copiedEntitiesOffsets.forEach((offset) -> {
            Location to = offset.clone().subtract(from);
            if (rotationDegreesY != 0) {
                Vector toVector = to.toVector().rotateAroundY(Math.PI * rotationDegreesY / 180D);
                to = new Location(null, toVector.getX(), toVector.getY(), toVector.getZ(),
                        (float) (to.getYaw() + rotationDegreesY), to.getPitch());
            }
            to.add(loc);
            to.setWorld(w);
            locations.add(to);
        });
        List<Entity> results = new ArrayList<>();
        for (int i = 0; i < copiedEntities.size(); i++)
            results.add(copiedEntities.get(i).createEntity(locations.get(i)));
        lastPasted.add(results);
        if (lastPasted.size() > MAX_HISTORY)
            lastPasted.remove(0);
        return true;
    }

    public boolean paste(@NotNull Location location, boolean roundLocation) {
        return paste(location, roundLocation, yRotation);
    }

    public boolean undoPaste() {
        if (lastPasted.isEmpty())
            return false;
        lastPasted.remove(lastPasted.size() - 1).forEach(Entity::remove);
        return true;
    }

    public boolean hasCopiedEntities() {
        return copiedEntities.size() > 0;
    }

    public int getCopiedEntitiesSize() {
        return copiedEntities.size();
    }

    public boolean hasAvailableUndo() {
        return lastPasted.size() > 0;
    }

    public int getAvailableUndo() {
        return lastPasted.size();
    }

    public List<Entity> getLastPasted() {
        return hasAvailableUndo() ? lastPasted.get(lastPasted.size() - 1) : Collections.emptyList();
    }
}
