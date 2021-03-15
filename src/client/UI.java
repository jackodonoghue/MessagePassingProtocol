package client;

import common.Message;
import common.MessageType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class UI {
    private static String username;
    private static String password;
    private static MPPClient client;
    private static SendMessageUI messageUI;
    private static ReceiveMessageUI receiveMessageUI;
    private static ActionListener listener = e -> {
        if (e.getSource().getClass().isInstance(new JButton())) {
            JButton event = (JButton) e.getSource();
            if (event.getText().trim().toLowerCase().equals("logout")) {
                try {
                    exit("Thank you come again");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else if (event.getText().trim().toLowerCase().equals("send")) {

                System.out.println("sending");
                if (client.sendMessage(new Message(username, messageUI.getMessageFromTextArea(), MessageType.SEND)).getType().equals(MessageType.SENDERR)) {
                    JOptionPane.showMessageDialog(null, "Error sending message");
                }

            } else if (event.getText().trim().toLowerCase().equals("get all messages")) {
                System.out.println("get all");
                List<Message> messageList;
                messageList = client.receiveAllMessages();
                System.out.println(messageList);
                receiveMessageUI.setAllMessages(messageList);
            }
        }
    };

    public static void main(String[] args){
        //create JFrame
        JFrame frame = new JFrame("Twitter Messaging Protocol");
        frame.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        try {

            //start client thread to handle backend
            client = MPPClientFactory.getMPPClient(username, password);

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
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setVisible(true);
            } else {
                exit("Wrong username password combo.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean login() throws IOException, ClassNotFoundException {
        LoginUI loginUI = new LoginUI();
        int result = loginUI.displayDialog();
        if (result == JOptionPane.OK_OPTION) {
            password = loginUI.getPassword();
            username = loginUI.getUsername();

            checkUserDetail(loginUI, username, "username");
            checkUserDetail(loginUI, password, "password");

            MessageType login = client.sendMessage(new Message(username, password, "", MessageType.LOGIN)).getType();
            System.out.println(login);
            return login == MessageType.LOGIN;
        } else {
            exit("Thank you. Come again");
            return false;
        }
    }

    private static void checkUserDetail(LoginUI loginUI, String userDetail, String detailType) throws IOException, ClassNotFoundException {
        if (userDetail == null || userDetail.trim().equals("")) {
            JOptionPane.showMessageDialog(null, "You must enter a " + detailType + ".");
            login();
        } else {
            username = loginUI.getUsername();
            System.out.println("" + detailType + ": " + userDetail);
        }
    }

    private static void exit(String exitMessage) throws IOException {
        JOptionPane.showMessageDialog(null, exitMessage);
        client.end();
        System.exit(0);
    }
}
