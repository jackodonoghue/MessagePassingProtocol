package client.application;

import common.Message;
import client.session.ClientStreamSocket;
import common.MessageType;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.UnknownHostException;

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

    public MPPClient() throws ConnectException, UnknownHostException, IOException {
        InetAddress HOSTNAME = InetAddress.getByName("localhost");
        int PORT_NUMBER = 7;
        this.SOCKET = new ClientStreamSocket(HOSTNAME, PORT_NUMBER);
    }

    public Message sendMessage(Message message) {
        if(SOCKET.sendMessage(message))
            return SOCKET.receiveMessage();
        else
            return new Message(MessageType.CONNERR);
    }

    public void end() {
        try {
            //using the sockets send message instead of MPPClient's send as retrieving an object
            // can result in error as the connection is closed
            SOCKET.sendMessage(new Message(MessageType.LOGOUT));
            SOCKET.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} // end class
