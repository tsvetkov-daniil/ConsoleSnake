package tsvetkov.daniil.object.controller;

import com.googlecode.lanterna.TerminalPosition;
import tsvetkov.daniil.object.Field;
import tsvetkov.daniil.exception.FieldOutBoundException;
import tsvetkov.daniil.object.AtomObject;
import tsvetkov.daniil.object.GameObject;
import tsvetkov.daniil.object.Ground;


import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

public class FieldController implements Iterable<AtomObject> {
    private Field field;
    private int modCount = 0;
    private final List<TerminalPosition> freeSpace;

    public FieldController(Field field) {
        this.field = field;
        freeSpace = new ArrayList<>((field.getHeight() - 2) * (field.getHeight() - 2));
        freeSpace.addAll(culcFreeSpace());
    }

    public AtomObject objectAt(TerminalPosition position) throws FieldOutBoundException {
        if (position.getColumn() > field.getWidth() || position.getRow() > field.getHeight())
            throw new FieldOutBoundException(position, field.getHeight(), field.getWidth());
        return field.getMatrix()[position.getRow()][position.getColumn()];
    }

    public synchronized void putObject(AtomObject atomObject) throws FieldOutBoundException {
        short posX = (short) atomObject.getPosition().getColumn();
        short posY = (short) atomObject.getPosition().getRow();

        AtomObject oldObj = objectAt(atomObject.getPosition());
        if (oldObj.getTag() != AtomObject.Tag.GROUND && atomObject.getTag() == AtomObject.Tag.DEFAULT_APPLE)
            throw new RuntimeException();
        if (oldObj.getTag() == AtomObject.Tag.GROUND && atomObject.getTag() != AtomObject.Tag.GROUND)
            freeSpace.remove(oldObj.getPosition());

        field.getMatrix()[posY][posX] = atomObject;
        modCount++;
    }

    public synchronized void putObject(GameObject gameObject) throws FieldOutBoundException {
        for (AtomObject tmp : gameObject.getSegments()) {
            putObject(tmp);
        }
    }

    public synchronized void removeObject(GameObject gameObject) {
        for (AtomObject tmp : gameObject.getSegments()) {
            removeAt(tmp.getPosition());
        }
    }

    public synchronized void removeAt(TerminalPosition position) {
        short posX = (short) position.getColumn();
        short posY = (short) position.getRow();

        Ground ground = new Ground(position);
        field.getMatrix()[posY][posX] = ground;
        freeSpace.add(ground.getPosition());

        modCount++;
    }

    public short getFieldWidth() {
        return field.getWidth();
    }

    public short getFieldHeight() {
        return field.getHeight();
    }

    public void clearField() {
        field = new Field(field.getFieldSize());
        modCount =0;
    }

    private List<TerminalPosition> culcFreeSpace() {
        List<TerminalPosition> tmpList = new ArrayList<>(freeSpace.size());
        for (AtomObject tmp : this) {
            if (tmp.getTag() == AtomObject.Tag.GROUND)
                freeSpace.add(tmp.getPosition());
        }
        return tmpList;
    }

    public Iterator<AtomObject> iterator() {
        return new FieldIterator();
    }

    public List<TerminalPosition> getFreeSpace() {
        return new ArrayList<>(freeSpace);
    }

    private class FieldIterator implements Iterator<AtomObject> {
        private final Field field;
        private final int expectedModCount;
        private int indexX;
        private int indexY;


        private FieldIterator() {
            this.expectedModCount = FieldController.this.modCount;
            this.indexX = 0;
            this.indexY = 0;
            this.field = FieldController.this.field;
        }

        @Override
        public synchronized boolean hasNext() {
            return indexX < field.getWidth() || indexY + 1 < field.getHeight();
        }

        @Override
        public synchronized AtomObject next() {
            if (expectedModCount < modCount)
                throw new ConcurrentModificationException();

            if (indexX == field.getWidth()) {
                indexX = 0;
                indexY++;
            }
            return field.getMatrix()[indexY][indexX++];
        }
    }


}
