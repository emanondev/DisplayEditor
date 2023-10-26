package emanondev.displayeditor;

import org.bukkit.NamespacedKey;

public class C {

    public static final NamespacedKey OWNER_KEY = new NamespacedKey(DisplayEditor.get(),"owner");

    public static final double MAX_EDIT_RADIUS = 64;
    /* TODO coming soon
    public static final int MAX_COPY_AREA_LENGTH = 10;
    public static final int MAX_COPY_AREA_ENTITIES = 50;*/
    public static final double DEFAULT_SELECT_RADIUS = 16;
    public static final int MAX_VIEW_RANGE = 128;
    public static final double MAX_EDIT_RADIUS_SQUARED = MAX_EDIT_RADIUS*MAX_EDIT_RADIUS;
    public static final double MOVE_COARSE = 0.5;
    public static final double MOVE_FINE = 0.05;
    public static final double SCALE_COARSE = 0.5;
    public static final double SCALE_FINE = 0.025;
    public static final double ROTATE_COARSE = 22.5;
    public static final double ROTATE_FINE = 2.5;
}
