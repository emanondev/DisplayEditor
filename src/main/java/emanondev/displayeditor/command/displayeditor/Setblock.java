package emanondev.displayeditor.command.displayeditor;

import emanondev.displayeditor.Util;
import emanondev.displayeditor.UtilsString;
import emanondev.displayeditor.command.AbstractCommand;
import emanondev.displayeditor.command.SubCmd;
import emanondev.displayeditor.selection.SelectionManager;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Setblock extends SubCmd {
    public Setblock(@NotNull AbstractCommand cmd) {
        super("setblock", cmd, true, false);
    }

    @Override
    public void onCommand(CommandSender sender, String alias, String[] args) {
        @Nullable Display sel = SelectionManager.getSelection((Player) sender);
        if (sel == null) {
            sendLanguageString("none-selected", null, sender);
            return;
        }
        if (!(sel instanceof BlockDisplay)) {
            sendLanguageString("wrong-type", null, sender);
            return;
        }
        BlockData data;
        if (args.length > 1) {
            try {
                Material mat = Material.valueOf(args[1].toUpperCase(Locale.ENGLISH));
                if (!mat.isBlock()) {
                    sendLanguageString("invalid-material", null, sender,"%material%",args[1]);
                    return;
                }
                data = Bukkit.createBlockData(mat);
            } catch (Exception e) {
                sendLanguageString("not-existing-material", null, sender,"%material%",args[1]);
                return;
            }
        }
        else {
            RayTraceResult trace = ((Player) sender).rayTraceBlocks(20, FluidCollisionMode.ALWAYS);
            if (trace!=null && trace.getHitBlock()!=null)
                data = trace.getHitBlock().getBlockData();
            else
                data = Bukkit.createBlockData(Material.AIR);
        }
        ((BlockDisplay) sel).setBlock(data);
        sendLanguageString("success", null, sender);
    }

    @Override
    public List<String> onComplete(CommandSender sender, String[] args) {
        if (args.length == 2)
            return Util.complete(args[1], Material.class, Material::isBlock);
        return Collections.emptyList();
    }
}
