package emanondev.displayeditor.command.displayeditor;

import emanondev.displayeditor.C;
import emanondev.displayeditor.Util;
import emanondev.displayeditor.UtilsString;
import emanondev.displayeditor.command.AbstractCommand;
import emanondev.displayeditor.command.SubCmd;
import emanondev.displayeditor.selection.EditorMode;
import emanondev.displayeditor.selection.SelectionManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Create extends SubCmd {
    public Create(@NotNull AbstractCommand cmd) {
        super("create", cmd, true, false);
    }

    @Override
    public void onCommand(CommandSender sender, String alias, String[] args) {
        if (args.length == 0) {
            onFail(sender,alias);
            return;
        }
        switch (args[1].toLowerCase(Locale.ENGLISH)) {
            case "item":
                item((Player) sender, alias, args);
                return;
            case "block":
                block((Player) sender, alias, args);
                return;
            case "text":
                text((Player) sender, alias, args);
                return;
        }
        onFail(sender,alias);
    }

    //create block [type=STONE/HAND]
    private void block(Player player, String alias, String[] args) {
        Material type = Material.STONE;
        if (args.length>2){
            try {
                type = Material.valueOf(args[2].toUpperCase(Locale.ENGLISH));
            }catch (Exception e){
                sendLanguageString("wrong-block-type", null, player);
                return;
            }
        }else if (getItemInHand(player)!=null && !getItemInHand(player).getType().isAir() && getItemInHand(player).getType().isBlock()){
            type = getItemInHand(player).getType();
        }
        if (!type.isBlock()){
            sendLanguageString("wrong-block-type", null, player);
            return;
        }
        //TODO can create on this position check
        BlockDisplay block = (BlockDisplay) player.getWorld().spawnEntity(player.getLocation(), EntityType.BLOCK_DISPLAY);
        block.setBlock(Bukkit.createBlockData(type));
        setOwner(block,player);
        SelectionManager.select(player,block);
        if (!SelectionManager.isOnEditorMode(player))
            SelectionManager.setEditorMode(player, EditorMode.POSITION);
        sendLanguageString("success-block", null, player);
    }


    //create item [item=HAND/STONE]
    private void item(Player player, String alias, String[] args) {
        ItemStack type = new ItemStack(Material.STONE);
        if (args.length>2){
            try {
                Material mat = Material.valueOf(args[2].toUpperCase(Locale.ENGLISH));
                if (!mat.isItem()){
                    sendLanguageString("wrong-item-type", null, player);
                    return;
                }
                type = new ItemStack(mat);
            }catch (Exception e){
                sendLanguageString("wrong-item-type", null, player);
                return;
            }
        }
        else if (getItemInHand(player)!=null && !getItemInHand(player).getType().isAir()){
            type = new ItemStack(getItemInHand(player));
        }
        //TODO can create on this position check
        ItemDisplay item = (ItemDisplay) player.getWorld().spawnEntity(player.getLocation(), EntityType.ITEM_DISPLAY);
        item.setItemStack(type);
        setOwner(item,player);
        SelectionManager.select(player,item);
        if (!SelectionManager.isOnEditorMode(player))
            SelectionManager.setEditorMode(player, EditorMode.POSITION);
        sendLanguageString("success-item", null, player);
    }

    //create text [text=Hologram]
    private void text(Player player, String alias, String[] args) {
        String text = "Hologram";
        if (args.length>2){
            text = String.join(" ", Arrays.asList(args).subList(2,args.length));
        }
        //TODO fix text
        //TODO apply censure or bypass it
        //TODO can create on this position check
        text = UtilsString.fix(text,null,true);
        TextDisplay textDisplay = (TextDisplay) player.getWorld().spawnEntity(player.getLocation(), EntityType.TEXT_DISPLAY,false);
        textDisplay.setBillboard(Display.Billboard.CENTER);
        textDisplay.setText(text);
        setOwner(textDisplay,player);
        SelectionManager.select(player,textDisplay);
        if (!SelectionManager.isOnEditorMode(player))
            SelectionManager.setEditorMode(player, EditorMode.POSITION);
        sendLanguageString("success-text", null, player);
    }

    @Override
    public List<String> onComplete(CommandSender sender, String[] args) {
        switch (args.length) {
            case 2:
                return Util.complete(args[1], "text", "item", "block");
            case 3:
                switch (args[1].toLowerCase(Locale.ENGLISH)) {
                    case "item":
                        return Util.complete(args[2], Material.class, Material::isItem);
                    case "block":
                        return Util.complete(args[2], Material.class, Material::isBlock);
                }
        }
        return Collections.emptyList();
    }



    private void setOwner(Display display, Player player) {
        display.getPersistentDataContainer().set(C.OWNER_KEY, PersistentDataType.STRING, player.getUniqueId().toString());
    }
}
