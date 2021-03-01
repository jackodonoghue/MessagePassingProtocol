package common;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper class of Socket which contains
 * methods for sending and receiving messages
 *
 * @author M. L. Liu
 * @author J O'Donoghue
 * <p>
 * Modified to use secure sockets.
 * Methods added to allow for the sending of all messages.
 */
public class MyStreamSocket {
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;
    private SSLSocket socket;
    private SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();

    public MyStreamSocket(InetAddress acceptorHost, int acceptorPort) throws IOException {
        socket = (SSLSocket) socketFactory.createSocket(acceptorHost, acceptorPort);
        socket.startHandshake();
        setStreams();
    }

    public MyStreamSocket(SSLSocket socket) throws IOException {
        this.socket = socket;
        setStreams();
    }

    private void setStreams() throws IOException {
        outStream = new ObjectOutputStream(socket.getOutputStream());
        inStream = new ObjectInputStream(socket.getInputStream());
    }

    public void sendMessage(Message message) throws IOException {
        System.out.println("writing new message");
        outStream.writeObject(message);
    }

    public Message receiveMessage() throws IOException, ClassNotFoundException {
        return (Message) inStream.readObject();
    }

    public void close() throws IOException {
        socket.close();
    }

    public List<Message> receiveAllMessages() throws IOException, ClassNotFoundException {
        System.out.println(inStream.readObject());
        List<Message> messages = (ArrayList<Message>) inStream.readObject();

        return messages;
    }

    public void sendAllMessages(List<Message> messages) throws IOException {
        System.out.println("writing a list " + messages);
        outStream.writeObject(messages);
        outStream.reset();
        System.out.println("done writing");
    }
}
