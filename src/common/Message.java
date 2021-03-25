package common;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {
    private List<?> payload;
    private MessageType type;

    public Message(List<?> payload, MessageType type) {
        this.payload = payload;
        this.type = type;
    }

    public Message(MessageType messageType) {
        this.type = messageType;
    }

    public MessageType getType() {
        return type;
    }

    public List<?> getPayload() { return payload;}
}
