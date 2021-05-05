package fzw.demo01;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author fzw
 * @description
 **/
public class MyClassLoader extends ClassLoader {

    public Class<?> loadClass(String name, String path) {
        Path classPath = Paths.get(path);
        File file = classPath.toFile();
        try (FileInputStream inputStream = new FileInputStream(file);) {
            byte[] allByte = inputStream.readAllBytes();
            for (int i = 0; i < allByte.length; i++) {
                allByte[i] = (byte) (255 - allByte[i]);
            }
            return super.defineClass(name, allByte, 0, allByte.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
