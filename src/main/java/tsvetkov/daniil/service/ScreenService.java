package tsvetkov.daniil.service;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalResizeListener;

import java.io.IOException;

public class ScreenService {
    public Screen getScreen() {
        return screen;
    }

    private Screen screen;
    private Terminal terminal;
    private TextGraphicsService textGraphicsService;

    public ScreenService(Terminal terminal) throws IOException {
        this.terminal = terminal;
        this.screen = new TerminalScreen(terminal);
        this.screen.setCursorPosition(null);
        this.terminal.addResizeListener((terminal1, terminalSize) -> {
            try {
                refresh(Screen.RefreshType.DELTA);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        this.screen.startScreen();
    }

    public void refresh(Screen.RefreshType refreshType) throws IOException {
        screen.doResizeIfNecessary();
        terminal.flush();
        screen.refresh(refreshType);
    }

    public class TextGraphicsService {
        private TextGraphics textGraphics;

        private TextGraphicsService() {
            textGraphics = screen.newTextGraphics();
        }

    }
}
