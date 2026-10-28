package emanondev.displayeditor.command;

import emanondev.displayeditor.DisplayEditor;
import emanondev.displayeditor.Util;
import emanondev.displayeditor.compability.Hooks;
import emanondev.displayeditor.utility.VersionUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


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
        Util.sendMessage(sender, plugin.getLanguageConfig(sender).loadMessage("lack-permission", "&cYou lack of permission %permission%",
                sender instanceof Player ? (Player) sender : null, true
                , "%permission%", permission));
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission(permission)) {
            StringBuilder copyText = new StringBuilder();
            if (sender instanceof Player)
                copyText.append("Server: ").append(Bukkit.getVersion()).append("\n")
                        .append("View: ").append(VersionUtils.getVersion()).append("\n")
                        .append("Java: ").append(System.getProperty("java.version")).append("\n")
                        .append("DisplayEditor: ").append(plugin.getDescription().getVersion())
                        .append("PAPI: ").append(Hooks.getPluginVersion("PlaceholderAPI", "Nope")).append("\n")
                        .append("Vault: ").append(Hooks.getPluginVersion("Vault", "Nope")).append("\n")
                        .append("Vanish: ").append(Hooks.getPluginVersion("PremiumVanish", Hooks.getPluginVersion("SuperVanish", "Nope")));
            ComponentBuilder msg = new ComponentBuilder(
                    net.md_5.bungee.api.ChatColor.BLUE + "Server: " + net.md_5.bungee.api.ChatColor.AQUA + Bukkit.getVersion() + "\n" +
                            net.md_5.bungee.api.ChatColor.BLUE + "Java: " + net.md_5.bungee.api.ChatColor.AQUA + System.getProperty("java.version") + "\n" +
                            net.md_5.bungee.api.ChatColor.BLUE + "View: " + net.md_5.bungee.api.ChatColor.AQUA + VersionUtils.getVersion() + "\n" +
                            net.md_5.bungee.api.ChatColor.BLUE + "DisplayEditor: " + net.md_5.bungee.api.ChatColor.AQUA + plugin.getDescription().getVersion() + "\n" +
                            net.md_5.bungee.api.ChatColor.BLUE + "PAPI: " + net.md_5.bungee.api.ChatColor.AQUA + Hooks.getPluginVersion("PlaceholderAPI", "Nope") + "\n" +
                            net.md_5.bungee.api.ChatColor.BLUE + "Vault: " + net.md_5.bungee.api.ChatColor.AQUA + Hooks.getPluginVersion("Vault", "Nope") + "\n" +
                            net.md_5.bungee.api.ChatColor.BLUE + "Vanish: " + net.md_5.bungee.api.ChatColor.AQUA + Hooks.getPluginVersion("PremiumVanish", Hooks.getPluginVersion("SuperVanish", "Nope")));
            if (sender instanceof Player && VersionUtils.isVersionAfter(1, 9)) {
                msg.append("\n").append(new ComponentBuilder(net.md_5.bungee.api.ChatColor.GOLD + net.md_5.bungee.api.ChatColor.UNDERLINE.toString() + "Click To Copy\n")
                        .event(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,
                                copyText.toString())).create());
                copyText.append("\nPlugins: ");
                for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                    copyText.append(plugin.getName()).append(" (").append(plugin.getDescription().getVersion()).append(plugin.isEnabled() ? "), " : " loaded only), ");
                }
                msg.append(new ComponentBuilder(net.md_5.bungee.api.ChatColor.GOLD + net.md_5.bungee.api.ChatColor.UNDERLINE.toString() + "Click To Copy with Plugin list\n")
                        .event(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,
                                copyText.substring(0, copyText.length() - 2))).create());
            } else {
                msg.append("\n" + net.md_5.bungee.api.ChatColor.BLUE + "Plugins: " + net.md_5.bungee.api.ChatColor.AQUA);
                for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                    copyText.append(plugin.getName()).append(" (").append(plugin.getDescription().getVersion()).append(plugin.isEnabled() ? "), " : " loaded only), ");
                }
                msg.append(copyText.substring(0, copyText.length() - 2));
            }
            Util.sendMessage(sender, msg.create());
        } else
            sendPermissionLackMessage(permission, sender);
        return true;
    }

    public void register() {
        try {
            plugin.registerCommand(plugin.getName().toLowerCase(Locale.ENGLISH) + "info", this, null);
        } catch (Exception e) {
            plugin.log("Unable to register command " + ChatColor.YELLOW + plugin.getName().toLowerCase(Locale.ENGLISH) + "info");
            e.printStackTrace();
        }
    }
}
