package emanondev.displayeditor.selection;

import emanondev.displayeditor.C;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Objects;

public class EditorModeListener implements Listener {

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
        if (mode != null) {
            event.setCancelled(true);
            if (event.getHand() == EquipmentSlot.HAND)
                handleClick(event, mode);
        }

    }

    @EventHandler
    public void event(PlayerDeathEvent event) {
        if (SelectionManager.isOnEditorMode(event.getEntity()))
            SelectionManager.setEditorMode(event.getEntity(), null);
    }

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
        if (slot == 7) {
            switch (event.getAction()) {
                case LEFT_CLICK_AIR:
                case LEFT_CLICK_BLOCK:
                    SelectionManager.swapEditorMode(event.getPlayer(), -1);
                    return;
                case RIGHT_CLICK_AIR:
                case RIGHT_CLICK_BLOCK:
                    SelectionManager.swapEditorMode(event.getPlayer());
                    return;
            }
            return;
        }
        boolean sneak = event.getPlayer().isSneaking();
        boolean isLeftClick = event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK;
        if (sel == null || !sel.isValid()) {
            if (editorMode == EditorMode.COPY_PASTE) {
                copyPasteHandleClick(event.getPlayer(), slot, isLeftClick, sel, sneak, editorMode);
                return;
            }
            //TODO feedback
            return;
        }
        switch (editorMode) {
            case POSITION:
                positionHandleClick(event.getPlayer(), slot, isLeftClick, sel, sneak, editorMode);
                return;
            case ROTATION:
                rotationHandleClick(event.getPlayer(), slot, isLeftClick, sel, sneak, editorMode);
                return;
            case SCALE:
                scaleHandleClick(event.getPlayer(), slot, isLeftClick, sel, sneak, editorMode);
                return;
            case SHADOW:
                shadowHandleClick(event.getPlayer(), slot, isLeftClick, sel, sneak, editorMode);
                return;
            case ENTITY_SPECIFIC:
                entitySpecificHandleClick(event.getPlayer(), slot, isLeftClick, sel, sneak, editorMode);
                return;
            case COPY_PASTE:
                copyPasteHandleClick(event.getPlayer(), slot, isLeftClick, sel, sneak, editorMode);
                return;
        }
    }

    private void copyPasteHandleClick(Player player, int slot, boolean isLeftClick, Display sel, boolean sneak, EditorMode editorMode) {
        switch (slot) {
            case 0:

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
                if (target == null)
                    return;

                SelectionManager.select(player, target);
        }
    }

    private void entitySpecificHandleClick(Player player, int slot, boolean isLeftClick, Display sel, boolean sneak, EditorMode editorMode) {
        if (sel instanceof TextDisplay) {
            TextDisplay display = (TextDisplay) sel;
            Color color = display.getBackgroundColor() == null ? Color.fromARGB(125, 125, 125, 125) : display.getBackgroundColor();
            switch (slot) {
                case 0:
                    if (isLeftClick)
                        edit(display, player, () -> display
                                .setBackgroundColor(color.setRed(Math.max(0, Math.min(255, color.getRed() - (sneak ? 1 : 16))))), editorMode);
                    else
                        edit(display, player, () -> display
                                .setBackgroundColor(color.setRed(Math.max(0, Math.min(255, color.getRed() + (sneak ? 1 : 16))))), editorMode);
                    return;

                case 1:

                    if (isLeftClick)
                        edit(display, player, () -> display
                                .setBackgroundColor(color.setGreen(Math.max(0, Math.min(255, color.getGreen() - (sneak ? 1 : 16))))), editorMode);
                    else
                        edit(display, player, () -> display
                                .setBackgroundColor(color.setGreen(Math.max(0, Math.min(255, color.getGreen() + (sneak ? 1 : 16))))), editorMode);
                    return;

                case 2:
                    if (isLeftClick)
                        edit(display, player, () -> display
                                .setBackgroundColor(color.setBlue(Math.max(0, Math.min(255, color.getBlue() - (sneak ? 1 : 16))))), editorMode);
                    else
                        edit(display, player, () -> display
                                .setBackgroundColor(color.setBlue(Math.max(0, Math.min(255, color.getBlue() + (sneak ? 1 : 16))))), editorMode);
                    return;

                case 3:
                    if (isLeftClick)
                        edit(display, player, () -> display
                                .setBackgroundColor(color.setAlpha(Math.max(0, Math.min(255, color.getAlpha() - (sneak ? 1 : 16))))), editorMode);
                    else
                        edit(display, player, () -> display
                                .setBackgroundColor(color.setAlpha(Math.max(0, Math.min(255, color.getAlpha() + (sneak ? 1 : 16))))), editorMode);
                    return;

                case 4:
                    if (isLeftClick)
                        edit(display, player, () -> display.setAlignment(
                                TextDisplay.TextAlignment.values()[(TextDisplay.TextAlignment.values().length
                                        + display.getAlignment().ordinal() - 1) % TextDisplay.TextAlignment.values().length]), editorMode);
                    else
                        edit(display, player, () -> display.setAlignment(
                                TextDisplay.TextAlignment.values()[(display.getAlignment().ordinal() + 1) % TextDisplay.TextAlignment.values().length]), editorMode);
                    return;

            }
            return;
        }
        if (sel instanceof BlockDisplay) {
            //TODO
            return;
        }
        if (sel instanceof ItemDisplay) {
            ItemDisplay display = (ItemDisplay) sel;
            switch (slot) {
                case 0:
                    if (isLeftClick)
                        edit(display, player, () -> display.setItemDisplayTransform(
                                ItemDisplay.ItemDisplayTransform.values()[(ItemDisplay.ItemDisplayTransform.values().length
                                        + display.getItemDisplayTransform().ordinal() - 1) % ItemDisplay.ItemDisplayTransform.values().length]), editorMode);
                    else
                        edit(display, player, () -> display.setItemDisplayTransform(
                                ItemDisplay.ItemDisplayTransform.values()[(display.getItemDisplayTransform().ordinal() + 1) % ItemDisplay.ItemDisplayTransform.values().length]), editorMode);
                    return;
                case 1:
                    edit(display, player, () -> {
                        ItemStack item = display.getItemStack();
                        if (item == null)
                            item = new ItemStack(Material.STONE);

                        if (item.getEnchantments().isEmpty())
                            item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                        else
                            item.getEnchantments().keySet().forEach(item::removeEnchantment);
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
        switch (slot) {
            case 0:
                if (isLeftClick)
                    edit(sel, player, () -> {
                        Transformation transf = sel.getTransformation();
                        transf.getScale().add(sneak ? -0.025F : -0.5F, sneak ? -0.025F : -0.5F, sneak ? -0.025F : -0.5F);
                        sel.setTransformation(transf);
                    }, mode);
                else
                    edit(sel, player, () -> {
                        Transformation transf = sel.getTransformation();
                        transf.getScale().add(sneak ? 0.025F : 0.5F, sneak ? 0.025F : 0.5F, sneak ? 0.025F : 0.5F);
                        sel.setTransformation(transf);
                    }, mode);
                return;
            case 1:
                if (isLeftClick)
                    edit(sel, player, () -> {
                        Transformation transf = sel.getTransformation();
                        transf.getScale().add(sneak ? -0.025F : -0.5F, 0, 0);
                        sel.setTransformation(transf);
                    }, mode);
                else
                    edit(sel, player, () -> {
                        Transformation transf = sel.getTransformation();
                        transf.getScale().add(sneak ? 0.025F : 0.5F, 0, 0);
                        sel.setTransformation(transf);
                    }, mode);
                return;
            case 2:
                if (isLeftClick)
                    edit(sel, player, () -> {
                        Transformation transf = sel.getTransformation();
                        transf.getScale().add(0, sneak ? -0.025F : -0.5F, 0);
                        sel.setTransformation(transf);
                    }, mode);
                else
                    edit(sel, player, () -> {
                        Transformation transf = sel.getTransformation();
                        transf.getScale().add(0, sneak ? 0.025F : 0.5F, 0);
                        sel.setTransformation(transf);
                    }, mode);
                return;
            case 3:
                if (isLeftClick)
                    edit(sel, player, () -> {
                        Transformation transf = sel.getTransformation();
                        transf.getScale().add(0, 0, sneak ? -0.025F : -0.5F);
                        sel.setTransformation(transf);
                    }, mode);
                else
                    edit(sel, player, () -> {
                        Transformation transf = sel.getTransformation();
                        transf.getScale().add(0, 0, sneak ? 0.025F : 0.5F);
                        sel.setTransformation(transf);
                    }, mode);
                return;
            case 6:
                edit(sel, player, () -> {
                    Transformation transf = sel.getTransformation();
                    transf.getScale().set(1, 1, 1);
                    sel.setTransformation(transf);
                }, mode);
                return;
        }
    }

    private void rotationHandleClick(Player player, int slot, boolean isLeftClick, Display sel, boolean sneak, EditorMode mode) {
        switch (slot) {
            case 0:
                if (isLeftClick)
                    edit(sel, player, () -> {
                        Transformation transf = sel.getTransformation();
                        Quaternionf rot = transf.getRightRotation();
                        rot.rotateAxis((float) (-Math.PI / (sneak ? 64 : 8)), new Vector3f(1, 0, 0));
                        sel.setTransformation(transf);
                    }, mode);
                else
                    edit(sel, player, () -> {
                        Transformation transf = sel.getTransformation();
                        Quaternionf rot = transf.getRightRotation();
                        rot.rotateAxis((float) (Math.PI / (sneak ? 64 : 8)), new Vector3f(1, 0, 0));
                        sel.setTransformation(transf);
                    }, mode);
                return;
            case 1:
                if (isLeftClick)
                    edit(sel, player, () -> {
                        Transformation transf = sel.getTransformation();
                        Quaternionf rot = transf.getRightRotation();
                        rot.rotateAxis((float) (-Math.PI / (sneak ? 64 : 8)), new Vector3f(0, 1, 0));
                        sel.setTransformation(transf);
                    }, mode);
                else
                    edit(sel, player, () -> {
                        Transformation transf = sel.getTransformation();
                        Quaternionf rot = transf.getRightRotation();
                        rot.rotateAxis((float) (Math.PI / (sneak ? 64 : 8)), new Vector3f(0, 1, 0));
                        sel.setTransformation(transf);
                    }, mode);
                return;
            case 2:
                if (isLeftClick)
                    edit(sel, player, () -> {
                        Transformation transf = sel.getTransformation();
                        Quaternionf rot = transf.getRightRotation();
                        rot.rotateAxis((float) (-Math.PI / (sneak ? 64 : 8)), new Vector3f(0, 0, 1));
                        sel.setTransformation(transf);
                    }, mode);
                else
                    edit(sel, player, () -> {
                        Transformation transf = sel.getTransformation();
                        Quaternionf rot = transf.getRightRotation();
                        rot.rotateAxis((float) (Math.PI / (sneak ? 64 : 8)), new Vector3f(0, 0, 1));
                        sel.setTransformation(transf);
                    }, mode);
                return;
            case 3:
                if (isLeftClick)
                    edit(sel, player, () -> sel.setBillboard(Display.Billboard.values()[(sel.getBillboard().ordinal()
                            + Display.Billboard.values().length - 1) % Display.Billboard.values().length]), mode);
                else
                    edit(sel, player, () -> sel.setBillboard(Display.Billboard.values()[(sel.getBillboard().ordinal()
                            + Display.Billboard.values().length + 1) % Display.Billboard.values().length]), mode);
                return;
            case 6:
                edit(sel, player, () -> {
                    Transformation transf = sel.getTransformation();
                    Quaternionf rot = transf.getRightRotation();
                    rot.setAngleAxis(0, 0, 0, 0);
                    sel.setTransformation(transf);
                }, mode);
                return;
        }
    }

    private void positionHandleClick(Player player, int slot, boolean isLeftClick, Display sel, boolean sneak, EditorMode mode) {
        switch (slot) {
            case 0:
                if (isLeftClick)
                    edit(sel, player, () -> sel.teleport(sel.getLocation().add(sneak ? -0.05 : -0.5, 0, 0)), mode);
                else
                    edit(sel, player, () -> sel.teleport(sel.getLocation().add(sneak ? 0.05 : 0.5, 0, 0)), mode);
                return;
            case 1:
                if (isLeftClick)
                    edit(sel, player, () -> sel.teleport(sel.getLocation().add(0, sneak ? -0.05 : -0.5, 0)), mode);
                else
                    edit(sel, player, () -> sel.teleport(sel.getLocation().add(0, sneak ? 0.05 : 0.5, 0)), mode);
                return;
            case 2:
                if (isLeftClick)
                    edit(sel, player, () -> sel.teleport(sel.getLocation().add(0, 0, sneak ? -0.05 : -0.5)), mode);
                else
                    edit(sel, player, () -> sel.teleport(sel.getLocation().add(0, 0, sneak ? 0.05 : 0.5)), mode);
                return;
            case 3:
                Location target = isLeftClick ? player.getEyeLocation() : player.getLocation();
                target.setYaw(sel.getLocation().getYaw());
                target.setPitch(sel.getLocation().getPitch());
                edit(sel, player, () -> sel.teleport(target), mode);
                return;
            case 6:
                Location loc = sel.getLocation();
                edit(sel, player, () -> sel.teleport(new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), loc.getYaw(), loc.getPitch())), mode);
                return;
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
        if (reloadBar)
            mode.setup(player);
    }


}
