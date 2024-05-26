package tsvetkov.daniil.gui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import tsvetkov.daniil.level.AbstractLevel;
import tsvetkov.daniil.level.DefaultLevel;
import tsvetkov.daniil.level.LevelFabric;
import tsvetkov.daniil.level.ObstacleLevel;

import javax.sound.sampled.Line;
import java.util.Collections;
import java.util.List;

public class GameGUI {
    AbstractLevel defaultLevel;
    Panel gamePanel;
    Window gameWindow;
    Window restartWindow;

    public GameGUI(MultiWindowTextGUI textGUI, LevelFabric levelFabric) {
        gameWindow = new BasicWindow("Snake");
        gameWindow.setHints(Collections.singletonList(Window.Hint.CENTERED));

        gamePanel = new Panel(new LinearLayout(Direction.VERTICAL));
        gamePanel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));

        restartWindow = createRestartWindow();

        defaultLevel = levelFabric.getInstance(new TerminalSize(50, 25));
        defaultLevel.start();
        defaultLevel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        gamePanel.addComponent(defaultLevel);


        gameWindow.setHints(Collections.singleton(Window.Hint.CENTERED));


        Panel scorePanel = new Panel(new LinearLayout(Direction.VERTICAL));
        Label scoreLabel = new Label("0");
        scoreLabel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        scorePanel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        scorePanel.addComponent(scoreLabel);

        Panel footer = new Panel(new GridLayout(2));
        footer.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill, LinearLayout.GrowPolicy.CanGrow));
        footer.addComponent(scorePanel.withBorder(Borders.singleLine("Score")));

        footer.addComponent(createInfoPanel(GridLayout.createLayoutData(GridLayout.Alignment.END,
                GridLayout.Alignment.CENTER, true, true)));
        gamePanel.addComponent(footer);
        gameWindow.setComponent(gamePanel);


        restartWindow = createRestartWindow();
        defaultLevel.setGameListener(new DefaultLevel.GameListener() {
            @Override
            public void onPoint(int point) {
                scoreLabel.setText(String.valueOf(point));
                scoreLabel.invalidate();
            }

            @Override
            public void onEnd() {
                try {
                    textGUI.getGUIThread().invokeAndWait(() -> textGUI.addWindowAndWait(createRestartWindow()));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onResume() {
                textGUI.addWindowAndWait(createResumeWindow());
            }

            @Override
            public void onWin() {
                try {
                    textGUI.getGUIThread().invokeAndWait(() -> textGUI.addWindowAndWait(createWinWindow()));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        textGUI.addWindowAndWait(gameWindow);

    }


    private Border createInfoPanel(LayoutData layoutData) {
        Panel infopanel = new Panel(new LinearLayout(Direction.VERTICAL));
        Label exitInfo = new Label("Press \"Q\" for exit");
        infopanel.addComponent(exitInfo);
        infopanel.setLayoutData(layoutData);
        return infopanel.withBorder(Borders.singleLine("Info"));
    }

    private Window createRestartWindow() {
        Window restartWindow = new BasicWindow("YOU LOOSE");
        restartWindow.setHints(List.of(Window.Hint.CENTERED));

        restartWindow.setComponent(createRestartPanel(restartWindow));
        return restartWindow;
    }

    private Window createResumeWindow() {
        Window resumeWindow = new BasicWindow("PAUSE");
        resumeWindow.setHints(List.of(Window.Hint.CENTERED));
        resumeWindow.setComponent(createResumePanel(resumeWindow));
        return resumeWindow;
    }

    private Panel createRestartPanel(Window window) {
        Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));
        Button restartButton = new Button("Restart", () ->
        {
            window.close();
            defaultLevel.restart();
        });
        restartButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        panel.addComponent(restartButton);
        Button mainMenuButton = new Button("Main Menu", () ->
        {
            window.close();
            defaultLevel.exit();
            gameWindow.close();
        });
        mainMenuButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        panel.addComponent(mainMenuButton);
        return panel;
    }

    private Panel createResumePanel(Window window) {
        Panel panel = createRestartPanel(window);
        Button restartButton = new Button("Continue", () ->
        {
            window.close();
            defaultLevel.setResume(false);
        });
        restartButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        panel.addComponent(0, restartButton);
        return panel;
    }

    private Window createWinWindow() {
        Window winWindow = new BasicWindow("YOU WON");
        winWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel winPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        new Label("Congratulation!").addTo(winPanel);
        Button okButton = new Button("OK", () -> {
            winWindow.close();
            defaultLevel.exit();
            gameWindow.close();
        }).addTo(winPanel);

        okButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        winWindow.setComponent(winPanel);
        return winWindow;
    }
}
