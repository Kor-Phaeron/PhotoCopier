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
    public long totalFilesSizeCopied;
    public long totalFilesSizeToCopy;
    public float percentCopied;
    public int copying;
    public int copying2;
    public static JProgressBar pb1;
    public double transferSpeedMB = 0;
    public String transferSpeedMBShow;
    public int totalFiles = 0;
    public int filesCopied = 1;

    public static void setPb(JProgressBar pb) {
        pb1 = pb;
    }


    public CopyDirVisitor(Path fromPath, Path toPath, CopyOption copyOption) {
        this.fromPath = fromPath;
        this.toPath = toPath;
        this.copyOption = copyOption;
        totalFilesSizeToCopy = Methods.size(fromPath);
        totalFiles = new File(String.valueOf(fromPath)).listFiles().length;
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

        percentCopied = (float) (totalFilesSizeCopied) / (float) totalFilesSizeToCopy * 100;
        float percentCopied2 = (float) (totalFilesSizeCopied) / (float) totalFilesSizeToCopy * 1000;
        double copyingPercent = percentCopied / 10;
        pb1.setString(numberFormat.format(percentCopied) + "% " + "(" + filesCopied + "/" + totalFiles + ")");
        copying = (int) percentCopied;
        copying2 = (int) percentCopied2 * 1;
        System.out.println("Total size to copy = " + totalFilesSizeToCopy);
        System.out.println("Overall progress " + numberFormat.format(percentCopied) + "%");
        System.out.println("Progressbar value: " + pb1.getValue());
        Long end = System.nanoTime();
        double fileSize = Methods.size(file) / 1024.0 / 1024.0;
        int timeElapsed = (int) ((end - start) / 10000000);

        transferSpeedMB = fileSize / timeElapsed * 100;
//        System.out.println("Время начала передачи файла: " + start/100000000);
//        System.out.println("Время конца передачи файла: " + end/100000000);
//        System.out.println("Время передачи файла: " + timeElapsed);
//        System.out.println("Размер переданного файла: " + numberFormat.format(fileSize) + " MB");
//        System.out.println("Скорость передачи данных: " + numberFormat.format(transferSpeedMB) + " MBps");
//        transferSpeedMBShow = numberFormat.format(transferSpeedMB);
        //System.out.println("TransferSpeedMBSHOW = " + transferSpeedMBShow);

        if (percentCopied == 100) {
            pb1.setValue(1000);
            MainGUI.copyButton.setVisible(false);
            MainGUI.moveButton.setVisible(false);
            MainGUI.stopButton.setVisible(false);
            MainGUI.copyDone.setVisible(true);
        }

        if (MainGUI.stopButton.getModel().isEnabled() == false) {
            System.out.println("Нажата кнопка стоп");
            MainGUI.stopButton.setEnabled(true);
            pb1.setValue(0);
            pb1.setString("0,00%");
            totalFilesSizeCopied = 0;
            filesCopied = 1;
            MainGUI.transferSpeedShow.setText("0.0 МБ/сек");
            MainGUI.outputTextArea.setText(null);
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
