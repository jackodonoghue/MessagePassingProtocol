package server;

import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This module contains the application logic of an echo server
 * which uses a stream-mode socket for interprocess communication.
 * Unlike EchoServer2, this server services clients concurrently.
 * A command-line argument is required to specify the server port.
 *
 * @author M. L. Liu
 */

public class MPPServer {
    private static ArrayList<MPPServerThread> clients = new ArrayList<>();
    private static List<String> allMessages;

    public static void main(String[] args) {
        int serverPort = 7;    // default port

        if (args.length == 1)
            serverPort = Integer.parseInt(args[0]);
        try {
            // instantiates a stream socket for accepting
            //   connections
            ServerSocket myConnectionSocket = new ServerSocket(serverPort);
            System.out.println("Echo server ready.");

            while (true) {  // forever loop
                // wait to accept a connection
                System.out.println("Waiting for a connection.");
                MyStreamSocket myDataSocket = new MyStreamSocket
                        (myConnectionSocket.accept());
                System.out.println("connection accepted");
                //Create new client
                MPPServerThread client = new MPPServerThread(myDataSocket);
                // Start a thread to handle this client's session
                Thread theThread = new Thread(client);
                theThread.start();
                clients.add(client);
                informAllClientsOfNewClients();
                // and go on to the next client
            } //end while forever
        } // end try
        catch (Exception ex) {
            ex.printStackTrace();
        } // end catch
    } //end main

    private static void informAllClientsOfNewClients() {
        for (MPPServerThread client : clients) {
            client.setClients(clients);
        }
    }

    public static List<String> sendAllMessages() {
        allMessages = new ArrayList<>();
        for (MPPServerThread client : clients) {
            client.getAllMessages().forEach(message -> allMessages.add(message));
        }

        return allMessages;
    }
} // end class
