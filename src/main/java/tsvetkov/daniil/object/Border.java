package tsvetkov.daniil.object;

import com.googlecode.lanterna.TerminalPosition;

public class Border extends AtomObject{
    public Border(TerminalPosition position) {
        super(position, Tag.BORDER);
    }
}
