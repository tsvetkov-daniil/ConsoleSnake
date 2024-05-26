package tsvetkov.daniil.level;

import com.googlecode.lanterna.TerminalSize;

import java.net.Proxy;
import java.util.Objects;

public class LevelFabric {
    private LevelType type;

    public LevelFabric(LevelType type) {
        if (Objects.nonNull(type)) {
            this.type = type;
        } else {
            this.type = LevelType.DEFAULT;
        }
    }

    public AbstractLevel getInstance(TerminalSize size) {
        switch (type) {
            case DEFAULT:
                return new DefaultLevel(size);
            case WITH_OBSTACLE:
                return new ObstacleLevel(size);
        }
        return new DefaultLevel(size);
    }

    public enum LevelType {
        DEFAULT, WITH_OBSTACLE;
    }
}
