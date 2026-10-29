package emanondev.displayeditor.command;

import emanondev.displayeditor.DisplayEditor;
import emanondev.displayeditor.Util;
import emanondev.displayeditor.compability.Hooks;
import emanondev.displayeditor.utility.VersionUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


/**
 * Utility command, prints information about the plugin and supported plugins
 */
public class DisplayEditorInfoCommand implements TabExecutor {

    private static final DisplayEditor plugin = DisplayEditor.get();
    private final String permission;

    public DisplayEditorInfoCommand() {
        this.permission = plugin.getName().toLowerCase(Locale.ENGLISH) + "." + plugin.getName().toLowerCase(Locale.ENGLISH) + "info";
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }

    public void sendPermissionLackMessage(@NotNull String permission, CommandSender sender) {
        plugin.getTranslator().send(sender, "generic.help.lack-permission",
                "%permission%", permission,
                "%prefix%", plugin.getTranslator().translateOrEmptyRaw(sender, "generic.help.prefix"));
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission(permission)) {
            String infoText = "Server: " + Bukkit.getVersion() + "\n"
                    + "Java: " + System.getProperty("java.version") + "\n"
                    + "View: " + VersionUtils.getVersion() + "\n"
                    + "DisplayEditor: " + plugin.getDescription().getVersion() + "\n"
                    + "PAPI: " + Hooks.getPluginVersion("PlaceholderAPI", "Nope") + "\n"
                    + "Vault: " + Hooks.getPluginVersion("Vault", "Nope") + "\n"
                    + "Vanish: " + Hooks.getPluginVersion("PremiumVanish",
                    Hooks.getPluginVersion("SuperVanish", "Nope"));

            String formattedInfo = "<blue>Server: <aqua>" + Bukkit.getVersion() + "\n"
                    + "<blue>Java: <aqua>" + System.getProperty("java.version") + "\n"
                    + "<blue>View: <aqua>" + VersionUtils.getVersion() + "\n"
                    + "<blue>DisplayEditor: <aqua>" + plugin.getDescription().getVersion() + "\n"
                    + "<blue>PAPI: <aqua>" + Hooks.getPluginVersion("PlaceholderAPI", "Nope") + "\n"
                    + "<blue>Vault: <aqua>" + Hooks.getPluginVersion("Vault", "Nope") + "\n"
                    + "<blue>Vanish: <aqua>" + Hooks.getPluginVersion("PremiumVanish",
                    Hooks.getPluginVersion("SuperVanish", "Nope"));
            Component message = MiniMessage.miniMessage().deserialize(formattedInfo);

            if (sender instanceof Player) {
                String pluginList = getPluginList();
                message = message.append(Component.newline())
                        .append(MiniMessage.miniMessage().deserialize("<gold><underlined>Click To Copy</underlined>")
                                .clickEvent(ClickEvent.copyToClipboard(infoText)))
                        .append(Component.newline())
                        .append(MiniMessage.miniMessage().deserialize("<gold><underlined>Click To Copy with Plugin list</underlined>")
                                .clickEvent(ClickEvent.copyToClipboard(infoText + "\nPlugins: " + pluginList)));
            } else {
                message = MiniMessage.miniMessage().deserialize(formattedInfo
                        + "\n<blue>Plugins: <aqua>" + getPluginList());
            }
            Util.sendComponent(sender, message);
        } else
            sendPermissionLackMessage(permission, sender);
        return true;
    }

    private String getPluginList() {
        return java.util.Arrays.stream(Bukkit.getPluginManager().getPlugins())
                .map(plugin -> plugin.getName() + " (" + plugin.getDescription().getVersion()
                        + (plugin.isEnabled() ? ")" : " loaded only)"))
                .collect(Collectors.joining(", "));
    }

    public void register() {
        try {
            plugin.registerCommand(plugin.getName().toLowerCase(Locale.ENGLISH) + "info", this, null);
        } catch (Exception e) {
            plugin.log("Unable to register command <yellow>" + plugin.getName().toLowerCase(Locale.ENGLISH) + "info");
            e.printStackTrace();
        }
    }
}
