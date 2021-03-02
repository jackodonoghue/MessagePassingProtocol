package server;

import common.MPPGetProperties;
import common.MyStreamSocket;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.ArrayList;

/**
 * This module contains the application logic of an echo server
 * which uses a stream-mode socket for interprocess communication.
 * Unlike EchoServer2, this server services clients concurrently.
 * A command-line argument is required to specify the server port.
 *
 * @author M. L. Liu
 * @author J O'Donoghue
 *
 * Modified for use with Message passing protocol
 *
 */

public class MPPServer {
    private static ArrayList<MPPServerThread> clients = new ArrayList<>();

    public static void main(String[] args) {
        int serverPort = 7;    // default port
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
            // instantiates a stream socket for accepting connections
            SSLServerSocketFactory serverSocketFactory = context.getServerSocketFactory();
            SSLServerSocket connectionSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(serverPort);
            System.out.println("Echo server ready.");

            while (true) {
                // wait to accept a connection
                System.out.println("Waiting for a connection.");
                MyStreamSocket myDataSocket = new MyStreamSocket
                        ((SSLSocket)connectionSocket.accept());
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
    }

    private static void informAllClientsOfNewClients() {
        for (MPPServerThread client : clients) {
            client.setClients(clients);
        }
    }
}
