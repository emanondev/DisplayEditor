package emanondev.displayeditor.selection.blockdata;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class BooleanInteractor extends BlockDataIntractor {

    private final Function<BlockData, Boolean> getCurrentValue;
    private final BiConsumer<BlockData, Boolean> applyValue;

    public BooleanInteractor(@NotNull String pathEnd, @NotNull Material mat, @NotNull Function<BlockData, Boolean> getCurrentValue,
                             @NotNull BiConsumer<BlockData, Boolean> applyValue) {
        super(pathEnd, mat);
        this.getCurrentValue = getCurrentValue;
        this.applyValue = applyValue;
    }

    @Override
    public void handleClick(@NotNull BlockDisplay disp, @NotNull Player player, boolean isLeftClick) {
        BlockData data = disp.getBlock();
        this.applyValue.accept(data, !getCurrentValue.apply(data));
        disp.setBlock(data);
    }

    @Override
    public String[] getHolders(BlockData data) {
        return new String[]{"%value%", String.valueOf(getCurrentValue.apply(data))};
    }
}
