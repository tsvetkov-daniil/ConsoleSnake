package tsvetkov.daniil.level;

import com.googlecode.lanterna.*;
import tsvetkov.daniil.object.Field;
import tsvetkov.daniil.object.controller.FieldController;
import tsvetkov.daniil.object.controller.SnakeController;

public class DefaultLevel extends AbstractLevel {
    public DefaultLevel(TerminalSize size) {
        super(createFieldController(size), new SnakeController(size));
    }

    private static FieldController createFieldController(TerminalSize terminalSize) {
        return new FieldController(new Field(terminalSize));
    }

}
