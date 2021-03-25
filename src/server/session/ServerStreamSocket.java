package server.session;

import common.Message;
import common.MessageType;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
public class ServerStreamSocket extends Socket {
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
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new Message(MessageType.CONNERR);
        }
    }

    public synchronized boolean closeConnection() {
//        socket.shutdownInput();
//        socket.shutdownOutput();
        try {
            socket.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
