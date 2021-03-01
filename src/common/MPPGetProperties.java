package common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

/**
 * @author Crunchify.com
 */

public class MPPGetProperties {
    private String result = "";
    private InputStream inputStream;

    private String keystore;
    private String password;
    private String keystoreName;
    private String location;

    public MPPGetProperties() throws IOException {
        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            System.out.println("CLass: " + getClass().getClassLoader().toString());

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                System.out.println("trying");
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
            // get the property value and print it out
            keystore = prop.getProperty("keystore");
            password = prop.getProperty("password");
            keystoreName = prop.getProperty("name");
            location = prop.getProperty("server-location");
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
    }

    public String getKeystore() {
        return keystore;
    }

    public String getPassword() {
        return password;
    }

    public String getKeystoreName() {
        return keystoreName;
    }

    public String getLocation() {
        return location;
    }
}
