package server.application;

import server.session.ServerStreamSocket;

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
 * @author J O'Donoghue
 * <p>
 * Modified for use with Message passing protocol
 */

public class MPPServer implements Runnable{
    private static ArrayList<MPPServerThread> clients = new ArrayList<>();
    private boolean done = false;

    public MPPServer() {
    }

    @Override
    public void run() {
        int serverPort = 7;    // default port
        MPPGetProperties properties;

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
            System.out.println("Server started.");

            while (!done) {
                // wait to accept a connection
                System.out.println("Waiting for a connection.");
                ServerStreamSocket socket = null;
                try {
                    socket = new ServerStreamSocket
                            ((SSLSocket) connectionSocket.accept());
                } catch (IOException e) {
                    System.err.println("error creating connection");
                    e.printStackTrace();
                }
                System.out.println("connection accepted");
                //Create new client
                MPPServerThread client = new MPPServerThread(socket);
                // Start a thread to handle this client's session
                Thread theThread = new Thread(client);
                theThread.start();
                clients.add(client);
                // and go on to the next client
                System.out.println("not done");
            } //end while forever
            for (MPPServerThread client : clients) {
                client.socket.closeConnection();
            }
            System.out.println("done");
        } // end try
        catch (Exception ex) {
            ex.printStackTrace();
        } // end catch
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
