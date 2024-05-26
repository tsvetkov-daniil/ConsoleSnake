package tsvetkov.daniil.theme;

import com.googlecode.lanterna.graphics.PropertyTheme;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public abstract class AbstractCustomTheme extends PropertyTheme {
    public AbstractCustomTheme(String path) {
        super(loadAsProperties(path), false);
    }

    private static Properties loadAsProperties(String path)
    {
        Properties properties = new Properties();
        try {
            String filePath = path;
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            properties.load(new StringReader(content));
            return properties;
        } catch (IOException var2) {
            IOException e = var2;
            System.out.println(e.getMessage());
            throw new RuntimeException("Unexpected I/O error");
        }
    }
}
