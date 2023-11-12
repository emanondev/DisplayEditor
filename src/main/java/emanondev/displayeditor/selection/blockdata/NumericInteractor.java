package emanondev.displayeditor.selection.blockdata;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class NumericInteractor extends BlockDataIntractor {

    private final Function<BlockData, Integer> getCurrentValue;
    private final BiConsumer<BlockData, Integer> applyValue;
    private final Function<BlockData, Integer> getMax;
    private final Function<BlockData, Integer> getMin;

    public NumericInteractor(@NotNull String pathEnd, @NotNull Material mat,
                             @NotNull Function<BlockData, Integer> getCurrentValue,
                             @NotNull BiConsumer<BlockData, Integer> applyValue,
                             @NotNull Function<BlockData, Integer> getMax) {
        this(pathEnd, mat, getCurrentValue, applyValue, (d) -> 0, getMax);
    }

    public NumericInteractor(@NotNull String pathEnd, @NotNull Material mat,
                             @NotNull Function<BlockData, Integer> getCurrentValue,
                             @NotNull BiConsumer<BlockData, Integer> applyValue,
                             @NotNull Function<BlockData, Integer> getMin,
                             @NotNull Function<BlockData, Integer> getMax) {
        super(pathEnd, mat);
        this.getCurrentValue = getCurrentValue;
        this.applyValue = applyValue;
        this.getMax = getMax;
        this.getMin = getMin;
    }

    @Override
    public void handleClick(@NotNull BlockDisplay disp, @NotNull Player player, boolean isLeftClick) {
        BlockData data = disp.getBlock();
        int current = getCurrentValue.apply(data);

        if (isLeftClick)
            current++;
        else
            current--;
        if (current < getMin.apply(data))
            current = getMax.apply(data);
        else if (current > getMax.apply(data))
            current = getMin.apply(data);

        this.applyValue.accept(data, current);
        disp.setBlock(data);
    }

    @Override
    public int getAmount(BlockData data) {
        return Math.max(1,Math.min(127,getCurrentValue.apply(data)));
    }

    @Override
    public String[] getHolders(BlockData data) {
        return new String[]{"%value%",String.valueOf(getCurrentValue.apply(data))};
    }
}
