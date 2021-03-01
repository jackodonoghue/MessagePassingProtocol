package server;

import common.MPPGetProperties;
import common.MyStreamSocket;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;

/**
 * This module contains the application logic of an echo server
 * which uses a stream-mode socket for interprocess communication.
 * Unlike EchoServer2, this server services clients concurrently.
 * A command-line argument is required to specify the server port.
 *
 * @author M. L. Liu
 * @author J. O'Donoghue
 * <p>
 * Modified to act as a concurrent server for that uses SSL to encrypt
 * communication with clients. This class is responsible for allocating
 * sockets to new client connections.
 */

public class MPPServer {
    private static ArrayList<MPPServerThread> clients = new ArrayList<>();

    public static void main(String[] args) {
        int serverPort = 7;
        MPPGetProperties properties;

        if (args.length == 1)
            serverPort = Integer.parseInt(args[0]);
        try {
            properties = new MPPGetProperties();
            String keystoreName = properties.getLocation() + properties.getKeystoreName();
            char[] keystorePassword = properties.getPassword().trim().toCharArray();
            char[] certificatePassword = properties.getPassword().trim().toCharArray();

            KeyStore keyStore = KeyStore.getInstance(properties.getKeystore());
            keyStore.load(new FileInputStream(keystoreName), keystorePassword);

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, certificatePassword);

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(keyManagerFactory.getKeyManagers(), null, null);

            SSLServerSocketFactory serverSocketFactory = context.getServerSocketFactory();
            SSLServerSocket connectionSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(serverPort);

            while (true) {
                System.out.println("Waiting for a connection.");
                MyStreamSocket myDataSocket = new MyStreamSocket((SSLSocket) connectionSocket.accept());
                System.out.println("connection accepted");
                MPPServerThread client = new MPPServerThread(myDataSocket);
                // Thread to handle this client's session
                Thread theThread = new Thread(client);
                theThread.start();
                clients.add(client);
                informAllClientsOfNewClients();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void informAllClientsOfNewClients() {
        for (MPPServerThread client : clients) {
            client.setClients(clients);
        }
    }
}
