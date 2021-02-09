import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class CopyDirVisitor extends SimpleFileVisitor<Path> {
    private final Path fromPath;
    private final Path toPath;
    private final CopyOption copyOption;
    public static long totalFilesSizeCopied;
    public static long totalFilesSizeToCopy;
    public static float percentCopied;
    public static int copying;
    public static int copying2;
//    static final String ANSI_GREEN = "\u001b[32m";
//    static final String ANSI_RESET = "\u001B[0m";
    public static ProgressMonitor pm1;
    public static JProgressBar pb1;


    public static void setPm(ProgressMonitor pm) {
        pm1 = pm;
    }

    public static void setPb(JProgressBar pb) {
        pb1 = pb;
    }


    public CopyDirVisitor(Path fromPath, Path toPath, CopyOption copyOption) {
        this.fromPath = fromPath;
        this.toPath = toPath;
        this.copyOption = copyOption;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        Path targetPath = toPath.resolve(fromPath.relativize(dir));
        if (!Files.exists(targetPath)) {
            Files.createDirectory(targetPath);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//        pm1.setProgress(copying);
        pb1.setValue(copying2);
        SwingUtilities.invokeLater(() -> MainGUI.outputTextArea.setText(MainGUI.outputTextArea.getText()
                + String.format(file.getFileName() + " is copying..." + "\n")));
        System.out.println((file.getFileName() + " is copying..."));
        Files.copy(file, toPath.resolve(fromPath.relativize(file)), copyOption);
        SwingUtilities.invokeLater(() -> MainGUI.outputTextArea.setText(MainGUI.outputTextArea.getText()
                + String.format(file.getFileName() + " is copying..." + "done!" + "\n")));
        System.out.println((file.getFileName() + " is copying..." + "done!"));
        DecimalFormat numberFormat = new DecimalFormat("0.00");
//        pm1.setNote("Скопировано " + numberFormat.format(percentCopied) + " %");
        totalFilesSizeCopied += Methods.size(file);
        totalFilesSizeToCopy = Methods.size(fromPath);
        percentCopied = (float) (totalFilesSizeCopied) / (float) totalFilesSizeToCopy * 100;
        float percentCopied2 = (float) (totalFilesSizeCopied) / (float) totalFilesSizeToCopy * 1000;
        double copyingPercent = percentCopied/10;
        pb1.setString(numberFormat.format(percentCopied)+"%");
        copying = (int) percentCopied;
        copying2 = (int) percentCopied2*1;
        System.out.println("Overall progress " + numberFormat.format(percentCopied) + "%");
        System.out.println("Progressbar value: " + pb1.getValue());
//        System.out.println("Overall progress x10 " + percentCopied*10 + "%");
//        System.out.println("Overall progress int " + (int) percentCopied + "%");
//        System.out.println("Overall progress int x10 " + (int) copyingPercent + "%");
//        System.out.println("Overall progress int/10 " + (int) percentCopied/10 + "%");
//        if (pm1.isCanceled()) {
//            pm1.close();
//            System.exit(1);
//            return FileVisitResult.TERMINATE;
//        }

        if (percentCopied == 100) {
            pb1.setValue(100);
        }

        try {
            TimeUnit.MILLISECONDS.sleep(0);
        } catch (InterruptedException | NullPointerException e) {
            e.printStackTrace();
        }

        return FileVisitResult.CONTINUE;
    }
}
