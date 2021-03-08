package server;

import common.Message;
import common.MyStreamSocket;

import java.util.ArrayList;
import java.util.List;

/**
 * This module is to be used with a concurrent Echo server.
 * Its run method carries out the logic of a client session.
 *
 * @author M. L. Liu
 * @author J O'Donoghue
 * <p>
 * Modified for use with Message passing protocol
 */

class MPPServerThread implements Runnable {
    static final String endMessage = ".";
    static final String allMessageCharacter = "*";
    MyStreamSocket myDataSocket;
    private List<Message> messages;
    private List<MPPServerThread> clients;
    private List<Message> allMessages;

    MPPServerThread(MyStreamSocket myDataSocket) {
        this.myDataSocket = myDataSocket;
    }

    //what happens in a thread
    public void run() {
        boolean done = false;
        Message message;

        try {
            messages = new ArrayList<>();
            while (!done) {
                message = myDataSocket.receiveMessage();
                System.out.println("message received: " + message);
                if ((message.getMessage().trim()).equals(endMessage)) {
                    //Session over; close the data socket.
                    System.out.println("Session over.");
                    myDataSocket.close();
                    done = true;
                } else if (message.getMessage().trim().equals(allMessageCharacter)) {
                    System.out.println("getting al messages");
                    myDataSocket.sendAllMessages(getAllMessages());
                } else {
                    // Now send the echo to the requester
                    messages.add(message);
                    myDataSocket.sendMessage(message);
                }
            }
        }// end try
        catch (Exception ex) {
            System.out.println("Exception caught in thread: " + ex);
        } // end catch
    }

    public List<Message> getAllMessages() {
        allMessages = new ArrayList<>();
        for (MPPServerThread client : clients) {
            client.messages.forEach(m -> this.allMessages.add(m));
        }

        System.out.println(allMessages);

        return this.allMessages;
    }

    public void setClients(ArrayList<MPPServerThread> clients) {
        this.clients = clients;
    }
} //end class
