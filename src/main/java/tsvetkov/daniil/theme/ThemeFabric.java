package tsvetkov.daniil.theme;

public class ThemeFabric {

    private static String GLOBAL_PATH="src/main/resources/properties";
    public static AbstractCustomTheme createTheme(Theme theme)
    {
        switch (theme)
        {
            case DEFAULT:
                return new DefaultTheme(GLOBAL_PATH);
            case BIG_SNAKE:
                return new BigSnakeTheme(GLOBAL_PATH);
            case DEFROST:
                return new DefrostTheme(GLOBAL_PATH);
            case BUSINESS_MACHINE:
                return new BusinessMachineTheme(GLOBAL_PATH);
            case CONQUEROR:
                return new Conqueror(GLOBAL_PATH);
            case BLASTER:
                return new BlasterTheme(GLOBAL_PATH);
        }
        return null;
    }
    public enum Theme {
        DEFAULT,
        BIG_SNAKE,
        BUSINESS_MACHINE,
        CONQUEROR,
        DEFROST,
        BLASTER;

    }
}
