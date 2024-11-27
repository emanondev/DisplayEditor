package emanondev.displayeditor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

public class DMessage {

    private static final LegacyComponentSerializer UNGLY_LEGACY = LegacyComponentSerializer.legacySection().toBuilder().hexColors().useUnusualXRepeatedCharacterHexFormat().build();
    private final StringBuilder raw = new StringBuilder();
    private final APlugin plugin;
    private final CommandSender target;
    private Player papiTarget;

    public DMessage(@NotNull APlugin plugin) {
        this(plugin, null, null);
    }

    public DMessage(@NotNull APlugin plugin, @Nullable CommandSender target, @Nullable Player papiTarget) {
        this.plugin = plugin;
        this.target = target;
        this.papiTarget = papiTarget;
    }

    public DMessage(@NotNull APlugin plugin, @Nullable CommandSender target) {
        this(plugin, target, target instanceof Player ? (Player) target : null);
    }

    @Contract("_ -> this")
    @NotNull
    public DMessage setPapiTarget(@Nullable Player target) {
        this.papiTarget = target;
        return this;
    }

    @Contract("_ -> this")
    @NotNull
    public DMessage append(@Nullable ChatColor color) {
        if (color != null)
            if (color.getColor() != null)
                append(color.getColor());
            else
                appendDirectly("<").appendDirectly(color).appendDirectly(">");
        return this;
    }

    @Contract("_ -> this")
    @NotNull
    public DMessage append(@Nullable java.awt.Color color) {
        if (color != null)
            appendDirectly("<").appendDirectly(color).appendDirectly(">");
        return this;
    }

    @Contract("_ -> this")
    @NotNull
    private DMessage appendDirectly(@Nullable String text) {
        if (text != null)
            raw.append(text);
        return this;
    }

    @Contract("_ -> this")
    @NotNull
    private DMessage appendDirectly(@NotNull ChatColor color) {
        raw.append(color.name()); //for formats
        return this;
    }

    @Contract("_ -> this")
    @NotNull
    private DMessage appendDirectly(@NotNull java.awt.Color color) {
        raw.append(color.getRed() < 16 ? "#0" : "#").append(Integer.toHexString(color.getRed()))
                .append(color.getGreen() < 16 ? "0" : "").append(Integer.toHexString(color.getGreen()))
                .append(color.getBlue() < 16 ? "0" : "").append(Integer.toHexString(color.getBlue()));
        return this;
    }

    @Contract("_ -> this")
    @NotNull
    public DMessage append(@Nullable org.bukkit.ChatColor color) {
        if (color != null)
            appendDirectly("<").appendDirectly(color).appendDirectly(">");
        return this;
    }

    @Contract("_ -> this")
    @NotNull
    private DMessage appendDirectly(@NotNull org.bukkit.ChatColor color) {
        raw.append(color.name());
        return this;
    }

    @Contract("_ -> this")
    @NotNull
    public DMessage append(@Nullable DyeColor color) {
        if (color != null)
            return append(color.getColor());
        return this;
    }

    @Contract("_ -> this")
    @NotNull
    public DMessage append(@Nullable Color color) {
        if (color != null)
            appendDirectly("<").appendDirectly(color).appendDirectly(">");
        return this;
    }

    @Contract("_ -> this")
    @NotNull
    private DMessage appendDirectly(@NotNull Color color) {
        raw.append(color.getRed() < 16 ? "#0" : "#").append(Integer.toHexString(color.getRed()))
                .append(color.getGreen() < 16 ? "0" : "").append(Integer.toHexString(color.getGreen()))
                .append(color.getBlue() < 16 ? "0" : "").append(Integer.toHexString(color.getBlue()));
        return this;
    }

    /**
     * Append the language text at give path
     * <p>
     * Works for both Strings and Lists of Strings
     *
     * @return this
     */
    @Contract("_, _ -> this")
    @NotNull
    public DMessage appendLang(@NotNull String path, String... holders) {
        if (!plugin.getLanguageConfig(target).contains(path))
            plugin.log("Debug: Missing language message at &e" + path + " &fof file &e" + plugin.getLanguageConfig(target).getFileName());
        if (plugin.getLanguageConfig(target).isList(path))
            return append(plugin.getLanguageConfig(target).getMultiMessage(path, null,null,false, holders));
        return append(plugin.getLanguageConfig(target).getMessage(path, null,null,false, holders));
    }

    @Contract("_, _ -> this")
    @NotNull
    public DMessage append(@Nullable List<String> text, String... holders) {
        if (text == null || text.isEmpty())
            return this;
        return append(String.join("\n", text), holders);
    }

    @Contract("_, _ -> this")
    @NotNull
    public DMessage append(@Nullable String text, String... holders) {
        if (text != null)
            raw.append(format(text, holders));
        return this;
    }

    @NotNull
    private String format(@NotNull String text, String... holders) {
        text = UtilsString.fix(text, papiTarget, false, holders);
        text = text.replace('§', '&');
        for (ChatColor color : ChatColor.values()) //formats
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
        return text;
    }

    @Contract("_, _, _ -> this")
    @NotNull
    public DMessage appendHover(@Nullable List<String> hoverText, @Nullable String message, String... holders) {
        if (message != null && !message.isEmpty())
            if (hoverText != null)
                appendDirectly("<hover:show_text:\"").append(String.join("\n", hoverText).replace("\"", "\\\""), holders).appendDirectly("\">").append(message, holders).appendDirectly("</hover>");
            else
                append(message);
        return this;
    }

    @Contract("_, _, _ -> this")
    @NotNull
    public DMessage appendHover(@Nullable List<String> hoverText, @Nullable List<String> message, String... holders) {
        if (message != null && !message.isEmpty())
            if (hoverText != null)
                appendDirectly("<hover:show_text:\"").append(String.join("\n", hoverText).replace("\"", "\\\""), holders).appendDirectly("\">").append(message, holders).appendDirectly("</hover>");
            else
                append(message);
        return this;
    }

    @Contract("_, _, _ -> this")
    @NotNull
    public DMessage appendHover(@Nullable List<String> hoverText, @Nullable DMessage message, String... holders) {
        if (message != null && !message.raw.isEmpty())
            if (hoverText != null)
                appendDirectly("<hover:show_text:\"").append(String.join("\n", hoverText).replace("\"", "\\\""), holders).appendDirectly("\">").append(message).appendDirectly("</hover>");
            else
                append(message);
        return this;
    }

    @Contract("_ -> this")
    @NotNull
    public DMessage append(@Nullable DMessage message) {
        if (message != null && !message.raw.isEmpty())
            raw.append(message.raw);
        return this;
    }

    @Contract("_, _, _ -> this")
    @NotNull
    public DMessage appendHover(@Nullable String hoverText, @Nullable String message, String... holders) {
        if (message != null && !message.isEmpty())
            if (hoverText != null)
                appendDirectly("<hover:show_text:\"").append(hoverText.replace("\"", "\\\""), holders).appendDirectly("\">").append(message, holders).appendDirectly("</hover>");
            else
                append(message);
        return this;
    }

    @Contract("_, _, _ -> this")
    @NotNull
    public DMessage appendHover(@Nullable String hoverText, @Nullable List<String> message, String... holders) {
        if (message != null && !message.isEmpty())
            if (hoverText != null)
                appendDirectly("<hover:show_text:\"").append(hoverText.replace("\"", "\\\""), holders).appendDirectly("\">").append(message, holders).appendDirectly("</hover>");
            else
                append(message);
        return this;
    }

    @Contract("_, _, _ -> this")
    @NotNull
    public DMessage appendHover(@Nullable String hoverText, @Nullable DMessage message, String... holders) {
        if (message != null && !message.raw.isEmpty())
            if (hoverText != null)
                appendDirectly("<hover:show_text:\"").append(hoverText.replace("\"", "\\\""), holders).appendDirectly("\">").append(message).appendDirectly("</hover>");
            else
                append(message);
        return this;
    }

    @Contract("_, _, _ -> this")
    @NotNull
    public DMessage appendHover(@Nullable DMessage hoverText, @Nullable String message, String... holders) {
        if (message != null && !message.isEmpty())
            if (hoverText != null)
                appendDirectly("<hover:show_text:\"").append(hoverText).appendDirectly("\">").append(message, holders).appendDirectly("</hover>");
            else
                append(message);
        return this;
    }

    @Contract("_, _, _ -> this")
    @NotNull
    public DMessage appendHover(@Nullable DMessage hoverText, @Nullable List<String> message, String... holders) {
        if (message != null && !message.isEmpty())
            if (hoverText != null)
                appendDirectly("<hover:show_text:\"").append(hoverText).appendDirectly("\">").append(message, holders).appendDirectly("</hover>");
            else
                append(message);
        return this;
    }

    @Contract("_, _ -> this")
    @NotNull
    public DMessage appendHover(@Nullable DMessage hoverText, @Nullable DMessage message) {
        if (message != null && !message.raw.isEmpty())
            if (hoverText != null)
                appendDirectly("<hover:show_text:\"").append(hoverText).appendDirectly("\">").append(message).appendDirectly("</hover>");
            else
                append(message);
        return this;
    }

    @Contract("_, _, _ -> this")
    @NotNull
    public DMessage appendRunCommand(@Nullable String command, @Nullable String message, String... holders) {
        if (message != null && !message.isEmpty())
            if (command != null && !command.isEmpty())
                appendDirectly("<click:run_command:\"").append(command.replace("\"", "\\\""), holders).appendDirectly("\">").append(message, holders).appendDirectly("</click>");
            else
                append(message);
        return this;
    }

    @Contract("_, _, _ -> this")
    @NotNull
    public DMessage appendRunCommand(@Nullable String command, @Nullable DMessage message, String... holders) {
        if (message != null && !message.raw.isEmpty())
            if (command != null && !command.isEmpty())
                appendDirectly("<click:run_command:\"").append(command.replace("\"", "\\\""), holders).appendDirectly("\">").append(message).appendDirectly("</click>");
            else
                append(message);
        return this;
    }

    @Contract("_, _, _ -> this")
    @NotNull
    public DMessage appendSuggest(@Nullable String suggestion, @Nullable String message, String... holders) {
        if (message != null && !message.isEmpty())
            if (suggestion != null)
                appendDirectly("<click:suggest_command:\"").append(suggestion.replace("\"", "\\\""), holders).appendDirectly("\">").append(message, holders).appendDirectly("</click>");
            else
                append(message);
        return this;
    }

    @Contract("_, _, _ -> this")
    @NotNull
    public DMessage appendSuggest(@Nullable String suggestion, @Nullable DMessage message, String... holders) {
        if (message != null && !message.raw.isEmpty())
            if (suggestion != null)
                appendDirectly("<click:suggest_command:\"").append(suggestion.replace("\"", "\\\""), holders).appendDirectly("\">").append(message).appendDirectly("</click>");
            else
                append(message);
        return this;
    }

    @Contract("_, _, _ -> this")
    @NotNull
    public DMessage appendAddSuggest(@Nullable String suggestion, @Nullable String message, String... holders) {
        if (message != null && !message.isEmpty())
            if (suggestion != null)
                appendDirectly("<insert:\"").append(suggestion.replace("\"", "\\\""), holders).appendDirectly("\">").append(message, holders).appendDirectly("</insert>");
            else
                append(message);
        return this;
    }

    @Contract("_, _, _ -> this")
    @NotNull
    public DMessage appendAddSuggest(@Nullable String suggestion, @Nullable DMessage message, String... holders) {
        if (message != null && !message.raw.isEmpty())
            if (suggestion != null)
                appendDirectly("<insert:\"").append(suggestion.replace("\"", "\\\""), holders).appendDirectly("\">").append(message).appendDirectly("</insert>");
            else
                append(message);
        return this;
    }

    @Contract("_, _, _ -> this")
    @NotNull
    public DMessage appendCopy(@Nullable String copy, @Nullable String message, String... holders) {
        if (message != null && !message.isEmpty())
            if (copy != null)
                appendDirectly("<click:copy_to_clipboard:\"").append(copy.replace("\"", "\\\""), holders).appendDirectly("\">").append(message, holders).appendDirectly("</click>");
            else
                append(message);
        return this;
    }

    @Contract("_, _, _ -> this")
    @NotNull
    public DMessage appendCopy(@Nullable String copy, @Nullable DMessage message, String... holders) {
        if (message != null && !message.raw.isEmpty())
            if (copy != null)
                appendDirectly("<click:copy_to_clipboard:\"").append(copy.replace("\"", "\\\""), holders).appendDirectly("\">").append(message).appendDirectly("</click>");
            else
                append(message);
        return this;
    }

    @Contract("_, _, _ -> this")
    @NotNull
    public DMessage appendGoPage(int page, @Nullable String message, String... holders) {
        if (message != null && !message.isEmpty())
            if (page > 0)
                appendDirectly("<click:change_page:").appendDirectly(String.valueOf(page)).appendDirectly(">").append(message, holders).appendDirectly("</click>");
            else
                append(message);
        return this;
    }

    @Contract("_, _ -> this")
    @NotNull
    public DMessage appendGoPage(int page, @Nullable DMessage message) {
        if (message != null && !message.raw.isEmpty())
            if (page > 0)
                appendDirectly("<click:change_page:").appendDirectly(String.valueOf(page)).appendDirectly(">").append(message).appendDirectly("</click>");
            else
                append(message);
        return this;
    }

    @Contract("_, _, _ -> this")
    @NotNull
    public DMessage appendOpenUrl(@Nullable String url, @Nullable String message, String... holders) {
        if (message != null && !message.isEmpty())
            if (url != null && !url.isEmpty())
                appendDirectly("<click:open_url:'").append(url, holders).appendDirectly("'>").append(message, holders).appendDirectly("</click>");
            else
                append(message);
        return this;
    }

    @Contract("_, _, _ -> this")
    @NotNull
    public DMessage appendOpenUrl(@Nullable String url, @Nullable DMessage message, String... holders) {
        if (message != null && !message.raw.isEmpty())
            if (url != null && !url.isEmpty())
                appendDirectly("<click:open_url:'").append(url, holders).appendDirectly("'>").append(message).appendDirectly("</click>");
            else
                append(message);
        return this;
    }

    @Override
    public String toString() {
        return raw.toString();
    }

    @Contract(" -> this")
    @NotNull
    public DMessage newLine() {
        return appendDirectly("\n");
    }

    @Contract("_, _ -> this")
    @NotNull
    public DMessage gradient(@Nullable String message, Color... colors) {
        if (message == null || message.isEmpty())
            return this;
        if (colors.length == 0)
            return append(message);
        return gradient(colors).append(message).appendDirectly("</gradient>");
    }

    @Contract("_ -> this")
    @NotNull
    public DMessage gradient(Color... colors) {
        if (colors.length == 0)
            return this;
        appendDirectly("<gradient");
        for (Color color : colors)
            appendDirectly(":").appendDirectly(color);
        return appendDirectly(">");
    }

    @Contract("_, _ -> this")
    @NotNull
    public DMessage gradient(@Nullable DMessage message, Color... colors) {
        if (message == null || message.raw.isEmpty())
            return this;
        if (colors.length == 0)
            return append(message);
        return gradient(colors).append(message).appendDirectly("</gradient>");
    }

    @Contract("_, _ -> this")
    @NotNull
    public DMessage gradient(@Nullable String message, java.awt.Color... colors) {
        if (message == null || message.isEmpty())
            return this;
        if (colors.length == 0)
            return append(message);
        return gradient(colors).append(message).appendDirectly("</gradient>");
    }

    @Contract("_ -> this")
    @NotNull
    public DMessage gradient(java.awt.Color... colors) {
        if (colors.length == 0)
            return this;
        appendDirectly("<gradient");
        for (java.awt.Color color : colors)
            appendDirectly(":").appendDirectly(color);
        return appendDirectly(">");
    }

    @Contract("_, _ -> this")
    @NotNull
    public DMessage gradient(@Nullable DMessage message, java.awt.Color... colors) {
        if (message == null || message.raw.isEmpty())
            return this;
        if (colors.length == 0)
            return append(message);
        return gradient(colors).append(message).appendDirectly("</gradient>");
    }

    @Contract("_, _ -> this")
    @NotNull
    public DMessage gradient(@Nullable String message, ChatColor... colors) {
        if (message == null || message.isEmpty())
            return this;
        if (colors.length == 0)
            return append(message);
        return gradient(colors).append(message).appendDirectly("</gradient>");
    }

    @Contract("_ -> this")
    @NotNull
    public DMessage gradient(ChatColor... colors) {
        if (colors.length == 0)
            return this;
        appendDirectly("<gradient");
        for (ChatColor color : colors)
            appendDirectly(":").appendDirectly(color);
        return appendDirectly(">");
    }

    @Contract("_, _ -> this")
    @NotNull
    public DMessage gradient(@Nullable DMessage message, ChatColor... colors) {
        if (message == null || message.raw.isEmpty())
            return this;
        if (colors.length == 0)
            return append(message);
        return gradient(colors).append(message).appendDirectly("</gradient>");
    }

    @Contract("_, _ -> this")
    @NotNull
    public DMessage gradient(@Nullable String message, org.bukkit.ChatColor... colors) {
        if (message == null || message.isEmpty())
            return this;
        if (colors.length == 0)
            return append(message);
        return gradient(colors).append(message).appendDirectly("</gradient>");
    }

    @Contract("_ -> this")
    @NotNull
    public DMessage gradient(org.bukkit.ChatColor... colors) {
        if (colors.length == 0)
            return this;
        appendDirectly("<gradient");
        for (org.bukkit.ChatColor color : colors)
            appendDirectly(":").appendDirectly(color);
        return appendDirectly(">");
    }

    @Contract("_, _ -> this")
    @NotNull
    public DMessage gradient(@Nullable DMessage message, org.bukkit.ChatColor... colors) {
        if (message == null || message.raw.isEmpty())
            return this;
        if (colors.length == 0)
            return append(message);
        return gradient(colors).append(message).appendDirectly("</gradient>");
    }

    @Contract(pure = true)
    public void send() {
        send(target);
    }

    @Contract(pure = true)
    public void send(CommandSender target) {
        if (target != null && !raw.isEmpty())
            if (target instanceof Player player)
                plugin.adventure().player(player).sendMessage(toMiniComponent());
            else //???
                plugin.adventure().sender(target).sendMessage(toMiniComponent());
    }

    @Contract(pure = true)
    public Component toMiniComponent() {
        return MiniMessage.miniMessage().deserialize(raw.toString());
    }

    @Contract(pure = true)
    public void send(@NotNull Collection<? extends CommandSender> targets) {
        targets.forEach(this::send);
    }

    @Contract(pure = true)
    public <T extends CommandSender> void send(@NotNull Collection<T> targets, @NotNull Predicate<T> shouldSend) {
        targets.forEach((t) -> {
            if (shouldSend.test(t)) send(t);
        });
    }

    @Contract(pure = true)
    public void sendActionBar() {
        sendActionBar(target);
    }

    @Contract(pure = true)
    public void sendActionBar(CommandSender target) {
        if (target instanceof Player player && !raw.isEmpty())
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, toBaseComponent());
            /*if (target instanceof Player player)
                plugin.adventure().player(player).sendActionBar(toMiniComponent());
            else //???
                plugin.adventure().sender(target).sendActionBar(toMiniComponent());*/
    }

    @Contract(pure = true)
    public void sendActionBar(@NotNull Collection<? extends CommandSender> targets) {
        targets.forEach(this::sendActionBar);
    }

    @Contract(pure = true)
    public <T extends CommandSender> void sendActionBar(@NotNull Collection<T> targets, @NotNull Predicate<T> shouldSend) {
        targets.forEach((t) -> {
            if (shouldSend.test(t)) send(t);
        });
    }

    @Contract(pure = true)
    public String toLegacy() {
        return UNGLY_LEGACY.serialize(toMiniComponent());
    }

    /**
     * Note: this also force non italic white text, since this parte of the api is
     */
    @Contract(pure = true)
    public List<String> toJsonMulti() {
        return toJsonMulti(true);
    }

    /**
     * @param forceFormat force Non-Italic White text
     */
    @Contract(pure = true)
    public List<String> toJsonMulti(boolean forceFormat) {
        String[] lines = raw.toString().replaceAll("(?i)<newline>", "\n").split("\n");
        for (int i = 0; i < lines.length; i++)
            lines[i] = GsonComponentSerializer.gson().serialize(MiniMessage.miniMessage()
                    .deserialize((lines[i].isEmpty() || !forceFormat || lines[i].startsWith("<!i><white>") ? "" : "<!i><white>") + lines[i]));
        return List.of(lines);
    }

    @Contract(pure = true)
    public List<String> toStringList() {
        String[] lines = raw.toString().replaceAll("(?i)<newline>", "\n").split("\n");
        return List.of(lines);
    }

    @Contract(pure = true)
    public BaseComponent[] toBaseComponent() {
        return ComponentSerializer.parse(toJson());
    }

    @Contract(pure = true)
    public String toJson() {
        return GsonComponentSerializer.gson().serialize(toMiniComponent());
    }

    @Contract("_ -> this")
    @NotNull
    public DMessage applyHolders(String... placeholders) {
        return applyHolders(null, placeholders);
    }

    @Contract("_, _ -> this")
    @NotNull
    public DMessage applyHolders(@Nullable Player target, String... placeholders) { //TODO may do better
        String str = UtilsString.fix(raw.toString(), target, false, placeholders);
        raw.delete(0, raw.length());
        raw.append(str);
        return this;
    }

}

















