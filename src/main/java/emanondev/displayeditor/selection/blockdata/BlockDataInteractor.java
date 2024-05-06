package emanondev.displayeditor.selection.blockdata;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;

public abstract class BlockDataInteractor {
    protected final String pathEnd;
    private final Material mat;

    protected BlockDataInteractor(String pathEnd, Material mat) {
        this.pathEnd = pathEnd;
        this.mat = mat;
    }

    public abstract void handleClick(BlockDisplay disp, Player player, boolean isLeftClick);

    public int getAmount(BlockData data) {
        return 1;
    }

    public String getLanguagePath(BlockData data) {
        return "editor.entity_specific.block_" + pathEnd;
    }

    public abstract String[] getHolders(BlockData data);

    public Material getMaterial(BlockData data) {
        return mat;
    }
}
