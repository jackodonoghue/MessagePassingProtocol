package client;

import javax.swing.*;
import java.awt.*;

public class UI {
    public static void main(String[] args) {

        //create jframe
        JFrame frame = new JFrame("Message Sending Protocol");
        frame.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        //Add client name
        frame.add(new JLabel("Welcome, client "), constraints);

        //Add send message UI
        constraints.weighty = 1;
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

        frame.setSize(500,500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
