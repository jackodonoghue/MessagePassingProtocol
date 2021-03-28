package server.presentation;

import server.application.MPPServer;

import javax.swing.*;

public class GUI {
    private JPanel serverPanel;
    private JButton start;
    private JButton stop;
    MPPServer server = new MPPServer();

    public GUI() {
        start.addActionListener(e -> (new Thread(server)).start());
        stop.addActionListener(e -> {
                    server.setDone(true);
                    JOptionPane.showMessageDialog(null, "The server will shut down when the next client tries to connect.");
                }
        );
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("GUI");
        frame.setContentPane(new GUI().serverPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
