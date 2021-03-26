package common;

import java.io.Serializable;
import java.util.List;

//serialised to allow Messages to be written to streams
public class Message implements Serializable {
    //Payload can be an object of any type
    private List<?> payload;
    private MessageType type;

    //message with a payload
    public Message(List<?> payload, MessageType type) {
        this.payload = payload;
        this.type = type;
    }

    //no payload
    public Message(MessageType messageType) {
        this.type = messageType;
    }

    public MessageType getType() {
        return type;
    }

    public List<?> getPayload() { return payload;}
}
