package client;

import javax.swing.*;
import java.awt.*;

public class LoginUI {
    private JTextField hostNameField, portNumberField, usernameField;

    LoginUI() {
    }

    private JPanel getLoginPanel() {
        hostNameField = new JTextField(10);
        portNumberField = new JTextField(10);
        usernameField = new JTextField(10);

        JPanel loginPanel = new JPanel(new GridLayout(3, 2));

        //hostPanel
        loginPanel.add(new JLabel("Host name:"));
        loginPanel.add(hostNameField);

        //Port panel
        loginPanel.add(new JLabel("Port number:"));
        loginPanel.add(portNumberField);

        //user panel
        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);

        return loginPanel;
    }

    public int displayLoginDialog() {
        return JOptionPane.showConfirmDialog(null, getLoginPanel(),
                "Login", JOptionPane.OK_CANCEL_OPTION);
    }

    public String getHostName() {
        if (hostNameField.getText() == null || (hostNameField.getText() != null && hostNameField.getText().trim().equals("")))
            return "localhost";
        return hostNameField.getText();
    }

    public String getPortNumber() {
        if (portNumberField.getText() == null || (portNumberField.getText() != null && portNumberField.getText().trim().equals("")))
            return "7";
        return portNumberField.getText();
    }

    public String getUsername() {
        return usernameField.getText();
    }
}
