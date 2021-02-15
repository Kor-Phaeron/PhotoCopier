import java.io.*;
import java.nio.file.*;

public class Copier {
    static String address;
    static File dest;
    public static void main(String[] args) throws IOException {
        System.out.println(address);
        if (MainGUI.copyButton.getModel().isPressed()){
        copyStart();
        }

        if (MainGUI.moveButton.getModel().isPressed()){
            moveStart();
        }
    }


    public static void copyStart() throws IOException {

        File src = new File("\\\\" + address + "\\DavWWWRoot\\DCIM\\Camera");
        Path srcPath = src.toPath();
        Path destPath = dest.toPath();
        System.out.println("Копирую из: " + "\\\\" + address + "\\DavWWWRoot\\DCIM\\Camera");
        Files.walkFileTree(srcPath, new CopyDirVisitor(srcPath, destPath, StandardCopyOption.REPLACE_EXISTING));
        //Another test message
    }

    public static void moveStart() throws IOException {

        File src = new File("\\\\" + address + "\\DavWWWRoot\\DCIM\\Camera");
        Path srcPath = src.toPath();
        Path destPath = dest.toPath();
        System.out.println("Перемещаю из: " + "\\\\" + address + "\\DavWWWRoot\\DCIM\\Camera");
        Files.walkFileTree(srcPath, new MoveDirVisitor(srcPath, destPath, StandardCopyOption.REPLACE_EXISTING));
        //Another test message
    }
}
