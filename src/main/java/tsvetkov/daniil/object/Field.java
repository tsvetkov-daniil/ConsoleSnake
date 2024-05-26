package tsvetkov.daniil.object;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;

public class Field {
    private AtomObject[][] matrix;
    private TerminalSize fieldSize;

    public Field(TerminalSize size) {
        this.fieldSize = size;
        matrix = new AtomObject[size.getRows()][size.getColumns()];
        initialize();
    }

    private void initialize() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (i == 0 || j == 0 ||
                        matrix.length - i == 1 || matrix[i].length - j == 1) {
                    matrix[i][j] = new Border(new TerminalPosition(j, i));
                } else {
                    matrix[i][j] = new Ground(new TerminalPosition(j, i));
                }
            }
        }
    }



    public short getWidth() {
        return (short) matrix[0].length;
    }


    public TerminalSize getFieldSize() {
        return fieldSize;
    }


    public short getHeight() {
        return (short) matrix.length;
    }

    public AtomObject[][] getMatrix() {
        return matrix;
    }
}
