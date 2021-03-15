package client;

import common.Message;
import common.MessageType;
import common.MyStreamSocket;
import server.UserStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.List;

/**
 * This module contains the presentation logic of an Echo Client.
 *
 * @author M. L. Liu
 * @author J O'Donoghue
 * <p>
 * Modified for use with Message passing protocol
 */
public class MPPClient {
    private final Message endMessage = new Message(MessageType.LOGOUT);
    private final Message allMessagesCharacter = new Message(MessageType.GET);
    private MyStreamSocket mySocket;
    private final InetAddress HOSTNAME = InetAddress.getByName("localhost");
    private final int PORT_NUMBER = 7;


    MPPClient(String username, String password) throws IOException {
        this.mySocket = new MyStreamSocket(HOSTNAME, PORT_NUMBER, username, password);
    }

    public void end() throws IOException {
        mySocket.sendMessage(endMessage);
        mySocket.close();
    }

    public Message sendMessage(Message message) {
        mySocket.sendMessage(message);
        return mySocket.receiveMessage();
    }

    public List<Message>
    receiveAllMessages() {
        mySocket.sendMessage(allMessagesCharacter);
        return mySocket.receiveAllMessages();
    }
} // end class
