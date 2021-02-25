package server;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This module is to be used with a concurrent Echo server.
 * Its run method carries out the logic of a client session.
 *
 * @author M. L. Liu
 */

class MPPServerThread implements Runnable {
    static final String endMessage = ".";
    MyStreamSocket myDataSocket;
    private ArrayList<String> messages;

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
                    for (String m: messages) {
                        System.out.println("end: "+m);
                    }
                    myDataSocket.close();
                    done = true;
                } //end if
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

    public ArrayList<String> getAllMessages(){
        return messages;
    }
} //end class 
