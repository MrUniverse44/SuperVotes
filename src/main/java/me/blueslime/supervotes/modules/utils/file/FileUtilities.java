package me.blueslime.supervotes.modules.utils.file;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtilities {
    public static InputStream build(String location) {
        if (!location.startsWith("/")) {
            return FileUtilities.class.getResourceAsStream("/" + location);
        }
        return FileUtilities.class.getResourceAsStream(location);
    }

    public static void saveResource(File file, InputStream resource) {
        checkFileExistence(file, resource);
    }

    public static void checkFileExistence(File file, InputStream resource) {
        if (!file.getParentFile().exists()) {
            boolean createFile = file.getParentFile().mkdirs();
            if (!createFile) {
                new NullPointerException("folder can't be created!").printStackTrace();
            }
        }

        if (!file.exists()) {
            try (InputStream in = resource) {
                cloneResource(file, in);
            } catch (Exception exception) {
                new NullPointerException("resource can't be cloned!").printStackTrace();
            }
        }
    }

    public static void cloneResource(File file, InputStream in) throws IOException {
        if (in != null) {
            Files.copy(in, file.toPath());
        } else {
            boolean created = file.createNewFile();
            if (!created) {
                new NullPointerException("file can't be created!").printStackTrace();
            }
        }
    }
}



