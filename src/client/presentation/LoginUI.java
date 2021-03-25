package client.presentation;

import javax.swing.*;
import java.awt.*;

public class LoginUI {
    private JTextField usernameField;
    private JPasswordField passwordField;

    private JPanel getLoginPanel() {
        usernameField = new JTextField(10);
        passwordField = new JPasswordField(10);
        passwordField.setEchoChar('*');

        JPanel loginPanel = new JPanel(new GridLayout(2, 2));

        //user panel
        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);

        //user panel
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);

        return loginPanel;
    }

    public int displayDialog() {
        return JOptionPane.showConfirmDialog(null, getLoginPanel(),
                "Login", JOptionPane.OK_CANCEL_OPTION);
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() { return new String(passwordField.getPassword());
    }
}
