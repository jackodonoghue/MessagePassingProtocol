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
 * Modified to work as a concurrent server that handles the
 * server thread and manages sending/recieving and ending
 * sessions.
 */

class MPPServerThread implements Runnable {
    static final String endMessage = ".";
    static final String allMessageCharacter = "*";
    MyStreamSocket myDataSocket;
    private List<Message> messages;
    private List<MPPServerThread> clients;

    MPPServerThread(MyStreamSocket myDataSocket) {
        this.myDataSocket = myDataSocket;
    }

    public synchronized void run() {
        boolean done = false;
        Message message;

        try {
            messages = new ArrayList<>();

            while (!done) {
                message = myDataSocket.receiveMessage();
                System.out.println("message received: " + message);
                if ((message.getMessage().trim()).equals(endMessage)) {
                    System.out.println("Session over.");
                    myDataSocket.close();
                    done = true;
                } else if (message.getMessage().trim().equals(allMessageCharacter)) {
                    System.out.println("getting al messages");
                    myDataSocket.sendAllMessages(getAllMessages());
                } else {
                    messages.add(message);
                    myDataSocket.sendMessage(message);
                }
            }
        } catch (Exception ex) {
            System.out.println("Exception caught in thread: " + ex);
        }
    }

    public List<Message> getAllMessages() {
        List<Message> allMessages = new ArrayList<>();
        for (MPPServerThread client : clients) {
            client.messages.forEach(m -> allMessages.add(m));
        }

        allMessages.forEach(m -> System.out.println(m.getMessage()));

        return allMessages;
    }

    public void setClients(ArrayList<MPPServerThread> clients) {
        this.clients = clients;
    }
} //end class
