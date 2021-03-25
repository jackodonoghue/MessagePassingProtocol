package server.application;

import common.Message;
import common.MessageType;
import server.session.ServerStreamSocket;

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
    ServerStreamSocket socket;
    private List<Message> messages;
    private List<MPPServerThread> clients;
    private List<Message> allMessages;
    private UserStore userStore;

    MPPServerThread(ServerStreamSocket socket) {
        this.socket = socket;
    }

    public void run() {
        boolean done = false;
        Message message;

            messages = new ArrayList<>();
            while (!done) {
                message = socket.receiveMessage();
                System.out.println("message received: " + message.getType());
                switch (message.getType()) {
                    case LOGIN:
                        String username = (String)message.getPayload().get(0);
                        String password = (String)message.getPayload().get(1);
                        List<String> payload = new ArrayList();
                        payload.add(username);
                        if(login(username, password)){
                            payload.add("Logged in");
                            socket.sendMessage(new Message(payload, MessageType.LOGINOK));
                        } else {
                            payload.add("Login failed. Incorrect password.");
                            socket.sendMessage(new Message(payload, MessageType.LOGINERR));
                            socket.closeConnection();
                        }
                        break;
                    case SEND:
                        messages.add(message);
                        if(!socket.sendMessage(new Message(MessageType.SENDOK)))
                            socket.sendMessage(new Message(MessageType.SENDERR));
                        break;
                    case GET:
                        if(!socket.sendMessage(new Message(getAllMessages(),MessageType.GETOK)))
                            socket.sendMessage(new Message(MessageType.GETERR));
                        break;
                    case LOGOUT:
                        System.out.println("Session over.");
                        if(socket.closeConnection()){
                            System.out.println("Connection closed");
                        }
                        else {
                            System.out.println("Error closing connection.");
                        }
                        done = true;
                        break;
                    case CONNERR:
                        System.out.println("Connection error. Shutting down");
                        socket.closeConnection();
                }
            }

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
            this.allMessages.addAll(client.messages);
        }

        System.out.println(allMessages);

        return this.allMessages;
    }

    public void setClients(ArrayList<MPPServerThread> clients) {
        this.clients = clients;
    }
}
