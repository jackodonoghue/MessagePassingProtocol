package server;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper class of Socket which contains
 * methods for sending and receiving messages
 *
 * @author M. L. Liu
 */
public class MyStreamSocket extends Socket {
    private Socket socket;
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;

    MyStreamSocket(InetAddress acceptorHost,
                   int acceptorPort) throws SocketException,
            IOException {
        socket = new Socket(acceptorHost, acceptorPort);
        setStreams();

    }

    MyStreamSocket(Socket socket) throws IOException {
        this.socket = socket;
        setStreams();
    }

    private void setStreams( ) throws IOException{
        // get an input stream for reading from the data socket
        outStream = new ObjectOutputStream(socket.getOutputStream());
        inStream = new ObjectInputStream(socket.getInputStream());
    }

    public void sendMessage(String message)
            throws IOException {
        outStream.writeObject(message);
    } // end sendMessage

    public String receiveMessage()
            throws IOException, ClassNotFoundException {
        // read a line from the data stream
        String message = (String)inStream.readObject();
        return message;
    } //end receiveMessage

    public void close()
            throws IOException {
        socket.close();
    }

    public List<String> receiveAllMessages()
            throws IOException, ClassNotFoundException {
        // read a line from the data stream
        List<String> message = (ArrayList<String>)inStream.readObject();
        return message;
    } //end receiveMessage

    //send all of the messages to the client
    public void sendAllMessages(List<String> messages) throws IOException {
        outStream.writeObject(messages);
    }
} //end class
