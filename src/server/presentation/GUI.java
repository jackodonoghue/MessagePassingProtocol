package server.presentation;

import server.application.MPPServer;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUI {
    private JPanel serverPanel;
    private JButton start;
    private JButton stop;
    private static MPPServer server = new MPPServer();
    private boolean started = false;

    public GUI() {
        start.addActionListener(e -> {
            if(!started){
                (new Thread(server)).start();
                started = true;
            }

        });
        stop.addActionListener(e -> {
                    server.setDone(true);
                    JOptionPane.showMessageDialog(null, "The server will shut down when the next client tries to connect.");
                }
        );
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("GUI");
        frame.setContentPane(new GUI().serverPanel);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("closed window");
                server.setDone(true);
                System.exit(0);
            }
        });
        frame.pack();
        frame.setVisible(true);
    }
}
