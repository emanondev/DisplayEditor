package emanondev.displayeditor;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Util {

    private static final int MAX_COMPLETES = 100;
    private static final int GAME_MAIN_VERSION = Integer.parseInt(
            Bukkit.getBukkitVersion().split("-")[0].split("\\.")[0]);
    private static final int GAME_VERSION = Integer.parseInt(
            Bukkit.getBukkitVersion().split("-")[0].split("\\.")[1]);
    private static final int GAME_SUB_VERSION = Bukkit.getBukkitVersion().split("-")[0].split("\\.").length < 3 ? 0 : Integer.parseInt(
            Bukkit.getBukkitVersion().split("-")[0].split("\\.")[2]);


    public static void flashEntities(@NotNull Player player, @NotNull Entity entity) {
        flashEntities(player, List.of(entity));
    }

    public static void flashEntities(@NotNull Player player, @NotNull Collection<Entity> entities) {
        if (entities.isEmpty())
            return;
        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (i > 10 || !player.isValid())
                    this.cancel();
                entities.forEach((en -> {
                    if (en.isValid())
                        if (i % 2 != 0)
                            player.showEntity(DisplayEditor.get(), en);
                        else
                            player.hideEntity(DisplayEditor.get(), en);
                }));
                i++;
            }
        }.runTaskTimer(DisplayEditor.get(), 2L, 2L);
    }

    @NotNull
    public static <T extends Enum<T>> List<String> complete(String prefix, @NotNull Class<T> enumClass) {
        prefix = prefix.toUpperCase();
        ArrayList<String> results = new ArrayList<>();
        int c = 0;
        for (T el : enumClass.getEnumConstants())
            if (el.toString().startsWith(prefix)) {
                results.add(el.toString().toLowerCase(Locale.ENGLISH));
                c++;
                if (c > MAX_COMPLETES)
                    return results;
            }
        return results;
    }

    @NotNull
    public static <T extends Enum<T>> List<String> complete(String prefix, @NotNull Class<T> type,
                                                            @NotNull Predicate<T> predicate) {
        prefix = prefix.toUpperCase();
        ArrayList<String> results = new ArrayList<>();
        int c = 0;
        for (T el : type.getEnumConstants())
            if (predicate.test(el) && el.toString().startsWith(prefix)) {
                results.add(el.toString().toLowerCase(Locale.ENGLISH));
                c++;
                if (c > MAX_COMPLETES)
                    return results;
            }
        return results;
    }

    @NotNull
    public static <T> List<String> complete(String prefix, @NotNull Collection<T> values, @NotNull Function<T,String> converter,
                                                            @NotNull Predicate<T> predicate) {
        prefix = prefix.toLowerCase(Locale.ENGLISH);
        ArrayList<String> results = new ArrayList<>();
        int c = 0;
        for (T el : values)
            if (predicate.test(el) && converter.apply(el).toLowerCase(Locale.ENGLISH).startsWith(prefix)) {
                results.add(converter.apply(el).toLowerCase(Locale.ENGLISH));
                c++;
                if (c > MAX_COMPLETES)
                    return results;
            }
        return results;
    }

    @NotNull
    public static List<String> complete(String prefix, String... list) {
        prefix = prefix.toLowerCase(Locale.ENGLISH);
        ArrayList<String> results = new ArrayList<>();
        int c = 0;
        for (String value : list)
            if (value.toLowerCase(Locale.ENGLISH).startsWith(prefix)) {
                results.add(value);
                c++;
                if (c > MAX_COMPLETES)
                    return results;
            }
        return results;
    }

    public static List<String> complete(String prefix, Collection<String> list) {
        prefix = prefix.toLowerCase(Locale.ENGLISH);
        ArrayList<String> results = new ArrayList<>();
        int c = 0;
        for (String value : list)
            if (value.toLowerCase(Locale.ENGLISH).startsWith(prefix)) {
                results.add(value);
                c++;
                if (c > MAX_COMPLETES)
                    return results;
            }
        return results;
    }

    public static List<String> completePlayers(String prefix) {
        ArrayList<String> names = new ArrayList<>();
        final String text = prefix.toLowerCase(Locale.ENGLISH);
        Bukkit.getOnlinePlayers().forEach((p) -> {
            if (p.getName().toLowerCase(Locale.ENGLISH).startsWith(text))
                names.add(p.getName());
        });
        return names;
    }

    public static void sendMessage(CommandSender sender, String message) {
        if (message == null || message.isEmpty())
            return;
        sender.sendMessage(message);
    }

    public static void sendMessage(CommandSender sender, BaseComponent... message) {
        if (sender instanceof Player)
            ((Player) sender).spigot().sendMessage(message);
        else
            sender.sendMessage(BaseComponent.toPlainText(message));
    }

    public static void logToFile(String message) {
        try {
            File dataFolder = DisplayEditor.get().getDataFolder();
            if (!dataFolder.exists())
                dataFolder.mkdir();
            Date date = new Date();
            File saveTo = new File(DisplayEditor.get().getDataFolder(),
                    "logs" + File.separatorChar
                            + new SimpleDateFormat(
                            DisplayEditor.get().getConfig().loadMessage("log.file-format", "yyyy.MM.dd", false))
                            .format(date)
                            + ".log");
            if (!saveTo.getParentFile().exists()) { // Create parent folders if they don't exist
                saveTo.getParentFile().mkdirs();
            }
            if (!saveTo.exists())
                saveTo.createNewFile();

            FileWriter fw = new FileWriter(saveTo, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(new SimpleDateFormat(
                    DisplayEditor.get().getConfig().loadMessage("log.log-date-format", "[dd.MM.yyyy HH:mm:ss]", false))
                    .format(date)
                    + message);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasBannedWords(Player user, String text) {
        if (user.hasPermission("displayeditor.bypass.censure"))
            return false;
        String message = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', text.toLowerCase(Locale.ENGLISH)));
        for (String regex : DisplayEditor.get().getConfig().getStringList("blocked.regex"))
            if (Pattern.compile(regex).matcher(message).find()) {
                if (DisplayEditor.get().getConfig().getBoolean("blocked.log.console", true))
                    sendMessage(Bukkit.getConsoleSender(), "user: §e" + user.getName() + "§r attempt to write '" + text
                            + "'§r (stripped by colors and lowcased) was blocked by regex: §e" + regex);
                if (DisplayEditor.get().getConfig().getBoolean("blocked.log.file", true))
                    logToFile("user: '" + user.getName() + "' attempt to write '" + text
                            + "' (stripped by colors and lowcased to '" + message + "') was blocked by regex: '" + regex
                            + "'");
                sendMessage(user,
                        DisplayEditor.get().getLanguageConfig(user).loadMessage("blocked-by-censure", "", null, true));
                return true;
            }
        for (String bannedWord : DisplayEditor.get().getConfig().getStringList("blocked.words"))
            if (message.contains(bannedWord.toLowerCase(Locale.ENGLISH))) {
                if (DisplayEditor.get().getConfig().getBoolean("blocked.log.console", true))
                    sendMessage(Bukkit.getConsoleSender(),
                            "user: §e" + user.getName() + "§r attempt to write '" + text
                                    + "'§r (stripped by colors and lowcased) was blocked by word: §e"
                                    + bannedWord.toLowerCase(Locale.ENGLISH));
                if (DisplayEditor.get().getConfig().getBoolean("blocked.log.file", true))
                    logToFile("user: '" + user.getName() + "' attempt to write '" + text
                            + "' (stripped by colors and lowcased to '" + message + "') was blocked by word: '"
                            + bannedWord.toLowerCase(Locale.ENGLISH) + "'");
                sendMessage(user,
                        DisplayEditor.get().getLanguageConfig(user).loadMessage("blocked-by-censure", "", null, true));
                return true;
            }
        return false;

    }

    public static String formatText(CommandSender sender, String text, String basePermission) {
        text = ChatColor.translateAlternateColorCodes('&', text);
        if (basePermission != null) {
            for (ChatColor style : ChatColor.values())
                if (style.isFormat()) {
                    if (!sender.hasPermission(basePermission + ".format." + style.name().toLowerCase(Locale.ENGLISH)))
                        text = text.replaceAll(style.toString(), "");
                } else if (!sender.hasPermission(basePermission + ".color." + style.name().toLowerCase(Locale.ENGLISH)))
                    text = text.replaceAll(style.toString(), "");
            if (sender.hasPermission(basePermission + ".color.hexa")) {
                try {
                    int from = 0;
                    while (text.indexOf("&#", from) >= 0) {
                        from = text.indexOf("&#", from) + 1;
                        text = text.replace(text.substring(from - 1, from + 7),
                                net.md_5.bungee.api.ChatColor.of(text.substring(from, from + 7)).toString());
                    }
                } catch (Throwable t) {
                }
            }
        }
        return text;

    }

    /**
     * for pre 1.13 compatibility
     *
     * @param color color
     * @return An ItemStack of selected Dye
     */
    public static ItemStack getDyeItemFromColor(DyeColor color) {
        return new ItemStack(Material.valueOf(color.name() + "_DYE"));
    }

    /**
     * for pre 1.13 compatibility
     *
     * @param color color
     * @return An ItemStack of selected Dyed wool
     */
    public static ItemStack getWoolItemFromColor(DyeColor color) {
        return new ItemStack(Material.valueOf(color.name() + "_WOOL"));
    }

    public static boolean isAirOrNull(ItemStack item) {
        return item == null || item.getType() == Material.AIR;
    }

    /**
     * Inclusive
     * isVersionUpTo(1,9) on 1.9.0 is true
     */
    public static boolean isVersionUpTo(int mainVersion, int version) {
        return isVersionUpTo(mainVersion, version, 99);
    }

    /**
     * Inclusive
     * isVersionUpTo(1,9,4) on 1.9.4 is true
     */
    public static boolean isVersionUpTo(int mainVersion, int version, int subVersion) {
        if (GAME_MAIN_VERSION > mainVersion)
            return false;
        if (GAME_MAIN_VERSION < mainVersion)
            return true;
        if (GAME_VERSION > version)
            return false;
        if (GAME_VERSION < version)
            return true;
        return GAME_SUB_VERSION <= subVersion;
    }

    /**
     * Inclusive
     * isVersionAfter(1,9) on 1.9.0 is true
     */
    public static boolean isVersionAfter(int mainVersion, int version) {
        return isVersionAfter(mainVersion, version, 0);
    }

    /**
     * Inclusive
     * isVersionAfter(1,9,4) on 1.9.4 is true
     */
    public static boolean isVersionAfter(int mainVersion, int version, int subVersion) {
        if (GAME_MAIN_VERSION < mainVersion)
            return false;
        if (GAME_MAIN_VERSION > mainVersion)
            return true;
        if (GAME_VERSION < version)
            return false;
        if (GAME_VERSION > version)
            return true;
        return GAME_SUB_VERSION >= subVersion;
    }

    /**
     * Inclusive
     */
    public static boolean isVersionInRange(int mainVersionMin, int versionMin,
                                           int mainVersionMax, int versionMax) {
        return isVersionInRange(mainVersionMin, versionMin, 0,
                mainVersionMax, versionMax, 99);
    }

    /**
     * Inclusive
     */
    public static boolean isVersionInRange(int mainVersionMin, int versionMin, int subVersionMin,
                                           int mainVersionMax, int versionMax, int subVersionMax) {
        return isVersionAfter(mainVersionMin, versionMin, subVersionMin)
                && isVersionUpTo(mainVersionMax, versionMax, subVersionMax);
    }

    public static boolean hasPaperAPI() {
        try {
            Class.forName("com.destroystokyo.paper.VersionHistoryManager$VersionData");
            return true;
        } catch (NoClassDefFoundError | ClassNotFoundException ex) {
            return false;
        }
    }

}
