package emanondev.displayeditor.compability;

import emanondev.displayeditor.DisplayEditor;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;

public class MiniMessageMM implements MiniMessageUtil {

    private static final net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer UNGLY_LEGACY = LegacyComponentSerializer.legacySection().toBuilder().hexColors().useUnusualXRepeatedCharacterHexFormat().build();

    @Getter
    private static final MiniMessagePaper instance = new MiniMessagePaper();

    @Override
    public String fromMiniToText(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        try {
            return UNGLY_LEGACY.serialize(MiniMessage.miniMessage().deserialize(text.replace("§", "&")));
        } catch (NoSuchMethodError e) {
            if (Bukkit.getPluginManager().isPluginEnabled("SCore")) {
                DisplayEditor.get().log("SCore is disabling MiniMessage compability?");
            } else {
                e.printStackTrace();
            }
            return text;
        } catch (Throwable e) {
            e.printStackTrace();
            return text;
        }
    }
}
