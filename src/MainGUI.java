import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.util.Vector;

import static javax.swing.GroupLayout.Alignment.*;

public class MainGUI {
    static String addressFromCopy;
    static String addressToCopy;
    static JFileChooser chooser;
    static String chooserTitle;
    static JLabel destPath0 = new JLabel("Папка не выбрана!");
    static JLabel destPath1 = new JLabel(" ");
    static JTextArea outputTextArea;
    static JScrollPane scrollPanel;
    static Vector<Component> fields = new Vector<>(5);

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {

        ToolTipManager.sharedInstance().setInitialDelay(0);
        ToolTipManager.sharedInstance().setDismissDelay(50000);

        JFrame frame = new JFrame("Копирование фотографий");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel controlPanel = new JPanel();
        outputTextArea = new JTextArea("",10,75);
        scrollPanel = new JScrollPane(outputTextArea);
        outputTextArea.setFont(outputTextArea.getFont().deriveFont(12f));
        controlPanel.add(scrollPanel);

        JButton folderChoose = new JButton("Выбрать папку");
        folderChoose.setFont(new Font("Comic sans MS", Font.BOLD, 18));

        destPath0.setFont(new Font("Comic sans MS", Font.BOLD, 18));
        destPath0.setForeground(Color.red);
        destPath1.setFont(new Font("Comic sans MS", Font.BOLD, 18));
        destPath1.setForeground(Color.red);


        folderChoose.addActionListener(e -> {
            chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setFileSelectionMode(1);
            chooserTitle = "Папка сохранения фотографий";
            chooser.setDialogTitle(chooserTitle);
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            //
            // disable the "All files" option.
            //
            chooser.setAcceptAllFileFilterUsed(false);
            //
            if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                System.out.println("getCurrentDirectory(): "
                        +  chooser.getCurrentDirectory());
                System.out.println("getSelectedFile() : "
                        +  chooser.getSelectedFile());
                addressToCopy = chooser.getSelectedFile().toString();
                Copier.dest = chooser.getSelectedFile();
                destPath0.setText("Путь копирования фотографий:");
                destPath0.setForeground(Color.getColor(null));
                destPath1.setText(chooser.getSelectedFile().toString());
                destPath1.setFont(new Font("Comic sans MS", Font.BOLD, 18));
                destPath1.setForeground(Color.green);
            }
            else {
                System.out.println("No Selection ");
            }
        });

        //WebDAV image + QR-Code
        URL urlWebDAVimg = MainGUI.class.getResource("/res/WebDAV.png");
        String webDAVname = "Значок WebDAV Server";
        URL urlWebDAVqr = MainGUI.class.getResource("/res/qrWebDAV.gif");
        String webDAVqrName = "<center>" + "QR код для скачивания" + "</center>";
        String webDAVServer = "<html><center><img src='" + urlWebDAVimg + "'>"+"<br />"
                                + webDAVname  +"<br />"
                                + "<img src='" + urlWebDAVqr + "'>"+ "<br />"
                                + webDAVqrName + "</center></html>";

        //PowerButton image
        URL urlPowerimg = MainGUI.class.getResource("/res/Power.png");
        String powerName = "<center>" + "Значок кнопки включения" + "</center>";
        String powerButton = "<html><center><img src='" + urlPowerimg + "'></center>"+"<br />"+ powerName + "</html>";

        //Icon image
        URL icoImage = MainGUI.class.getResource("/res/Copy.png");
        ImageIcon imgIcon = new ImageIcon(icoImage);
        frame.setIconImage(imgIcon.getImage());

        //Instructions
        JLabel instructionHelpWebDAV = new JLabel("(?)");
        instructionHelpWebDAV.setFont(new Font("Comic sans MS", Font.BOLD, 18));
        instructionHelpWebDAV.setForeground(Color.blue);
        instructionHelpWebDAV.setToolTipText(webDAVServer);
        JLabel instructionHelpPower = new JLabel("(?)");
        instructionHelpPower.setFont(new Font("Comic sans MS", Font.BOLD, 18));
        instructionHelpPower.setForeground(Color.blue);
        instructionHelpPower.setToolTipText(powerButton);
        JLabel instruction0 = new JLabel("Инструкция:");
        instruction0.setFont(new Font("Comic sans MS", Font.BOLD, 18));
        JLabel instruction1 = new JLabel("1. Запустите на телефоне приложение WebDAV server.");
        instruction1.setFont(new Font("Comic sans MS", Font.BOLD, 18));
        JLabel instruction2 = new JLabel("2. В приложении нажмите на значок включения.");
        instruction2.setFont(new Font("Comic sans MS", Font.BOLD, 18));
        JLabel instruction3 = new JLabel("3. Введите высветившийся адрес в поля ниже.");
        instruction3.setFont(new Font("Comic sans MS", Font.BOLD, 18));
        JLabel instruction4 = new JLabel("4. Выберите папку, куда сохранить фотографии.");
        instruction4.setFont(new Font("Comic sans MS", Font.BOLD, 18));
        JLabel instruction5 = new JLabel("5. Нажмите на кнопку \"Старт\".");
        instruction5.setFont(new Font("Comic sans MS", Font.BOLD, 18));

        //address elements
        JLabel addressEl0 = new JLabel("http://");
        addressEl0.setFont(new Font("Comic sans MS", Font.BOLD, 18));
        JLabel addressEl1 = new JLabel(".");
        addressEl1.setFont(new Font("Comic sans MS", Font.BOLD, 18));
        JLabel addressEl2 = new JLabel(".");
        addressEl2.setFont(new Font("Comic sans MS", Font.BOLD, 18));
        JLabel addressEl3 = new JLabel(".");
        addressEl3.setFont(new Font("Comic sans MS", Font.BOLD, 18));
        JLabel addressEl4 = new JLabel(":");
        addressEl4.setFont(new Font("Comic sans MS", Font.BOLD, 18));


        JButton start = new JButton("Старт");
        start.setFont(new Font("Comic sans MS", Font.BOLD, 18));

        KeyListener k = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void keyReleased(KeyEvent e) {
                JTextField source = (JTextField) e.getSource();
                // TODO Auto-generated method stub
                if (source.getText().length() == 3) {
                    fields.get((1 + fields.indexOf(source))
                            % fields.size()).requestFocus();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
            }
        };

        //Text fields for addresses
        JTextField addressString1 = new JTextField(3);
        addressString1.setHorizontalAlignment(JTextField.CENTER);
        addressString1.addKeyListener(k);
        fields.add(addressString1);

        JTextField addressString2 = new JTextField(4);
        addressString2.setHorizontalAlignment(JTextField.CENTER);
        addressString2.addKeyListener(k);
        fields.add(addressString2);

        JTextField addressString3 = new JTextField(4);
        addressString3.setHorizontalAlignment(JTextField.CENTER);
        addressString3.addKeyListener(k);
        fields.add(addressString3);

        JTextField addressString4 = new JTextField(4);
        addressString4.setHorizontalAlignment(JTextField.CENTER);
        addressString4.addKeyListener(k);
        fields.add(addressString4);

        JTextField addressStringPort = new JTextField(4);
        addressStringPort.setHorizontalAlignment(JTextField.CENTER);
        fields.add(addressStringPort);

        //Setting layout
        GroupLayout layout = new GroupLayout(frame.getContentPane());
        frame.getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);


        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(CENTER)
                        .addComponent(instruction0)
                        .addGroup(layout.createParallelGroup(LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(instruction1)
                                        .addComponent(instructionHelpWebDAV))
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(instruction2)
                                        .addComponent(instructionHelpPower))
                                .addComponent(instruction3)
                                .addComponent(instruction4)
                                .addComponent(instruction5))
                .addGroup(layout.createParallelGroup(CENTER)
                                .addComponent(folderChoose)
                                .addComponent(destPath0)
                                .addComponent(destPath1)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(addressEl0)
                                .addComponent(addressString1)
                                .addComponent(addressEl1)
                                .addComponent(addressString2)
                                .addComponent(addressEl2)
                                .addComponent(addressString3)
                                .addComponent(addressEl3)
                                .addComponent(addressString4)
                                .addComponent(addressEl4)
                                .addComponent(addressStringPort))
                        .addComponent(start)
                        .addComponent(controlPanel)
                        )));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(instruction0)
                .addGroup(layout.createParallelGroup()
                        .addComponent(instruction1)
                        .addComponent(instructionHelpWebDAV))
                .addGroup(layout.createParallelGroup()
                        .addComponent(instruction2)
                        .addComponent(instructionHelpPower))
                .addComponent(instruction3)
                .addGroup(layout.createParallelGroup()
                        .addComponent(addressEl0)
                        .addComponent(addressString1)
                        .addComponent(addressEl1)
                        .addComponent(addressString2)
                        .addComponent(addressEl2)
                        .addComponent(addressString3)
                        .addComponent(addressEl3)
                        .addComponent(addressString4)
                        .addComponent(addressEl4)
                        .addComponent(addressStringPort))
                .addComponent(instruction4)
                .addComponent(folderChoose)
                .addComponent(destPath0)
                .addComponent(destPath1)
                .addComponent(instruction5)
                .addComponent(start)
                .addComponent(controlPanel)
        );

        frame.setResizable(false);
        //frame.setSize(1200,800);
        SwingUtilities.updateComponentTreeUI(frame);
        frame.pack();
        frame.setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        addressFromCopy = addressString1.getText() + "."
                + addressString2.getText() + "."
                + addressString3.getText() + "."
                + addressString4.getText() + "@"
                + addressStringPort.getText();


        start.addActionListener(arg0 -> {
            String address = addressString1.getText() + "."
                    + addressString2.getText() + "."
                    + addressString3.getText() + "."
                    + addressString4.getText() + "@"
                    + addressStringPort.getText();
            System.out.println("Адрес задан: " + address);
            Copier.address = address;
        });

        start.addActionListener(createStartTaskActionListener(frame));
    }

    private static ActionListener createStartTaskActionListener(JFrame frame) {
        //for progress monitor dialog title
        UIManager.put("ProgressMonitor.progressText", "Копирование...");
        //frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        frame.setResizable(false);
        return (ae) -> new Thread(() -> {
            //creating ProgressMonitor instance
            ProgressMonitor pm = new ProgressMonitor(frame, "Копирование фотографий",
                    "Копирование...", 0, 100);
            //decide after 0 millis whether to show popup or not
            pm.setMillisToDecideToPopup(0);
            //after deciding if predicted time is longer than 100 show popup
            pm.setMillisToPopup(0);
            CopyDirVisitor.setPm(pm);
            try {
                Thread.sleep(1);
                Copier.copyStart();
            }
            catch (IOException | InterruptedException | NullPointerException e) {
                StringBuilder sb = new StringBuilder("Error: ");
                sb.append(e.getMessage());
                sb.append("\n");
                for (StackTraceElement ste : e.getStackTrace()) {
                    sb.append(ste.toString());
                    sb.append("\n");
                }
                JTextArea jta = new JTextArea(sb.toString());
                jta.setFont(jta.getFont().deriveFont(12f));
                JScrollPane jsp = new JScrollPane(jta){
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(480, 320);
                    }
                };
                JOptionPane.showMessageDialog(
                        null, jsp, "Error", JOptionPane.ERROR_MESSAGE);
            }
            pm.setNote("Копирование завершено!");
        }).start();
    }

//    private static ActionListener createStartTaskActionListener(JFrame frame) {
//        //for progress monitor dialog title
//        UIManager.put("ProgressMonitor.progressText", "Копирование...");
//        //frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
//        //frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//        frame.setLayout(new FlowLayout(FlowLayout.CENTER));
//        frame.setResizable(false);
//        return (ae) -> new Thread(() -> {
//            //creating ProgressMonitor instance
//            //ProgressMonitor pm = new ProgressMonitor(frame, "Копирование фотографий",
////                    "Копирование...", 0, 100);
//            JProgressBar pb = new JProgressBar(JProgressBar.HORIZONTAL);
//            //decide after 0 millis whether to show popup or not
//            pb.setMinimum(0);
//            pb.setMaximum(100);
//            //after deciding if predicted time is longer than 100 show popup
//            frame.add(pb);
//            frame.pack();
//            frame.setVisible(true);
//
//            CopyDirVisitor.setPb(pb);
//            try {
//                Thread.sleep(1);
//                Copier.copyStart();
//            }
//            catch (IOException | InterruptedException | NullPointerException e) {
//                StringBuilder sb = new StringBuilder("Error: ");
//                sb.append(e.getMessage());
//                sb.append("\n");
//                for (StackTraceElement ste : e.getStackTrace()) {
//                    sb.append(ste.toString());
//                    sb.append("\n");
//                }
//                JTextArea jta = new JTextArea(sb.toString());
//                jta.setFont(jta.getFont().deriveFont(12f));
//                JScrollPane jsp = new JScrollPane(jta){
//                    @Override
//                    public Dimension getPreferredSize() {
//                        return new Dimension(480, 320);
//                    }
//                };
//                JOptionPane.showMessageDialog(
//                        null, jsp, "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        }).start();
//    }
}