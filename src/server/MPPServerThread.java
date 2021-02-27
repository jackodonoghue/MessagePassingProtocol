package server;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This module is to be used with a concurrent Echo server.
 * Its run method carries out the logic of a client session.
 *
 * @author M. L. Liu
 */

class MPPServerThread implements Runnable {
    static final String endMessage = ".";
    static final String allMessageCharacter = "*";
    MyStreamSocket myDataSocket;
    private List<String> messages;
    private List<MPPServerThread> clients;
    private List<String> allMessages;

    MPPServerThread(MyStreamSocket myDataSocket) {
        this.myDataSocket = myDataSocket;
    }

    //what happens in a thread
    public void run() {
        boolean done = false;
        String message;

        try {
            messages = new ArrayList<>();
            //loop
            while (!done) {
                message = myDataSocket.receiveMessage();
                System.out.println("message received: " + message);
                if ((message.trim()).equals(endMessage)) {
                    //Session over; close the data socket.
                    System.out.println("Session over.");
                    myDataSocket.close();
                    done = true;
                } //end if
                else if(message.trim().equals(allMessageCharacter)){
                    System.out.println("getting al messages");
                    getAllMessages();
                    myDataSocket.sendAllMessages(messages);
                }
                else {
                    // Now send the echo to the requester
                    messages.add(message);
                    myDataSocket.sendMessage(message);
                } //end else
            } //end while !done
        }// end try
        catch (Exception ex) {
            System.out.println("Exception caught in thread: " + ex);
        } // end catch
    } //end run

    public List<String> getAllMessages(){
        allMessages = new ArrayList<>();
        for (MPPServerThread client: clients) {
            client.messages.forEach(m -> this.allMessages.add(m));
        }
        return this.allMessages;
    }

    public void setClients(ArrayList<MPPServerThread> clients) {
        this.clients = clients;
    }
} //end class
