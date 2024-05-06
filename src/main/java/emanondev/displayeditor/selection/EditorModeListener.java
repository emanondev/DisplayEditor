package emanondev.displayeditor.selection;

import emanondev.displayeditor.C;
import emanondev.displayeditor.SoundUtil;
import emanondev.displayeditor.Util;
import emanondev.displayeditor.selection.blockdata.BlockDataInteractor;
import emanondev.displayeditor.selection.blockdata.BlockDataUtil;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.*;

public class EditorModeListener implements Listener {

    private final HashMap<UUID, Long> lastPlayerInteraction = new HashMap<>();

    @EventHandler
    public void event(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player && SelectionManager.isOnEditorMode((Player) event.getEntity()))
            event.setCancelled(true);
    }

    @EventHandler
    public void event(PlayerTeleportEvent event) {//if change world
        if (!SelectionManager.isOnEditorMode(event.getPlayer()))
            return;
        if (event.getTo() == null || Objects.equals(event.getFrom().getWorld(), event.getTo().getWorld())) {
            SelectionManager.deselect(event.getPlayer());
        }
    }

    @EventHandler
    public void event(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player && SelectionManager.isOnEditorMode((Player) event.getWhoClicked()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void event(EntityDamageByEntityEvent event) {
        if (C.EXIT_ON_HIT && event.getEntity() instanceof Player p && SelectionManager.isOnEditorMode(p))
            SelectionManager.setEditorMode(p, null);
    }

    @EventHandler
    public void event(EntityResurrectEvent event) {
        if (event.isCancelled() && event.getEntity() instanceof Player p && SelectionManager.isOnEditorMode(p))
            SelectionManager.setEditorMode(p, null);
    }

    @EventHandler
    public void event(PlayerDropItemEvent event) { //TODO may use to select another DisplayEntity
        if (SelectionManager.isOnEditorMode(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler
    public void event(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL)
            return;
        EditorMode mode = SelectionManager.getEditorMode(event.getPlayer());
        if (mode == null)
            return;
        event.setCancelled(true);
        if (event.getHand() != EquipmentSlot.HAND)
            return;

        //there is a strange bug that fires the event multiple times
        long nowMs = System.currentTimeMillis();
        long lastMs = this.lastPlayerInteraction.getOrDefault(event.getPlayer().getUniqueId(), nowMs - 150);

        if (lastMs + 100 >= nowMs) //2 tick
            return;
        this.lastPlayerInteraction.put(event.getPlayer().getUniqueId(), nowMs);
        handleClick(event, mode);
    }

    /*
    @EventHandler
    public void event(PlayerDeathEvent event) {
        if (SelectionManager.isOnEditorMode(event.getEntity()))
            SelectionManager.setEditorMode(event.getEntity(), null);
    }*/

    @EventHandler
    public void event(PlayerQuitEvent event) {
        if (SelectionManager.isOnEditorMode(event.getPlayer()))
            SelectionManager.setEditorMode(event.getPlayer(), null);
    }

    @EventHandler
    public void event(InventoryOpenEvent event) { //TODO?

    }

    @EventHandler
    public void event(PlayerSwapHandItemsEvent event) {
        if (SelectionManager.isOnEditorMode(event.getPlayer())) {
            event.setCancelled(true);
            SelectionManager.swapEditorMode(event.getPlayer());
        }
    }


    private void handleClick(PlayerInteractEvent event, EditorMode editorMode) {
        int slot = event.getPlayer().getInventory().getHeldItemSlot();
        Display sel = SelectionManager.getSelection(event.getPlayer());
        if (slot == 8) {
            SelectionManager.setEditorMode(event.getPlayer(), null);
            return;
        }
        boolean isLeftClick = event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK;
        if (slot == 7) {
            SelectionManager.swapEditorMode(event.getPlayer(), isLeftClick ? -1 : 1);
            SoundUtil.playSoundPageTurn(event.getPlayer());
            return;
        }
        boolean sneak = event.getPlayer().isSneaking();
        if (sel == null || !sel.isValid()) {
            if (editorMode == EditorMode.COPY_PASTE) {
                copyPasteHandleClick(event.getPlayer(), slot, isLeftClick, sel, sneak, editorMode);
                return;
            }
            selectNearest(event.getPlayer(), slot, isLeftClick, sel, sneak);
            return;
        }
        switch (editorMode) {
            case POSITION -> positionHandleClick(event.getPlayer(), slot, isLeftClick, sel, sneak, editorMode);
            case ROTATION -> rotationHandleClick(event.getPlayer(), slot, isLeftClick, sel, sneak, editorMode);
            case SCALE -> scaleHandleClick(event.getPlayer(), slot, isLeftClick, sel, sneak, editorMode);
            case SHADOW -> shadowHandleClick(event.getPlayer(), slot, isLeftClick, sel, sneak, editorMode);
            case ENTITY_SPECIFIC -> entitySpecificHandleClick(event.getPlayer(), slot, isLeftClick, sel, sneak, editorMode);
            case COPY_PASTE -> copyPasteHandleClick(event.getPlayer(), slot, isLeftClick, sel, sneak, editorMode);
        }
    }

    private void selectNearest(Player player, int slot, boolean isLeftClick, Display sel, boolean sneak) {
        Display target = null;
        for (Entity en : player.getNearbyEntities(C.DEFAULT_SELECT_RADIUS, C.DEFAULT_SELECT_RADIUS, C.DEFAULT_SELECT_RADIUS)) {
            if (en instanceof Display) {
                if (target == null)
                    target = (Display) en;
                else //select closest  //TODO check if is owner or may select/edit not owned
                    target = target.getLocation().distanceSquared(player.getLocation()) > en.getLocation().distanceSquared(player.getLocation()) ?
                            (Display) en : target;
            }
        }
        if (target == null) {
            //TODO feedback none near
            return;
        }
        SelectionManager.select(player, target);
    }

    private void copyPasteHandleClick(Player player, int slot, boolean isLeftClick, Display sel, boolean sneak, EditorMode editorMode) {
        if (slot == 0) {
            selectNearest(player, slot, isLeftClick, sel, sneak);
        }
        if (!Util.isVersionAfter(1, 20, 2))
            return;
        CopyPasteOption option = SelectionManager.getCopyPasteOption(player);
        switch (slot) {
            case 1 -> {
                Display disp = SelectionManager.getSelection(player);
                if (disp != null) {
                    option.copy(List.of(disp), player.getLocation());
                    SoundUtil.playSoundUIClick(player);
                    editorMode.setup(player);
                    Util.flashEntities(player, disp);
                } else {
                    SoundUtil.playSoundNo(player);
                }
            }
            case 2 -> {
                BoundingBox box = new BoundingBox().shift(player.getLocation()).expand(option.getCopyRadius());
                Collection<Entity> list = player.getWorld().getNearbyEntities(box, (en) -> !(en instanceof Player));
                if (!option.copy(list, player.getLocation())) {
                    SoundUtil.playSoundNo(player);
                    return;
                }
                SoundUtil.playSoundUIClick(player);
                editorMode.setup(player);
                Util.flashEntities(player, list);
            }
            case 3 -> {
                if (!option.paste(player.getLocation(), !sneak)) {
                    SoundUtil.playSoundNo(player);
                    return;
                }
                SoundUtil.playSoundUIClick(player);
                editorMode.setup(player);
                Util.flashEntities(player, option.getLastPasted());
            }
            case 4 -> {
                if (!option.undoPaste()) {
                    SoundUtil.playSoundNo(player);
                    return;
                }
                SoundUtil.playSoundUIClick(player);
                editorMode.setup(player);
            }
            case 5 -> {
                option.addYRotation((isLeftClick ? -1 : 1) * (sneak ? 5 : 90));
                SoundUtil.playSoundUIClick(player);
                editorMode.setup(player);
            }
        }
    }

    private void entitySpecificHandleClick(Player player, int slot, boolean isLeftClick, Display sel, boolean sneak, EditorMode editorMode) {
        if (sel instanceof TextDisplay display) {
            Color color = display.getBackgroundColor() == null ? Color.fromARGB(125, 125, 125, 125) : display.getBackgroundColor();
            switch (slot) {
                case 0 -> edit(display, player, () -> display.setBackgroundColor(color.setRed(Math.max(0, Math.min(255,
                        color.getRed() + (isLeftClick ? -1 : 1) * (sneak ? 1 : 16))))), editorMode);
                case 1 -> edit(display, player, () -> display.setBackgroundColor(color.setGreen(Math.max(0, Math.min(255,
                        color.getGreen() + (isLeftClick ? -1 : 1) * (sneak ? 1 : 16))))), editorMode);
                case 2 -> edit(display, player, () -> display.setBackgroundColor(color.setBlue(Math.max(0, Math.min(255,
                        color.getBlue() + (isLeftClick ? -1 : 1) * (sneak ? 1 : 16))))), editorMode);
                case 3 -> edit(display, player, () -> display.setBackgroundColor(color.setAlpha(Math.max(0, Math.min(255,
                        color.getAlpha() + (isLeftClick ? -1 : 1) * (sneak ? 1 : 16))))), editorMode);
                case 4 -> edit(display, player, () -> display.setAlignment(TextDisplay.TextAlignment.values()[(TextDisplay.TextAlignment.values().length
                        + display.getAlignment().ordinal() + (isLeftClick ? -1 : 1)) % TextDisplay.TextAlignment.values().length]), editorMode);
            }
            return;
        }
        if (sel instanceof BlockDisplay) {
            BlockData data = ((BlockDisplay) sel).getBlock();
            List<BlockDataInteractor> values = BlockDataUtil.getBlockDataValues(data);
            if (values.size() > slot)
                edit(sel, player, () -> values.get(slot).handleClick((BlockDisplay) sel, player, isLeftClick), editorMode);
            return;
        }
        if (sel instanceof ItemDisplay display) {
            switch (slot) {
                case 0 -> edit(display, player, () -> display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.values()[(ItemDisplay.ItemDisplayTransform.values().length
                        + display.getItemDisplayTransform().ordinal() + (isLeftClick ? -1 : 1)) % ItemDisplay.ItemDisplayTransform.values().length]), editorMode);

                case 1 -> edit(display, player, () -> {
                    ItemStack item = display.getItemStack();
                    if (item == null)
                        item = new ItemStack(Material.STONE);

                    if (item.getEnchantments().isEmpty())
                        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                    else
                        item.getEnchantments().keySet().forEach(item::removeEnchantment);
                    display.setItemStack(item);
                }, editorMode);
                case 2, 3, 4, 5, 6 -> edit(display, player, () -> {
                    ItemStack item = display.getItemStack();
                    if (item == null)
                        item = new ItemStack(Material.STONE);
                    ItemMeta meta = item.getItemMeta();
                    assert meta != null;
                    int value = meta.hasCustomModelData() ? meta.getCustomModelData() : 0;
                    value += (Math.pow(10, 2 * (slot - 2) + (sneak ? 1 : 0)) * (isLeftClick ? -1 : 1));
                    meta.setCustomModelData(value);
                    item.setItemMeta(meta);
                    display.setItemStack(item);
                }, editorMode);
            }
            return;
        }
    }

    private void shadowHandleClick(Player player, int slot, boolean isLeftClick, Display sel, boolean sneak, EditorMode mode) {
        switch (slot) {
            case 0:
                if (isLeftClick)
                    edit(sel, player, () -> {
                        if (sneak) {
                            sel.setBrightness(null);
                        } else {
                            Display.Brightness bright = sel.getBrightness();
                            if (bright == null)
                                bright = new Display.Brightness(0, 0);
                            sel.setBrightness(new Display.Brightness(bright.getBlockLight(), Math.max(0, bright.getSkyLight() - 1)));
                        }
                    }, mode);
                else
                    edit(sel, player, () -> {
                        if (sneak) {
                            sel.setBrightness(null);
                        } else {
                            Display.Brightness bright = sel.getBrightness();
                            if (bright == null)
                                bright = new Display.Brightness(0, 0);
                            sel.setBrightness(new Display.Brightness(bright.getBlockLight(), Math.min(15, bright.getSkyLight() + 1)));
                        }
                    }, mode);
                return;
            case 1:
                if (isLeftClick)
                    edit(sel, player, () -> {
                        if (sneak) {
                            sel.setBrightness(null);
                        } else {
                            Display.Brightness bright = sel.getBrightness();
                            if (bright == null)
                                bright = new Display.Brightness(0, 0);
                            sel.setBrightness(new Display.Brightness(Math.max(0, bright.getBlockLight() - 1), bright.getSkyLight()));
                        }
                    }, mode);
                else
                    edit(sel, player, () -> {
                        if (sneak) {
                            sel.setBrightness(null);
                        } else {
                            Display.Brightness bright = sel.getBrightness();
                            if (bright == null)
                                bright = new Display.Brightness(0, 0);
                            sel.setBrightness(new Display.Brightness(Math.min(15, bright.getBlockLight() + 1), bright.getSkyLight()));
                        }
                    }, mode);
                return;
            case 2:
                if (isLeftClick)
                    edit(sel, player, () -> {
                        float range = Math.min(C.MAX_VIEW_RANGE, Math.max(0, sel.getViewRange() * 64 + (sneak ? 1 : 4))) / 64;
                        sel.setViewRange(range);
                    }, mode);
                else
                    edit(sel, player, () -> {
                        float range = Math.min(C.MAX_VIEW_RANGE, Math.max(0, sel.getViewRange() * 64 - (sneak ? 1 : 4))) / 64;
                        sel.setViewRange(range);
                    }, mode);
                return;
        }
    }

    private void scaleHandleClick(Player player, int slot, boolean isLeftClick, Display sel, boolean sneak, EditorMode mode) {
        float scale = (float) ((isLeftClick ? -1 : 1) * (sneak ? C.SCALE_FINE : C.SCALE_COARSE));
        switch (slot) {
            case 4 -> edit(sel, player, () -> {
                Transformation transf = sel.getTransformation();
                transf.getScale().add(scale, scale, scale);
                sel.setTransformation(transf);
            }, mode);
            case 0 -> edit(sel, player, () -> {
                Transformation transf = sel.getTransformation();
                transf.getScale().add(scale, 0, 0);
                sel.setTransformation(transf);
            }, mode);
            case 1 -> edit(sel, player, () -> {
                Transformation transf = sel.getTransformation();
                transf.getScale().add(0, scale, 0);
                sel.setTransformation(transf);
            }, mode);
            case 2 -> edit(sel, player, () -> {
                Transformation transf = sel.getTransformation();
                transf.getScale().add(0, 0, scale);
                sel.setTransformation(transf);
            }, mode);
            case 6 -> edit(sel, player, () -> {
                Transformation transf = sel.getTransformation();
                transf.getScale().set(1, 1, 1);
                sel.setTransformation(transf);
            }, mode);
        }
    }

    private void rotationHandleClick(Player player, int slot, boolean isLeftClick, Display sel, boolean sneak, EditorMode mode) {
        float rotationDegrees = (float) ((isLeftClick ? -1 : 1) * (Math.PI / 180 * (sneak ? C.ROTATE_FINE : C.ROTATE_COARSE)));
        switch (slot) {
            case 0 -> edit(sel, player, () -> {
                Transformation transf = sel.getTransformation();
                Quaternionf rot = transf.getRightRotation();
                rot.rotateAxis(rotationDegrees, new Vector3f(1, 0, 0));
                sel.setTransformation(transf);
            }, mode);
            case 1 -> edit(sel, player, () -> {
                Transformation transf = sel.getTransformation();
                Quaternionf rot = transf.getRightRotation();
                rot.rotateAxis(rotationDegrees, new Vector3f(0, 1, 0));
                sel.setTransformation(transf);
            }, mode);
            case 2 -> edit(sel, player, () -> {
                Transformation transf = sel.getTransformation();
                Quaternionf rot = transf.getRightRotation();
                rot.rotateAxis(rotationDegrees, new Vector3f(0, 0, 1));
                sel.setTransformation(transf);
            }, mode);
            case 4 -> edit(sel, player, () -> sel.setBillboard(Display.Billboard.values()[(sel.getBillboard().ordinal()
                    + Display.Billboard.values().length + (isLeftClick ? -1 : 1)) % Display.Billboard.values().length]), mode);
            case 6 -> edit(sel, player, () -> {
                Transformation transf = sel.getTransformation();
                Quaternionf rot = transf.getRightRotation();
                rot.setAngleAxis(0, 0, 0, 0);
                sel.setTransformation(transf);
            }, mode);
        }
    }

    private void positionHandleClick(Player player, int slot, boolean isLeftClick, Display sel, boolean sneak, EditorMode mode) {
        double move = (isLeftClick ? -1 : 1) * (sneak ? C.MOVE_FINE : C.MOVE_COARSE);
        switch (slot) {
            case 0 -> edit(sel, player, () -> sel.teleport(sel.getLocation().add(move, 0, 0)), mode);
            case 1 -> edit(sel, player, () -> sel.teleport(sel.getLocation().add(0, move, 0)), mode);
            case 2 -> edit(sel, player, () -> sel.teleport(sel.getLocation().add(0, 0, move)), mode);
            case 4 -> {
                Location target = isLeftClick ? player.getEyeLocation() : player.getLocation();
                target.setYaw(sel.getLocation().getYaw());
                target.setPitch(sel.getLocation().getPitch());
                edit(sel, player, () -> sel.teleport(target), mode);
            }
            case 6 -> {
                Location loc = sel.getLocation();
                edit(sel, player, () -> sel.teleport(new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), loc.getYaw(), loc.getPitch())), mode);
            }
        }
    }

    private void edit(Display selection, Player player, Runnable consumer, EditorMode mode) {
        edit(selection, player, consumer, mode, false, true);
    }

    private void edit(Display selection, Player player, Runnable consumer, EditorMode mode, boolean bypassDistanceCheck, boolean reloadBar) {
        if (!bypassDistanceCheck && selection.getLocation().distanceSquared(player.getLocation()) > C.MAX_EDIT_RADIUS_SQUARED) {
            //TODO feedback
            return;
        }
        if (!player.isOp() && !player.getUniqueId().toString().equals(selection.getPersistentDataContainer().get(C.OWNER_KEY, PersistentDataType.STRING))) {
            //TODO
            return;
        }
        consumer.run();
        SoundUtil.playSoundUIClick(player);
        if (reloadBar)
            mode.setup(player);
    }


}
