package tsvetkov.daniil.object;

import java.util.List;

public abstract class GameObject {
    public abstract List<? extends AtomObject> getSegments();
}
