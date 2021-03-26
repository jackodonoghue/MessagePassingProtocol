package server.session;

import common.Message;
import common.MessageType;

import javax.net.ssl.SSLSocket;
import java.io.EOFException;
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
public class ServerStreamSocket {
    private SSLSocket socket;
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;

    public ServerStreamSocket(SSLSocket socket) throws IOException {
        this.socket = socket;
        setStreams();
    }

    private void setStreams() throws IOException {
        outStream = new ObjectOutputStream(socket.getOutputStream());
        inStream = new ObjectInputStream(socket.getInputStream());
    }

    public boolean sendMessage(Message message) {
        try {
            //reset stops out stream from referencing old state of an object. was preventing get all messages from displaying updated lists
            outStream.reset();
            outStream.writeObject(message);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Message receiveMessage(){
        try {
            return (Message) inStream.readObject();
        } catch (EOFException e) {
            return new Message(MessageType.CONNERR);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new Message(MessageType.CONNERR);
        }
    }

    public synchronized boolean closeConnection() {
        boolean closed;
        try {
            outStream.close();
            inStream.close();
            socket.close();
            System.out.println();
            closed = true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Connection already closed");
            closed = false;
        }
        return closed;
    }

    public int getPortNumber() {
        return socket.getPort();
    }
}
