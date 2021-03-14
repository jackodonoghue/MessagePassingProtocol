package common;

import java.io.Serializable;

public class Message implements Serializable {
    private String username;
    private String password;
    private String message;
    private MessageType type;

    public Message(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public Message(String username, String message, MessageType type) {
        this.username = username;
        this.message = message;
        this.type = type;
    }

    public Message(String username, String password, String message, MessageType type) {
        this.username = username;
        this.message = message;
        this.type = type;
        this.password = password;
    }

    public Message(MessageType messageType) {
        this.type = messageType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
