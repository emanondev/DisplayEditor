package emanondev.displayeditor.command.displayeditor;

import emanondev.displayeditor.UtilsString;
import emanondev.displayeditor.command.AbstractCommand;
import emanondev.displayeditor.command.SubCmd;
import emanondev.displayeditor.selection.SelectionManager;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Settext extends SubCmd {
    public Settext(@NotNull AbstractCommand cmd) {
        super("settext", cmd, true, false);
    }

    @Override
    public void onCommand(CommandSender sender, String alias, String[] args) {
        Display sel = SelectionManager.getSelection((Player) sender);
        if (sel == null) {
            sendLanguageString("none-selected", sender);
            return;
        }
        if (!(sel instanceof TextDisplay)) {
            sendLanguageString("wrong-type", sender);
            return;
        }
        String text = "";
        if (args.length > 1) {
            text = String.join(" ", Arrays.asList(args).subList(1, args.length));
        }
        Component component = UtilsString.fix(text, null, true);
        ((TextDisplay) sel).text(component);
        sendLanguageString("success", sender);
    }

    @Override
    public List<String> onComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
