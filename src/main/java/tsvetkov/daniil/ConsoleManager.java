package tsvetkov.daniil;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.ExtendedTerminal;
import com.googlecode.lanterna.terminal.Terminal;
import tsvetkov.daniil.service.ScreenService;
import tsvetkov.daniil.service.TerminalService;

import java.io.IOException;

public class ConsoleManager {

    private TerminalService terminalService;
    private ScreenService screenService;

    public ConsoleManager(TerminalSize terminalSize) {
        try {
            Terminal terminal = new DefaultTerminalFactory()
                    .setInitialTerminalSize(terminalSize).createTerminal();
            terminalService = new TerminalService(terminal);

            screenService = new ScreenService(terminal);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public TerminalService getTerminalService() {
        return terminalService;
    }

    public ScreenService getScreenService() {
        return screenService;
    }
}
