package emanondev.displayeditor.command;

import emanondev.displayeditor.DisplayEditor;
import emanondev.displayeditor.command.displayeditor.*;
import emanondev.displayeditor.command.entityeditor.Copy;
import emanondev.displayeditor.command.entityeditor.Edit;
import emanondev.displayeditor.command.entityeditor.Spawn;
import emanondev.displayeditor.command.entityeditor.SpawnAll;

public class EntityEditorCommand extends AbstractCommand {
    public static EntityEditorCommand instance;

    public EntityEditorCommand() {
        super("EntityEditor", DisplayEditor.get());
        instance = this;
        this.registerSubCommand(new Copy(this));
        this.registerSubCommand(new Spawn(this));
        this.registerSubCommand(new Edit(this));
        this.registerSubCommand(new SpawnAll(this));
    }

    public static EntityEditorCommand get() {
        return instance;
    }

}
