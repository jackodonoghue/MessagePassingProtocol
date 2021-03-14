package common;

public enum MessageType {
    LOGIN (100),
    SEND (101),
    GET (102),
    LOGOUT (103),
    LOGINERR (200),
    SENDERR (201),
    GETERR (202);

    MessageType(int i) {
    }
}
