package client.presentation;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionListener;

public class ClientInfoUI extends JPanel {
    JButton logoutButton = new JButton("Logout");

    ClientInfoUI(String username, ActionListener listener) {
        add(new JLabel("Welcome, " + username));
        logoutButton.addActionListener(listener);
        add(logoutButton);
    }
}
