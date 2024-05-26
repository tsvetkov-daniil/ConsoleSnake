package tsvetkov.daniil.exception;

import com.googlecode.lanterna.TerminalPosition;

public class FieldOutBoundException extends Exception {
    public FieldOutBoundException(TerminalPosition position, short maxFieldHeight, short maxFieldWidth) {
        super(String.format("The value %d,%d is out of the bounds of the field %dx%d", position.getRow(), position.getColumn(), maxFieldHeight, maxFieldWidth));
    }
}
