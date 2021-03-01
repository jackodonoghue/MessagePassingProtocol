package client;

import common.Message;
import common.MyStreamSocket;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * This module contains the presentation logic of an Echo Client.
 *
 * @author M. L. Liu
 * @author J O'Donoghue
 *
 * Modified to work with multiple threads and allow for object creation.
 *
 *
 */
public class MPPClient {
    final Message allMessagesCharacter = new Message("", "*");
    final Message endMessage = new Message("", ".");
    private MyStreamSocket mySocket;
    private InetAddress serverHost;
    private int portNumber;
    private String hostName;

    MPPClient(String hostName, String portNumber) throws IOException {
        this.portNumber = (portNumber == "") ? 7 : Integer.parseInt(portNumber);
        this.hostName = (hostName == "") ? "localhost" : hostName;
        this.serverHost = InetAddress.getByName(this.hostName);
        //Instantiates a stream-mode socket and wait for a connection.
        this.mySocket = new MyStreamSocket(this.serverHost, this.portNumber);
    }

    public void sendMessage(Message message) throws IOException {
        mySocket.sendMessage(message);
    }

    public List<Message> getAllMessages() throws IOException, ClassNotFoundException {
        mySocket.sendMessage(allMessagesCharacter);
        return mySocket.receiveAllMessages();
    }

    public void end() throws IOException, ClassNotFoundException {
        mySocket.sendMessage(endMessage);
        mySocket.close();
    }
}
