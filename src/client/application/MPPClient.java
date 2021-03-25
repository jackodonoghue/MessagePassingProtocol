package client.application;

import common.Message;
import common.MessageType;
import client.session.ClientStreamSocket;

import java.io.IOException;
import java.net.InetAddress;

/**
 * This module contains the presentation logic of an Echo Client.
 *
 * @author M. L. Liu
 * @author J O'Donoghue
 * <p>
 * Modified for use with Message passing protocol
 */
public class MPPClient {
    private final ClientStreamSocket SOCKET;

    MPPClient() throws IOException {
        InetAddress HOSTNAME = InetAddress.getByName("localhost");
        int PORT_NUMBER = 7;
        this.SOCKET = new ClientStreamSocket(HOSTNAME, PORT_NUMBER);
    }

    public Message sendMessage(Message message) {
        return SOCKET.sendMessage(message);
    }

    public boolean end() {
        SOCKET.sendMessage(new Message(MessageType.LOGOUT));
        return SOCKET.closeConnection();
    }
} // end class
