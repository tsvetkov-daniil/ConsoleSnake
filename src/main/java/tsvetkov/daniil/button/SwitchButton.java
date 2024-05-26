package tsvetkov.daniil.button;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TerminalTextUtils;
import com.googlecode.lanterna.graphics.*;
import com.googlecode.lanterna.gui2.AbstractInteractableComponent;
import com.googlecode.lanterna.gui2.InteractableRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class SwitchButton extends AbstractInteractableComponent<SwitchButton> {
    private final List<SwitchElement> elements;
    private int index;


    public SwitchButton(SwitchElement switchElement) {
        this.elements = new CopyOnWriteArrayList<>();
        elements.add(switchElement);
    }

    public boolean setActive(short index) {
        if (index <= 0 && index < elements.size()) {
            this.index = index;
            return true;
        } else {
            return false;
        }
    }

    public boolean setActive(SwitchElement element) {
        int index = elements.indexOf(element);

        if (index >= 0) {
            this.index = index;
            return true;
        }
        return false;
    }

    public Optional<SwitchElement> getElement(String label) {
        return elements.stream().filter(a -> Objects.equals(a.label, label)).findFirst();
    }

    @Override
    protected InteractableRenderer<SwitchButton> createDefaultRenderer() {
        return new DefaultSwitchButtonRenderer();
    }

    public SwitchElement getCurrent() {
        return elements.get(index);
    }

    public List<SwitchElement> getElements() {
        return (List<SwitchElement>) new ArrayList<>(elements);
    }

    private boolean hasNext() {
        return index + 1 < elements.size();
    }

    private boolean hasPrevious() {
        return index > 0;
    }

    public void addElement(SwitchElement switchElement) {
        if (!elements.contains(switchElement)) {
            elements.add(switchElement);
            this.invalidate();
        }
    }


    @Override
    protected Result handleKeyStroke(KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.ArrowRight) {
            if (index + 1 < elements.size()) {
                index++;
                return Result.HANDLED;
            }
        } else if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
            if (index > 0) {
                index--;
                return Result.HANDLED;
            }
        }
        return super.handleKeyStroke(keyStroke);
    }

    public String getLabel() {
        return getCurrent().label;
    }

    public static class BorderedSwitchButtonRenderer extends SwitchButtonRenderer {
        public BorderedSwitchButtonRenderer() {
            super();
        }

        @Override
        public TerminalPosition getCursorLocation(SwitchButton switchButton) {
            return null;
        }
        public TerminalSize getPreferredSize(SwitchButton component) {
            return new TerminalSize(super.getPreferredSize(component).getColumns() + 3, 4);
        }

        public void drawComponent(TextGUIGraphics graphics, SwitchButton switchButton) {
            ThemeDefinition themeDefinition = switchButton.getThemeDefinition();
            graphics.applyThemeStyle(themeDefinition.getNormal());
            TerminalSize size = graphics.getSize();
            graphics.drawLine(1, 0, size.getColumns() - 3, 0, '─');
            graphics.drawLine(1, size.getRows() - 2, size.getColumns() - 3, size.getRows() - 2, '─');
            graphics.drawLine(0, 1, 0, size.getRows() - 3, '│');
            graphics.drawLine(size.getColumns() - 2, 1, size.getColumns() - 2, size.getRows() - 3, '│');
            graphics.setCharacter(0, 0, '┌');
            graphics.setCharacter(size.getColumns() - 2, 0, '┐');
            graphics.setCharacter(size.getColumns() - 2, size.getRows() - 2, '┘');
            graphics.setCharacter(0, size.getRows() - 2, '└');
            graphics.drawLine(1, 1, size.getColumns() - 3, 1, ' ');
            if (switchButton.isFocused()) {
                graphics.applyThemeStyle(themeDefinition.getActive());
            }
            if (switchButton.hasPrevious()) {
                graphics.setCharacter(1, 1, themeDefinition.getCharacter("LEFT_BORDER", '<'));
            }
            if (switchButton.hasNext()) {
                graphics.setCharacter(graphics.getSize().getColumns() - 3, 1, themeDefinition.getCharacter("RIGHT_BORDER", '>'));
            }
            graphics.putString(1+getLabelShift(switchButton,graphics.getSize()), 1, TerminalTextUtils.fitString(switchButton.getLabel(), size.getColumns() - 5));
            graphics.applyThemeStyle(themeDefinition.getInsensitive());
            graphics.drawLine(1, size.getRows() - 1, size.getColumns() - 1, size.getRows() - 1, ' ');
            graphics.drawLine(size.getColumns() - 1, 1, size.getColumns() - 1, size.getRows() - 2, ' ');
        }
    }

    public static class FlatSwitchButtonRenderer extends SwitchButtonRenderer {
        public FlatSwitchButtonRenderer() {
        }
        @Override
        public TerminalPosition getCursorLocation(SwitchButton switchButton) {
            return new TerminalPosition(getLabelShift(switchButton,switchButton.getSize()),0);
        }
        public void drawComponent(TextGUIGraphics graphics, SwitchButton switchButton) {
            ThemeDefinition themeDefinition = switchButton.getThemeDefinition();
            if (switchButton.isFocused()) {
                graphics.applyThemeStyle(themeDefinition.getActive());
            } else {
                graphics.applyThemeStyle(themeDefinition.getInsensitive());
            }

            graphics.fill(' ');
            if (switchButton.isFocused()) {
                graphics.applyThemeStyle(themeDefinition.getSelected());
            } else {
                graphics.applyThemeStyle(themeDefinition.getNormal());
            }
            if (switchButton.hasPrevious()) {
                graphics.setCharacter(0, 0, themeDefinition.getCharacter("LEFT_BORDER", '<'));
            }
            if (switchButton.hasNext()) {
                graphics.setCharacter(graphics.getSize().getColumns() - 1, 0, themeDefinition.getCharacter("RIGHT_BORDER", '>'));
            }
            graphics.putString(getLabelShift(switchButton,graphics.getSize()), 0, switchButton.getLabel());
        }
    }


    public static class DefaultSwitchButtonRenderer extends SwitchButtonRenderer {

        @Override
        public TerminalPosition getCursorLocation(SwitchButton switchButton) {
            return new TerminalPosition(getLabelShift(switchButton,switchButton.getSize())+1,0);
        }
        @Override
        public void drawComponent(TextGUIGraphics graphics, SwitchButton switchButton) {
            ThemeDefinition themeDefinition = switchButton.getThemeDefinition();
            if (switchButton.isFocused()) {
                graphics.applyThemeStyle(themeDefinition.getActive());
            } else {
                graphics.applyThemeStyle(themeDefinition.getInsensitive());
            }

            graphics.fill(' ');
            if (switchButton.hasPrevious()) {
                graphics.setCharacter(0, 0, themeDefinition.getCharacter("LEFT_BORDER", '<'));
            }
            if (switchButton.hasNext()) {
                graphics.setCharacter(graphics.getSize().getColumns() - 1, 0, themeDefinition.getCharacter("RIGHT_BORDER", '>'));
            }
            if (switchButton.isFocused()) {
                graphics.applyThemeStyle(themeDefinition.getActive());
            } else {
                graphics.applyThemeStyle(themeDefinition.getPreLight());
            }

            int labelShift = this.getLabelShift(switchButton, graphics.getSize());
            graphics.setCharacter(1 + labelShift, 0, switchButton.getLabel().charAt(0));
            if (TerminalTextUtils.getColumnWidth(switchButton.getLabel()) != 1) {
                if (switchButton.isFocused()) {
                    graphics.applyThemeStyle(themeDefinition.getSelected());
                } else {
                    graphics.applyThemeStyle(themeDefinition.getNormal());
                }
                graphics.putString(1 + labelShift + 1, 0, switchButton.getLabel().substring(1));
            }
        }


    }

    public static abstract class SwitchButtonRenderer implements SwitchRenderer
    {



        @Override
        public TerminalSize getPreferredSize(SwitchButton switchButton) {
            return new TerminalSize(switchButton.getElements().stream().mapToInt(a -> a.label.length())
                    .max().getAsInt() + 4, 1);
        }

         int getLabelShift(SwitchButton button, TerminalSize size) {
            int availableSpace = size.getColumns() - 2;
            if (availableSpace <= 0) {
                return 0;
            } else {
                int labelShift = 0;
                int widthInColumns = TerminalTextUtils.getColumnWidth(button.getLabel());
                if (availableSpace > widthInColumns) {
                    labelShift = (size.getColumns() - 2 - widthInColumns) / 2;
                }

                return labelShift;
            }
        }
    }
    public interface SwitchRenderer extends InteractableRenderer<SwitchButton> {

    }

    public static class SwitchElement {
        private List<Listener> listeners;
        private String label;

        public void runListeners() {
            listeners.forEach(Listener::doAction);
        }

        public SwitchElement(String label) {
            this(label, () -> {
            });
        }

        public SwitchElement(String label, Listener listener) {
            this.listeners = new CopyOnWriteArrayList<>();
            this.label = label;
            this.addListener(listener);
        }


        private void addListener(Listener listener) {
            listeners.add(listener);
        }


        @Override
        public boolean equals(Object obj) {
            return ((SwitchElement) obj).label.equals(this.label);
        }

        @Override
        public int hashCode() {
            return label.hashCode();
        }

        public interface Listener {
            void doAction();
        }
    }
}

