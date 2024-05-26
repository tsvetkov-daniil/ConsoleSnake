package tsvetkov.daniil.object;

import com.googlecode.lanterna.TerminalPosition;

public class Apple extends AtomObject{
    public Apple(TerminalPosition position) {
        super(position, Tag.DEFAULT_APPLE);
    }
}
