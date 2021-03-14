package client;

import java.io.IOException;

public class MPPClientFactory {
    public static MPPClient getMPPClient(String username, String password) throws IOException {
        return new MPPClient(username, password);
    }
}
