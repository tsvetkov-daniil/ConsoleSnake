package tsvetkov.daniil.object;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;

import java.util.spi.AbstractResourceBundleProvider;

public class SnakeSegment extends AtomObject {
    private Direction direction;

    public SnakeSegment(TerminalPosition position, Direction direction) {
        super(position, Tag.SNAKE);
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void setPosition(TerminalPosition position) {
        changeDirection(culcDeltaPosition(getPosition(), position));
        super.setPosition(position);
    }

    private void changeDirection(TerminalPosition deltaPos) {
        if(deltaPos.getRow() != 0 && deltaPos.getColumn() !=0)
        {
        }
        else if (deltaPos.getColumn() < 0) {
            direction = Direction.LEFT;
        } else if (deltaPos.getColumn() > 0) {
            direction = Direction.RIGHT;
        } else if (deltaPos.getRow() < 0) {
            direction = Direction.UP;
        } else if (deltaPos.getRow() > 0) {
            direction = Direction.DOWN;
        }
    }

    private TerminalPosition culcDeltaPosition(TerminalPosition fromPos, TerminalPosition toPos) {
        return toPos.minus(fromPos);
    }
}
