package client.session;

import common.Message;
import common.MessageType;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
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
public class ClientStreamSocket {
    private SSLSocket socket;
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;

    public ClientStreamSocket(InetAddress acceptorHost, int acceptorPort) throws IOException {
        SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        socket = (SSLSocket) socketFactory.createSocket(acceptorHost, acceptorPort);
        socket.startHandshake();
        setStreams();
    }

    private void setStreams() throws IOException {
        outStream = new ObjectOutputStream(socket.getOutputStream());
        inStream = new ObjectInputStream(socket.getInputStream());
    }

    public Message sendMessage(Message message) {
        try {
            outStream.reset();
            outStream.writeObject(message);
            return receiveMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return new Message(MessageType.CONNERR);
        }
    }

    //used in send message to return the message type and list of messages.
    public Message receiveMessage(){
        try {
            return (Message) inStream.readObject();
        } catch (EOFException f){
            //this exception is thrown when the connection is terminated while reading
            return new Message(MessageType.CONNERR);
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new Message(MessageType.CONNERR);
        }
    }

    public boolean closeConnection() {
        try {
            //Not using sendMessage() as the client would try to receive a message on a closed connection. In the case of LOGOUT, the client does not need a response
            outStream.writeObject(new Message(MessageType.LOGOUT));
            socket.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
