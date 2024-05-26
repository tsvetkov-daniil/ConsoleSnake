package tsvetkov.daniil.service;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class TerminalService {
    private Terminal terminal;

    public TerminalService(Terminal terminal)
    {
        this.terminal = terminal;
    }
}
