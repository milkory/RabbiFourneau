package rabbitown.bukkit.fourneau.util;

import lombok.SneakyThrows;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Milkory
 */
public class IO {

    public static void writeFile(File file, String str) throws IOException {
        initFile(file);
        var out = new FileOutputStream(file);
        out.write(str.getBytes(StandardCharsets.UTF_8));
        out.close();
    }

    /** Initialize a file, including creating the parent folders and itself. */
    public static File initFile(File file) throws IOException {
        if (!file.exists()) {
            initFolder(file.getParentFile());
            if (!file.createNewFile()) {
                throw new IOException("Failed to create file - " + file.getPath());
            }
        }
        return file;
    }

    /** Initialize a folder, which means to create it. */
    public static File initFolder(File file) throws IOException {
        if (!file.exists()) {
            if (file.isFile() || !file.mkdirs()) {
                throw new IOException("Failed to create folder - " + file.getPath());
            }
        }
        return file;
    }

    @SneakyThrows public static File initFolder0(File file) {
        return initFolder(file);
    }

}
