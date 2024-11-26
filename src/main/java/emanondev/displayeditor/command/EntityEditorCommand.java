package emanondev.displayeditor.command;

import emanondev.displayeditor.DisplayEditor;
import emanondev.displayeditor.command.displayeditor.*;
import emanondev.displayeditor.command.entityeditor.Copy;

public class EntityEditorCommand extends AbstractCommand {
    public static EntityEditorCommand instance;

    public EntityEditorCommand() {
        super("EntityEditor", DisplayEditor.get());
        instance = this;

        this.registerSubCommand(new Copy(this));
    }

    public static EntityEditorCommand get() {
        return instance;
    }

}
