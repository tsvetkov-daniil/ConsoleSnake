package tsvetkov.daniil.object.controller;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import tsvetkov.daniil.object.Direction;
import tsvetkov.daniil.object.Snake;
import tsvetkov.daniil.object.SnakeSegment;

import java.util.LinkedList;

import static tsvetkov.daniil.object.Direction.*;

public class SnakeController {
    private Snake snake;
    private short startSize;
    private TerminalPosition defaultSnakePosition;
    private Direction defaultDirection;
    private TerminalPosition lastHeadPos;

    public SnakeController(TerminalSize size) {
        this(new TerminalPosition(size.getColumns() / 2, size.getRows() / 2), UP, (short)3);
    }


    public SnakeController(TerminalPosition startPos, Direction direction, short startSize) {
        this.defaultDirection = direction;
        this.startSize = startSize;
        this.defaultSnakePosition = startPos;
        this.snake = new Snake(defaultSnakePosition, defaultDirection, startSize);
        this.lastHeadPos = snake.getHead().getPosition();
    }

    public Snake getSnake() {
        return snake;
    }

    public void reset() {
        snake = new Snake(defaultSnakePosition, defaultDirection, startSize);
    }

    public SnakeController setStartSize(short startSize) {
        this.startSize = startSize;
        return this;
    }

    public SnakeController setDefaultSnakePosition(TerminalPosition defaultSnakePosition) {
        this.defaultSnakePosition = defaultSnakePosition;
        return this;
    }

    public SnakeController setDefaultDirection(Direction defaultDirection) {
        this.defaultDirection = defaultDirection;
        return this;
    }

    public void moveRight() {
        if (snake.getDirection() != LEFT && isPossibleDirection(Direction.RIGHT)) snake.setDirection(Direction.RIGHT);
    }

    public void moveLeft() {
        if (snake.getDirection() != Direction.RIGHT && isPossibleDirection(LEFT)) snake.setDirection(LEFT);
    }

    public void moveUp() {
        if (snake.getDirection() != DOWN && isPossibleDirection(UP)) snake.setDirection(UP);
    }

    public void moveDown() {
        if (snake.getDirection() != UP && isPossibleDirection(DOWN)) snake.setDirection(DOWN);
    }

    public void step() {
        snake.getSegments().add(0,new SnakeSegment(aheadPosition(),snake.getDirection()));
        snake.getSegments().remove(snake.getSegments().size()-1);
    }

    public void grow() {
        snake.getSegments().add(0,new SnakeSegment(aheadPosition(), snake.getDirection()));
    }


    public TerminalPosition aheadPosition() {
        return aheadPosition(snake.getDirection());
    }

    protected TerminalPosition aheadPosition(Direction direction) {
        short headX = (short) snake.getHead().getPosition().getColumn();
        short headY = (short) snake.getHead().getPosition().getRow();

        switch (direction) {
            case LEFT:
                return new TerminalPosition(headX - 1, headY);
            case RIGHT:
                return new TerminalPosition(headX + 1, headY);
            case UP:
                return new TerminalPosition(headX, headY - 1);
            case DOWN:
                return new TerminalPosition(headX, headY + 1);
        }
        return null;
    }

    private boolean isPossibleDirection(Direction direction) {
        if(lastHeadPos.equals(snake.getHead().getPosition()))
            return false;
        lastHeadPos = snake.getHead().getPosition();
        return snake.getSegments().size() < 2 || !aheadPosition(direction).equals(snake.getSegments().get(1).getPosition());
    }

}
