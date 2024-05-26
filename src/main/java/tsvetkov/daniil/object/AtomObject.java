package tsvetkov.daniil.object;

import com.googlecode.lanterna.TerminalPosition;

import java.util.Iterator;

public class AtomObject {
    private Tag tag;
    private TerminalPosition position;

    public AtomObject(TerminalPosition position, Tag tag) {
        this.tag = tag;
        this.position = position;
    }
    public void setPosition(TerminalPosition position) {
        this.position = position;
    }

    public TerminalPosition getPosition() {
        return position;
    }

    public Tag getTag() {
        return tag;
    }

    @Override
    public boolean equals(Object obj) {
        return tag.equals(((AtomObject)obj).getTag()) && position.equals(((AtomObject)obj).getPosition());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + tag.hashCode();
        result = prime * result + position.hashCode();
        return result;
    }

    public enum Tag {
        SNAKE, DEFAULT_APPLE, GROUND, OBSTACLE, BORDER;
    }
}
