package it.leogioia.test.utils;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FileUtil {
    private FileUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static final String PATH_DELIMITER = FileSystems.getDefault().getSeparator();

    public static String readResourceFile(String resourceFilePath) throws IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:" + resourceFilePath);
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void copyFile(String sourcePath, String targetPath) throws IOException {
        FileUtils.copyFile(new File(sourcePath), new File(targetPath));
    }

    public static File loadResourceFile(Class testClass, String fileName) throws URISyntaxException {
        URL resourceUrl = testClass.getResource(fileName);
        return Paths.get(resourceUrl.toURI()).toFile();
    }
}
