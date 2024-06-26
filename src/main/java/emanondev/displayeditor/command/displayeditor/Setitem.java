package emanondev.displayeditor.command.displayeditor;

import emanondev.displayeditor.Util;
import emanondev.displayeditor.command.AbstractCommand;
import emanondev.displayeditor.command.SubCmd;
import emanondev.displayeditor.gui.SelectItemGui;
import emanondev.displayeditor.selection.SelectionManager;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Setitem extends SubCmd {
    public Setitem(@NotNull AbstractCommand cmd) {
        super("setitem", cmd, true, false);
    }

    @Override
    public void onCommand(CommandSender sender, String alias, String[] args) {
        Player player = ((Player) sender);
        Display sel = SelectionManager.getSelection(player);
        if (sel == null) {
            sendLanguageString("none-selected", null, sender);
            return;
        }
        if (!(sel instanceof ItemDisplay)) {
            sendLanguageString("wrong-type", null, sender);
            return;
        }
        //ItemStack item;
        if (args.length > 1) {
            try {
                Material mat = Material.valueOf(args[1].toUpperCase(Locale.ENGLISH));
                if (!mat.isItem()) {
                    sendLanguageString("invalid-material", null, sender, "%material%", args[1]);
                    return;
                }
                ItemStack item = new ItemStack(mat);
                ((ItemDisplay) sel).setItemStack(item);
                sendLanguageString("success", null, sender);
            } catch (Exception e) {
                sendLanguageString("not-existing-material", null, sender, "%material%", args[1]);
                return;
            }
            return;
        } else {
            //item = player.getInventory().getItemInMainHand();
            player.openInventory(new SelectItemGui(player).getInventory());
        }
        //((ItemDisplay) sel).setItemStack(item);
    }

    @Override
    public List<String> onComplete(CommandSender sender, String[] args) {
        if (args.length == 2)
            return Util.complete(args[1], Material.class, Material::isItem);
        return Collections.emptyList();
    }
}
