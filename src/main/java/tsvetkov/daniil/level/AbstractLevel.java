package tsvetkov.daniil.level;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.ThemeDefinition;
import com.googlecode.lanterna.gui2.AbstractInteractableComponent;
import com.googlecode.lanterna.gui2.InteractableRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import tsvetkov.daniil.object.controller.FieldController;
import tsvetkov.daniil.object.controller.SnakeController;
import tsvetkov.daniil.exception.FieldOutBoundException;
import tsvetkov.daniil.object.*;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public abstract class AbstractLevel extends AbstractInteractableComponent<AbstractLevel> implements Runnable {
    private final FieldController fieldController;
    private final SnakeController snakeController;
    private boolean isContinue;
    private GameListener gameListener;
    private int points;
    private boolean isResume;
    private int restartCount;


    public AbstractLevel(FieldController fieldController, SnakeController snakeController) {
        this.fieldController = fieldController;
        this.snakeController = snakeController;
    }


    protected void initialize() {
        isContinue = false;
        try {
            fieldController.putObject(snakeController.getSnake());
            setNewApple();
        } catch (FieldOutBoundException e) {
            throw new RuntimeException(e);
        }
        isContinue = true;
        createControlThread();
    }

    protected void reset() {
        isContinue = false;
        setPoints(0);
        fieldController.clearField();
        snakeController.reset();
        initialize();
    }

    private void setPoints(int points) {
        this.points = points;
        if (Objects.nonNull(gameListener)) {
            gameListener.onPoint(points);
        }
    }

    protected FieldController getFieldController() {
        return fieldController;
    }

    public void start() {
        initialize();
    }

    public void restart() {
        restartCount++;
        reset();
    }

    private void setNewApple() throws FieldOutBoundException {
        List<TerminalPosition> freeSpace = fieldController.getFreeSpace();
        Random rand = new Random();
        int index = rand.nextInt(freeSpace.size());

        TerminalPosition applePosition = new TerminalPosition(freeSpace.get(index).getColumn(),
                freeSpace.get(index).getRow());

        fieldController.putObject(new Apple(applePosition));
    }


    private void createControlThread() {
        Thread.currentThread().interrupt();
        Thread controllThread = new Thread(this);
        controllThread.setName("LevelControlThread");
        controllThread.setDaemon(true);
        controllThread.start();
    }

    @Override
    protected Result handleKeyStroke(KeyStroke keyStroke) {
        if (keyStroke != null && !isResume && isContinue) {
            switch (keyStroke.getKeyType()) {
                case ArrowUp:
                    snakeController.moveUp();
                    return Result.HANDLED;
                case ArrowDown:
                    snakeController.moveDown();
                    return Result.HANDLED;
                case ArrowLeft:
                    snakeController.moveLeft();
                    return Result.HANDLED;
                case ArrowRight:
                    snakeController.moveRight();
                    return Result.HANDLED;
            }
            if (Objects.nonNull(keyStroke.getCharacter()) && keyStroke.getCharacter().equals('q')) {
                try {
                    this.getTextGUI().getGUIThread().invokeAndWait(() -> gameListener.doResume(this));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return Result.HANDLED;
            }
        }
        return Result.UNHANDLED;
    }

    @Override
    public void run() {
        int cachedRestartCount = this.restartCount;
        try {
            while (isContinue) {
                while (isResume) {
                    Thread.yield();
                }
                if (this.restartCount != cachedRestartCount) {
                    break;
                }
                AtomObject.Tag aheadObjTag = fieldController.objectAt(snakeController.aheadPosition()).getTag();
                if (aheadObjTag == AtomObject.Tag.OBSTACLE || aheadObjTag == AtomObject.Tag.SNAKE || aheadObjTag == AtomObject.Tag.BORDER) {
                    isContinue = false;
                } else if (aheadObjTag == AtomObject.Tag.DEFAULT_APPLE) {
                    setPoints(++points);
                    checkWin();
                    setNewApple();
                    fieldController.removeObject(snakeController.getSnake());
                    snakeController.grow();

                } else {
                    fieldController.removeObject(snakeController.getSnake());
                    snakeController.step();
                }
                fieldController.putObject(snakeController.getSnake());
                invalidate();
                Thread.sleep(150);
            }
            if (Objects.nonNull(gameListener) && this.restartCount == cachedRestartCount) {
                gameListener.onEnd();
            }
        } catch (FieldOutBoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkWin() {
        if (fieldController.getFreeSpace().isEmpty()) {
            isContinue = false;
            setEnabled(false);
            restartCount++;
            gameListener.onWin();
        }
    }

    protected boolean isResume() {
        return isResume;
    }

    public void setResume(boolean resume) {
        isResume = resume;
    }

    protected boolean isContinue() {
        return isContinue;
    }

    protected void setContinue(boolean aContinue) {
        isContinue = aContinue;
    }

    public void setGameListener(GameListener gameListener) {
        this.gameListener = gameListener;
    }

    public void exit() {
        restartCount++;
        this.isContinue = false;
        this.setEnabled(false);
    }

    public void removeGameListener() {
        this.gameListener = null;
    }

    @Override
    protected InteractableRenderer<AbstractLevel> createDefaultRenderer() {
        return new DefaultAbstractLevelRenderer();
    }


    public static class DefaultAbstractLevelRenderer implements AbstractLevelRenderer {
        public DefaultAbstractLevelRenderer() {
        }

        public TerminalPosition getCursorLocation(AbstractLevel defaultLevel) {
            return null;
        }

        public TerminalSize getPreferredSize(AbstractLevel defaultLevel) {
            return new TerminalSize(defaultLevel.fieldController.getFieldWidth(), defaultLevel.fieldController.getFieldHeight());
        }

        public void drawComponent(TextGUIGraphics graphics, AbstractLevel defaultLevel) {
            for (AtomObject tmp : defaultLevel.fieldController) {
                drawCell(graphics, tmp,defaultLevel.getThemeDefinition(),(short)(defaultLevel.fieldController.getFieldHeight()-1),
                        (short)(defaultLevel.fieldController.getFieldWidth()-1));
            }

        }

        protected void drawCell(TextGUIGraphics graphics, AtomObject tmp, ThemeDefinition themeDefinition, short maxHeight, short maxWidth) {
            AtomObject.Tag tag = tmp.getTag();
            if (tag == AtomObject.Tag.GROUND) {
                graphics.applyThemeStyle(themeDefinition.getCustom("GROUND"));
                graphics.setCharacter(tmp.getPosition(), themeDefinition.getCharacter("GROUND_CHAR", ' '));
            }  else if (tag == AtomObject.Tag.SNAKE) {
                graphics.applyThemeStyle(themeDefinition.getCustom("SNAKE"));
                if (tmp instanceof SnakeSegment) {
                    switch (((SnakeSegment) tmp).getDirection()) {
                        case UP:
                            graphics.setCharacter(tmp.getPosition(), themeDefinition.getCharacter("UP_CHAR", '▲'));
                            break;
                        case DOWN:
                            graphics.setCharacter(tmp.getPosition(),  themeDefinition.getCharacter("DOWN_CHAR", '▼'));
                            break;
                        case LEFT:
                            graphics.setCharacter(tmp.getPosition(),  themeDefinition.getCharacter("LEFT_CHAR", '◀'));
                            break;
                        case RIGHT:
                            graphics.setCharacter(tmp.getPosition(),  themeDefinition.getCharacter("RIGHT_CHAR", '▶'));
                            break;
                    }
                }
            } else if (tag == AtomObject.Tag.DEFAULT_APPLE) {
                graphics.applyThemeStyle(themeDefinition.getCustom("APPLE"));
                graphics.setCharacter(tmp.getPosition(),  themeDefinition.getCharacter("DEFAULT_APPLE_CHAR", '♥'));
            }else if(tag == AtomObject.Tag.BORDER)
            {graphics.applyThemeStyle(themeDefinition.getCustom("BORDER"));
                    if(tmp.getPosition().getColumn() == 0)
                    {
                        graphics.setCharacter(tmp.getPosition(),  themeDefinition.getCharacter("BORDER_LEFT", '▶'));
                    } else if (tmp.getPosition().getColumn() == maxWidth) {
                        graphics.setCharacter(tmp.getPosition(),  themeDefinition.getCharacter("BORDER_RIGHT", '◀'));
                    }else if(tmp.getPosition().getRow() == 0)
                    {
                        graphics.setCharacter(tmp.getPosition(),  themeDefinition.getCharacter("TOP_BORDER", '▼'));
                    }else if(tmp.getPosition().getRow() == maxHeight)
                    {
                        graphics.setCharacter(tmp.getPosition(),  themeDefinition.getCharacter("BOTTOM_BORDER", '▲'));
                    }
            }
        }
    }

    public interface AbstractLevelRenderer extends InteractableRenderer<AbstractLevel>{}

    public interface GameListener {
        void onPoint(int point);

        void onEnd();

        void onResume();

        default void doResume(AbstractLevel defaultLevel) {
            defaultLevel.isResume = true;
            onResume();
            defaultLevel.isResume = false;
        }

        void onWin();
    }
}
