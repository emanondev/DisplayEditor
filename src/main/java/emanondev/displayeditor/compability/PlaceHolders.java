package emanondev.displayeditor.compability;

import emanondev.displayeditor.DisplayEditor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * This class will automatically register as a placeholder expansion when a jar
 * including this class is added to the directory
 * {@code /plugins/PlaceholderAPI/expansions} on your server. <br>
 * <br>
 * If you create such a class inside your own plugin, you have to register it
 * manually in your plugins {@code onEnable()} by using
 * {@code new YourExpansionClass().register();}
 */
public class PlaceHolders extends PlaceholderExpansion {
    public PlaceHolders() {

        DisplayEditor.get().log("Hooked into PlaceHolderAPI:");
    }

    /**
     * The name of the person who created this expansion should go here.
     *
     * @return The name of the author as a String.
     */
    @Override
    @NotNull
    public String getAuthor() {
        return "emanon";
    }

    /**
     * The placeholder identifier should go here. <br>
     * This is what tells PlaceholderAPI to call our onRequest method to obtain a
     * value if a placeholder starts with our identifier. <br>
     * This must be unique and can not contain % or _
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    @NotNull
    public String getIdentifier() {
        return "itemedit";
    }

    /**
     * if the expansion requires another plugin as a dependency, the proper name of
     * the dependency should go here. <br>
     * Set this to {@code null} if your placeholders do not require another plugin
     * to be installed on the server for them to work. <br>
     * <br>
     * This is extremely important to set your plugin here, since if you don't do
     * it, your expansion will throw errors.
     *
     * @return The name of our dependency.
     */
    @Override
    public String getRequiredPlugin() {
        return DisplayEditor.get().getName();
    }

    /**
     * This is the version of this expansion. <br>
     * You don't have to use numbers, since it is set as a String.
     *
     * @return The version as a String.
     */
    @Override
    @NotNull
    public String getVersion() {
        return "1.0";
    }

    /**
     * This is the method called when a placeholder with our identifier is found and
     * needs a value. <br>
     * We specify the value identifier in this method. <br>
     * Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param player A {@link Player Player}.
     * @param value  A String containing the identifier/value.
     * @return possibly-null String of the requested identifier.
     */
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String value) {
        //TODO
        return null;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }
}