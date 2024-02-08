package emanondev.displayeditor.command;

import emanondev.displayeditor.DisplayEditor;
import emanondev.displayeditor.command.displayeditor.*;

public class DisplayEditorCommand extends AbstractCommand {
    public static DisplayEditorCommand instance;

    public DisplayEditorCommand() {
        super("displayeditor", DisplayEditor.get());
        instance = this;

        this.registerSubCommand(new Create(this));
        this.registerSubCommand(new Select(this));
        this.registerSubCommand(new Deselect(this));
        this.registerSubCommand(new Editormode(this));
        this.registerSubCommand(new Delete(this));
        this.registerSubCommand(new Settext(this));
        this.registerSubCommand(new Setitem(this));
        this.registerSubCommand(new Setblock(this));
    }

    public static DisplayEditorCommand get() {
        return instance;
    }

}
