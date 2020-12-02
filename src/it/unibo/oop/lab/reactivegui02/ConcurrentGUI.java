package it.unibo.oop.lab.reactivegui02;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;



public class ConcurrentGUI extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;
    private final JLabel label = new JLabel("0");
    private final JButton up = new JButton("up");
    private final JButton stop = new JButton("stop");
    private final JButton down = new JButton("down");
    private final Agent agent = new Agent();

    public ConcurrentGUI() {
        super();
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (dim.getWidth() * ConcurrentGUI.WIDTH_PERC), (int) (dim.getHeight() * ConcurrentGUI.HEIGHT_PERC));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        final JPanel panel = new JPanel();
        panel.add(label);
        panel.add(up);
        panel.add(down);
        panel.add(stop);
        this.getContentPane().add(panel);
        this.setVisible(true);
        new Thread(agent).start();

        up.addActionListener(e -> agent.setDirection(true));
        stop.addActionListener(e -> agent.stopCounting());
        down.addActionListener(e -> agent.setDirection(false));
    }

    public Agent getAgent() {
        return this.agent;
    }

    public class Agent implements Runnable {

        private int counter;
        private volatile boolean stop;
        private volatile boolean direction;

        @Override
        public void run() {
            while (!stop) {
                try {
                    SwingUtilities.invokeAndWait(() -> { 
                        ConcurrentGUI.this.label.setText(String.valueOf(this.counter));
                     });
                    this.counter += this.direction ? +1 : -1;
                    Thread.sleep(100);
                } catch (InterruptedException | InvocationTargetException ex) { 
                    System.err.println("Errore nell' esecuzione del thread");
                    ex.printStackTrace();
                }
            }
        }

        public void stopCounting() {
            this.stop = true;
            SwingUtilities.invokeLater(() -> {
                ConcurrentGUI.this.up.setEnabled(false);
                ConcurrentGUI.this.down.setEnabled(false);
             });
        }

        public void setDirection(final boolean direction) {
            this.direction = direction;
        }
    }
}

