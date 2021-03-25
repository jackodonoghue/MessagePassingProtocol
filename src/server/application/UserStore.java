package server.application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

public class UserStore {
    private final String username;
    private final String password;
    private File userDirectory;
    private File passwordFile;
    private final String BASE_PATH = ".\\..\\users\\";

    UserStore(String username, String password) {
        this.username = username.trim();
        this.password = password.trim();
        userDirectory = new File(BASE_PATH + this.username);
        passwordFile = new File(BASE_PATH + this.username + "\\password.txt");
    }

    public boolean addUser() throws IOException {
        if (!userDirectory.exists()) {
            Files.createDirectories(userDirectory.toPath());
            System.out.println(userDirectory.getAbsolutePath());
            addPassword();
            return true;
        } else {
            System.out.println("user exists");
            System.out.println(userDirectory.getAbsolutePath());
            System.out.println(checkPassword());
            return checkPassword();
        }
    }

    private boolean checkPassword() {
        try (Scanner scanner = new Scanner(passwordFile)) {
            if (scanner.hasNextLine()) {
                String password = scanner.nextLine();
                System.out.println("password is " + password);
                System.out.println(this.password);
                scanner.close();
                return this.password.equals(password.trim());
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return true;
    }

    private void addPassword() throws IOException {
        Files.createFile(passwordFile.toPath());
        FileWriter myWriter = null;
        try {
            myWriter = new FileWriter(BASE_PATH + username + "\\password.txt");
            myWriter.write(password.trim());
            myWriter.close();
            System.out.println("Added password." + password);
        } catch (IOException e) {
            System.out.println("Error adding password");
            e.printStackTrace();
        }
        finally {
            assert myWriter != null;
            myWriter.close();
        }
    }
}
