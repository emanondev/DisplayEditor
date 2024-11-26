package emanondev.displayeditor.command;

import emanondev.displayeditor.DisplayEditor;
import emanondev.displayeditor.command.displayeditor.*;
import emanondev.displayeditor.command.entityeditor.Copy;
import emanondev.displayeditor.command.entityeditor.Spawn;

public class EntityEditorCommand extends AbstractCommand {
    public static EntityEditorCommand instance;

    public EntityEditorCommand() {
        super("EntityEditor", DisplayEditor.get());
        instance = this;
        this.registerSubCommand(new Copy(this));
        this.registerSubCommand(new Spawn(this));
    }

    public static EntityEditorCommand get() {
        return instance;
    }

}
