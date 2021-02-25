package client;

import javax.swing.*;
import java.awt.*;

public class UI {
    public static void main(String[] args) {

        //create jframe
        JFrame frame = new JFrame("Twitter Messaging Protocol");
        frame.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        //Login Dialog
        String username = JOptionPane.showInputDialog(null, "Enter username: ");

        if (username == null || (username != null && username.trim().equals(""))) {
            exit();
        }
        //Add client name
        constraints.weighty = .1;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridy = 0;
        frame.add(new ClientInfoUI(username), constraints);

        //Add send message UI
        constraints.weighty = .25;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridy = 1;
        frame.add(new SendMessageUI(), constraints);

        //Add receive message UI
        constraints.weighty = 1;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridy = 2;
        frame.add(new RecieveMessageUI(), constraints);

        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static void exit() {
        JOptionPane.showMessageDialog(null, "Thank you come again.");
        System.exit(0);
    }
}
