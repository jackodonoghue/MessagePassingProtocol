package client.presentation;

import client.application.MPPClient;
import client.application.MPPClientFactory;
import common.Message;
import common.MessageType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UI {
    private static String username;
    private static MPPClient client = null;
    private static SendMessageUI messageUI;
    private static ReceiveMessageUI receiveMessageUI;
    private static ActionListener listener = e -> {
        if (e.getSource().getClass().isInstance(new JButton())) {
            JButton event = (JButton) e.getSource();
            if (event.getText().trim().toLowerCase().equals("logout")) {
                System.out.println("logout");
                exit("Thank you come again");
            } else if (event.getText().trim().toLowerCase().equals("send")) {
                System.out.println("sending");
                List<String> payload = new ArrayList();
                payload.add(username);
                payload.add(messageUI.getMessageFromTextArea());
                if (client.sendMessage(new Message(payload, MessageType.SEND)).getType().equals(MessageType.SENDERR)) {
                    JOptionPane.showMessageDialog(null, "Error sending message");
                }
            } else if (event.getText().trim().toLowerCase().equals("get all messages")) {
                Message messageList;
                messageList = client.sendMessage(new Message(MessageType.GET));
                System.out.println(messageList);
                receiveMessageUI.setAllMessages(messageList);
            }
        }
    };

    public static void main(String[] args) {
        //create JFrame
        JFrame frame = new JFrame("Twitter Messaging Protocol");
        frame.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();


        //start client thread to handle backend
        try {
            client = MPPClientFactory.getMPPClient();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Login Dialog
        if (login()) {
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
            frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    closeWindow();
                }
            });
            frame.setVisible(true);
        } else {
            exit("Wrong username password combo.");
        }
    }

    private static void closeWindow() {
        System.out.println("closed window");
        exit("Thank you. Come again.");
    }

    private static boolean login() {
        LoginUI loginUI = new LoginUI();
        int result = loginUI.displayDialog();
        if (result == JOptionPane.OK_OPTION) {
            String password = loginUI.getPassword();
            username = loginUI.getUsername();

            checkUserDetail(loginUI, username, "username");
            checkUserDetail(loginUI, password, "password");

            List<String> payload = new ArrayList();
            payload.add(username);
            payload.add(password);

            MessageType login = client.sendMessage(new Message(payload, MessageType.LOGIN)).getType();
            System.out.println(login);
            return login == MessageType.LOGINOK;
        } else {
            exit("Thank you. Come again");
            return false;
        }
    }

    private static void checkUserDetail(LoginUI loginUI, String userDetail, String detailType) {
        if (userDetail == null || userDetail.trim().equals("")) {
            JOptionPane.showMessageDialog(null, "You must enter a " + detailType + ".");
            login();
        } else {
            username = loginUI.getUsername();
            System.out.println("" + detailType + ": " + userDetail);
        }
    }

    private static void exit(String exitMessage) {
        System.out.println("exiting");
        if (client != null && client.end()) {
            JOptionPane.showMessageDialog(null, exitMessage);
            System.exit(0);
        } else {
            JOptionPane.showMessageDialog(null, "Error closing connection");
        }
    }
}
