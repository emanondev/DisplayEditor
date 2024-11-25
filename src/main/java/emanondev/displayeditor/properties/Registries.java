package emanondev.displayeditor.properties;

import emanondev.displayeditor.properties.impl.Property;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static emanondev.displayeditor.properties.EntityProperties.*;


public class Registries {

    public static final PropertyRegistry PROPERTIES;

    static {
        PROPERTIES = new PropertyRegistry();
        initProperties();
    }

    protected static void initProperties() {
        PROPERTIES.registerProperties(
                ENTITY_SNAPSHOT,
                ENTITY_TYPE,
                INVENTORYHOLDER_CONTENTS);
        AttributeProperties.ATTRIBUTABLE_BASE_VALUES.values().forEach(PROPERTIES::registerProperty);
        AttributeProperties.ATTRIBUTABLE_MODIFIERS.values().forEach(PROPERTIES::registerProperty);
        PROPERTIES.registerProperties(
                CUSTOM_NAME_VISIBLE,
                CUSTOM_NAME,
                FALLING_DISTANCE,
                GLOWING,
                GRAVITY,
                FIRE_TICKS,
                FREEZE_TICKS,
                VISUAL_FIRE,
                INVULNERABLE,
                PERSISTENT,
                PORTAL_COOLDOWN,
                SILENT,
                TICKS_LIVED,
                VISIBLE_BY_DEFAULT,
                DIRECTION,
                VELOCITY,
                OP,
                AI,
                ARROW_COOLDOWN,
                ARROWS_IN_BODY,
                CAN_PICKUP_ITEMS,
                COLLIDABLE,
                GLIDING,
                ITEM_IN_USE_TICKS,
                MAXIMUM_AIR,
                MAXIMUM_NO_DAMAGE_TICKS,
                NO_DAMAGE_TICKS,
                NO_ACTION_TICKS,
                LAST_DAMAGE,
                REMOVE_WHEN_FAR_AWAY,
                SWIMMING,
                ACTIVE_POTION_EFFECTS,
                REMAINING_AIR,
                ARROW_CRITICAL,
                ARROW_DAMAGE,
                ARROW_ITEM,
                ARROW_WEAPON,
                ARROW_PICKUP_STATUS,
                HORSE_DOMESTICATION,
                HORSE_MAX_DOMESTICATION,
                HORSE_EATING_HAYSTACK,
                HORSE_JUMP_STRENGTH,
                AGEABLE_AGE,
                ALLAY_CAN_DUPLICATE,
                ALLAY_DUPLICATE_COOLDOWN,
                ANIMALS_BREED_CAUSE,
                ANIMALS_LOVE_MODE_TICKS,
                AXOLOTL_VARIANT,
                AXOLOTL_PLAYING_DEAD,
                BAT_AWAKE,
                BEE_ANGER,
                BEE_CANNOT_ENTER_HIVE_TICKS,
                BEE_FLOWER,
                BEE_HIVE,
                BEE_HAS_NECTAR,
                BEE_HAS_STUNG,
                BLOCKDISPLAY_BLOCK_DATA,
                BREEDABLE_AGE_LOCK,
                BREEDABLE_BREED,
                CAMEL_DASHING,
                CAT_TYPE,
                CAT_COLLAR_COLOR,
                CHESTED_HORSE,
                COMMANDMINECART_COMMAND,
                COMMANDMINECART_NAME,
                CREEPER_EXPLOSION_RADIUS,
                CREEPER_FUSE_TICKS,
                CREEPER_MAX_FUSE_TICKS,
                CREEPER_POWERED,
                ENDERCRYSTAL_BEAM_TARGET,
                ENDERCRYSTAL_SHOWING_BOTTOM,
                ENDERMAN_CARRIED_BLOCK,
                ENDERSIGNAL_CARRIED_BLOCK,
                ENDERSIGNAL_DESPAWN_TIMER,
                ENDERSIGNAL_ITEM,
                ENDERSIGNAL_TARGET_LOCATION,
                EVOKER_FANGS_ATTACK_DELAY,
                EXPERIENCE_ORB_EXPERIENCE,
                EXPLOSIVE_YIELD,
                EXPLOSIVE_INCENDIARY,
                EXPLOSIVEMINECART_FUSE_TICKS,
                FALLINGBLOCK_CANCEL_DROP,
                FALLINGBLOCK_DROP_ITEM,
                FALLINGBLOCK_HURT_ENTITIES,
                FALLINGBLOCK_MAX_DAMAGE,
                FALLINGBLOCK_DAMAGE_PER_BLOCK,
                FIREBALL_ACCELERATION,
                FOX_CROUCHING,
                FOX_SLEEPING,
                FOX_TYPE,
                FOX_FIRST_TRUSTED,
                FOX_SECOND_TRUSTED,
                FROG_TYPE,
                GHAST_CHARGING,
                GLOWINGSQUID_DARK_TICKS,
                GOAT_LEFT_HORN,
                GOAT_RIGHT_HORN,
                GOAT_SCREAMING,
                GUARDIAN_LASER_ACTIVATED,
                GUARDIAN_LASER_TICKS,
                HOGLIN_CONVERSION_TIME,
                HOGLIN_IMMUNE_TO_ZOMBIFICATION,
                HOGLIN_ABLE_TO_BE_HUNTED,
                HOPPERMINECART_ENABLED,
                HORSE_COLOR,
                HORSE_STYLE,
                ZOMBIE_CONVERSION_TIME,
                INTERACTION_HEIGHT,
                INTERACTION_WIDTH,
                INTERACTION_RESPONSIVE,
                IRONGOLEM_PLAYER_CREATED,
                ITEM_ITEM_STACK,
                ITEM_OWNER,
                ITEM_PICKUP_DELAY,
                ITEM_THROWER,
                ITEM_UNLIMITED_LIFETIME,
                ITEMDISPLAY_TRANSFORM,
                ITEMDISPLAY_ITEM_STACK,
                LIGHTNINGSTRIKE_FLASHES,
                LIGHTNINGSTRIKE_LIFE_TICKS,
                LLAMA_COLOR,
                LLAMA_STRENGTH,
                MINECART_DAMAGE,
                MINECART_DERAILED_VELOCITY_MOD,
                MINECART_DISPLAY_BLOCK_DATA,
                MINECART_DISPLAY_BLOCK_OFFSET,
                MINECART_FLYING_VELOCITY_MOD,
                MINECART_MAX_SPEED,
                MINECART_SLOW_WHEN_EMPTY,
                MOB_AWARE,
                MOB_TARGET,
                MUSHROOMCOW_VARIANT,
                MUSHROOMCOW_NEXT_STEW_EFFECTS,
                OCELOT_TRUSTING,
                OMINOUS_ITEM,
                OMINOUS_SPAWN_TICKS,
                PAINTING_ART,
                PANDA_EATING,
                PANDA_HIDDEN_GENE,
                PANDA_MAIN_GENE,
                PANDA_ON_BACK,
                PANDA_ROLLING,
                PANDA_SNEEZING,
                PARROT_VARIANT,
                PHANTOM_SIZE,
                PIGLIN_ABLE_TO_HUNT,
                ABSTRACTPIGLIN_CONVERSION_TIME,
                ABSTRACTPIGLIN_IMMUNE_TO_ZOMBIFICATION,
                PIGZOMBIE_ANGER_LEVEL,
                PIGZOMBIE_ANGRY,
                POWERED_MINECART_FUEL,
                PUFFERFISH_PUFF_STATE,
                RABBIT_TYPE,
                RAIDER_CAN_JOIN_RAID,
                RAIDER_CELEBRATING,
                RAIDER_PATROL_LEADER,
                RAIDER_PATROL_TARGET,
                COLORABLE_COLOR,
                SHEARABLE_SHEARED,
                SHULKER_ATTACHED_FACE,
                SHULKER_PEEK,
                SHULKERBULLET_TARGET,
                SIZEDFIREBALL_DISPLAY_ITEM,
                SKELETON_CONVERSION_TIME,
                SKELETONHORSE_TRAPPED,
                SKELETONHORSE_TRAP_TIME,
                SLIME_SIZE,
                SNIFFER_STATE,
                SNOWMAN_DERP,
                SPECTRALARROW_GLOWING_TICKS,
                SPELLCASTER_SPELL,
                STEERABLE_BOOST_TICKS,
                STEERABLE_CURRENT_BOOST_TICKS,
                STEERABLE_SADDLED,
                STRIDER_SHIVERING,
                TADPOLE_AGE,
                TAMEABLE_OWNER,
                TAMEABLE_TAMED,
                TEXTDISPLAY_ALIGNMENT,
                TEXTDISPLAY_BACKGROUND_COLOR,
                TEXTDISPLAY_DEFAULT_BACKGROUND,
                TEXTDISPLAY_LINE_WIDTH,
                TEXTDISPLAY_SEE_THROUGH,
                TEXTDISPLAY_SHADOWED,
                TEXTDISPLAY_TEXT,
                TEXTDISPLAY_TEXT_OPACITY,
                THROWABLEPROJECTILE_ITEM,
                TNT_FUSE_TICKS,
                TNT_SOURCE,
                LOOT_TABLE,
                LOOT_SEED,
                TROPICALFISH_BODY_COLOR,
                TROPICALFISH_PATTERN,
                TROPICALFISH_PATTERN_COLOR,
                VEX_BOUND,
                VEX_CHARGING,
                VEX_LIFE_TICKS,
                VILLAGER_PROFESSION,
                VILLAGER_EXPERIENCE,
                VILLAGER_LEVEL,
                VILLAGER_TYPE,
                VINDICATOR_JOHNNY,
                INVENTORYHOLDER_CONTENTS,
                DAMAGEABLE_HEALTH,
                DAMAGEABLE_ABSORPTION,
                WANDERINGTRADER_DESPAWN_DELAY,
                WITHER_INVULNERABILITY_TICKS,
                WITHERSKULL_CHARGED,
                WOLF_ANGRY,
                WOLF_COLLAR_COLOR,
                WOLF_INTERESTED,
                WOLF_VARIANT,
                ZOMBIE_CAN_BREAK_DOORS,
                ZOMBIE_CONVERSION_TIME,
                ZOMBIEVILLAGER_CONVERSION_PLAYER,
                ZOMBIEVILLAGER_CONVERSION_TIME,
                ZOMBIEVILLAGER_PROFESSION,
                ZOMBIEVILLAGER_TYPE,
                ARROW_BASE_POTION_TYPE,
                ARROW_COLOR,
                SITTABLE_SITTING,
                ITEMFRAME_FIXED,
                ITEMFRAME_ITEM,
                ITEMFRAME_ITEM_DROP_CHANCE,
                ITEMFRAME_ROTATION,
                ITEMFRAME_VISIBLE);
    }


    public static class PropertyRegistry implements Registry<Property<?, ?>> {

        private final Map<NamespacedKey, Property<?, ?>> properties = new LinkedHashMap<>();
        private final Map<Class<? extends Entity>, List<Property<?, ?>>> propertiesByEntityType = new LinkedHashMap<>();

        private PropertyRegistry() {
        }

        public void registerProperty(Property<?, ?> property) {
            if (properties.containsKey(property.getKey()))
                throw new IllegalArgumentException("duplicated key " + property.getKey());
            properties.put(property.getKey(), property);
            if (propertiesByEntityType.isEmpty())
                for (EntityType type : EntityType.values())
                    if (type.isSpawnable())
                        propertiesByEntityType.put(type.getEntityClass(), new ArrayList<>());
            for (Class<? extends Entity> clazz : propertiesByEntityType.keySet())
                if (property.getEntityClass().isAssignableFrom(clazz))
                    propertiesByEntityType.get(clazz).add(property);
        }

        @SuppressWarnings("unchecked")
        public <E extends Entity> List<Property<E, ?>> getAllByEntity(Class<E> clazz) {
            return Collections.unmodifiableList((List<? extends Property<E, ?>>) propertiesByEntityType.get(clazz));
        }

        public void registerProperties(Collection<Property<?, ?>> properties) {
            for (Property<?, ?> p : properties)
                registerProperty(p);
        }

        public void registerProperties(Property<?, ?>... properties) {
            for (Property<?, ?> p : properties)
                registerProperty(p);
        }

        @NotNull
        @Override
        public Iterator<Property<?, ?>> iterator() {
            return properties.values().iterator();
        }

        @Nullable
        @Override
        public Property<?, ?> get(@NotNull NamespacedKey key) {
            return properties.get(key);
        }

        @NotNull
        @Override
        public Stream<Property<?, ?>> stream() {
            return StreamSupport.stream(spliterator(), false);
        }
    }
}
