package tsvetkov.daniil.object;

import com.googlecode.lanterna.TerminalPosition;

public class Obstacle extends AtomObject {
    public Obstacle(TerminalPosition position) {
        super(position, Tag.OBSTACLE);
    }
}