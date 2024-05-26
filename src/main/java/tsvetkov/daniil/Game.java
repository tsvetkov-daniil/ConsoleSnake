package tsvetkov.daniil;

import com.googlecode.lanterna.TerminalSize;
import tsvetkov.daniil.gui.MainGUI;
import java.io.IOException;


public class Game {
    public static void main(String[] args) throws IOException {
        MainGUI snakeUI = new MainGUI(new ConsoleManager(new TerminalSize(100,50)));
    }
}
