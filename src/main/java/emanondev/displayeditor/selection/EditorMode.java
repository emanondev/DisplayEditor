package emanondev.displayeditor.selection;

import emanondev.displayeditor.C;
import emanondev.displayeditor.DisplayEditor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Light;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public enum EditorMode {

    POSITION,
    SCALE,
    ROTATION,
    SHADOW,
    ENTITY_SPECIFIC,
    COPY_PASTE,
    ;

    private final DecimalFormat optional3Digit = new DecimalFormat("0.###");

    EditorMode() {
        optional3Digit.setRoundingMode(RoundingMode.HALF_DOWN);
    }

    public void setup(Player player) {
        Inventory inv = player.getInventory();
        Display display = SelectionManager.getSelection(player);


        inv.setItem(7, setDesc(craftItem(Material.PAPER, ordinal() + 1), player, "editor.all.page"));
        inv.setItem(8, setDesc(craftItem(Material.BARRIER), player, "editor.all.exit"));
        if (display == null && this != COPY_PASTE) {
            for (int i = 0; i < 7; i++)
                inv.setItem(0, setDesc(craftItem(Material.STRUCTURE_VOID), player, "editor.all.select"));
            return;
        }
        String[] holders;
        switch (this) {
            case POSITION: {
                Location loc = display.getLocation();
                holders = new String[]{"%x_offset%", optional3Digit.format(loc.getX() - loc.getBlockX()),
                        "%y_offset%", optional3Digit.format(loc.getY() - loc.getBlockY()),
                        "%z_offset%", optional3Digit.format(loc.getZ() - loc.getBlockZ()),
                        "%move_coarse%", String.valueOf(C.MOVE_COARSE), "%move_fine%", String.valueOf(C.MOVE_FINE)};
                inv.setItem(0, setDesc(craftItem(Material.LAPIS_LAZULI), player, "editor.position.x", holders));
                inv.setItem(1, setDesc(craftItem(Material.REDSTONE), player, "editor.position.y", holders));
                inv.setItem(2, setDesc(craftItem(Material.EMERALD), player, "editor.position.z", holders));
                inv.setItem(3, null);
                inv.setItem(4, setDesc(craftItem(Material.ENDER_PEARL), player, "editor.position.teleport"));
                inv.setItem(5, null);
                inv.setItem(6, setDesc(craftItem(Material.ANVIL), player, "editor.position.reset"));
                return;
            }
            case ROTATION: {
                Vector3f vect = display.getTransformation().getRightRotation().getEulerAnglesXYZ(new Vector3f());
                double x = vect.x;
                x = (x / Math.PI + (x < 0 ? 2 : 0)) * 180;
                double y = vect.y;
                y = (y / Math.PI + (y < 0 ? 2 : 0)) * 180;
                double z = vect.z;
                z = (z / Math.PI + (z < 0 ? 2 : 0)) * 180;

                holders = new String[]{"%x_degree%", optional3Digit.format(x),
                        "%y_degree%", optional3Digit.format(y), "%z_degree%", optional3Digit.format(z),
                        "%rotate_coarse%", String.valueOf(C.ROTATE_COARSE), "%rotate_fine%", String.valueOf(C.ROTATE_FINE)};
                inv.setItem(0, setDesc(craftItem(Material.MUSIC_DISC_WAIT), player, "editor.rotation.x", holders));
                inv.setItem(1, setDesc(craftItem(Material.MUSIC_DISC_BLOCKS), player, "editor.rotation.y", holders));
                inv.setItem(2, setDesc(craftItem(Material.MUSIC_DISC_FAR), player, "editor.rotation.z", holders));
                inv.setItem(3, null);
                inv.setItem(4, setDesc(craftItem(Material.MUSIC_DISC_STRAD), player, "editor.rotation.mode",
                        "%value%", display.getBillboard().name().toLowerCase(Locale.ENGLISH)));
                inv.setItem(5, null);
                inv.setItem(6, setDesc(craftItem(Material.ANVIL), player, "editor.rotation.reset"));
                return;
            }
            case SCALE: {
                Vector3f scale = display.getTransformation().getScale();
                holders = new String[]{"%x_scale%", optional3Digit.format(scale.x),
                        "%y_scale%", optional3Digit.format(scale.y),
                        "%z_scale%", optional3Digit.format(scale.z), "%scale_coarse%", String.valueOf(C.SCALE_COARSE), "%scale_fine%", String.valueOf(C.SCALE_FINE)};
                inv.setItem(0, setDesc(craftItem(Material.BLUE_DYE), player, "editor.scale.x", holders));
                inv.setItem(1, setDesc(craftItem(Material.RED_DYE), player, "editor.scale.y", holders));
                inv.setItem(2, setDesc(craftItem(Material.LIME_DYE), player, "editor.scale.z", holders));
                inv.setItem(3, null);
                inv.setItem(4, setDesc(craftItem(Material.GRAY_DYE), player, "editor.scale.all", holders));
                inv.setItem(5, null);
                inv.setItem(6, setDesc(craftItem(Material.ANVIL), player, "editor.scale.reset"));
                return;
            }
            case SHADOW: {
                Display.Brightness brightness = display.getBrightness();
                if (brightness == null) {
                    inv.setItem(0, setDesc(craftItem(Material.BLACK_CONCRETE_POWDER), player, "editor.shadow.skylight"));
                    inv.setItem(1, setDesc(craftItem(Material.BLACK_CONCRETE_POWDER), player, "editor.shadow.blocklight"));
                } else {
                    ItemStack itemStack = craftItem(Material.LIGHT);
                    BlockDataMeta meta = (BlockDataMeta) itemStack.getItemMeta();
                    assert meta != null;
                    meta.setBlockData(Bukkit.createBlockData(Material.LIGHT, (data) -> ((Light) data).setLevel(brightness.getSkyLight())));
                    itemStack.setItemMeta(meta);
                    inv.setItem(0, setDesc(itemStack, player, "editor.shadow.skylight"));
                    ItemStack itemStack2 = craftItem(Material.LIGHT);
                    BlockDataMeta meta2 = (BlockDataMeta) itemStack2.getItemMeta();
                    assert meta2 != null;
                    meta2.setBlockData(Bukkit.createBlockData(Material.LIGHT, (data) -> ((Light) data).setLevel(brightness.getBlockLight())));
                    itemStack2.setItemMeta(meta2);
                    inv.setItem(1, setDesc(itemStack2, player, "editor.shadow.blocklight"));
                }
                inv.setItem(2, setDesc(craftItem(Material.ENDER_EYE, Math.max(1, Math.min(127, (int) (display.getViewRange() * 64)))), player,
                        "editor.shadow.see_distance", "%value%", String.valueOf((int) (display.getViewRange() * 64))));
                inv.setItem(3, null);
                inv.setItem(4, null);
                inv.setItem(5, null);
                inv.setItem(6, null);
                return;
            }
            case ENTITY_SPECIFIC:
                if (display instanceof TextDisplay) {
                    TextDisplay textDisplay = (TextDisplay) display;
                    Color backGround = textDisplay.getBackgroundColor();
                    inv.setItem(0, setDesc(backGround == null ? craftItem(Material.GRAY_STAINED_GLASS_PANE) :
                                    craftItem(Material.RED_STAINED_GLASS_PANE, Math.min(127, backGround.getRed() / 2 + 1)), player,
                            "editor.entity_specific.text_background_red", "%value%", backGround == null ? "?" : String.valueOf(backGround.getRed())));
                    inv.setItem(1, setDesc(backGround == null ? craftItem(Material.GRAY_STAINED_GLASS_PANE) :
                                    craftItem(Material.LIME_STAINED_GLASS_PANE, Math.min(127, backGround.getGreen() / 2 + 1)), player,
                            "editor.entity_specific.text_background_green", "%value%", backGround == null ? "?" : String.valueOf(backGround.getGreen())));
                    inv.setItem(2, setDesc(backGround == null ? craftItem(Material.GRAY_STAINED_GLASS_PANE) :
                                    craftItem(Material.BLUE_STAINED_GLASS_PANE, Math.min(127, backGround.getBlue() / 2 + 1)), player,
                            "editor.entity_specific.text_background_blue", "%value%", backGround == null ? "?" : String.valueOf(backGround.getBlue())));
                    inv.setItem(3, setDesc(backGround == null ? craftItem(Material.GRAY_STAINED_GLASS_PANE) :
                                    craftItem(Material.GRAY_STAINED_GLASS_PANE, Math.min(127, backGround.getAlpha() / 2 + 1)), player,
                            "editor.entity_specific.text_background_alpha", "%value%", backGround == null ? "?" : String.valueOf(backGround.getAlpha())));
                    inv.setItem(4, setDesc(craftItem(Material.GLOBE_BANNER_PATTERN), player,
                            "editor.entity_specific.text_alignment", "%value%",
                            textDisplay.getAlignment().name().toLowerCase(Locale.ENGLISH)));
                    inv.setItem(5, null);
                    inv.setItem(6, null);
                    return;
                }
                if (display instanceof BlockDisplay) {
                    inv.setItem(0, setDesc(craftItem(Material.STRUCTURE_VOID), player, "editor.entity_specific.dataeditor_soon")); //TODO edit data
                    BlockData data = ((BlockDisplay) display).getBlock();

                    inv.setItem(1, null);
                    inv.setItem(2, null);
                    inv.setItem(3, null);
                    inv.setItem(4, null);
                    inv.setItem(5, null);
                    inv.setItem(6, null);
                    return;
                }
                if (display instanceof ItemDisplay) {
                    ItemDisplay itemDisplay = (ItemDisplay) display;
                    inv.setItem(0, setDesc(craftItem(Material.GLOBE_BANNER_PATTERN), player,
                            "editor.entity_specific.item_view", "%value%",
                            itemDisplay.getItemDisplayTransform().name().toLowerCase(Locale.ENGLISH)));
                    inv.setItem(1, setDesc(craftItem(Material.ENCHANTED_BOOK), player,
                            "editor.entity_specific.item_glow"));
                    inv.setItem(2, null);
                    inv.setItem(3, null);
                    inv.setItem(4, null);
                    inv.setItem(5, null);
                    inv.setItem(6, null);
                    return;
                }
                return;
            case COPY_PASTE:
                inv.setItem(0, setDesc(craftItem(Material.STRUCTURE_VOID), player, "editor.copy_paste.select"));
                inv.setItem(1, setDesc(craftItem(Material.SPONGE), player, "editor.copy_paste.copypaste_soon"));
                inv.setItem(2, null);
                inv.setItem(3, null);
                inv.setItem(4, null);
                inv.setItem(5, null);
                inv.setItem(6, null);
                return;
        }
    }


    @Contract("null,_,_,_->null;!null,_,_,_->!null")
    private ItemStack setDesc(@Nullable ItemStack item, Player target, @NotNull String fullPath, String... holders) {
        if (item == null) {
            return null;
        } else {
            item.setItemMeta(this.setDesc(item.getItemMeta(), target, fullPath, holders));
            return item;
        }
    }

    @Contract("null,_,_,_->null;!null,_,_,_->!null")
    private ItemMeta setDesc(@Nullable ItemMeta meta, Player target, @NotNull String fullPath, String... holders) {
        if (meta == null) {
            return null;
        } else {
            List<String> list = DisplayEditor.get().getLanguageConfig(target).loadMultiMessage(fullPath, null, target, true, holders);
            meta.setDisplayName(list != null && !list.isEmpty() ? list.get(0) : " ");
            if (list != null && !list.isEmpty()) {
                meta.setLore(list.subList(1, list.size()));
            }
            return meta;
        }
    }

    private ItemStack craftItem(Material mat) {
        return craftItem(mat, 1);
    }

    private ItemStack craftItem(Material mat, int amount) {
        ItemStack item = new ItemStack(mat, amount);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.addItemFlags(ItemFlag.values());
        item.setItemMeta(meta);
        return item;
    }

}
