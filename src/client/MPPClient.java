package client;

import common.Message;

import java.io.*;
import java.util.List;

/**
 * This module contains the presentaton logic of an Echo Client.
 *
 * @author M. L. Liu
 */
public class MPPClient implements Runnable{
    private final String endMessage = ".";
    private final String allMessagesCharacter = "*";
    private BufferedReader br;
    private InputStreamReader is;
    private MPPClientHelper helper;
    private boolean done;
    private Message message, echo;
    private String hostName, portNumber;
    private String username;

    MPPClient(String hostName, String portNumber, String username) {
        is = new InputStreamReader(System.in);
        br = new BufferedReader(is);
        this.hostName = hostName;
        this.portNumber = portNumber;
        this.username = username;
    } //end main

    public void run() {
        try {
            //connect to host with port number
            helper = new MPPClientHelper(getHostName(hostName), getPortNumber(portNumber));

            done = false;

            //maintain connection to server
            while (!done) {
                //send a message
                System.out.println("Enter a line to receive an echo "
                        + "from the server, or a single period to quit.");
                message = new Message(this.username, br.readLine());

                //check for end connection
                //end connection if requested
                if ((message.getMessage().trim()).equals(endMessage)) {
                    end();
                }
                else if (message.getMessage().trim().equals(allMessagesCharacter)){
                    List<Message> messages = helper.getAllMessages();
                    System.out.println(messages);
                }
                //otherwise 'get message'
                else {
                    echo = helper.getMessage(message);
                    System.out.println(echo);
                }
            } // end while
        } // end try
        catch (Exception ex) {
            ex.printStackTrace();
        } //end catch
    }

    public void end() throws IOException {
        this.done = true;
        helper.terminateConnection();
    }

    public void sendMessage(Message message) throws IOException, ClassNotFoundException {
        echo = helper.getMessage(message);
        System.out.println(echo);
    }

    public List<Message>
    receiveAllMessages() throws IOException, ClassNotFoundException {
        return helper.getAllMessages();
    }

    private String getPortNumber(String portNum) throws IOException {
        if (portNum.length() == 0)
            portNum = "7";          // default port number
        return portNum;
    }

    private String getHostName(String hostName) throws IOException {
        if (hostName.length() == 0) // if user did not enter a name
            hostName = "localhost";  //   use the default host name
        return hostName;
    }
} // end class
