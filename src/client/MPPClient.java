package client;

import java.io.*;
import java.util.List;

/**
 * This module contains the presentaton logic of an Echo Client.
 *
 * @author M. L. Liu
 */
public class MPPClient {
    static final String endMessage = ".";
    static final String allMessagesCharacter = "*";

    public static void main(String[] args) {
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);
        try {
            //get host name
            System.out.println("Welcome to the Echo client.\n" +
                    "What is the name of the server host?");
            String hostName = br.readLine();
            if (hostName.length() == 0) // if user did not enter a name
                hostName = "localhost";  //   use the default host name

            //get port number
            System.out.println("What is the port number of the server host?");
            String portNum = br.readLine();
            if (portNum.length() == 0)
                portNum = "7";          // default port number

            //connect to host with port number
            MPPClientHelper helper = new MPPClientHelper(hostName, portNum);

            boolean done = false;
            String message, echo;
            //maintain connection to server
            while (!done) {
                //send a message
                System.out.println("Enter a line to receive an echo "
                        + "from the server, or a single period to quit.");
                message = br.readLine();
                //check for end connection

                //end connection if requested
                if ((message.trim()).equals(endMessage)) {
                    done = true;
                    helper.terminateConnection();
                }
                else if (message.trim().equals(allMessagesCharacter)){
                    List<String> messages = helper.getAllMessages();
                    System.out.println(messages);
                }
                //otherwise 'get message'
                else {
                    echo = helper.getMessage(message);
                    System.out.println(echo);
                }
            } // end while
        } // end try
        catch (Exception ex) {
            ex.printStackTrace();
        } //end catch
    } //end main
} // end class
