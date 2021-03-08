package common;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

/**
 * A wrapper class of Socket which contains
 * methods for sending and receiving messages
 *
 * @author M. L. Liu
 * @author J O'Donoghue
 * <p>
 * Modified for use with Message passing protocol
 */
public class MyStreamSocket extends Socket {
    private SSLSocket socket;
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;

    public MyStreamSocket(InetAddress acceptorHost, int acceptorPort) throws IOException {
        SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        socket = (SSLSocket) socketFactory.createSocket(acceptorHost, acceptorPort);
        socket.startHandshake();
        setStreams();

    }

    public MyStreamSocket(SSLSocket socket) throws IOException {
        this.socket = socket;
        setStreams();
    }

    private void setStreams() throws IOException {
        // get an input stream for reading from the data socket
        outStream = new ObjectOutputStream(socket.getOutputStream());
        inStream = new ObjectInputStream(socket.getInputStream());
    }

    public void sendMessage(Message message)
            throws IOException {
        outStream.writeObject(message);
    }

    public Message receiveMessage()
            throws IOException, ClassNotFoundException {
        // read a line from the data stream
        return (Message) inStream.readObject();
    }

    public void close()
            throws IOException {
        socket.close();
    }

    public List<Message> receiveAllMessages()
            throws IOException, ClassNotFoundException {
        // read a line from the data stream
        List<Message> messages = (List<Message>) inStream.readObject();
        return messages;
    }

    //send all of the messages to the client
    public void sendAllMessages(List<Message> messages) throws IOException {
        outStream.writeObject(messages);
    }
}
