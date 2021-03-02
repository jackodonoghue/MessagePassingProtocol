package client;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ClientInfoUI extends JPanel {
    JButton logoutButton = new JButton("Logout");
    ClientInfoUI(String username, ActionListener listener) {
        add(new JLabel("Welcome, " + username));
        logoutButton.addActionListener(listener);
        add(logoutButton);
    }
}
