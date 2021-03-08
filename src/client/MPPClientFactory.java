package client;

import java.io.IOException;

public class MPPClientFactory {
    public static MPPClient getMPPClient(String hostName, String portNumber) throws IOException {
        return new MPPClient(hostName, portNumber);
    }
}
