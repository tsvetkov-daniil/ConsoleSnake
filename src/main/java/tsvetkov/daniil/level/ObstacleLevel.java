package tsvetkov.daniil.level;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.ThemeDefinition;
import com.googlecode.lanterna.gui2.InteractableRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import tsvetkov.daniil.object.Field;
import tsvetkov.daniil.object.controller.FieldController;
import tsvetkov.daniil.exception.FieldOutBoundException;
import tsvetkov.daniil.object.*;
import tsvetkov.daniil.object.controller.SnakeController;

import java.util.Random;

public class ObstacleLevel extends AbstractLevel {

    public ObstacleLevel(TerminalSize size) {
        super(createFieldWithObstacle(size), new SnakeController(size));
    }

    private static FieldController createFieldWithObstacle(TerminalSize terminalSize) {
        return createObstacle(new FieldController(new Field(terminalSize)));
    }

    private static FieldController createObstacle(FieldController fieldController)
    {
        Random rnd = new Random();
        int obstacleNum = (fieldController.getFieldHeight() +fieldController.getFieldWidth())/10+1;
        for (int i = 0; i < obstacleNum; i++) {
            int randInt = rnd.nextInt(fieldController.getFreeSpace().size());
            TerminalPosition tmpPos = fieldController.getFreeSpace().get(randInt);
            try {
                fieldController.putObject(new Obstacle(tmpPos));
            } catch (FieldOutBoundException e) {
                throw new RuntimeException(e);
            }

        }
        return fieldController;
    }

    @Override
    public void reset() {
        super.reset();
        createObstacle(getFieldController());
    }


    @Override
    protected InteractableRenderer<AbstractLevel> createDefaultRenderer() {
        return new ObstacleLevelDefaultRenderer();
    }

    public static class ObstacleLevelDefaultRenderer extends DefaultAbstractLevelRenderer{
        @Override
        protected void drawCell(TextGUIGraphics graphics, AtomObject tmp, ThemeDefinition themeDefinition, short maxHeight, short maxWidth) {
            if (tmp.getTag() == AtomObject.Tag.OBSTACLE) {
                graphics.applyThemeStyle(themeDefinition.getCustom("OBSTACLE"));
                graphics.setCharacter(tmp.getPosition(), themeDefinition.getCharacter("OBSTACLE_CHAR", '█'));
            }else {
                super.drawCell(graphics, tmp, themeDefinition, maxHeight, maxWidth);
            }
        }
    }
}
