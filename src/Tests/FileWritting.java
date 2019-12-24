package Tests;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.*;

public class FileWritting {
    public static void main(String[] args) throws Exception {
        Path p = FileSystems.getDefault().getPath(".","data","songs","0");
        Files.createDirectories(p.getParent());
        File f = p.toFile();
        FileOutputStream out = new FileOutputStream(f);
        out.write("test".getBytes());
    }
}
