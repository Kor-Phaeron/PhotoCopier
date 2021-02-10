import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLOutput;
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
    public static JProgressBar pb1;
    public static double transferSpeedMB = 0;
    public static String transferSpeedMBShow;
    public static int totalFiles = 0;
    public static int filesCopied = 1;

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
        totalFiles = new File(String.valueOf(fromPath)).listFiles().length;
        Long start = System.nanoTime();
        pb1.setValue(copying2);
        SwingUtilities.invokeLater(() -> MainGUI.outputTextArea.setText(MainGUI.outputTextArea.getText()
                + String.format(file.getFileName() + " is copying..." + "\n")));
        System.out.println((file.getFileName() + " is copying..."));
        Files.copy(file, toPath.resolve(fromPath.relativize(file)), copyOption);
        SwingUtilities.invokeLater(() -> MainGUI.outputTextArea.setText(MainGUI.outputTextArea.getText()
                + String.format(file.getFileName() + " is copying..." + "done!" + "\n")));
        System.out.println((file.getFileName() + " is copying..." + "done!"));
        DecimalFormat numberFormat = new DecimalFormat("0.00");
        totalFilesSizeCopied += Methods.size(file);
        totalFilesSizeToCopy = Methods.size(fromPath);
        percentCopied = (float) (totalFilesSizeCopied) / (float) totalFilesSizeToCopy * 100;
        float percentCopied2 = (float) (totalFilesSizeCopied) / (float) totalFilesSizeToCopy * 1000;
        double copyingPercent = percentCopied/10;
        pb1.setString(numberFormat.format(percentCopied)+"% " + "(" + filesCopied + "/" + totalFiles + ")");
        copying = (int) percentCopied;
        copying2 = (int) percentCopied2*1;
        System.out.println("Overall progress " + numberFormat.format(percentCopied) + "%");
        System.out.println("Progressbar value: " + pb1.getValue());
        Long end = System.nanoTime();
        double fileSize = Methods.size(file)/1024.0/1024.0;
        int timeElapsed = (int) ((end - start)/10000000);

        transferSpeedMB = fileSize/timeElapsed*100;
//        System.out.println("Время начала передачи файла: " + start/100000000);
//        System.out.println("Время конца передачи файла: " + end/100000000);
//        System.out.println("Время передачи файла: " + timeElapsed);
//        System.out.println("Размер переданного файла: " + numberFormat.format(fileSize) + " MB");
//        System.out.println("Скорость передачи данных: " + numberFormat.format(transferSpeedMB) + " MBps");
        transferSpeedMBShow = numberFormat.format(transferSpeedMB);
        //System.out.println("TransferSpeedMBSHOW = " + transferSpeedMBShow);

        if (percentCopied == 100) {
            pb1.setValue(100);
        }

        if (MainGUI.stop.getModel().isEnabled() == false) {
            System.out.println("Нажата кнопка стоп");
            MainGUI.stop.setEnabled(true);
            pb1.setValue(0);
            pb1.setString("0,00%");
            totalFilesSizeCopied = 0;
            filesCopied = 1;
            return FileVisitResult.TERMINATE;
        }

        try {
            TimeUnit.MILLISECONDS.sleep(0);
        } catch (InterruptedException | NullPointerException e) {
            e.printStackTrace();
        }
        filesCopied += 1;
        SwingUtilities.invokeLater(() -> MainGUI.transferSpeedShow.setText(numberFormat.format(transferSpeedMB) + " МБ/сек"));
        SwingUtilities.invokeLater(() -> MainGUI.filesCopied.setText(filesCopied + " из " + totalFiles + "."));
        return FileVisitResult.CONTINUE;
    }
}
