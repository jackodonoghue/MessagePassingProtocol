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
 */

public class MPPClientHelper {

    static final Message endMessage = new Message("", ".");
    static final Message allMessagesCharacter = new Message("", "*");
    private MyStreamSocket mySocket;
    private InetAddress serverHost;
    private int serverPort;

    MPPClientHelper(String hostName, String portNum) throws SocketException, UnknownHostException, IOException {
        this.serverHost = InetAddress.getByName(hostName);
        this.serverPort = Integer.parseInt(portNum);
        //Instantiates a stream-mode socket and wait for a connection.
        this.mySocket = new MyStreamSocket(this.serverHost, this.serverPort);
        System.out.println("Connection request made");
    } // end constructor


    public Message getMessage(Message message) throws SocketException, IOException, ClassNotFoundException {
        Message echo;
        mySocket.sendMessage(message);
        // now receive the echo
        echo = mySocket.receiveMessage();
        return echo;
    } // end getEcho

    public void terminateConnection() throws SocketException, IOException {
        mySocket.sendMessage(endMessage);
        mySocket.close();
    } // end done

    public List<Message> getAllMessages() throws IOException, ClassNotFoundException {
        mySocket.sendMessage(allMessagesCharacter);
        return mySocket.receiveAllMessages();
    }
} //end class
