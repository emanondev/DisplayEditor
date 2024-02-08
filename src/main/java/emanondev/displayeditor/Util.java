package emanondev.displayeditor;

import emanondev.displayeditor.compability.MiniMessageUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Util {
    private static final int MAX_COMPLETES = 100;
    private static final int GAME_MAIN_VERSION = Integer.parseInt(
            Bukkit.getServer().getClass().getPackage().getName().substring(
                            Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1)
                    .split("_")[0].substring(1));
    private static final int GAME_VERSION = Integer.parseInt(
            Bukkit.getServer().getClass().getPackage().getName().substring(
                            Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1)
                    .split("_")[1]);
    private static final int GAME_SUB_VERSION = Integer.parseInt(
            Bukkit.getServer().getClass().getPackage().getName().substring(
                            Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1)
                    .split("_")[2].substring(1));

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

    public static @NotNull <T extends Enum<T>> List<String> complete(String prefix, @NotNull Class<T> type,
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

    public static boolean isAllowedRenameItem(CommandSender sender, Material type) {
        if (sender.hasPermission("itemedit.bypass.rename_type_restriction"))
            return true;

        List<String> values = DisplayEditor.get().getConfig().getStringList("blocked.type-blocked-rename");
        if (values.isEmpty())
            return true;
        String id = type.name();
        for (String name : values)
            if (id.equalsIgnoreCase(name)) {
                sendMessage(sender,
                        DisplayEditor.get().getLanguageConfig(sender).loadMessage("blocked-by-type-restriction", "", null, true));
                return false;
            }
        return true;
    }

    /**
     * for pre 1.13 compatibility
     *
     * @param color color
     * @return An ItemStack of selected Dye
     */
    @SuppressWarnings("deprecation")
    public static ItemStack getDyeItemFromColor(DyeColor color) {
        try {
            return new ItemStack(Material.valueOf(color.name() + "_DYE"));
        } catch (Exception e) {
            return new ItemStack(Material.valueOf("INK_SACK"), 1, (short) 0, getData(color));
        }
    }

    /**
     * for pre 1.13 compatibility
     *
     * @param color color
     * @return An ItemStack of selected Dyed wool
     */
    @SuppressWarnings("deprecation")
    public static ItemStack getWoolItemFromColor(DyeColor color) {
        try {
            return new ItemStack(Material.valueOf(color.name() + "_WOOL"));
        } catch (Exception e) {
            return new ItemStack(Material.valueOf("WOOL"), 1, (short) 0, getData(color));
        }
    }

    private static Byte getData(DyeColor color) {
        return switch (color.name()) { //Silver
            case "BLACK" -> 0;
            case "BLUE" -> 4;
            case "BROWN" -> 3;
            case "CYAN" -> 6;
            case "GRAY" -> 8;
            case "GREEN" -> 2;
            case "LIGHT_BLUE" -> 12;
            case "LIME" -> 10;
            case "MAGENTA" -> 13;
            case "ORANGE" -> 14;
            case "PINK" -> 9;
            case "PURPLE" -> 5;
            case "RED" -> 1;
            case "SILVER", "LIGHT_GRAY" -> 7;
            case "WHITE" -> 15;
            case "YELLOW" -> 11;
            default -> throw new IllegalStateException();
        };
    }

    public static boolean isAirOrNull(ItemStack item) {
        return item == null || item.getType() == Material.AIR;
    }

    /**
     * Inclusive
     * isVersionUpTo(1,9) on 1.9.0 is true
     */
    public static boolean isVersionUpTo(int mainGameVersion, int gameVersion) {
        return isVersionUpTo(mainGameVersion, gameVersion, 99);
    }

    /**
     * Inclusive<br>
     * isVersionUpTo(1,9,4) on 1.9.4 is true<br>
     * isVersionUpTo(1,9,4) on 1.10.0 is false
     */
    public static boolean isVersionUpTo(int mainGameVersion, int gameVersion, int gameSubVersion) {
        if (GAME_MAIN_VERSION > mainGameVersion)
            return false;
        if (GAME_MAIN_VERSION < mainGameVersion)
            return true;
        if (GAME_VERSION > gameVersion)
            return false;
        if (GAME_VERSION < gameVersion)
            return true;
        return GAME_SUB_VERSION <= gameSubVersion;
    }

    /**
     * Inclusive
     * isVersionAfter(1,9) on 1.9.0 is true
     */
    public static boolean isVersionAfter(int mainGameVersion, int gameVersion) {
        return isVersionAfter(mainGameVersion, gameVersion, 0);
    }

    /**
     * Inclusive
     * isVersionAfter(1,9,4) on 1.9.4 is true
     */
    public static boolean isVersionAfter(int mainGameVersion, int gameVersion, int gameSubVersion) {
        if (GAME_MAIN_VERSION < mainGameVersion)
            return false;
        if (GAME_MAIN_VERSION > mainGameVersion)
            return true;
        if (GAME_VERSION < gameVersion)
            return false;
        if (GAME_VERSION > gameVersion)
            return true;
        return GAME_SUB_VERSION >= gameSubVersion;
    }

    /**
     * Inclusive
     */
    public static boolean isVersionInRange(int mainGameVersionMin, int gameVersionMin,
                                           int mainGameVersionMax, int gameVersionMax) {
        return isVersionInRange(mainGameVersionMin, gameVersionMin, 0,
                mainGameVersionMax, gameVersionMax, 99);
    }

    /**
     * Inclusive
     */
    public static boolean isVersionInRange(int mainGameVersionMin, int gameVersionMin, int gameSubVersionMin,
                                           int mainGameVersionMax, int gameVersionMax, int gameSubVersionMax) {
        return isVersionAfter(mainGameVersionMin, gameVersionMin, gameSubVersionMin)
                && isVersionUpTo(mainGameVersionMax, gameVersionMax, gameSubVersionMax);
    }

    public static boolean isAllowedChangeLore(CommandSender sender, Material type) {
        if (sender.hasPermission("itemedit.bypass.lore_type_restriction"))
            return true;

        List<String> values = DisplayEditor.get().getConfig().getStringList("blocked.type-blocked-lore");
        if (values.isEmpty())
            return true;
        String id = type.name();
        for (String name : values)
            if (id.equalsIgnoreCase(name)) {
                sendMessage(sender,
                        DisplayEditor.get().getLanguageConfig(sender).loadMessage("blocked-by-type-restriction-lore", "", null, true));
                return false;
            }
        return true;
    }

    public static boolean hasPaperAPI() {
        try {
            Class.forName("com.destroystokyo.paper.VersionHistoryManager$VersionData");
            return true;
        } catch (NoClassDefFoundError | ClassNotFoundException ex) {
            return false;
        }
    }

    public static boolean hasMiniMessageAPI() {
        return MiniMessageUtil.hasMiniMessage();
    }
}
