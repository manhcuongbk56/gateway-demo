package echo.common;

import lombok.NonNull;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

    public static Path pathOfFileInClasspath (@NonNull String fileLocation) {
        Path filePath;
        try {
            filePath = Paths.get(
                    (new FileUtils()).getClass().getClassLoader().getResource(fileLocation).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return filePath;
    }
}
