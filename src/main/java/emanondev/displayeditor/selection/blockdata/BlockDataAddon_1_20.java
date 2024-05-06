package emanondev.displayeditor.selection.blockdata;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Brushable;
import org.bukkit.block.data.Hatchable;
import org.bukkit.block.data.type.PinkPetals;
import org.bukkit.block.data.type.TurtleEgg;

import java.util.List;

class BlockDataAddon_1_20 {

    public static void add(List<BlockDataInteractor> values, BlockData data) {

        if (data instanceof Hatchable && !(data instanceof TurtleEgg)) {
            values.add(new NumericInteractor("hatch", Material.TURTLE_EGG,
                    (d) -> ((Hatchable) d).getHatch(),
                    (d, v) -> ((Hatchable) d).setHatch(v),
                    (d) -> ((Hatchable) d).getMaximumHatch()));
        }
        if (data instanceof Brushable) {
            values.add(new NumericInteractor("dusted", Material.BRUSH,
                    (d) -> ((Brushable) d).getDusted(),
                    (d, v) -> ((Brushable) d).setDusted(v),
                    (d) -> ((Brushable) d).getMaximumDusted()));
        }
        if (data instanceof PinkPetals) {
            values.add(new NumericInteractor("floweramount", Material.PINK_PETALS,
                    (d) -> ((PinkPetals) d).getFlowerAmount(),
                    (d, v) -> ((PinkPetals) d).setFlowerAmount(v),
                    (d) -> ((PinkPetals) d).getMaximumFlowerAmount()));
        }
    }
}
