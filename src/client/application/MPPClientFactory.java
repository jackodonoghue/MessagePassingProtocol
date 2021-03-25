package client.application;

import java.io.IOException;

public class MPPClientFactory {
    public static MPPClient getMPPClient() throws IOException {
        return new MPPClient();
    }

    private MPPClientFactory(){
        throw new IllegalStateException("This is a utility class");
    }
}
