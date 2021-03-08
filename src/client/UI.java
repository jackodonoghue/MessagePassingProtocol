package client;

import common.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class UI {
    private static String hostname;
    private static String portNumber;
    private static String username;
    private static MPPClient client;
    private static SendMessageUI messageUI;
    private static ReceiveMessageUI receiveMessageUI;
    private static ActionListener listener = e -> {
        if (e.getSource().getClass().isInstance(new JButton())) {
            JButton event = (JButton) e.getSource();
            if (event.getText().trim().toLowerCase().equals("logout")) {
                try {
                    exit();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else if (event.getText().trim().toLowerCase().equals("send")) {
                try {
                    System.out.println("sending");
                    client.sendMessage(new Message(username, messageUI.getMessageFromTextArea()));
                } catch (IOException | ClassNotFoundException exception) {
                    exception.printStackTrace();
                }
            } else if (event.getText().trim().toLowerCase().equals("get all messages")) {
                System.out.println("get all");
                try {
                    List<Message> messageList = client.receiveAllMessages();
                    System.out.println(messageList);
                    receiveMessageUI.setAllMessages(messageList);
                } catch (IOException | ClassNotFoundException exception) {
                    exception.printStackTrace();
                }
            }
        }
    };

    public static void main(String[] args) throws IOException {
        //create JFrame
        JFrame frame = new JFrame("Twitter Messaging Protocol");
        frame.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        //Login Dialog
        login();

        try {
            //start client thread to handle backend
            client = MPPClientFactory.getMPPClient(hostname, portNumber);
            Thread theThread = new Thread(client);
            theThread.start();

            //display clients frontend
            constraints.weighty = .1;
            constraints.weightx = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.gridy = 0;
            frame.add(new ClientInfoUI(username, listener), constraints);

            //Add send message UI
            messageUI = new SendMessageUI(listener);
            constraints.weighty = .25;
            constraints.weightx = 1;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridy = 1;
            frame.add(messageUI, constraints);

            //Add receive message UI
            receiveMessageUI = new ReceiveMessageUI(listener);
            constraints.weighty = 1;
            constraints.weightx = 1;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridy = 2;
            frame.add(receiveMessageUI, constraints);

            frame.setSize(500, 500);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void login() throws IOException {
        LoginUI loginUI = new LoginUI();
        int result = loginUI.displayDialog();
        if (result == JOptionPane.OK_OPTION) {
            //create new client
            hostname = loginUI.getHostName();
            portNumber = loginUI.getPortNumber();
            if (loginUI.getUsername() == null || (loginUI.getUsername() != null && loginUI.getUsername().trim().equals(""))) {
                JOptionPane.showMessageDialog(null, "You must enter a username.");
                login();
            } else {
                username = loginUI.getUsername();
                System.out.println("username: " + loginUI.getUsername());
            }
        } else {
            exit();
        }
    }

    private static void exit() throws IOException {
        JOptionPane.showMessageDialog(null, "Thank you come again.");
        client.end();
        System.exit(0);
    }
}
