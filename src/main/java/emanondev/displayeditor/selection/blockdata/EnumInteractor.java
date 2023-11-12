package emanondev.displayeditor.selection.blockdata;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class EnumInteractor<E extends Enum<E>> extends BlockDataIntractor {

    private final Function<BlockData, E> getCurrentValue;
    private final Class<E> enumClass;
    private final BiFunction<BlockData, E, Boolean> allowed;
    private final BiConsumer<BlockData, E> applyValue;

    public EnumInteractor(@NotNull String pathEnd, @NotNull Material mat, @NotNull Class<E> clazz, @NotNull Function<BlockData, E> getCurrentValue,
                          @NotNull BiConsumer<BlockData, E> applyValue) {
        this(pathEnd, mat, clazz, getCurrentValue, applyValue, (i, v) -> true);
    }

    public EnumInteractor(@NotNull String pathEnd, @NotNull Material mat, @NotNull Class<E> clazz, @NotNull Function<BlockData, E> getCurrentValue,
                          @NotNull BiConsumer<BlockData, E> applyValue, @NotNull BiFunction<BlockData, E, Boolean> allowed) {
        super(pathEnd, mat);
        this.enumClass = clazz;
        this.getCurrentValue = getCurrentValue;
        this.applyValue = applyValue;
        this.allowed = allowed;
    }

    @Override
    public void handleClick(@NotNull BlockDisplay disp, @NotNull Player player, boolean isLeftClick) {
        BlockData data = disp.getBlock();
        E current = getCurrentValue.apply(data);
        int index = current.ordinal();
        int safeCounter = 0;
        while (safeCounter < 200) {
            safeCounter++;
            if (isLeftClick)
                index--;
            else
                index++;
            if (index < 0)
                index += enumClass.getEnumConstants().length;
            else if (index >= enumClass.getEnumConstants().length)
                index = 0;
            E temp = enumClass.getEnumConstants()[index];
            if (allowed.apply(data, temp)) {
                try {
                    this.applyValue.accept(data, temp);
                    disp.setBlock(data);
                    return;
                } catch (Exception ignored) {

                }
            }
        }
        new IllegalStateException("Unable to find a consistent state for " + enumClass.getSimpleName()).printStackTrace();
    }

    @Override
    public String[] getHolders(BlockData data) {
        return new String[]{"%value%",String.valueOf(getCurrentValue.apply(data).name())};
    }
}
