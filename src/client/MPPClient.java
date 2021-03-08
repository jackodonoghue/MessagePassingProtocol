package client;

import common.Message;
import common.MyStreamSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.List;

/**
 * This module contains the presentaton logic of an Echo Client.
 *
 * @author M. L. Liu
 * @author J O'Donoghue
 * <p>
 * Modified for use with Message passing protocol
 */
public class MPPClient implements Runnable {
    private BufferedReader br;
    private boolean done;
    private final Message endMessage = new Message("", ".");
    private final Message allMessagesCharacter = new Message("", "*");
    private MyStreamSocket mySocket;

    MPPClient(String hostName, String portNumber) throws IOException {
        hostName = hostName == null || hostName.trim().equals("") ? "localhost" : portNumber;
        portNumber = portNumber == null || portNumber.trim().equals("") ? "7" : portNumber;
        InputStreamReader is = new InputStreamReader(System.in);
        br = new BufferedReader(is);
        InetAddress serverHost = InetAddress.getByName(hostName);
        int serverPort = Integer.parseInt(portNumber);
        //Instantiates a stream-mode socket and wait for a connection.
        this.mySocket = new MyStreamSocket(serverHost, serverPort);
    }

    public void run() {
        try {
            done = false;
            //maintain connection to server
            while (!done) {
                System.out.println("Enter a line to receive an echo "
                        + "from the server, or a single period to quit.");
                Message message = new Message("", br.readLine());
                System.out.println("message is " + message.getMessage());

                //check for end connection
                //end connection if requested
                if (message.equals(endMessage)) {
                    end();
                } else if (message.equals(allMessagesCharacter)) {
                    List<Message> messages = receiveAllMessages();
                    System.out.println(messages);
                }
                //otherwise 'get message'
                else {
                    Message echo = sendMessage(message);
                    System.out.println(echo);
                }
            } // end while
        } // end try
        catch (Exception ex) {
            ex.printStackTrace();
        } //end catch
    }

    public void end() throws IOException {
        this.done = true;
        mySocket.sendMessage(endMessage);
        mySocket.close();
    }

    public Message sendMessage(Message message) throws IOException, ClassNotFoundException {
        mySocket.sendMessage(message);
        return mySocket.receiveMessage();
    }

    public List<Message>
    receiveAllMessages() throws IOException, ClassNotFoundException {
        mySocket.sendMessage(allMessagesCharacter);
        return mySocket.receiveAllMessages();
    }
} // end class
