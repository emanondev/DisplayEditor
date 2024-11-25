package emanondev.displayeditor.properties;

import emanondev.displayeditor.properties.impl.*;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTables;
import org.bukkit.loot.Lootable;
import org.bukkit.material.Colorable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class EntityProperties {
    
    
        /*
        Firework
        Display
        ArmorStand
        AreaCloudEffect
         */

        /* since 1.21.1
        RIPTIDING(LivingEntity.class,
                (Entity e) -> ((LivingEntity) e).isRiptiding(),
                LivingEntity::setRiptiding, false),
*/

    //TODO BUGGED with Invisibility effect liv.setInvisible();
    //   liv.setLeashHolder()
    //    liv.setMemory();
    //TODO ENTITY metadata

    public static final Property<Goat, Boolean> GOAT_LEFT_HORN = new BooleanProperty<>(
            "GOAT_LEFT_HORN", Goat.class,
            Goat::hasLeftHorn, Goat::setLeftHorn, () -> true);
    public static final Property<Goat, Boolean> GOAT_RIGHT_HORN = new BooleanProperty<>(
            "GOAT_RIGHT_HORN", Goat.class,
            Goat::hasRightHorn, Goat::setRightHorn, () -> true);
    public static final Property<Goat, Boolean> GOAT_SCREAMING = new BooleanProperty<>(
            "GOAT_SCREAMING", Goat.class,
            Goat::isScreaming, Goat::setScreaming, () -> false);
    public static final Property<Guardian, Boolean> GUARDIAN_LASER_ACTIVATED = new BooleanProperty<>(
            "GUARDIAN_LASER_ACTIVATED", Guardian.class,
            Guardian::hasLaser, Guardian::setLaser, () -> false);
    public static final Property<Guardian, Integer> GUARDIAN_LASER_TICKS = NumberProperty.fromInt(
            "GUARDIAN_LASER_TICKS", Guardian.class,
            Guardian::getLaserTicks, Guardian::setLaserTicks, () -> 0);
    public static final Property<Hoglin, Integer> HOGLIN_CONVERSION_TIME = NumberProperty.fromInt(
            "HOGLIN_CONVERSION_TIME", Hoglin.class,
            (e) -> e.isConverting() ? e.getConversionTime() : -1, Hoglin::setConversionTime, () -> -1);
    public static final Property<Hoglin, Boolean> HOGLIN_IMMUNE_TO_ZOMBIFICATION = new BooleanProperty<>(
            "HOGLIN_IMMUNE_TO_ZOMBIFICATION", Hoglin.class,
            Hoglin::isImmuneToZombification, Hoglin::setImmuneToZombification, () -> false);
    public static final Property<Hoglin, Boolean> HOGLIN_ABLE_TO_BE_HUNTED = new BooleanProperty<>(
            "HOGLIN_ABLE_TO_BE_HUNTED", Hoglin.class,
            Hoglin::isAbleToBeHunted, Hoglin::setIsAbleToBeHunted, () -> true);
    public static final Property<HopperMinecart, Boolean> HOPPERMINECART_ENABLED = new BooleanProperty<>(
            "HOPPERMINECART_ENABLED", HopperMinecart.class,
            HopperMinecart::isEnabled, HopperMinecart::setEnabled, () -> true);
    public static final Property<Horse, Horse.Color> HORSE_COLOR = new EnumProperty<>(
            "HORSE_COLOR", Horse.class, Horse.Color.class,
            Horse::getColor, Horse::setColor, () -> Horse.Color.BROWN);
    public static final Property<Horse, Horse.Style> HORSE_STYLE = new EnumProperty<>(
            "HORSE_STYLE", Horse.class, Horse.Style.class,
            Horse::getStyle, Horse::setStyle, () -> Horse.Style.NONE);
    public static final Property<Interaction, Float> INTERACTION_HEIGHT = NumberProperty.fromFloat(
            "INTERACTION_HEIGHT", Interaction.class,
            Interaction::getInteractionHeight, Interaction::setInteractionHeight, () -> 1.0f);
    public static final Property<Interaction, Float> INTERACTION_WIDTH = NumberProperty.fromFloat(
            "INTERACTION_WIDTH", Interaction.class,
            Interaction::getInteractionWidth, Interaction::setInteractionWidth, () -> 1.0f);
    public static final Property<Interaction, Boolean> INTERACTION_RESPONSIVE = new BooleanProperty<>(
            "INTERACTION_RESPONSIVE", Interaction.class,
            Interaction::isResponsive, Interaction::setResponsive, () -> true);
    public static final Property<IronGolem, Boolean> IRONGOLEM_PLAYER_CREATED = new BooleanProperty<>(
            "IRONGOLEM_PLAYER_CREATED", IronGolem.class,
            IronGolem::isPlayerCreated, IronGolem::setPlayerCreated, () -> false);
    public static final Property<Item, ItemStack> ITEM_ITEM_STACK = new ConfSerProperty<>(
            "ITEM_ITEM_STACK", Item.class, ItemStack.class,
            Item::getItemStack, Item::setItemStack, () -> new ItemStack(Material.STONE));
    public static final Property<Item, UUID> ITEM_OWNER = new UuidProperty<>(
            "ITEM_OWNER", Item.class,
            Item::getOwner, Item::setOwner);
    public static final Property<Item, Integer> ITEM_PICKUP_DELAY = NumberProperty.fromInt(
            "ITEM_PICKUP_DELAY", Item.class,
            Item::getPickupDelay, Item::setPickupDelay, () -> 0);
    public static final Property<Item, UUID> ITEM_THROWER = new UuidProperty<>(
            "ITEM_THROWER", Item.class,
            Item::getThrower, Item::setThrower);
    public static final Property<Item, Boolean> ITEM_UNLIMITED_LIFETIME = new BooleanProperty<>(
            "ITEM_UNLIMITED_LIFETIME", Item.class,
            Item::isUnlimitedLifetime, Item::setUnlimitedLifetime, () -> false);
    public static final Property<ItemDisplay, ItemDisplay.ItemDisplayTransform> ITEMDISPLAY_TRANSFORM = new EnumProperty<>(
            "ITEMDISPLAY_TRANSFORM", ItemDisplay.class, ItemDisplay.ItemDisplayTransform.class,
            ItemDisplay::getItemDisplayTransform, ItemDisplay::setItemDisplayTransform,
            () -> ItemDisplay.ItemDisplayTransform.NONE);
    public static final Property<ItemDisplay, ItemStack> ITEMDISPLAY_ITEM_STACK = new ConfSerProperty<>(
            "ITEMDISPLAY_ITEM_STACK", ItemDisplay.class, ItemStack.class,
            ItemDisplay::getItemStack, ItemDisplay::setItemStack, () -> new ItemStack(Material.STONE));
    public static final Property<LightningStrike, Integer> LIGHTNINGSTRIKE_FLASHES = NumberProperty.fromInt(
            "LIGHTNINGSTRIKE_FLASHES", LightningStrike.class,
            LightningStrike::getFlashes, LightningStrike::setFlashes, () -> 0);
    public static final Property<LightningStrike, Integer> LIGHTNINGSTRIKE_LIFE_TICKS = NumberProperty.fromInt(
            "LIGHTNINGSTRIKE_LIFE_TICKS", LightningStrike.class,
            LightningStrike::getLifeTicks, LightningStrike::setLifeTicks, () -> 0);
    public static final Property<Llama, Llama.Color> LLAMA_COLOR = new EnumProperty<>(
            "LLAMA_COLOR", Llama.class, Llama.Color.class,
            Llama::getColor, Llama::setColor, () -> Llama.Color.WHITE);
    public static final Property<Llama, Integer> LLAMA_STRENGTH = NumberProperty.fromInt(
            "LLAMA_STRENGTH", Llama.class,
            Llama::getStrength, Llama::setStrength, () -> 3);
    public static final Property<Minecart, Double> MINECART_DAMAGE = NumberProperty.fromDouble(
            "MINECART_DAMAGE", Minecart.class,
            Minecart::getDamage, Minecart::setDamage, () -> 0D);
    public static final Property<Minecart, Vector> MINECART_DERAILED_VELOCITY_MOD = new ConfSerProperty<>(
            "MINECART_DERAILED_VELOCITY_MOD", Minecart.class, Vector.class,
            Minecart::getDerailedVelocityMod, Minecart::setDerailedVelocityMod, () -> new Vector(0.75, 0.75, 0.75)
    );
    public static final Property<Minecart, String> MINECART_DISPLAY_BLOCK_DATA = new StringProperty<>(
            "MINECART_DISPLAY_BLOCK_DATA", Minecart.class,
            (Minecart e) -> e.getDisplayBlockData().getAsString(),
            (Minecart e, String v) -> e.setDisplayBlockData(v == null ? null : Bukkit.createBlockData(v)),
            () -> null);
    public static final Property<Minecart, Integer> MINECART_DISPLAY_BLOCK_OFFSET = NumberProperty.fromInt(
            "MINECART_DISPLAY_BLOCK_OFFSET", Minecart.class,
            Minecart::getDisplayBlockOffset, Minecart::setDisplayBlockOffset, () -> 0);
    public static final Property<Minecart, Vector> MINECART_FLYING_VELOCITY_MOD = new ConfSerProperty<>(
            "MINECART_FLYING_VELOCITY_MOD", Minecart.class, Vector.class,
            Minecart::getFlyingVelocityMod, Minecart::setFlyingVelocityMod, () -> new Vector(0.95, 1.0, 0.95));
    public static final Property<Minecart, Double> MINECART_MAX_SPEED = NumberProperty.fromDouble(
            "MINECART_MAX_SPEED", Minecart.class,
            Minecart::getMaxSpeed, Minecart::setMaxSpeed, () -> 0.4);
    public static final Property<Minecart, Boolean> MINECART_SLOW_WHEN_EMPTY = new BooleanProperty<>(
            "MINECART_SLOW_WHEN_EMPTY", Minecart.class,
            Minecart::isSlowWhenEmpty, Minecart::setSlowWhenEmpty, () -> true);
    public static final Property<Mob, Boolean> MOB_AWARE = new BooleanProperty<>(
            "MOB_AWARE", Mob.class,
            Mob::isAware, Mob::setAware, () -> true);
    public static final Property<Mob, UUID> MOB_TARGET = new UuidProperty<>(
            "MOB_TARGET", Mob.class,
            (Mob mob) -> mob.getTarget() == null ? null : mob.getTarget().getUniqueId(),
            (Mob mob, UUID value) -> {
                Entity target = Bukkit.getEntity(value);
                mob.setTarget(target instanceof LivingEntity liv ? liv : null);
            });
    public static final Property<MushroomCow, MushroomCow.Variant> MUSHROOMCOW_VARIANT = new EnumProperty<>(
            "MUSHROOMCOW_VARIANT", MushroomCow.class, MushroomCow.Variant.class,
            MushroomCow::getVariant, MushroomCow::setVariant, () -> MushroomCow.Variant.RED);
    public static final Property<Ocelot, Boolean> OCELOT_TRUSTING = new BooleanProperty<>(
            "OCELOT_TRUSTING", Ocelot.class,
            Ocelot::isTrusting, Ocelot::setTrusting, () -> false);
    @SuppressWarnings("UnstableApiUsage")
    public static final Property<OminousItemSpawner, ItemStack> OMINOUS_ITEM = new ConfSerProperty<>(
            "OMINOUS_ITEM", OminousItemSpawner.class, ItemStack.class,
            OminousItemSpawner::getItem, OminousItemSpawner::setItem, () -> null);
    @SuppressWarnings("UnstableApiUsage")
    public static final Property<OminousItemSpawner, Long> OMINOUS_SPAWN_TICKS = NumberProperty.fromLong(
            "OMINOUS_SPAWN_TICKS", OminousItemSpawner.class,
            OminousItemSpawner::getSpawnItemAfterTicks, OminousItemSpawner::setSpawnItemAfterTicks, () -> 20L);
    public static final Property<Painting, Art> PAINTING_ART = new KeyedProperty<>(
            "PAINTING_ART", Painting.class,
            Painting::getArt, Painting::setArt, () -> Art.KEBAB, Registry.ART);
    public static final Property<Panda, Boolean> PANDA_EATING = new BooleanProperty<>(
            "PANDA_EATING", Panda.class,
            Panda::isEating, Panda::setEating, () -> false);
    public static final Property<Panda, Panda.Gene> PANDA_HIDDEN_GENE = new EnumProperty<>(
            "PANDA_HIDDEN_GENE", Panda.class, Panda.Gene.class,
            Panda::getHiddenGene, Panda::setHiddenGene, () -> Panda.Gene.NORMAL);
    public static final Property<Panda, Panda.Gene> PANDA_MAIN_GENE = new EnumProperty<>(
            "PANDA_MAIN_GENE", Panda.class, Panda.Gene.class,
            Panda::getMainGene, Panda::setMainGene, () -> Panda.Gene.NORMAL);
    public static final Property<Panda, Boolean> PANDA_ON_BACK = new BooleanProperty<>(
            "PANDA_ON_BACK", Panda.class,
            Panda::isOnBack, Panda::setOnBack, () -> false);
    public static final Property<Panda, Boolean> PANDA_ROLLING = new BooleanProperty<>(
            "PANDA_ROLLING", Panda.class,
            Panda::isRolling, Panda::setRolling, () -> false);
    public static final Property<Panda, Boolean> PANDA_SNEEZING = new BooleanProperty<>(
            "PANDA_SNEEZING", Panda.class,
            Panda::isSneezing, Panda::setSneezing, () -> false);
    public static final Property<Parrot, Parrot.Variant> PARROT_VARIANT = new EnumProperty<>(
            "PARROT_VARIANT", Parrot.class, Parrot.Variant.class,
            Parrot::getVariant, Parrot::setVariant, () -> Parrot.Variant.RED);
    public static final Property<Phantom, Integer> PHANTOM_SIZE = NumberProperty.fromInt(
            "PHANTOM_SIZE", Phantom.class,
            Phantom::getSize, Phantom::setSize, () -> 5);
    public static final Property<Piglin, Boolean> PIGLIN_ABLE_TO_HUNT = new BooleanProperty<>(
            "PIGLIN_ABLE_TO_HUNT", Piglin.class,
            Piglin::isAbleToHunt, Piglin::setIsAbleToHunt, () -> true);
    public static final Property<PiglinAbstract, Integer> ABSTRACTPIGLIN_CONVERSION_TIME = NumberProperty.fromInt(
            "ABSTRACTPIGLIN_CONVERSION_TIME", PiglinAbstract.class,
            (PiglinAbstract p) -> p.isConverting() ? p.getConversionTime() : -1,
            PiglinAbstract::setConversionTime, () -> -1);
    public static final Property<PiglinAbstract, Boolean> ABSTRACTPIGLIN_IMMUNE_TO_ZOMBIFICATION = new BooleanProperty<>(
            "ABSTRACTPIGLIN_IMMUNE_TO_ZOMBIFICATION", PiglinAbstract.class,
            PiglinAbstract::isImmuneToZombification, PiglinAbstract::setImmuneToZombification, () -> false);
    public static final Property<PigZombie, Integer> PIGZOMBIE_ANGER_LEVEL = NumberProperty.fromInt(
            "PIGZOMBIE_ANGER_LEVEL", PigZombie.class,
            PigZombie::getAnger, PigZombie::setAnger, () -> 0);
    public static final Property<PigZombie, Boolean> PIGZOMBIE_ANGRY = new BooleanProperty<>(
            "PIGZOMBIE_ANGRY", PigZombie.class,
            PigZombie::isAngry, PigZombie::setAngry, () -> false);
    public static final Property<PoweredMinecart, Integer> POWERED_MINECART_FUEL = NumberProperty.fromInt(
            "POWERED_MINECART_FUEL", PoweredMinecart.class,
            PoweredMinecart::getFuel, PoweredMinecart::setFuel, () -> 0);
    public static final Property<PufferFish, Integer> PUFFERFISH_PUFF_STATE = NumberProperty.fromInt(
            "PUFFERFISH_PUFF_STATE", PufferFish.class,
            PufferFish::getPuffState, PufferFish::setPuffState, () -> 0);
    public static final Property<Rabbit, Rabbit.Type> RABBIT_TYPE = new EnumProperty<>(
            "RABBIT_TYPE", Rabbit.class, Rabbit.Type.class,
            Rabbit::getRabbitType, Rabbit::setRabbitType, () -> Rabbit.Type.BROWN);
    public static final Property<Raider, Boolean> RAIDER_CAN_JOIN_RAID = new BooleanProperty<>(
            "RAIDER_CAN_JOIN_RAID", Raider.class,
            Raider::isCanJoinRaid, Raider::setCanJoinRaid, () -> true);
    public static final Property<Raider, Boolean> RAIDER_CELEBRATING = new BooleanProperty<>(
            "RAIDER_CELEBRATING", Raider.class,
            Raider::isCelebrating, Raider::setCelebrating, () -> false);
    public static final Property<Raider, Boolean> RAIDER_PATROL_LEADER = new BooleanProperty<>(
            "RAIDER_PATROL_LEADER", Raider.class,
            Raider::isPatrolLeader, Raider::setPatrolLeader, () -> false);
    public static final Property<Raider, Location> RAIDER_PATROL_TARGET = new ConfSerProperty<>(
            "RAIDER_PATROL_TARGET", Raider.class, Location.class,
            (Raider r) -> r.getPatrolTarget() == null ? null : r.getLocation(),
            (Raider r, Location l) -> r.setPatrolTarget(l == null || !l.isWorldLoaded() ? null : l.getBlock()), () -> null);
    public static final Property<Colorable, DyeColor> COLORABLE_COLOR = new EnumProperty<>(
            "COLORABLE_COLOR", Colorable.class, DyeColor.class,
            Colorable::getColor, Colorable::setColor, () -> DyeColor.WHITE);
    public static final Property<Shearable, Boolean> SHEARABLE_SHEARED = new BooleanProperty<>(
            "SHEARABLE_SHEARED", Shearable.class,
            Shearable::isSheared, Shearable::setSheared, () -> false);
    public static final Property<Shulker, BlockFace> SHULKER_ATTACHED_FACE = new EnumProperty<>(
            "SHULKER_ATTACHED_FACE", Shulker.class, BlockFace.class,
            Shulker::getAttachedFace, Shulker::setAttachedFace, () -> BlockFace.UP);
    public static final Property<Shulker, Float> SHULKER_PEEK = NumberProperty.fromFloat(
            "SHULKER_PEEK", Shulker.class,
            Shulker::getPeek, Shulker::setPeek, () -> 0F);
    public static final Property<ShulkerBullet, UUID> SHULKERBULLET_TARGET = new UuidProperty<>(
            "SHULKERBULLET_TARGET", ShulkerBullet.class,
            (ShulkerBullet s) -> s.getTarget() == null ? null : s.getTarget().getUniqueId(),
            (ShulkerBullet s, UUID uuid) -> s.setTarget(Bukkit.getEntity(uuid)));
    public static final Property<SizedFireball, ItemStack> SIZEDFIREBALL_DISPLAY_ITEM = new ConfSerProperty<>(
            "SIZEDFIREBALL_DISPLAY_ITEM", SizedFireball.class, ItemStack.class,
            SizedFireball::getDisplayItem, SizedFireball::setDisplayItem, () -> new ItemStack(Material.FIRE_CHARGE));
    public static final Property<Skeleton, Integer> SKELETON_CONVERSION_TIME = NumberProperty.fromInt(
            "SKELETON_CONVERSION_TIME", Skeleton.class,
            (Skeleton s) -> s.isConverting() ? s.getConversionTime() : -1, Skeleton::setConversionTime, () -> -1);
    public static final Property<SkeletonHorse, Boolean> SKELETONHORSE_TRAPPED = new BooleanProperty<>(
            "SKELETONHORSE_TRAPPED", SkeletonHorse.class,
            SkeletonHorse::isTrapped, SkeletonHorse::setTrapped, () -> false);
    public static final Property<SkeletonHorse, Integer> SKELETONHORSE_TRAP_TIME = NumberProperty.fromInt(
            "SKELETONHORSE_TRAP_TIME", SkeletonHorse.class,
            SkeletonHorse::getTrapTime, SkeletonHorse::setTrapTime, () -> 0);
    public static final Property<Slime, Integer> SLIME_SIZE = NumberProperty.fromInt(
            "SLIME_SIZE", Slime.class,
            Slime::getSize, Slime::setSize, () -> 1);
    public static final Property<Sniffer, Sniffer.State> SNIFFER_STATE = new EnumProperty<>(
            "SNIFFER_STATE", Sniffer.class, Sniffer.State.class,
            Sniffer::getState, Sniffer::setState, () -> Sniffer.State.IDLING);
    public static final Property<Snowman, Boolean> SNOWMAN_DERP = new BooleanProperty<>(
            "SNOWMAN_DERP", Snowman.class,
            Snowman::isDerp, Snowman::setDerp, () -> false);
    public static final Property<SpectralArrow, Integer> SPECTRALARROW_GLOWING_TICKS = NumberProperty.fromInt(
            "SPECTRALARROW_GLOWING_TICKS", SpectralArrow.class,
            SpectralArrow::getGlowingTicks, SpectralArrow::setGlowingTicks, () -> 60);
    public static final Property<Spellcaster, Spellcaster.Spell> SPELLCASTER_SPELL = new EnumProperty<>(
            "SPELLCASTER_SPELL", Spellcaster.class, Spellcaster.Spell.class,
            Spellcaster::getSpell, Spellcaster::setSpell, () -> Spellcaster.Spell.NONE);
    public static final Property<Steerable, Integer> STEERABLE_BOOST_TICKS = NumberProperty.fromInt(
            "STEERABLE_BOOST_TICKS", Steerable.class,
            Steerable::getBoostTicks, Steerable::setBoostTicks, () -> 0);
    public static final Property<Steerable, Integer> STEERABLE_CURRENT_BOOST_TICKS = NumberProperty.fromInt(
            "STEERABLE_CURRENT_BOOST_TICKS", Steerable.class,
            Steerable::getCurrentBoostTicks, Steerable::setCurrentBoostTicks, () -> 0);
    public static final Property<Steerable, Boolean> STEERABLE_SADDLED = new BooleanProperty<>(
            "STEERABLE_SADDLED", Steerable.class,
            Steerable::hasSaddle, Steerable::setSaddle, () -> false);
    public static final Property<Strider, Boolean> STRIDER_SHIVERING = new BooleanProperty<>(
            "STRIDER_SHIVERING", Strider.class,
            Strider::isShivering, Strider::setShivering, () -> false);
    public static final Property<Tadpole, Integer> TADPOLE_AGE = NumberProperty.fromInt(
            "TADPOLE_AGE", Tadpole.class,
            Tadpole::getAge, Tadpole::setAge, () -> 0);
    public static final Property<Tameable, UUID> TAMEABLE_OWNER = new UuidProperty<>(
            "TAMEABLE_OWNER", Tameable.class,
            (Tameable t) -> t.getOwner() == null ? null : t.getOwner().getUniqueId(),
            (Tameable t, UUID uuid) -> t.setOwner(uuid == null ? null : Bukkit.getOfflinePlayer(uuid)));
    public static final Property<Tameable, Boolean> TAMEABLE_TAMED = new BooleanProperty<>(
            "TAMEABLE_TAMED", Tameable.class,
            Tameable::isTamed, Tameable::setTamed, () -> false);
    public static final Property<TextDisplay, TextDisplay.TextAlignment> TEXTDISPLAY_ALIGNMENT = new EnumProperty<>(
            "TEXTDISPLAY_ALIGNMENT", TextDisplay.class, TextDisplay.TextAlignment.class,
            TextDisplay::getAlignment, TextDisplay::setAlignment, () -> TextDisplay.TextAlignment.CENTER);
    public static final Property<TextDisplay, Color> TEXTDISPLAY_BACKGROUND_COLOR = new ConfSerProperty<>(
            "TEXTDISPLAY_BACKGROUND_COLOR", TextDisplay.class, Color.class,
            TextDisplay::getBackgroundColor, TextDisplay::setBackgroundColor,
            () -> Color.fromARGB(0, 0, 0, 0));
    public static final Property<TextDisplay, Boolean> TEXTDISPLAY_DEFAULT_BACKGROUND = new BooleanProperty<>(
            "TEXTDISPLAY_DEFAULT_BACKGROUND", TextDisplay.class,
            TextDisplay::isDefaultBackground, TextDisplay::setDefaultBackground, () -> true);
    public static final Property<TextDisplay, Integer> TEXTDISPLAY_LINE_WIDTH = NumberProperty.fromInt(
            "TEXTDISPLAY_LINE_WIDTH", TextDisplay.class,
            TextDisplay::getLineWidth, TextDisplay::setLineWidth, () -> 100);
    public static final Property<TextDisplay, Boolean> TEXTDISPLAY_SEE_THROUGH = new BooleanProperty<>(
            "TEXTDISPLAY_SEE_THROUGH", TextDisplay.class,
            TextDisplay::isSeeThrough, TextDisplay::setSeeThrough, () -> false);
    public static final Property<TextDisplay, Boolean> TEXTDISPLAY_SHADOWED = new BooleanProperty<>(
            "TEXTDISPLAY_SHADOWED", TextDisplay.class,
            TextDisplay::isShadowed, TextDisplay::setShadowed, () -> false);
    public static final Property<TextDisplay, String> TEXTDISPLAY_TEXT = new StringProperty<>(
            "TEXTDISPLAY_TEXT", TextDisplay.class,
            TextDisplay::getText, TextDisplay::setText, () -> null);
    public static final Property<TextDisplay, Byte> TEXTDISPLAY_TEXT_OPACITY = NumberProperty.fromByte(
            "TEXTDISPLAY_TEXT_OPACITY", TextDisplay.class,
            TextDisplay::getTextOpacity, TextDisplay::setTextOpacity, () -> (byte) 255);
    public static final Property<ThrowableProjectile, ItemStack> THROWABLEPROJECTILE_ITEM = new ConfSerProperty<>(
            "THROWABLEPROJECTILE_ITEM", ThrowableProjectile.class, ItemStack.class,
            ThrowableProjectile::getItem, ThrowableProjectile::setItem, () -> new ItemStack(Material.SNOWBALL));
    public static final Property<TNTPrimed, Integer> TNT_FUSE_TICKS = NumberProperty.fromInt(
            "TNT_FUSE_TICKS", TNTPrimed.class,
            TNTPrimed::getFuseTicks, TNTPrimed::setFuseTicks, () -> 80);
    public static final Property<TNTPrimed, UUID> TNT_SOURCE = new UuidProperty<>(
            "TNT_SOURCE", TNTPrimed.class,
            (TNTPrimed tnt) -> tnt.getSource() == null ? null : tnt.getSource().getUniqueId(),
            (TNTPrimed tnt, UUID uuid) -> tnt.setSource(Bukkit.getEntity(uuid)));
    public static final Property<Lootable, LootTables> LOOT_TABLE = new KeyedProperty<>(
            "LOOT_TABLE", Lootable.class,
            (Lootable l) -> l.getLootTable() == null ? null : Registry.LOOT_TABLES.get(l.getLootTable().getKey()),
            (Lootable l, LootTables t) -> l.setLootTable(t.getLootTable()), () -> null, Registry.LOOT_TABLES);
    public static final Property<Lootable, Long> LOOT_SEED = NumberProperty.fromLong(
            "LOOT_SEED", Lootable.class,
            Lootable::getSeed, Lootable::setSeed, () -> 0L);
    public static final Property<TropicalFish, DyeColor> TROPICALFISH_BODY_COLOR = new EnumProperty<>(
            "TROPICALFISH_BODY_COLOR", TropicalFish.class, DyeColor.class,
            TropicalFish::getBodyColor, TropicalFish::setBodyColor, () -> DyeColor.WHITE);
    public static final Property<TropicalFish, TropicalFish.Pattern> TROPICALFISH_PATTERN = new EnumProperty<>(
            "TROPICALFISH_PATTERN", TropicalFish.class, TropicalFish.Pattern.class,
            TropicalFish::getPattern, TropicalFish::setPattern, () -> TropicalFish.Pattern.BETTY);
    public static final Property<TropicalFish, DyeColor> TROPICALFISH_PATTERN_COLOR = new EnumProperty<>(
            "TROPICALFISH_PATTERN_COLOR", TropicalFish.class, DyeColor.class,
            TropicalFish::getPatternColor, TropicalFish::setPatternColor, () -> DyeColor.WHITE);
    public static final Property<Vex, Location> VEX_BOUND = new ConfSerProperty<>(
            "VEX_BOUND", Vex.class, Location.class,
            Vex::getBound, Vex::setBound, () -> null);
    public static final Property<Vex, Boolean> VEX_CHARGING = new BooleanProperty<>(
            "VEX_CHARGING", Vex.class,
            Vex::isCharging, Vex::setCharging, () -> false);
    public static final Property<Vex, Integer> VEX_LIFE_TICKS = NumberProperty.fromInt(
            "VEX_LIFE_TICKS", Vex.class,
            Vex::getLifeTicks, Vex::setLifeTicks, () -> 0);
    public static final Property<Villager, Villager.Profession> VILLAGER_PROFESSION = new KeyedProperty<>(
            "VILLAGER_PROFESSION", Villager.class,
            Villager::getProfession, Villager::setProfession, () -> Villager.Profession.NONE, Registry.VILLAGER_PROFESSION);
    public static final Property<Villager, Integer> VILLAGER_EXPERIENCE = NumberProperty.fromInt(
            "VILLAGER_EXPERIENCE", Villager.class,
            Villager::getVillagerExperience, Villager::setVillagerExperience, () -> 0);
    public static final Property<Villager, Integer> VILLAGER_LEVEL = NumberProperty.fromInt(
            "VILLAGER_LEVEL", Villager.class,
            Villager::getVillagerLevel, Villager::setVillagerLevel, () -> 1);
    public static final Property<Villager, Villager.Type> VILLAGER_TYPE = new KeyedProperty<>(
            "VILLAGER_TYPE", Villager.class,
            Villager::getVillagerType, Villager::setVillagerType, () -> Villager.Type.PLAINS, Registry.VILLAGER_TYPE);
    public static final Property<Vindicator, Boolean> VINDICATOR_JOHNNY = new BooleanProperty<>(
            "VINDICATOR_JOHNNY", Vindicator.class,
            Vindicator::isJohnny, Vindicator::setJohnny, () -> false);
    @SuppressWarnings("unchecked")
    public static final Property<InventoryHolder, List<ItemStack>> INVENTORYHOLDER_CONTENTS = new ConfSerCollProperty<>(
            "INVENTORYHOLDER_CONTENTS", InventoryHolder.class,
            (Class<List<ItemStack>>) (Class<?>) List.class, ItemStack.class,
            (InventoryHolder i) -> List.of(i.getInventory().getContents()),
            (InventoryHolder i, List<ItemStack> v) -> i.getInventory().setContents(v.toArray(new ItemStack[0])),
            Collections::emptyList);
    public static final Property<Damageable, Double> DAMAGEABLE_HEALTH = NumberProperty.fromDouble(
            "DAMAGEABLE_HEALTH", Damageable.class,
            Damageable::getHealth, Damageable::setHealth, () -> 20D);
    public static final Property<Damageable, Double> DAMAGEABLE_ABSORPTION = NumberProperty.fromDouble(
            "DAMAGEABLE_ABSORPTION", Damageable.class,
            Damageable::getAbsorptionAmount, Damageable::setAbsorptionAmount, () -> 0D);
    public static final Property<WanderingTrader, Integer> WANDERINGTRADER_DESPAWN_DELAY = NumberProperty.fromInt(
            "WANDERINGTRADER_DESPAWN_DELAY", WanderingTrader.class,
            WanderingTrader::getDespawnDelay, WanderingTrader::setDespawnDelay, () -> 0);
    public static final Property<Wither, Integer> WITHER_INVULNERABILITY_TICKS = NumberProperty.fromInt(
            "WITHER_INVULNERABILITY_TICKS", Wither.class,
            Wither::getInvulnerabilityTicks, Wither::setInvulnerabilityTicks, () -> 0);
    public static final Property<WitherSkull, Boolean> WITHERSKULL_CHARGED = new BooleanProperty<>(
            "WITHERSKULL_CHARGED", WitherSkull.class,
            WitherSkull::isCharged, WitherSkull::setCharged, () -> false);
    public static final Property<Wolf, Boolean> WOLF_ANGRY = new BooleanProperty<>(
            "WOLF_ANGRY", Wolf.class,
            Wolf::isAngry, Wolf::setAngry, () -> false);
    public static final Property<Wolf, DyeColor> WOLF_COLLAR_COLOR = new EnumProperty<>(
            "WOLF_COLLAR_COLOR", Wolf.class, DyeColor.class,
            Wolf::getCollarColor, Wolf::setCollarColor, () -> DyeColor.RED);
    public static final Property<Wolf, Boolean> WOLF_INTERESTED = new BooleanProperty<>(
            "WOLF_INTERESTED", Wolf.class,
            Wolf::isInterested, Wolf::setInterested, () -> false);
    public static final Property<Wolf, Wolf.Variant> WOLF_VARIANT = new KeyedProperty<>(
            "WOLF_VARIANT", Wolf.class,
            Wolf::getVariant, Wolf::setVariant, () -> Wolf.Variant.WOODS, Registry.WOLF_VARIANT);
    public static final Property<Zombie, Boolean> ZOMBIE_CAN_BREAK_DOORS = new BooleanProperty<>(
            "ZOMBIE_CAN_BREAK_DOORS", Zombie.class,
            Zombie::canBreakDoors, Zombie::setCanBreakDoors, () -> false);
    public static final Property<Zombie, Integer> ZOMBIE_CONVERSION_TIME = NumberProperty.fromInt(
            "ZOMBIE_CONVERSION_TIME", Zombie.class,
            (Zombie s) -> s.isConverting() ? s.getConversionTime() : -1, Zombie::setConversionTime, () -> -1);
    public static final Property<ZombieVillager, UUID> ZOMBIEVILLAGER_CONVERSION_PLAYER = new UuidProperty<>(
            "ZOMBIEVILLAGER_CONVERSION_PLAYER", ZombieVillager.class,
            (z) -> z.getConversionPlayer() == null ? null : z.getConversionPlayer().getUniqueId(),
            (z, uuid) -> z.setConversionPlayer(uuid == null ? null : Bukkit.getOfflinePlayer(uuid)),
            () -> null);
    public static final Property<ZombieVillager, Integer> ZOMBIEVILLAGER_CONVERSION_TIME = NumberProperty.fromInt(
            "ZOMBIEVILLAGER_CONVERSION_TIME", ZombieVillager.class,
            (ZombieVillager s) -> s.isConverting() ? s.getConversionTime() : -1, ZombieVillager::setConversionTime, () -> -1);
    public static final Property<ZombieVillager, Villager.Profession> ZOMBIEVILLAGER_PROFESSION = new KeyedProperty<>(
            "ZOMBIEVILLAGER_PROFESSION", ZombieVillager.class,
            ZombieVillager::getVillagerProfession, ZombieVillager::setVillagerProfession,
            () -> Villager.Profession.NONE, Registry.VILLAGER_PROFESSION);
    public static final Property<ZombieVillager, Villager.Type> ZOMBIEVILLAGER_TYPE = new KeyedProperty<>(
            "ZOMBIEVILLAGER_TYPE", ZombieVillager.class,
            ZombieVillager::getVillagerType, ZombieVillager::setVillagerType,
            () -> Villager.Type.PLAINS, Registry.VILLAGER_TYPE);
    public static final Property<Arrow, PotionType> ARROW_BASE_POTION_TYPE = new EnumProperty<>(
            "ARROW_BASE_POTION_TYPE", Arrow.class, PotionType.class,
            Arrow::getBasePotionType,
            Arrow::setBasePotionType, () -> null);
    public static final Property<Arrow, Color> ARROW_COLOR = new ConfSerProperty<>(
            "ARROW_COLOR", Arrow.class, Color.class,
            Arrow::getColor, Arrow::setColor, () -> Color.WHITE);
    public static final Property<Sittable, Boolean> SITTABLE_SITTING = new BooleanProperty<>(
            "SITTABLE_SITTING", Sittable.class,
            Sittable::isSitting, Sittable::setSitting, () -> false);
    public static final Property<ItemFrame, Boolean> ITEMFRAME_FIXED = new BooleanProperty<>(
            "ITEMFRAME_FIXED", ItemFrame.class,
            ItemFrame::isFixed, ItemFrame::setFixed, () -> false);
    public static final Property<ItemFrame, ItemStack> ITEMFRAME_ITEM = new ConfSerProperty<>(
            "ITEMFRAME_ITEM", ItemFrame.class, ItemStack.class,
            ItemFrame::getItem, ItemFrame::setItem, () -> null);
    public static final Property<ItemFrame, Float> ITEMFRAME_ITEM_DROP_CHANCE = NumberProperty.fromFloat(
            "ITEMFRAME_ITEM_DROP_CHANCE", ItemFrame.class,
            ItemFrame::getItemDropChance, ItemFrame::setItemDropChance, () -> 1.0f);
    public static final Property<ItemFrame, Rotation> ITEMFRAME_ROTATION = new EnumProperty<>(
            "ITEMFRAME_ROTATION", ItemFrame.class, Rotation.class,
            ItemFrame::getRotation, ItemFrame::setRotation, () -> Rotation.NONE);
    public static final Property<ItemFrame, Boolean> ITEMFRAME_VISIBLE = new BooleanProperty<>(
            "ITEMFRAME_VISIBLE", ItemFrame.class,
            ItemFrame::isVisible, ItemFrame::setVisible, () -> true);
    public static Property<Entity, Boolean> CUSTOM_NAME_VISIBLE = new BooleanProperty<>(
            "CUSTOM_NAME_VISIBLE", Entity.class,
            Entity::isCustomNameVisible, Entity::setCustomNameVisible, () -> true);
    public static Property<Entity, EntityType> ENTITY_TYPE = new EnumProperty<>(
            "ENTITY_TYPE", Entity.class, EntityType.class,
            Entity::getType, (e, v) -> {
    }, () -> null);
    @SuppressWarnings("UnstableApiUsage")
    public static Property<Entity, EntitySnapshot> ENTITY_SNAPSHOT = new SimpleProperty<>(
            "ENTITY_TYPE", Entity.class, EntitySnapshot.class,
            Entity::createSnapshot, (e, v) -> {
    }, () -> null,
            (snap, map) -> map.put("entity_type".toLowerCase(Locale.ENGLISH), snap == null ? null : snap.getAsString()),
            (map) -> map.get("entity_type") instanceof String raw ? Bukkit.getEntityFactory().createEntitySnapshot(raw) : null);
    public static Property<Nameable, String> CUSTOM_NAME = new StringProperty<>(
            "CUSTOM_NAME", Nameable.class,
            Nameable::getCustomName, Nameable::setCustomName, () -> null);
    public static Property<Entity, Float> FALLING_DISTANCE = NumberProperty.fromFloat(
            "FALLING_DISTANCE", Entity.class,
            Entity::getFallDistance, Entity::setFallDistance, () -> 0F);
    public static Property<Entity, Boolean> GLOWING = new BooleanProperty<>(
            "GLOWING", Entity.class,
            Entity::isGlowing, Entity::setGlowing, () -> false);
    public static Property<Entity, Boolean> GRAVITY = new BooleanProperty<>(
            "GRAVITY", Entity.class,
            Entity::hasGravity, Entity::setGravity, () -> true);
    public static Property<Entity, Integer> FIRE_TICKS = NumberProperty.fromInt(
            "FIRE_TICKS", Entity.class,
            Entity::getFireTicks, Entity::setFireTicks, () -> 0);
    public static Property<Entity, Integer> FREEZE_TICKS = NumberProperty.fromInt(
            "FREEZE_TICKS", Entity.class,
            Entity::getFreezeTicks, Entity::setFreezeTicks, () -> 0);
    public static Property<Entity, Boolean> VISUAL_FIRE = new BooleanProperty<>(
            "VISUAL_FIRE", Entity.class,
            Entity::isVisualFire, Entity::setVisualFire, () -> false);
    public static Property<Entity, Boolean> INVULNERABLE = new BooleanProperty<>(
            "INVULNERABLE", Entity.class,
            Entity::isInvulnerable, Entity::setInvulnerable, () -> false);
    public static Property<Entity, Boolean> PERSISTENT = new BooleanProperty<>(
            "PERSISTENT", Entity.class,
            Entity::isPersistent, Entity::setPersistent, () -> false);
    public static Property<Entity, Integer> PORTAL_COOLDOWN = NumberProperty.fromInt(
            "PORTAL_COOLDOWN", Entity.class,
            Entity::getPortalCooldown, Entity::setPortalCooldown, () -> 0);
    public static Property<Entity, Boolean> SILENT = new BooleanProperty<>(
            "SILENT", Entity.class,
            Entity::isSilent, Entity::setSilent, () -> false);
    public static Property<Entity, Integer> TICKS_LIVED = NumberProperty.fromInt(
            "TICKS_LIVED", Entity.class,
            Entity::getTicksLived, Entity::setTicksLived, () -> 0);
    public static Property<Entity, Boolean> VISIBLE_BY_DEFAULT = new BooleanProperty<>(
            "VISIBLE_BY_DEFAULT",
            Entity.class, Entity::isVisibleByDefault, Entity::setVisibleByDefault, () -> true);
    public static Property<Entity, Vector> DIRECTION = new ConfSerProperty<>(
            "DIRECTION", Entity.class, Vector.class,
            (Entity e) -> e.getLocation().getDirection(),
            (Entity e, Vector value) -> e.teleport(e.getLocation().setDirection(value)),
            () -> new Vector(1, 0, 0));
    public static Property<Entity, Vector> VELOCITY = new ConfSerProperty<>(
            "VELOCITY", Entity.class, Vector.class,
            Entity::getVelocity, Entity::setVelocity, () -> new Vector(0, 0, 0));
    public static Property<Entity, Boolean> OP = new BooleanProperty<>(
            "OP", Entity.class,
            Entity::isOp, Entity::setOp, () -> false);
    public static Property<LivingEntity, Boolean> AI = new BooleanProperty<>(
            "AI", LivingEntity.class,
            LivingEntity::hasAI, LivingEntity::setAI, () -> true);
    public static Property<LivingEntity, Integer> ARROW_COOLDOWN = NumberProperty.fromInt(
            "ARROW_COOLDOWN", LivingEntity.class,
            LivingEntity::getArrowCooldown, LivingEntity::setArrowCooldown, () -> 0);
    public static Property<LivingEntity, Integer> ARROWS_IN_BODY = NumberProperty.fromInt(
            "ARROWS_IN_BODY", LivingEntity.class,
            LivingEntity::getArrowsInBody, LivingEntity::setArrowsInBody, () -> 0);
    public static Property<LivingEntity, Boolean> CAN_PICKUP_ITEMS = new BooleanProperty<>(
            "CAN_PICKUP_ITEMS", LivingEntity.class,
            LivingEntity::getCanPickupItems, LivingEntity::setCanPickupItems, () -> false);
    public static Property<LivingEntity, Boolean> COLLIDABLE = new BooleanProperty<>(
            "COLLIDABLE", LivingEntity.class,
            LivingEntity::isCollidable, LivingEntity::setCollidable, () -> true);
    public static Property<LivingEntity, Boolean> GLIDING = new BooleanProperty<>(
            "GLIDING", LivingEntity.class,
            LivingEntity::isGliding, LivingEntity::setGliding, () -> false);
    public static Property<LivingEntity, Integer> ITEM_IN_USE_TICKS = NumberProperty.fromInt(
            "ITEM_IN_USE_TICKS", LivingEntity.class,
            LivingEntity::getItemInUseTicks, LivingEntity::setItemInUseTicks, () -> 0);
    public static Property<LivingEntity, Integer> MAXIMUM_AIR = NumberProperty.fromInt(
            "MAXIMUM_AIR", LivingEntity.class,
            LivingEntity::getMaximumAir, LivingEntity::setMaximumAir, () -> 300);
    public static Property<LivingEntity, Integer> MAXIMUM_NO_DAMAGE_TICKS = NumberProperty.fromInt(
            "MAXIMUM_NO_DAMAGE_TICKS", LivingEntity.class,
            LivingEntity::getMaximumNoDamageTicks, LivingEntity::setMaximumNoDamageTicks, () -> 10);
    public static Property<LivingEntity, Integer> NO_DAMAGE_TICKS = NumberProperty.fromInt(
            "NO_DAMAGE_TICKS", LivingEntity.class,
            LivingEntity::getNoDamageTicks, LivingEntity::setNoDamageTicks, () -> 0);

    /*
    public static final Property<Salmon, Salmon.Variant> SALMON_VARIANT = new Property<>(
            "SALMON_VARIANT", Salmon.class, Salmon.Variant.class,
            Salmon::getVariant, Salmon::setVariant, Salmon.Variant.SMALL         );*/
    public static Property<LivingEntity, Integer> NO_ACTION_TICKS = NumberProperty.fromInt(
            "NO_ACTION_TICKS", LivingEntity.class,
            LivingEntity::getNoActionTicks, LivingEntity::setNoActionTicks, () -> 0);
    public static Property<LivingEntity, Double> LAST_DAMAGE = NumberProperty.fromDouble(
            "LAST_DAMAGE", LivingEntity.class,
            LivingEntity::getLastDamage, LivingEntity::setLastDamage, () -> 0D);
    public static Property<LivingEntity, Boolean> REMOVE_WHEN_FAR_AWAY = new BooleanProperty<>(
            "REMOVE_WHEN_FAR_AWAY", LivingEntity.class,
            LivingEntity::getRemoveWhenFarAway, LivingEntity::setRemoveWhenFarAway, () -> false);
    public static Property<LivingEntity, Boolean> SWIMMING = new BooleanProperty<>(
            "SWIMMING", LivingEntity.class,
            LivingEntity::isSwimming,
            LivingEntity::setSwimming, () -> false);
    @SuppressWarnings("unchecked")
    public static Property<LivingEntity, Collection<PotionEffect>> ACTIVE_POTION_EFFECTS = new ConfSerCollProperty<>(
            "ACTIVE_POTION_EFFECTS", LivingEntity.class,
            (Class<Collection<PotionEffect>>) (Class<?>) Collection.class, PotionEffect.class,
            (LivingEntity l) -> l.getActivePotionEffects().stream()
                    .map(p -> new PotionEffect(p.serialize()))
                    .collect(Collectors.toList()),
            (LivingEntity l, Collection<PotionEffect> v) -> {
                l.getActivePotionEffects().clear();
                if (v != null)
                    l.addPotionEffects(v);
            }, List::of);
    public static Property<LivingEntity, Integer> REMAINING_AIR = NumberProperty.fromInt(
            "REMAINING_AIR", LivingEntity.class,
            LivingEntity::getRemainingAir, LivingEntity::setRemainingAir, () -> 0);
    public static Property<AbstractArrow, Boolean> ARROW_CRITICAL = new BooleanProperty<>(
            "ARROW_CRITICAL", AbstractArrow.class,
            org.bukkit.entity.AbstractArrow::isCritical, org.bukkit.entity.AbstractArrow::setCritical,
            () -> false);
    public static Property<AbstractArrow, Double> ARROW_DAMAGE = NumberProperty.fromDouble(
            "ARROW_DAMAGE", AbstractArrow.class,
            org.bukkit.entity.AbstractArrow::getDamage, org.bukkit.entity.AbstractArrow::setDamage,
            () -> 10D);
    @SuppressWarnings("UnstableApiUsage")
    public static Property<AbstractArrow, ItemStack> ARROW_ITEM = new ConfSerProperty<>(
            "ARROW_ITEM", AbstractArrow.class, ItemStack.class,
            org.bukkit.entity.AbstractArrow::getItem, org.bukkit.entity.AbstractArrow::setItem,
            () -> new ItemStack(Material.ARROW));
    @SuppressWarnings("UnstableApiUsage")
    public static Property<AbstractArrow, ItemStack> ARROW_WEAPON = new ConfSerProperty<>(
            "ARROW_WEAPON", AbstractArrow.class, ItemStack.class,
            org.bukkit.entity.AbstractArrow::getWeapon, org.bukkit.entity.AbstractArrow::setWeapon,
            () -> new ItemStack(Material.BOW));
    public static Property<AbstractArrow, AbstractArrow.PickupStatus> ARROW_PICKUP_STATUS =
            new EnumProperty<>("ARROW_WEAPON", AbstractArrow.class, AbstractArrow.PickupStatus.class,
                    org.bukkit.entity.AbstractArrow::getPickupStatus,
                    org.bukkit.entity.AbstractArrow::setPickupStatus,
                    () -> AbstractArrow.PickupStatus.ALLOWED);
    public static Property<AbstractHorse, Integer> HORSE_DOMESTICATION = NumberProperty.fromInt(
            "HORSE_DOMESTICATION", AbstractHorse.class,
            AbstractHorse::getDomestication, AbstractHorse::setDomestication, () -> 0);
    public static Property<AbstractHorse, Integer> HORSE_MAX_DOMESTICATION = NumberProperty.fromInt(
            "HORSE_MAX_DOMESTICATION", AbstractHorse.class,
            AbstractHorse::getMaxDomestication, AbstractHorse::setMaxDomestication, () -> 0);
    public static Property<AbstractHorse, Boolean> HORSE_EATING_HAYSTACK = new BooleanProperty<>(
            "HORSE_EATING_HAYSTACK", AbstractHorse.class,
            AbstractHorse::isEatingHaystack, AbstractHorse::setEatingHaystack, () -> true);
    public static Property<AbstractHorse, Double> HORSE_JUMP_STRENGTH = NumberProperty.fromDouble(
            "HORSE_JUMP_STRENGTH", AbstractHorse.class,
            AbstractHorse::getJumpStrength, AbstractHorse::setJumpStrength, () -> 3D);
    public static Property<Ageable, Integer> AGEABLE_AGE = NumberProperty.fromInt(
            "AGEABLE_AGE", Ageable.class,
            Ageable::getAge, Ageable::setAge, () -> 0);
    public static Property<Allay, Boolean> ALLAY_CAN_DUPLICATE = new BooleanProperty<>(
            "ALLAY_CAN_DUPLICATE", Allay.class,
            Allay::canDuplicate, Allay::setCanDuplicate, () -> true);
    public static Property<Allay, Long> ALLAY_DUPLICATE_COOLDOWN = NumberProperty.fromLong(
            "ALLAY_DUPLICATE_COOLDOWN", Allay.class,
            Allay::getDuplicationCooldown, Allay::setDuplicationCooldown, () -> 0L);
    public static Property<Animals, UUID> ANIMALS_BREED_CAUSE = new UuidProperty<>(
            "ANIMALS_BREED_CAUSE", Animals.class,
            Animals::getBreedCause, Animals::setBreedCause);
    public static Property<Animals, Integer> ANIMALS_LOVE_MODE_TICKS = NumberProperty.fromInt(
            "ANIMALS_LOVE_MODE_TICKS", Animals.class,
            Animals::getLoveModeTicks, Animals::setLoveModeTicks, () -> 0);
    public static Property<Axolotl, Axolotl.Variant> AXOLOTL_VARIANT = new EnumProperty<>(
            "AXOLOTL_VARIANT", Axolotl.class, Axolotl.Variant.class,
            Axolotl::getVariant, Axolotl::setVariant, () -> Axolotl.Variant.BLUE);
    public static Property<Axolotl, Boolean> AXOLOTL_PLAYING_DEAD = new BooleanProperty<>(
            "AXOLOTL_PLAYING_DEAD", Axolotl.class,
            Axolotl::isPlayingDead, Axolotl::setPlayingDead, () -> false);
    public static Property<Bat, Boolean> BAT_AWAKE = new BooleanProperty<>(
            "BAT_AWAKE", Bat.class,
            Bat::isAwake, Bat::setAwake, () -> true);
    public static Property<Bee, Integer> BEE_ANGER = NumberProperty.fromInt(
            "BEE_ANGER", Bee.class,
            Bee::getAnger, Bee::setAnger, () -> 0);
    public static Property<Bee, Integer> BEE_CANNOT_ENTER_HIVE_TICKS = NumberProperty.fromInt(
            "BEE_CANNOT_ENTER_HIVE_TICKS", Bee.class,
            Bee::getCannotEnterHiveTicks, Bee::setCannotEnterHiveTicks, () -> 0);
    public static Property<Bee, Location> BEE_FLOWER = new ConfSerProperty<>(
            "BEE_FLOWER", Bee.class, Location.class,
            Bee::getFlower, Bee::setFlower, () -> null);
    public static Property<Bee, Location> BEE_HIVE = new ConfSerProperty<>(
            "BEE_HIVE", Bee.class, Location.class,
            Bee::getHive, Bee::setHive, () -> null);
    public static Property<Bee, Boolean> BEE_HAS_NECTAR = new BooleanProperty<>(
            "BEE_HAS_NECTAR", Bee.class,
            Bee::hasNectar, Bee::setHasNectar, () -> false);
    public static Property<Bee, Boolean> BEE_HAS_STUNG = new BooleanProperty<>(
            "BEE_HAS_STUNG", Bee.class,
            Bee::hasStung, Bee::setHasStung, () -> true);
    public static Property<BlockDisplay, String> BLOCKDISPLAY_BLOCK_DATA = new StringProperty<>(
            "BLOCKDISPLAY_BLOCK_DATA", BlockDisplay.class,
            (BlockDisplay e) -> e.getBlock().getAsString(),
            (BlockDisplay e, String value) -> e.setBlock(Bukkit.createBlockData(value)),
            () -> Bukkit.createBlockData(Material.STONE).getAsString());
    //BOAT_TYPE(Boat.class,
    //        (Entity e) -> ((Boat) e).getBoatType(),
    //        (Entity e, Boat.Type value) -> ((Boat) e).setBoatType(value), Boat.Type.OAK,
    //        Boat.Type.class);
    public static Property<Breedable, Boolean> BREEDABLE_AGE_LOCK = new BooleanProperty<>(
            "BREEDABLE_AGE_LOCK", Breedable.class,
            Breedable::getAgeLock, Breedable::setAgeLock, () -> false);
    public static Property<Breedable, Boolean> BREEDABLE_BREED = new BooleanProperty<>(
            "BREEDABLE_BREED", Breedable.class,
            Breedable::canBreed, Breedable::setBreed, () -> true);
    public static Property<Camel, Boolean> CAMEL_DASHING = new BooleanProperty<>(
            "CAMEL_DASHING", Camel.class,
            Camel::isDashing, Camel::setDashing, () -> false);
    public static Property<Cat, Cat.Type> CAT_TYPE = new KeyedProperty<>(
            "CAT_TYPE", Cat.class,
            Cat::getCatType, Cat::setCatType, () -> Cat.Type.RED, Registry.CAT_VARIANT);
    public static Property<Cat, DyeColor> CAT_COLLAR_COLOR = new EnumProperty<>(
            "CAT_COLLAR_COLOR", Cat.class, DyeColor.class,
            Cat::getCollarColor, Cat::setCollarColor, () -> DyeColor.RED);
    public static Property<ChestedHorse, Boolean> CHESTED_HORSE = new BooleanProperty<>(
            "CHESTED_HORSE", ChestedHorse.class,
            ChestedHorse::isCarryingChest, ChestedHorse::setCarryingChest, () -> false);
    public static Property<CommandMinecart, String> COMMANDMINECART_COMMAND = new StringProperty<>(
            "COMMANDMINECART_COMMAND", CommandMinecart.class,
            CommandMinecart::getCommand, CommandMinecart::setCommand, () -> null);
    public static Property<CommandMinecart, String> COMMANDMINECART_NAME = new StringProperty<>(
            "COMMANDMINECART_NAME", CommandMinecart.class,
            Entity::getName, CommandMinecart::setName, () -> null);
    public static Property<Creeper, Integer> CREEPER_EXPLOSION_RADIUS = NumberProperty.fromInt(
            "CREEPER_EXPLOSION_RADIUS", Creeper.class,
            Creeper::getExplosionRadius, Creeper::setExplosionRadius, () -> 3);
    public static Property<Creeper, Integer> CREEPER_FUSE_TICKS = NumberProperty.fromInt(
            "CREEPER_FUSE_TICKS", Creeper.class,
            Creeper::getFuseTicks, Creeper::setFuseTicks, () -> 0);
    public static Property<Creeper, Integer> CREEPER_MAX_FUSE_TICKS = NumberProperty.fromInt(
            "CREEPER_MAX_FUSE_TICKS", Creeper.class,
            Creeper::getMaxFuseTicks, Creeper::setMaxFuseTicks, () -> 40);
    public static Property<Creeper, Boolean> CREEPER_POWERED = new BooleanProperty<>(
            "CREEPER_POWERED", Creeper.class,
            Creeper::isPowered, Creeper::setPowered, () -> false);
    public static Property<EnderCrystal, Location> ENDERCRYSTAL_BEAM_TARGET = new ConfSerProperty<>(
            "ENDERCRYSTAL_BEAM_TARGET", EnderCrystal.class, Location.class,
            EnderCrystal::getBeamTarget, EnderCrystal::setBeamTarget, () -> null);
    public static Property<EnderCrystal, Boolean> ENDERCRYSTAL_SHOWING_BOTTOM = new BooleanProperty<>(
            "ENDERCRYSTAL_SHOWING_BOTTOM", EnderCrystal.class,
            EnderCrystal::isShowingBottom, EnderCrystal::setShowingBottom, () -> true);
    public static Property<Enderman, String> ENDERMAN_CARRIED_BLOCK = new StringProperty<>(
            "ENDERMAN_CARRIED_BLOCK", Enderman.class,
            (Enderman e) -> {
                BlockData value = e.getCarriedBlock();
                return value == null ? null : value.getAsString();
            },
            (Enderman e, String value) -> e.setCarriedBlock(Bukkit.createBlockData(value)),
            () -> null);
    public static Property<EnderSignal, Boolean> ENDERSIGNAL_CARRIED_BLOCK = new BooleanProperty<>(
            "ENDERSIGNAL_CARRIED_BLOCK", EnderSignal.class,
            EnderSignal::getDropItem, EnderSignal::setDropItem, () -> true);
    public static Property<EnderSignal, Integer> ENDERSIGNAL_DESPAWN_TIMER = NumberProperty.fromInt(
            "ENDERSIGNAL_DESPAWN_TIMER", EnderSignal.class,
            EnderSignal::getDespawnTimer, EnderSignal::setDespawnTimer, () -> 100);
    public static Property<EnderSignal, ItemStack> ENDERSIGNAL_ITEM = new ConfSerProperty<>(
            "ENDERSIGNAL_ITEM", EnderSignal.class, ItemStack.class,
            EnderSignal::getItem, EnderSignal::setItem, () -> null);
    public static Property<EnderSignal, Location> ENDERSIGNAL_TARGET_LOCATION = new ConfSerProperty<>(
            "ENDERSIGNAL_TARGET_LOCATION", EnderSignal.class, Location.class,
            EnderSignal::getTargetLocation, EnderSignal::setTargetLocation,
            () -> new Location(Bukkit.getWorlds().get(0), 0, 0, 0));
    public static Property<EvokerFangs, Integer> EVOKER_FANGS_ATTACK_DELAY = NumberProperty.fromInt(
            "EVOKER_FANGS_ATTACK_DELAY", EvokerFangs.class,
            EvokerFangs::getAttackDelay, EvokerFangs::setAttackDelay, () -> 20);
    public static Property<ExperienceOrb, Integer> EXPERIENCE_ORB_EXPERIENCE = NumberProperty.fromInt(
            "EXPERIENCE_ORB_EXPERIENCE", ExperienceOrb.class,
            ExperienceOrb::getExperience, ExperienceOrb::setExperience, () -> 4);
    public static Property<Explosive, Float> EXPLOSIVE_YIELD = NumberProperty.fromFloat(
            "EXPLOSIVE_YIELD", Explosive.class,
            Explosive::getYield, Explosive::setYield, () -> 3F);
    public static Property<Explosive, Boolean> EXPLOSIVE_INCENDIARY = new BooleanProperty<>(
            "EXPLOSIVE_INCENDIARY", Explosive.class,
            Explosive::isIncendiary, Explosive::setIsIncendiary, () -> true);
    public static Property<ExplosiveMinecart, Integer> EXPLOSIVEMINECART_FUSE_TICKS = NumberProperty.fromInt(
            "EXPLOSIVEMINECART_FUSE_TICKS", ExplosiveMinecart.class,
            ExplosiveMinecart::getFuseTicks, ExplosiveMinecart::setFuseTicks, () -> 0);
    public static Property<FallingBlock, Boolean> FALLINGBLOCK_CANCEL_DROP = new BooleanProperty<>(
            "FALLINGBLOCK_CANCEL_DROP", FallingBlock.class,
            FallingBlock::getCancelDrop, FallingBlock::setCancelDrop, () -> false);
    public static Property<FallingBlock, Boolean> FALLINGBLOCK_DROP_ITEM = new BooleanProperty<>(
            "FALLINGBLOCK_DROP_ITEM", FallingBlock.class,
            FallingBlock::getDropItem, FallingBlock::setDropItem, () -> true);
    public static Property<FallingBlock, Boolean> FALLINGBLOCK_HURT_ENTITIES = new BooleanProperty<>(
            "FALLINGBLOCK_HURT_ENTITIES", FallingBlock.class,
            FallingBlock::canHurtEntities, FallingBlock::setHurtEntities, () -> true);
    public static Property<FallingBlock, Integer> FALLINGBLOCK_MAX_DAMAGE = NumberProperty.fromInt(
            "FALLINGBLOCK_MAX_DAMAGE", FallingBlock.class,
            FallingBlock::getMaxDamage, FallingBlock::setMaxDamage, () -> 20);
    public static Property<FallingBlock, Float> FALLINGBLOCK_DAMAGE_PER_BLOCK = NumberProperty.fromFloat(
            "FALLINGBLOCK_DAMAGE_PER_BLOCK", FallingBlock.class,
            FallingBlock::getDamagePerBlock, FallingBlock::setDamagePerBlock, () -> 1F);
    public static Property<Fireball, Vector> FIREBALL_ACCELERATION = new ConfSerProperty<>(
            "FIREBALL_ACCELERATION", Fireball.class, Vector.class,
            Fireball::getAcceleration, Fireball::setAcceleration,
            () -> new Vector(1, 0, 0));
    public static Property<Fox, Boolean> FOX_CROUCHING = new BooleanProperty<>(
            "FOX_CROUCHING", Fox.class,
            Fox::isCrouching, Fox::setCrouching, () -> false);
    public static Property<Fox, Boolean> FOX_SLEEPING = new BooleanProperty<>(
            "FOX_SLEEPING", Fox.class,
            Fox::isSleeping, Fox::setSleeping, () -> false);
    public static Property<Fox, Fox.Type> FOX_TYPE = new EnumProperty<>(
            "FOX_TYPE", Fox.class, Fox.Type.class,
            Fox::getFoxType, Fox::setFoxType, () -> Fox.Type.RED);
    public static Property<Fox, UUID> FOX_FIRST_TRUSTED = new UuidProperty<>(
            "FOX_FIRST_TRUSTED", Fox.class,
            (Fox e) -> {
                AnimalTamer value = e.getFirstTrustedPlayer();
                return value == null ? null : value.getUniqueId();
            },
            (Fox e, UUID value) -> e.setFirstTrustedPlayer(value == null ? null :
                    Bukkit.getOfflinePlayer(value)));
    public static Property<Fox, UUID> FOX_SECOND_TRUSTED = new UuidProperty<>(
            "FOX_SECOND_TRUSTED", Fox.class,
            (Fox e) -> {
                AnimalTamer value = e.getSecondTrustedPlayer();
                return value == null ? null : value.getUniqueId();
            },
            (Fox e, UUID value) -> e.setSecondTrustedPlayer(value == null ? null :
                    Bukkit.getOfflinePlayer(value)));
    public static Property<Frog, Frog.Variant> FROG_TYPE = new KeyedProperty<>(
            "FROG_TYPE", Frog.class,
            Frog::getVariant, Frog::setVariant, () -> Frog.Variant.TEMPERATE, Registry.FROG_VARIANT);
    public static Property<Ghast, Boolean> GHAST_CHARGING = new BooleanProperty<>(
            "GHAST_CHARGING", Ghast.class,
            Ghast::isCharging, Ghast::setCharging, () -> false);
    public static Property<GlowSquid, Integer> GLOWINGSQUID_DARK_TICKS = NumberProperty.fromInt(
            "GLOWING_SQUID_DARK_TICKS", GlowSquid.class,
            GlowSquid::getDarkTicksRemaining, GlowSquid::setDarkTicksRemaining, () -> 100);
    @SuppressWarnings("unchecked")
    public static Property<MushroomCow, Collection<PotionEffect>> MUSHROOMCOW_NEXT_STEW_EFFECTS = new ConfSerCollProperty<>(
            "MUSHROOMCOW_NEXT_STEW_EFFECTS", MushroomCow.class,
            (Class<Collection<PotionEffect>>) (Class<?>) Collection.class, PotionEffect.class,
            (MushroomCow m) -> m.getEffectsForNextStew().stream()
                    .map(p -> new PotionEffect(p.serialize()))
                    .collect(Collectors.toList()),
            (MushroomCow m, Collection<PotionEffect> v) -> {
                m.clearEffectsForNextStew();
                v.forEach(e -> m.addEffectToNextStew(e, true));
            }, List::of);
}
