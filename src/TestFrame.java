import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

public class TestFrame extends JFrame {

    static private int BOR = 10;

    public static void createGUI() {
        final JFrame frame = new JFrame("Test frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(BOR, BOR, BOR, BOR));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(Box.createVerticalGlue());

        JProgressBar progressBar1 = new JProgressBar();
        progressBar1.setIndeterminate(true);
        panel.add(progressBar1);

        panel.add(Box.createVerticalGlue());

        final JProgressBar progressBar2 = new JProgressBar();
        progressBar2.setStringPainted(true);
        progressBar2.setMinimum(0);
        progressBar2.setMaximum(100);
        panel.add(progressBar2);

        panel.add(Box.createVerticalGlue());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));

        buttonsPanel.add(Box.createHorizontalGlue());

        JButton increment = new JButton("+10%");
        increment.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent r) {
                CopyDirVisitor.setPb(progressBar2);
                try {
                    Thread.sleep(1);
                    Copier.copyStart();
                } catch (IOException | InterruptedException | NullPointerException e) {
                    StringBuilder sb = new StringBuilder("Error: ");
                    sb.append(e.getMessage());
                    sb.append("\n");
                    for (StackTraceElement ste : e.getStackTrace()) {
                        sb.append(ste.toString());
                        sb.append("\n");
                    }
                    JTextArea jta = new JTextArea(sb.toString());
                    jta.setFont(jta.getFont().deriveFont(12f));
                    JScrollPane jsp = new JScrollPane(jta) {
                        @Override
                        public Dimension getPreferredSize() {
                            return new Dimension(480, 320);
                        }
                    };
                    JOptionPane.showMessageDialog(
                            null, jsp, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonsPanel.add(increment);

        buttonsPanel.add(Box.createHorizontalGlue());

        JButton decrement = new JButton("-10%");
        decrement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int value = progressBar2.getValue() - 10;
                int minimum = progressBar2.getMinimum();
                if(value < minimum) {
                    value = minimum;
                }
                progressBar2.setValue(value);
            }
        });
        buttonsPanel.add(decrement);

        buttonsPanel.add(Box.createHorizontalGlue());

        panel.add(buttonsPanel);

        panel.add(Box.createVerticalGlue());

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(panel, BorderLayout.CENTER);

        frame.setPreferredSize(new Dimension(260, 225));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                createGUI();
            }
        });
    }

}