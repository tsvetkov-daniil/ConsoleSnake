package tsvetkov.daniil.gui;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import tsvetkov.daniil.ConsoleManager;
import tsvetkov.daniil.button.SwitchButton;
import tsvetkov.daniil.level.LevelFabric;
import tsvetkov.daniil.theme.ThemeFabric;

import java.io.IOException;
import java.util.*;

public class MainGUI {
    private final MultiWindowTextGUI textGUI;
    private final Screen screen;
    private final ConsoleManager consoleManager;
    private final ThemeFabric.Theme theme = ThemeFabric.Theme.DEFAULT;
    private final Window mainMenuWindows;
    private LevelFabric.LevelType levelType;

    public MainGUI(ConsoleManager consoleManager) throws IOException {
        this.consoleManager = consoleManager;
        this.screen = consoleManager.getScreenService().getScreen();
        this.textGUI = new MultiWindowTextGUI(screen);
        this.textGUI.setTheme(ThemeFabric.createTheme(ThemeFabric.Theme.DEFAULT));
        this.mainMenuWindows = makeMainMenu();
        this.textGUI.addWindowAndWait(mainMenuWindows);
        this.textGUI.updateScreen();

    }

    private Window makeMainMenu() {
        Window mainWindow = new BasicWindow("Main Menu");
        mainWindow.setHints(Collections.singleton(Window.Hint.CENTERED));
        LinearLayout panelLayout = new LinearLayout(Direction.VERTICAL);
        Panel mainPanel = new Panel(panelLayout);

        Button playButton = new Button("Play", () -> new GameGUI(textGUI,new LevelFabric(levelType)));
        playButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        mainPanel.addComponent(playButton);


        Button settingsButton = new Button("Settings", () -> textGUI.addWindowAndWait(settingsWindow()));
        settingsButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        mainPanel.addComponent(settingsButton);


        Button exitButton = new Button("Exit", () -> System.exit(0));
        exitButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        mainPanel.addComponent(exitButton);


        mainWindow.setComponent(mainPanel);

        return mainWindow;
    }


    private SwitchButton createSwitchThemeButton() {
        SwitchButton switchButton = new SwitchButton(new SwitchButton.SwitchElement("Default", () ->
                textGUI.setTheme(ThemeFabric.createTheme(ThemeFabric.Theme.DEFAULT))));
        switchButton.addElement(new SwitchButton.SwitchElement("Blaster",
                () -> textGUI.setTheme(ThemeFabric.createTheme(ThemeFabric.Theme.BLASTER))));
        switchButton.addElement(new SwitchButton.SwitchElement("Big Snake",
                () -> textGUI.setTheme(ThemeFabric.createTheme(ThemeFabric.Theme.BIG_SNAKE))));
        switchButton.addElement(new SwitchButton.SwitchElement("Business Machine",
                () -> textGUI.setTheme(ThemeFabric.createTheme(ThemeFabric.Theme.BUSINESS_MACHINE))));
        switchButton.addElement(new SwitchButton.SwitchElement("Conqueror",
                () -> textGUI.setTheme(ThemeFabric.createTheme(ThemeFabric.Theme.CONQUEROR))));
        switchButton.addElement(new SwitchButton.SwitchElement("Defrost",
                () -> textGUI.setTheme(ThemeFabric.createTheme(ThemeFabric.Theme.DEFROST))));
        return switchButton;
    }


    private SwitchButton createSwitchLevelButton() {
        SwitchButton switchButton = new SwitchButton(new SwitchButton.SwitchElement("Default",()->levelType = LevelFabric.LevelType.DEFAULT));
        switchButton.addElement(new SwitchButton.SwitchElement("Obstacles",()->levelType = LevelFabric.LevelType.WITH_OBSTACLE));
        return switchButton;
    }

    private Window settingsWindow() {
        Window settingsWindow = new BasicWindow("Settings");
        settingsWindow.setHints(Collections.singleton(Window.Hint.CENTERED));

        Panel settingsPanel = new Panel(new GridLayout(2));
        new Label("Graphics: ").addTo(settingsPanel);

        SwitchButton switchButton = createSwitchThemeButton().addTo(settingsPanel);
        switchButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));

        new Label("Level: ").addTo(settingsPanel);

        SwitchButton switchLevelButton = createSwitchLevelButton().addTo(settingsPanel).addTo(settingsPanel);
        switchLevelButton.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.CENTER));
        settingsWindow.setComponent(settingsPanel);
        new EmptySpace().addTo(settingsPanel);
        new EmptySpace().addTo(settingsPanel);

        Button backButton = new Button("Back", settingsWindow::close);
        settingsPanel.addComponent(backButton);

        Button applyButton = new Button("Save", () -> {
            switchButton.getCurrent().runListeners();
            switchLevelButton.getCurrent().runListeners();
            mainMenuWindows.invalidate();
            settingsWindow.close();
        });
        applyButton.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.END, GridLayout.Alignment.CENTER));
        settingsPanel.addComponent(applyButton);
        return settingsWindow;
    }
}
