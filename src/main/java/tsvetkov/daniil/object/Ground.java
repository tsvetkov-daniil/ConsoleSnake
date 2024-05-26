package tsvetkov.daniil.object;

import com.googlecode.lanterna.TerminalPosition;

public class Ground extends AtomObject{
    public Ground(TerminalPosition position) {
        super(position, Tag.GROUND);
    }
}
