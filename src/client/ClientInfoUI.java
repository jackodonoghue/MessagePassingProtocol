package client;

import javax.swing.*;
import java.awt.*;

public class ClientInfoUI extends JPanel {
    ClientInfoUI(String username) {
        add(new JLabel("Welcome, " + username));
        add(new JButton("Logout"));
    }
}
