package emanondev.displayeditor;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ItemBuilder {
    private final ItemStack result;
    private ItemMeta resultMeta;

    public ItemBuilder(@NotNull Material result) {
        this(new ItemStack(result));
    }

    public ItemBuilder(@NotNull ItemStack result) {
        this.result = new ItemStack(result);
        this.resultMeta = this.result.getItemMeta();
    }

    /**
     * Sets unbreakable.<br>
     * Sets all {@link ItemFlag}.
     *
     * @return this for chaining.
     */
    @Contract(" -> this")
    public ItemBuilder setGuiProperty() {
        setUnbreakable(true);
        hideAllFlags();
        if (!resultMeta.hasDisplayName())
            resultMeta.setDisplayName(" ");
        return this;
    }

    /**
     * @param value Should this be unbreakable?
     * @return this for chaining.
     */
    @Contract("_ -> this")
    public ItemBuilder setUnbreakable(boolean value) {
        this.resultMeta.setUnbreakable(value);
        return this;
    }

    /**
     * Sets all {@link ItemFlag} on this
     *
     * @return this for chaining.
     */
    @Contract(" -> this")
    public ItemBuilder hideAllFlags() {
        this.resultMeta.addItemFlags(ItemFlag.values());
        return this;
    }

    /**
     * @return created ItemStack.
     */
    @Contract(" -> new")
    public ItemStack build() {
        this.result.setItemMeta(this.resultMeta);
        return this.result;
    }

    /**
     * @param flag Adds the flag to this
     * @return this for chaining.
     */
    @Contract("_ -> this")
    public ItemBuilder addFlag(ItemFlag flag) {
        this.resultMeta.addItemFlags(flag);
        return this;
    }

    /**
     * @param displayName Set displayName of this
     * @return this for chaining.
     */
    @Contract("_ -> this")
    public ItemBuilder setDisplayName(String displayName) {

        this.resultMeta.setDisplayName(displayName);
        return this;
    }

    /**
     * Adds line to lore.
     *
     * @param line Line to add.
     * @return this for chaining.
     */
    @Contract("_ -> this")
    public ItemBuilder addLore(String line) {
        if (line == null)
            return this;
        List<String> lore = this.resultMeta.hasLore() ? new ArrayList<>(this.resultMeta.getLore()) : new ArrayList<>();
        lore.add(line);
        this.resultMeta.setLore(lore);
        return this;
    }

    /**
     * Sets lore.
     *
     * @param lore Lore to set.
     * @return this for chaining.
     */
    @Contract("_ -> this")
    public ItemBuilder setLore(@Nullable List<String> lore) {
        this.resultMeta.setLore(lore);
        return this;
    }

    /**
     * Add enchantment.
     *
     * @param enchantment Enchantment to set.
     * @param level       Level to set.
     * @return this for chaining.
     */
    @Contract("_, _ -> this")
    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        if (level == 0)
            this.resultMeta.removeEnchant(enchantment);
        else
            this.resultMeta.addEnchant(enchantment, level, true);
        return this;
    }

    /**
     * For both Potion and Leather Armor
     *
     * @param color Applied color.
     * @return this for chaining.
     * @throws IllegalStateException if this meta is not LeatherArmorMeta nor
     *                               PotionMeta
     */
    @Contract("_ -> this")
    public ItemBuilder setColor(Color color) {
        if (resultMeta instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta) this.resultMeta).setColor(color);
            return this;
        } else if (resultMeta instanceof PotionMeta) {
            ((PotionMeta) this.resultMeta).setColor(color);
            return this;
        } else
            new IllegalStateException("meta is not LeatherArmorMeta nor PotionMeta").printStackTrace();
        return this;
    }

    /**
     * Set author name
     *
     * @param name Name of the Author.
     * @return this for chaining.
     * @throws IllegalStateException if this meta is not BookMeta
     */
    @Contract("_ -> this")
    public ItemBuilder setAuthor(String name) {
        if (this.resultMeta instanceof BookMeta)
            ((BookMeta) this.resultMeta).setAuthor(name);
        else
            new IllegalStateException("meta is not BookMeta").printStackTrace();
        return this;
    }

    /**
     * Set book title.
     *
     * @param title Title of the book.
     * @return this for chaining.
     * @throws IllegalStateException if this meta is not BookMeta
     */
    @Contract("_ -> this")
    public ItemBuilder setTitle(String title) {
        if (this.resultMeta instanceof BookMeta)
            ((BookMeta) this.resultMeta).setTitle(title);
        else
            new IllegalStateException("meta is not BookMeta").printStackTrace();
        return this;
    }

    /**
     * Set book title.
     *
     * @param page Index of the page to set.
     * @param text Text of the page.
     * @return this for chaining.
     * @throws IllegalStateException if this meta is not BookMeta
     */
    @Contract("_, _ -> this")
    public ItemBuilder setPage(int page, String text) {
        if (this.resultMeta instanceof BookMeta)
            ((BookMeta) this.resultMeta).setPage(page, text);
        else
            new IllegalStateException("meta is not BookMeta").printStackTrace();
        return this;
    }

    /**
     * Add Potion effect.
     *
     * @param effect Effect to add to Potion.
     * @return this for chaining.
     * @throws IllegalStateException if this meta is not PotionMeta
     */
    @Contract("_ -> this")
    public ItemBuilder addPotionEffect(PotionEffect effect) {
        if (this.resultMeta instanceof PotionMeta)
            ((PotionMeta) this.resultMeta).addCustomEffect(effect, true);
        else
            new IllegalStateException("meta is not PotionMeta").printStackTrace();
        return this;
    }

    @SuppressWarnings("deprecation")
    @Contract("_ -> this")
    public ItemBuilder setSkullOwner(String name) {
        if (this.resultMeta instanceof SkullMeta)
            ((SkullMeta) this.resultMeta).setOwner(name);
        else
            new IllegalStateException("meta is not SkullMeta").printStackTrace();
        return this;
    }

    @Contract("_ -> this")
    public ItemBuilder setSkullOwner(OfflinePlayer player) {
        if (this.resultMeta instanceof SkullMeta)
            ((SkullMeta) this.resultMeta).setOwningPlayer(player);
        else
            new IllegalStateException("meta is not SkullMeta").printStackTrace();
        return this;
    }

    @Contract("_ -> this")
    public ItemBuilder setDamage(int dmg) {
        if (this.resultMeta instanceof Damageable)
            ((Damageable) this.resultMeta).setDamage(dmg);
        else if (dmg != 0)
            new IllegalStateException("meta is not Damageable").printStackTrace();
        return this;
    }

    @Contract("_ -> this")
    public ItemBuilder setCustomModelData(Integer data) {
        this.resultMeta.setCustomModelData(data);
        return this;
    }

    @Contract("_ -> this")
    public ItemBuilder setAmount(int amount) {
        if (amount > 0)
            this.result.setAmount(amount);
        else
            new IllegalStateException("invalid amount (" + amount + ")").printStackTrace();
        return this;
    }

    @Contract(" -> new")
    public ItemBuilder clone() {
        ItemBuilder copy = new ItemBuilder(this.result);
        copy.resultMeta = this.resultMeta.clone();
        return copy;
    }

    @Contract("_, _ -> this")
    public ItemBuilder addPattern(DyeColor color, PatternType patternType) {
        if (this.resultMeta instanceof BannerMeta)
            ((BannerMeta) this.resultMeta).addPattern(new Pattern(color, patternType));
        else
            new IllegalStateException("meta is not BannerMeta").printStackTrace();
        return this;
    }

    /**
     * Update both title and lore with description
     *
     * @param description Description text
     * @param holders     Additional placeholders to replace in the format
     *                    "holder1", "value1", "holder2", "value2"...
     * @return this for chaining.
     */
    @Contract("_, _ -> this")
    public ItemBuilder setDescription(List<String> description, String... holders) {
        return setDescription(description, true, null, holders);
    }

    /**
     * update both title and lore
     *
     * @param description Text to set
     * @param color       Whether translate color codes or not
     * @param player      Player target for PlaceHolderAPI holders
     * @param holders     Additional placeholders to replace in the format
     *                    "holder1", "value1", "holder2", "value2"...
     * @return this for chaining.
     */
    @Contract("_, _, _, _ -> this")
    public ItemBuilder setDescription(List<String> description, boolean color, Player player, String... holders) {
        List<String> list = UtilsString.fix(description, player, color, holders);
        if (list == null || list.isEmpty()) {
            this.resultMeta.setDisplayName(null);
            this.resultMeta.setLore(null);
        } else if (list.size() == 1) {
            this.resultMeta.setDisplayName(list.get(0));
            this.resultMeta.setLore(null);
        } else {
            try {
                this.resultMeta.setDisplayName(list.remove(0));
            } catch (UnsupportedOperationException e) {
                list = new ArrayList<>(list);
                this.resultMeta.setDisplayName(list.remove(0));
            }
            this.resultMeta.setLore(list);
        }
        return this;
    }

    /**
     * Update both title and lore with description
     *
     * @param description Text to set
     * @param color       Whether translate color codes or not
     * @param holders     Additional placeholders to replace in the format
     *                    "holder1", "value1", "holder2", "value2"...
     * @return this for chaining.
     */
    @Contract("_, _, _ -> this")
    @Deprecated
    public ItemBuilder setDescription(List<String> description, boolean color, String... holders) {
        return setDescription(description, color, null, holders);
    }

    @Contract("_, _ -> this")
    @Deprecated
    public ItemBuilder setMiniDescription(List<String> description, String... holders) {
        return setMiniDescription(description, null, holders);
    }

    @Contract("_, _, _ -> this")
    @Deprecated
    public ItemBuilder setMiniDescription(List<String> description, Player player, String... holders) {
        List<String> list = UtilsString.fix(description, player, false, holders);
        if (list == null || list.isEmpty()) {
            this.resultMeta.setDisplayName("");
            this.resultMeta.setLore(null);
        } else if (list.size() == 1) {
            this.resultMeta.setLore(null);
            Map<String, Object> map = new LinkedHashMap<>(resultMeta.serialize());
            map.put("display-name", format(list.get(0)));
            map.put("==", "ItemMeta");
            this.resultMeta = (ItemMeta) ConfigurationSerialization.deserializeObject(map);
        } else {
            Map<String, Object> map = new LinkedHashMap<>(resultMeta.serialize());
            try {
                map.put("display-name", format(list.remove(0)));
            } catch (UnsupportedOperationException e) {
                list = new ArrayList<>(list);
                map.put("display-name", format(list.remove(0)));
            }
            for (int i = 0; i < list.size(); i++)
                list.set(i, format(list.get(i)));
            map.put("lore", list);
            map.put("==", "ItemMeta");
            this.resultMeta = (ItemMeta) ConfigurationSerialization.deserializeObject(map);
        }
        return this;
    }

    private String format(String text) {
        if (text == null)
            return null;
        text = UtilsString.fix(text, null, false);
        text = text.replace('§', '&');
        for (ChatColor color : ChatColor.values())
            text = text.replace("&" + color.toString().charAt(1),
                    (color.getColor() == null ? "" : "<reset>") +
                            "<" + color.getName()
                            .toLowerCase(Locale.ENGLISH) + ">");
        try {
            int from = 0;
            while (text.indexOf("&#", from) >= 0) {
                from = text.indexOf("&#", from) + 1;
                text = text.replace(text.substring(from - 1, from + 7),
                        "<reset><#" + text.substring(from, from + 7) + ">");
            }
        } catch (Throwable ignored) {
        }
        return GsonComponentSerializer.gson().serialize(MiniMessage.miniMessage().deserialize(text));
    }

    @Contract("_ -> this")
    public ItemBuilder addDescription(@NotNull DMessage message) {
        List<String> list = message.toJsonMulti();
        if (list == null || list.isEmpty())
            return this;
        Map<String, Object> map = new LinkedHashMap<>(resultMeta.serialize());
        List<String> lore = map.containsKey("lore") ? new ArrayList<>((List<String>) map.get("lore")) : new ArrayList<>();
        lore.addAll(list);
        map.put("lore", lore);
        map.put("==", "ItemMeta");
        this.resultMeta = (ItemMeta) ConfigurationSerialization.deserializeObject(map);
        return this;
    }

    @Contract("_, _ -> this")
    public ItemBuilder setPage(int page, @NotNull DMessage message) {
        if (this.resultMeta instanceof BookMeta)
            ((BookMeta) this.resultMeta).spigot().setPage(page, message.toBaseComponent());
        else
            new IllegalStateException("meta is not BookMeta").printStackTrace();
        return this;
    }

    @Contract("_ -> this")
    public ItemBuilder setPages(DMessage... messages) {
        if (this.resultMeta instanceof BookMeta)
            for (DMessage message : messages)
                addPage(message);
        else
            new IllegalStateException("meta is not BookMeta").printStackTrace();
        return this;
    }

    @Contract("_ -> this")
    public ItemBuilder addPage(@NotNull DMessage message) {
        if (this.resultMeta instanceof BookMeta)
            ((BookMeta) this.resultMeta).spigot().addPage(message.toBaseComponent());
        else
            new IllegalStateException("meta is not BookMeta").printStackTrace();
        return this;
    }

    @Contract("_, _, _ -> this")
    public ItemBuilder applyPlaceholders(@NotNull APlugin plugin, @Nullable Player target, String... placeholders) {
        DMessage msg = getDescription(plugin);
        msg.applyHolders(target, placeholders);
        return setDescription(msg);
    }

    public DMessage getDescription(@NotNull APlugin plugin) {
        DMessage msg = new DMessage(plugin, null);
        Map<String, Object> map = new LinkedHashMap<>(resultMeta.serialize());
        if (map.containsKey("display-name"))
            msg.append(MiniMessage.miniMessage().serialize(GsonComponentSerializer.gson().deserialize((String) map.get("display-name"))));
        msg.append("\n");
        if (map.containsKey("lore"))
            for (String line : (List<String>) map.get("lore"))
                msg.append("\n").append(MiniMessage.miniMessage().serialize(GsonComponentSerializer.gson().deserialize(line)));
        return msg;
    }

    @Contract("_ -> this")
    public ItemBuilder setDescription(@NotNull DMessage message) {
        List<String> list = message.toJsonMulti();
        if (list == null || list.isEmpty()) {
            this.resultMeta.setDisplayName("");
            this.resultMeta.setLore(null);
            return this;
        }
        if (list.size() == 1) {
            this.resultMeta.setLore(null);
            Map<String, Object> map = new LinkedHashMap<>(resultMeta.serialize());
            map.put("display-name", list.get(0));
            map.put("==", "ItemMeta");
            this.resultMeta = (ItemMeta) ConfigurationSerialization.deserializeObject(map);
            return this;
        }
        list = new ArrayList<>(list);
        Map<String, Object> map = new LinkedHashMap<>(resultMeta.serialize());
        map.put("display-name", list.remove(0));
        map.put("lore", list);
        map.put("==", "ItemMeta");
        this.resultMeta = (ItemMeta) ConfigurationSerialization.deserializeObject(map);
        return this;
    }

    @Contract("_, _, _ -> this")
    public <T, Z> ItemBuilder addNamespacedKey(@NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type, Z value) {
        resultMeta.getPersistentDataContainer().set(key, type, value);
        return this;
    }

    @Contract(" -> this")
    public ItemBuilder clearEnchantments() {
        for (Enchantment enchantment : resultMeta.getEnchants().keySet())
            resultMeta.removeEnchant(enchantment);
        return this;
    }

    @Contract("_ -> this")
    public ItemBuilder setColor(@Nullable DyeColor color) {
        if (color == null)
            return this;
        if (resultMeta instanceof LeatherArmorMeta meta) {
            meta.setColor(color.getColor());
            return this;
        } else if (resultMeta instanceof PotionMeta meta) {
            meta.setColor(color.getColor());
            return this;
        }
        int index = result.getType().name().indexOf("_", result.getType().name().startsWith("LIGHT_") ? 7 : 1);
        if (index > 0)
            try {
                switch (result.getType().name().substring(index)) {
                    case "_BANNER", "_BED", "_CANDLE", "_CARPET", "_CONCRETE", "_CONCRETE_POWDER", "_DYE", "_GLAZED_TERRACOTTA", "_SHULKER_BOX", "_STAINED_GLASS",
                            "_STAINED_GLASS_PANE", "_TERRACOTTA", "_WOOL" -> result.setType(Material.getMaterial(color.name() + result.getType().name().substring(index)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        return this;
    }


    @Contract("_ -> this")
    public ItemBuilder glow(@Nullable Boolean value) {
        if (Util.isVersionAfter(1,20,5))
            resultMeta.setEnchantmentGlintOverride(value);
        else
            if (value!=null)
                resultMeta.addEnchant(Enchantment.LURE,value?1:0,true);
        return this;
    }

}
