package client.presentation;

import client.application.MPPClient;
import common.Message;
import common.MessageType;
import common.MPPGetProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Main {

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
                String messageText = messageUI.getMessageFromTextArea();
                List<String> payload = new ArrayList();
                payload.add(username);
                if(messageText.equals("")){
                    JOptionPane.showMessageDialog(null, "You cannot send an empty message");
                }
                else{
                    payload.add(messageText);
                    if (client.sendMessage(new Message(payload, MessageType.SEND)).getType().equals(MessageType.SENDERR)) {
                        JOptionPane.showMessageDialog(null, "Error sending message");
                    }
                }
            } else if (event.getText().trim().toLowerCase().equals("get all messages")) {
                Message messageList;
                messageList = client.sendMessage(new Message(MessageType.GET));
                receiveMessageUI.setAllMessages(messageList);
            }
        }
    };

    public static void main(String[] args) {
        //get SSL properties
        MPPGetProperties properties = null;
        try {
            properties = new MPPGetProperties("client");
        }catch (NullPointerException e) {
           e.printStackTrace();
           exit("Could not access Properties File");
        }catch (IOException e) {
            e.printStackTrace();
            exit("Could not access Keystore");
        }
        
        String trustStoreLocation = (properties != null ? properties.getLocation() : null) + properties.getKeystoreName();

        //set SSL properties
        System.setProperty("javax.net.ssl.trustStore", trustStoreLocation);
        System.setProperty("javax.net.ssl.trustStorePassword", properties.getPassword());

        //start client thread to handle backend
        try {
            client = new MPPClient();
        } catch (ConnectException connectException) {
            connectException.printStackTrace();
            exit("Could not connect to server");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            exit("Invalid Hostname");
        } catch (IOException e) {
            e.printStackTrace();
            exit("Could not crete Socket");
        }

        //Login Dialog
        if (login()) {
            //create JFrame
            JFrame frame = new JFrame("Twitter Messaging Protocol");
            createClientFrontEnd(frame);
            frame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Wrong username password combo. Shutting down.");
            System.exit(0);
        }
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

            //if the client gets a loginok message from the server the client will be logged in
            return client.sendMessage(new Message(payload, MessageType.LOGIN)).getType() == MessageType.LOGINOK;
        }
        else if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
            exit("Thank you. Come again.");
            return false;
        }
        else {
            return false;
        }
    }

    private static void checkUserDetail(LoginUI loginUI, String userDetail, String detailType) {
        if (userDetail == null || userDetail.trim().equals("")) {
            JOptionPane.showMessageDialog(null, "You must enter a " + detailType + ".");
            login();
        } else {
            username = loginUI.getUsername();
        }
    }

    private static void exit(String exitMessage) {
        JOptionPane.showMessageDialog(null, exitMessage);
        //close connection if it exists. Might not be initialised if the user exits before entering credentials
        if(client != null){
            System.out.println("client.end");
            client.end();
        }
        System.out.println("exiting");
        System.exit(0);
    }

    private static void createClientFrontEnd(JFrame frame) {
        frame.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        //display clients frontend
        constraints.weighty = .1;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridy = 0;
        frame.add(new ClientInfoUI(username, listener), constraints);

        //Add send message Main
        messageUI = new SendMessageUI(listener);
        constraints.weighty = .25;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridy = 1;
        frame.add(messageUI, constraints);

        //Add receive message Main
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
                System.out.println("closed window");
                exit("Thank you. Come again.");
            }
        });
    }
}
