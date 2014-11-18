package Server;

/**
 * Class to describe the Server to Client protocol
 */
public class ServerToClientProtocol {
    /**
    private static final String USERNAME = "[a-zA-Z0-9_\\.]+";
    private static final String SPACE = " ";
    private static final String CHAT_MESSAGE = "[^\n]";
    private static final String CHATID = "[0-9]+";
    private static final String END = "$";
    private static final String ASK_FOR_USERNAME = "connection_accepted";
    
    
    Server To Client :
    
        SERVERMESSAGE :== ( RELAYMESSAGE | NEWUSER | USERTERM | USEREXITED | CONNECTIONACCEPTED) NEWLINE
        
        CONNECTIONACCEPTED :== "connectionaccepted"
        LOGINSUCCESS :== "loginsuccess"
        LOGINFAILURE :== "loginfailed" SPACE MESSAGE
        NEWUSER :== "newuser" SPACE USERNAME
        
        LOGOUTSUCCESS :== "logoutsuccess"
        
        CHATCREATIONSUCCESS :== "chatcreated" SPACE CHATID SPACE USERNAME SPACE USERNAME
        CHATCREATIONFAILED :== "chatfailed" SPACE MESSAGE
        
        ADDCHATUSERSUCCESS :== "addchatsuccess"
        ADDCHATUSERFAILURE :== "addchatfailure" SPACE MESSAGE
        NEWCHATUSER :== "newchatuser" SPACE CHATID SPACE USERNAME SPACE TYPING
        
        ADDEDTOCHAT :== "addedtochat" SPACE CHATID SPACE USERNAME
        
        LEAVECHATSUCCESS :== "chatleftsuccess" SPACE CHATID
        LEAVECHATFAILURE :== "chatleftfailure" SPACE MESSAGE
        CHATUSEREXITED :== "chatuserexited" SPACE CHATID SPACE USERNAME
        
        TYPING_STATUS :== "typingstatus" SPACE CHATID SPACE USERNAME SPACE TYPING
        TYPING :== ((no_text)|(is_typing)|(has_typed))
        
        CHATMESSAGEFAIL :== "chatmessagefail" SPACE MESSAGE
        
        RELAYMESSAGE :== "relaymessage" SPACE CHATID SPACE USERNAME SPACE MESSAGE
        
        USERTERM :== "userterminated" SPACE USERNAME
        
        GENERALERRORMESSAGE :== MESSAGE
        
        USERNAME :== [a-z0-9]+
        CHATID = [0-9]+
        MESSAGE :== .+
        NEWLINE :== “\n”
    */
    
    public static String LOGIN() {
        return "connectionaccepted\n";
    }
    
    public static String LOGIN_SUCCESS() {
        return "loginsuccess\n";
    }
    
    /**
     * Sends a login fail message with the reason for failure
     * reason = "duplicate username" if username already exists
     * @param message
     * @return
     */
    public static String LOGIN_FAIL(String message) {
        return "loginfailed "+message+"\n";
    }
    
    public static String NEW_USER(String username) {
        return "newuser "+username+"\n";
    }
    
    public static String LOGOUT(String username) {
        return "userterminated "+username+"\n";
    }
    
    public static String LOGOUTSUCCESS() {
        return "logoutsuccess"+"\n";
    }
    
    public static String CHAT_CREATION_SUCCESS(int chatID, String hostUser, String otherUser) {
        return "chatcreated "+chatID+" "+hostUser+" "+otherUser+"\n";
    }
    
    public static String CHAT_CREATION_FAILURE(String message) {
        return "chatfailed "+message+"\n";
    }
    
    public static String ADD_CHAT_USER_SUCCESS() {
        return "addchatsuccess"+"\n";
    }
    
    public static String ADD_CHAT_USER_FAILURE(String errorMessage) {
        return "addchatfailure "+errorMessage+"\n";
    }
    
    public static String NEW_CHAT_USER(int chatID, String username, String typing) {
        return "newchatuser "+chatID+" "+username+" "+typing+"\n";
    }
    
    public static String CHAT_USER_EXITED(int chatID, String username) {
        return "chatuserexited "+chatID+" "+username+"\n";
    }
    
    public static String LEAVE_CHAT_SUCCESS(int chatID) {
        return "chatleftsuccess "+chatID+"\n";
    }
    
    public static String LEAVE_CHAT_FAILURE(String message) {
        return "chatleftfailure "+message+"\n";
    }
    
    public static String GENERAL_ERROR_MESSAGE(String message) {
        return message+"\n";
    }
    
    public static String IS_TYPING(int chatID, String username) {
        return "typingstatus "+chatID+" "+username+" "+"is_typing"+"\n";        
    }
    
    public static String HAS_TYPED(int chatID, String username) {
        return "typingstatus "+chatID+" "+username+" "+"has_typed"+"\n";        
    }
    
    public static String NO_TEXT(int chatID, String username) {
        return "typingstatus "+chatID+" "+username+" "+"no_text"+"\n";        
    }
    
    public static String SEND_CHATMESSAGE_FAIL(String message) {
        return "chatmessagefail "+message+"\n";
    }
    
    public static String RELAY_MESSAGE(int chatID, String username, String message) {
        return "relaymessage "+chatID+" "+username+" "+message+"\n";
    }
    
    public static String ADDED_TO_CHAT(int chatID, String username) {
        return "addedtochat "+chatID+" "+username+"\n";
    }
}
