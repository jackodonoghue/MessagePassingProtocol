package server;

import common.Message;
import common.MessageType;
import common.MyStreamSocket;

import java.io.IOException;
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
    private UserStore userStore;

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
                switch (message.getType()) {
                    case LOGIN:
                        if(login(message.getUsername(), message.getPassword())){
                            myDataSocket.sendMessage(new Message(message.getUsername(), "Logged in", MessageType.LOGIN));
                        } else {
                            myDataSocket.sendMessage(new Message(message.getUsername(), "Login failed. Incorrect password.", MessageType.LOGINERR));
                        }
                        break;
                    case SEND:
                        messages.add(message);
                        myDataSocket.sendMessage(message);
                        break;
                    case GET:
                        System.out.println("getting all messages");
                        try {
                            myDataSocket.sendAllMessages(getAllMessages());
                        } catch (IOException e) {
                            System.out.println("error sending messages");
                            e.printStackTrace();
                        }
                        break;
                    case LOGOUT:
                        //Session over; close the data socket.
                        System.out.println("Session over.");
                        try {
                            myDataSocket.close();
                        } catch (IOException e) {
                            System.out.println("error closing socket");
                            e.printStackTrace();
                        }
                        done = true;
                }
            }
        }// end try
        catch (Exception ex) {
            System.out.println("Exception caught in thread: " + ex);
        } // end catch
    }

    private boolean login(String username, String password) {
        userStore = new UserStore(username, password);
        try {
            System.out.println("adding user");
            return userStore.addUser();
        } catch (IOException e) {
            System.out.println("login failed");
            e.printStackTrace();
        }
        return false;
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
