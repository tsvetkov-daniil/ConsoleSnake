package tsvetkov.daniil.object;

import com.googlecode.lanterna.TerminalPosition;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Snake extends GameObject {
    private final LinkedList<SnakeSegment> segments;
    private Direction direction;

    public Snake(TerminalPosition startPosition, Direction direction, short size) {
        this.segments = new LinkedList<>();
        this.direction = direction;

        short lastX = (short) startPosition.getColumn();
        short lastY = (short) startPosition.getRow();
        for (int i = 0; i < size; i++) {
            segments.addLast(new SnakeSegment(new TerminalPosition(lastX, lastY),direction));
            switch (direction) {
                case UP:
                    lastY++;
                    break;
                case DOWN:
                    lastY--;
                    break;
                case LEFT:
                    lastX++;
                    break;
                case RIGHT:
                    lastX--;
                    break;
            }
        }
    }

    public AtomObject getHead() {
        return segments.getFirst();
    }

    public AtomObject getTail() {
        return segments.getLast();
    }


    public List<SnakeSegment> getSegments() {
        return segments;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }


}
