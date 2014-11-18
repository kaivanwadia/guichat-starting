package Server;

/**
 * Class to describe the Client to Server Protocol
 *
 */
public class ClientToServerProtocol {
    private static final String USERNAME = "[a-zA-Z0-9_\\.]+";
    private static final String SPACE = " ";
    private static final String CHAT_MESSAGE = ".+";
    private static final String CHATID = "[0-9]+";
    private static final String END = "$";
    private static final String TYPING = "((no_text)|(is_typing)|(has_typed))";
    
    /**
    Client To Server:
        CLIENTMESSAGE :== ( SENDMESSAGE | NEWCHAT | ENDCHAT | TERMCON | LOGIN | TYPINGSTATUS) NEWLINE
        SENDMESSAGE :== "chatmessage" CHATID SPACE MESSAGE
        NEWCHAT :== "newchat" SPACE USERNAME SPACE USERNAME
        ADDTOCHAT :== "addtochat" SPACE CHATID SPACE USERNAME
        LEAVECHAT :== "leavechat" SPACE CHATID SPACE USERNAME
        TERMCON :== "terminateconnection"
        LOGIN :== "login" SPACE USERNAME
        TYPINGSTATUS :== "typingstatus" SPACE CHATID SPACE USERNAME SPACE TYPING
        NEWLINE :== "\n"
        SPACE :== " "
        MESSAGE :== .+
        TYPING :== ((no_text)|(is_typing)|(has_typed))
        CHATID = [0-9]+
        USERNAME :== [a-z0-9]+
    */
    
    public enum CToSMessages {
        LOGIN,
        LOGOUT,
        NEW_CHAT,
        ADD_CHAT,
        LEAVE_CHAT,
        CHAT_MESSAGE,
        TYPING_STATUS,
        UNKNOWN
    }
    
    /**
     * Method to take in the message (from client to server) and return the appropriate protocol
     * @param message - The message received by the server
     * @return - The protocol of the message
     */
    public static CToSMessages messageToProtocol (String message) {
        CToSMessages protocol;
        if (message.matches("^login"+SPACE+USERNAME+END)) {
            protocol = CToSMessages.LOGIN;
        } else if (message.matches("^terminateconnection")) {
            protocol = CToSMessages.LOGOUT;
        } else if (message.matches("^newchat"+SPACE+USERNAME+SPACE+USERNAME+END)) {
            protocol = CToSMessages.NEW_CHAT;
        } else if (message.matches("^addtochat"+SPACE+CHATID+SPACE+USERNAME+END)) {
            protocol = CToSMessages.ADD_CHAT;
        } else if (message.matches("^leavechat"+SPACE+CHATID+SPACE+USERNAME+END)) {
            protocol = CToSMessages.LEAVE_CHAT;
        } else if (message.matches("^chatmessage"+CHATID+SPACE+CHAT_MESSAGE+END)) {
            protocol = CToSMessages.CHAT_MESSAGE;
        } else if (message.matches("^typingstatus"+SPACE+CHATID+SPACE+USERNAME+SPACE+TYPING)) {
            protocol = CToSMessages.TYPING_STATUS;
        } else {
            protocol = CToSMessages.UNKNOWN;
        }
        return protocol;
    }
}