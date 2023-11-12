package emanondev.displayeditor.selection.blockdata;

import emanondev.displayeditor.Util;
import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.*;

import java.util.ArrayList;
import java.util.List;

public class BlockDataUtil {

    public static List<BlockDataIntractor> getBlockDataValues(BlockData data) {
        List<BlockDataIntractor> values = new ArrayList<>();
        if (data instanceof Ageable) {
            values.add(new NumericInteractor("ageable", Material.WHEAT_SEEDS,
                    (d) -> ((Ageable) d).getAge(),
                    (d, v) -> ((Ageable) d).setAge(v),
                    (d) -> ((Ageable) d).getMaximumAge()));
        }
        if (data instanceof AnaloguePowerable) {
            values.add(new NumericInteractor("power", Material.REDSTONE_TORCH,
                    (d) -> ((AnaloguePowerable) d).getPower(),
                    (d, v) -> ((AnaloguePowerable) d).setPower(v),
                    (d) -> ((AnaloguePowerable) d).getMaximumPower()));
        }
        if (data instanceof Attachable) {
            values.add(new BooleanInteractor("attachable", Material.WHEAT_SEEDS,
                    (d) -> ((Attachable) d).isAttached(),
                    (d, v) -> ((Attachable) d).setAttached(v)));
        }
        if (data instanceof Bamboo) {
            values.add(new EnumInteractor<>("bambooleaves", Material.BAMBOO,Bamboo.Leaves.class,
                    (d) -> ((Bamboo) d).getLeaves(),
                    (d, v) -> ((Bamboo) d).setLeaves(v)));
        }
        if (data instanceof Bed) {
            values.add(new EnumInteractor<>("bedpart", Material.RED_BED,Bed.Part.class,
                    (d) -> ((Bed) d).getPart(),
                    (d, v) -> ((Bed) d).setPart(v)));
        }
        if (data instanceof Beehive) {
            values.add(new NumericInteractor("honeylevel", Material.HONEYCOMB,
                    (d) -> ((Beehive) d).getHoneyLevel(),
                    (d, v) -> ((Beehive) d).setHoneyLevel(v),
                    (d) -> ((Beehive) d).getMaximumHoneyLevel()));
        }
        if (data instanceof Bell) {
            values.add(new EnumInteractor<>("bellattachment", Material.BELL,Bell.Attachment.class,
                    (d) -> ((Bell) d).getAttachment(),
                    (d, v) -> ((Bell) d).setAttachment(v)));
        }
        if (data instanceof BigDripleaf) {
            values.add(new EnumInteractor<>("bigdripleaftilt", Material.BIG_DRIPLEAF,BigDripleaf.Tilt.class,
                    (d) -> ((BigDripleaf) d).getTilt(),
                    (d, v) -> ((BigDripleaf) d).setTilt(v)));
        }
        if (data instanceof Bisected) {
            values.add(new EnumInteractor<>("bisectedhalf", Material.SPRUCE_TRAPDOOR,Bisected.Half.class,
                    (d) -> ((Bisected) d).getHalf(),
                    (d, v) -> ((Bisected) d).setHalf(v)));
        }
        if (data instanceof BrewingStand) {
            //TODO
        }
        if (data instanceof Cake) {
            values.add(new NumericInteractor("cakebites", Material.CAKE,
                    (d) -> ((Cake) d).getBites(),
                    (d, v) -> ((Cake) d).setBites(v),
                    (d) -> ((Cake) d).getMaximumBites()));
        }
        if (data instanceof Candle) {
            values.add(new NumericInteractor("candles", Material.CANDLE,
                    (d) -> ((Candle) d).getCandles(),
                    (d, v) -> ((Candle) d).setCandles(v),
                    (d) -> 1,
                    (d) -> ((Candle) d).getMaximumCandles()));//TODO min = 1?
        }
        if (data instanceof CaveVinesPlant) {
            values.add(new BooleanInteractor("berries", Material.SWEET_BERRIES,
                    (d) -> ((CaveVinesPlant) d).isBerries(),
                    (d, v) -> ((CaveVinesPlant) d).setBerries(v)));
        }
        if (data instanceof Chest) {
            values.add(new EnumInteractor<>("chesttype", Material.CHEST,Chest.Type.class,
                    (d) -> ((Chest) d).getType(),
                    (d, v) -> ((Chest) d).setType(v)));
        }
        if (data instanceof ChiseledBookshelf) {
            values.add(new NumericInteractor("books1", Material.CHISELED_BOOKSHELF,
                    (d) -> {
                        ChiseledBookshelf c = ((ChiseledBookshelf) d);
                        return (c.isSlotOccupied(0)?1:0)+(c.isSlotOccupied(1)?2:0)+(c.isSlotOccupied(2)?4:0);
                    },
                    (d, v) -> {
                        ChiseledBookshelf c = ((ChiseledBookshelf) d);
                        c.setSlotOccupied(0,v%2!=0);
                        c.setSlotOccupied(1,v/2%2!=0);
                        c.setSlotOccupied(2,v/4%2!=0);
                    },
                    (d) -> 7));
            values.add(new NumericInteractor("books2", Material.CHISELED_BOOKSHELF,
                    (d) -> {
                        ChiseledBookshelf c = ((ChiseledBookshelf) d);
                        return (c.isSlotOccupied(3)?1:0)+(c.isSlotOccupied(4)?2:0)+(c.isSlotOccupied(5)?4:0);
                    },
                    (d, v) -> {
                        ChiseledBookshelf c = ((ChiseledBookshelf) d);
                        c.setSlotOccupied(3,v%2!=0);
                        c.setSlotOccupied(4,v/2%2!=0);
                        c.setSlotOccupied(5,v/4%2!=0);
                    },
                    (d) -> 7));
        }
        if (data instanceof Comparator) {
            values.add(new EnumInteractor<>("comparatormode", Material.COMPARATOR,Comparator.Mode.class,
                    (d) -> ((Comparator) d).getMode(),
                    (d, v) -> ((Comparator) d).setMode(v)));
        }
        if (data instanceof DaylightDetector) {
            values.add(new BooleanInteractor("inverted", Material.DAYLIGHT_DETECTOR,
                    (d) -> ((DaylightDetector) d).isInverted(),
                    (d, v) -> ((DaylightDetector) d).setInverted(v)));
        }
        if (data instanceof Directional) {
            values.add(new EnumInteractor<>("directional", Material.HOPPER,BlockFace.class,
                    (d) -> ((Directional) d).getFacing(),
                    (d, v) -> ((Directional) d).setFacing(v),
                    (d,v) -> ((Directional) d).getFaces().contains(v)));
        }
        if (data instanceof Door) {
            values.add(new EnumInteractor<>("hinge", Material.SPRUCE_DOOR,Door.Hinge.class,
                    (d) -> ((Door) d).getHinge(),
                    (d, v) -> ((Door) d).setHinge(v)));
        }
        if (data instanceof EndPortalFrame) {
            values.add(new BooleanInteractor("eye", Material.END_PORTAL_FRAME,
                    (d) -> ((EndPortalFrame) d).hasEye(),
                    (d, v) -> ((EndPortalFrame) d).setEye(v)));
        }
        if (data instanceof FaceAttachable) {
            //TODO
        }
        if (data instanceof Gate) {
            values.add(new BooleanInteractor("wall", Material.OAK_FENCE_GATE,
                    (d) -> ((Gate) d).isInWall(),
                    (d, v) -> ((Gate) d).setInWall(v)));
        }
        if (data instanceof Hangable) {
            values.add(new BooleanInteractor("hanging", Material.LANTERN,
                    (d) -> ((Hangable) d).isHanging(),
                    (d, v) -> ((Hangable) d).setHanging(v)));
        }
        if (data instanceof Jigsaw) {
            values.add(new EnumInteractor<>("jigsaworientation", Material.WHEAT_SEEDS,Jigsaw.Orientation.class,
                    (d) -> ((Jigsaw) d).getOrientation(),
                    (d, v) -> ((Jigsaw) d).setOrientation(v)));
        }
        if (data instanceof Lectern) {
            //TODO ?
        }
        if (data instanceof Levelled) {
            values.add(new NumericInteractor("level", Material.SNOW,
                    (d) -> ((Levelled) d).getLevel(),
                    (d, v) -> ((Levelled) d).setLevel(v),
                    (d) -> ((Levelled) d).getMaximumLevel()));
        }
        if (data instanceof Lightable) {
            values.add(new BooleanInteractor("lit", Material.RED_CANDLE,
                    (d) -> ((Lightable) d).isLit(),
                    (d, v) -> ((Lightable) d).setLit(v)));
        }
        if (data instanceof MultipleFacing) {
            //TODO
        }
        if (data instanceof Openable) {
            values.add(new BooleanInteractor("open", Material.BARREL,
                    (d) -> ((Openable) d).isOpen(),
                    (d, v) -> ((Openable) d).setOpen(v)));
        }
        if (data instanceof Orientable) {
            values.add(new EnumInteractor<>("axis", Material.OAK_LOG,Axis.class,
                    (d) -> ((Orientable) d).getAxis(),
                    (d, v) -> ((Orientable) d).setAxis(v),
                    (d, v) -> ((Orientable) d).getAxes().contains(v)));
        }
        if (data instanceof Piston) {
            values.add(new BooleanInteractor("extended", Material.PISTON,
                    (d) -> ((Piston) d).isExtended(),
                    (d, v) -> ((Piston) d).setExtended(v)));
        }
        if (data instanceof PistonHead) {
            values.add(new BooleanInteractor("short", Material.PISTON,
                    (d) -> ((PistonHead) d).isShort(),
                    (d, v) -> ((PistonHead) d).setShort(v)));
        }
        if (data instanceof PointedDripstone) {
            values.add(new EnumInteractor<>("dripstonethickness", Material.POINTED_DRIPSTONE,PointedDripstone.Thickness.class,
                    (d) -> ((PointedDripstone) d).getThickness(),
                    (d, v) -> ((PointedDripstone) d).setThickness(v)));
            values.add(new EnumInteractor<>("directional", Material.HOPPER,BlockFace.class,
                    (d) -> ((PointedDripstone) d).getVerticalDirection(),
                    (d, v) -> ((PointedDripstone) d).setVerticalDirection(v),
                    (d, v) -> ((PointedDripstone) d).getVerticalDirections().contains(v)));
        }
        if (data instanceof Powerable) {
            values.add(new BooleanInteractor("powered", Material.POWERED_RAIL,
                    (d) -> ((Powerable) d).isPowered(),
                    (d, v) -> ((Powerable) d).setPowered(v)));
        }
        if (data instanceof Rail) {
            values.add(new EnumInteractor<>("railshape", Material.RAIL,Rail.Shape.class,
                    (d) -> ((Rail) d).getShape(),
                    (d, v) -> ((Rail) d).setShape(v),
                    (d, v) -> ((Rail) d).getShapes().contains(v)));
        }
        if (data instanceof RedstoneWire) {
            //TODO
        }
        if (data instanceof Repeater) {
            values.add(new NumericInteractor("delay", Material.REPEATER,
                    (d) -> ((Repeater) d).getDelay(),
                    (d, v) -> ((Repeater) d).setDelay(v),
                    (d) -> ((Repeater) d).getMinimumDelay(),
                    (d) -> ((Repeater) d).getMaximumDelay()));
            values.add(new BooleanInteractor("locked", Material.REPEATER,
                    (d) -> ((Repeater) d).isLocked(),
                    (d, v) -> ((Repeater) d).setLocked(v)));
        }
        if (data instanceof RespawnAnchor) {
            values.add(new NumericInteractor("charges", Material.RESPAWN_ANCHOR,
                    (d) -> ((RespawnAnchor) d).getCharges(),
                    (d, v) -> ((RespawnAnchor) d).setCharges(v),
                    (d) -> ((RespawnAnchor) d).getMaximumCharges()));
        }
        if (data instanceof Rotatable) {
            values.add(new EnumInteractor<>("rotation", Material.OAK_SIGN,BlockFace.class,
                    (d) -> ((Rotatable) d).getRotation(),
                    (d, v) -> ((Rotatable) d).setRotation(v)));
        }
        if (data instanceof Scaffolding) {
            values.add(new BooleanInteractor("bottom", Material.SCAFFOLDING,
                    (d) -> ((Scaffolding) d).isBottom(),
                    (d, v) -> ((Scaffolding) d).setBottom(v)));
        }
        if (data instanceof SculkCatalyst) {
            values.add(new BooleanInteractor("bloom", Material.SCULK_CATALYST,
                    (d) -> ((SculkCatalyst) d).isBloom(),
                    (d, v) -> ((SculkCatalyst) d).setBloom(v)));
        }
        if (data instanceof SculkSensor) {
            values.add(new EnumInteractor<>("sculkphase", Material.SCULK_SENSOR,SculkSensor.Phase.class,
                    (d) -> ((SculkSensor) d).getPhase(),
                    (d, v) -> ((SculkSensor) d).setPhase(v)));
        }
        if (data instanceof SculkShrieker) {
            values.add(new BooleanInteractor("shrieking", Material.SCULK_SHRIEKER,
                    (d) -> ((SculkShrieker) d).isShrieking(),
                    (d, v) -> ((SculkShrieker) d).setShrieking(v)));
            values.add(new BooleanInteractor("cansummon", Material.SCULK_SHRIEKER,
                    (d) -> ((SculkShrieker) d).isCanSummon(),
                    (d, v) -> ((SculkShrieker) d).setCanSummon(v)));
        }
        if (data instanceof SeaPickle) {
            values.add(new NumericInteractor("pickles", Material.SEA_PICKLE,
                    (d) -> ((SeaPickle) d).getPickles(),
                    (d, v) -> ((SeaPickle) d).setPickles(v),
                    (d) -> ((SeaPickle) d).getMinimumPickles(),
                    (d) -> ((SeaPickle) d).getMaximumPickles()));
        }
        if (data instanceof Slab) {
            values.add(new EnumInteractor<>("slabtype", Material.STONE_SLAB,
                    Slab.Type.class,
                    (d) -> ((Slab) d).getType(),
                    (d, v) -> ((Slab) d).setType(v)));
        }
        if (data instanceof Snow) {
            values.add(new NumericInteractor("layers", Material.SNOW,
                    (d) -> ((Snow) d).getLayers(),
                    (d, v) -> ((Snow) d).setLayers(v),
                    (d) -> ((Snow) d).getMinimumLayers(),
                    (d) -> ((Snow) d).getMaximumLayers()));
        }
        if (data instanceof Snowable) {
            values.add(new BooleanInteractor("snowy", Material.SNOW,
                    (d) -> ((Snowable) d).isSnowy(),
                    (d, v) -> ((Snowable) d).setSnowy(v)));
        }
        if (data instanceof Stairs) {
            values.add(new EnumInteractor<>("stairsshape", Material.STONE_STAIRS,
                    Stairs.Shape.class,
                    (d) -> ((Stairs) d).getShape(),
                    (d, v) -> ((Stairs) d).setShape(v)));
        }
        if (data instanceof TechnicalPiston) {
            values.add(new EnumInteractor<>("pistontype", Material.STICKY_PISTON,
                    TechnicalPiston.Type.class,
                    (d) -> ((TechnicalPiston) d).getType(),
                    (d, v) -> ((TechnicalPiston) d).setType(v)));
        }
        if (data instanceof Tripwire) {
            values.add(new BooleanInteractor("disarmed", Material.TRIPWIRE_HOOK,
                    (d) -> ((Tripwire) d).isDisarmed(),
                    (d, v) -> ((Tripwire) d).setDisarmed(v)));
        }
        if (data instanceof TurtleEgg) {
            values.add(new NumericInteractor("eggs", Material.TURTLE_EGG,
                    (d) -> ((TurtleEgg) d).getEggs(),
                    (d, v) -> ((TurtleEgg) d).setEggs(v),
                    (d) -> ((TurtleEgg) d).getMinimumEggs(),
                    (d) -> ((TurtleEgg) d).getMaximumEggs()));
            values.add(new NumericInteractor("hatch", Material.TURTLE_EGG,
                    (d) -> ((TurtleEgg) d).getHatch(),
                    (d, v) -> ((TurtleEgg) d).setHatch(v),
                    (d) -> ((TurtleEgg) d).getMaximumHatch()));
        }
        if (data instanceof Wall) {
            //TODO
            values.add(new BooleanInteractor("wallup", Material.STONE_BRICK_WALL,
                    (d) -> ((Wall) d).isUp(),
                    (d, v) -> ((Wall) d).setUp(v)));
        }
        if (data instanceof Waterlogged) {
            values.add(new BooleanInteractor("waterlogged", Material.WATER_CAULDRON,
                    (d) -> ((Waterlogged) d).isWaterlogged(),
                    (d, v) -> ((Waterlogged) d).setWaterlogged(v)));
        }
        if (Util.isVersionUpTo(1,19,4))
            return values;
        BlockDataAddon_1_20.add(values,data);
        return values;
    }
}
