import java.io.*;
import java.nio.file.*;

public class Copier {
    static String address;
    static File dest;
    public static void main(String[] args) throws IOException {
        System.out.println(address);
        copyStart();
    }


    public static void copyStart() throws IOException {

        File src = new File("\\\\" + address + "\\DavWWWRoot\\DCIM\\Camera");
        //File src = new File("\\\\" + "100.83.139.247@8080" + "\\DavWWWRoot\\DCIM\\Camera");
        Path srcPath = src.toPath();
        //dest = new File("D:\\Temp\\123");
        Path destPath = dest.toPath();
        System.out.println("Копирую из: " + "\\\\" + address + "\\DavWWWRoot\\DCIM\\Camera");
        Files.walkFileTree(srcPath, new CopyDirVisitor(srcPath, destPath, StandardCopyOption.REPLACE_EXISTING));
        //Test message
    }
}
