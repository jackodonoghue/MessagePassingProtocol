package client;

import common.Message;
import common.MyStreamSocket;

import java.net.*;
import java.io.*;
import java.util.List;

/**
 * This class is a module which provides the application logic
 * for an Echo client using stream-mode socket.
 *
 * @author M. L. Liu
 * @author J O'Donoghue
 *
 * Modyfied for use with a GUI, by removing certain code.
 * Changed to work with the message passing protocol.
 *
 */

public class MPPClientHelper {
    final Message endMessage = new Message("", ".");
    final Message allMessagesCharacter = new Message("", "*");
    private MyStreamSocket mySocket;
    private InetAddress serverHost;
    private int serverPort;

    MPPClientHelper(String hostName, String portNum) throws IOException {
        this.serverHost = InetAddress.getByName(hostName);
        this.serverPort = Integer.parseInt(portNum);
        //Instantiates a stream-mode socket and wait for a connection.
        this.mySocket = new MyStreamSocket(this.serverHost, this.serverPort);
        System.out.println("Connection request made");
    }


    public Message getMessage(Message message) throws IOException, ClassNotFoundException {
        Message msg;
        mySocket.sendMessage(message);
        msg = mySocket.receiveMessage();
        return msg;
    }

    public void terminateConnection() throws IOException {
        mySocket.sendMessage(endMessage);
        mySocket.close();
    }

    public List<Message> getAllMessages() throws IOException, ClassNotFoundException {
        mySocket.sendMessage(allMessagesCharacter);
        return mySocket.receiveAllMessages();
    }
}
