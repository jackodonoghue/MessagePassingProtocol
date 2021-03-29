package common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Crunchify.com
 * @author J O'Donoghue
 * <p>
 *
 * This class is used to  retrieve properties from a file not added in git
 * Modified to use parameters for Message passing protocol
 */

public class MPPGetProperties {
    private InputStream inputStream;

    private String keystore;
    private String password;
    private String keystoreName;
    private String location;

    public MPPGetProperties(String application) throws IOException {
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
            if(application.equals("server"))
                keystoreName = prop.getProperty("server");
            else
                keystoreName = prop.getProperty("client");
            location = prop.getProperty(application + "-location");
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            assert inputStream != null;
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
