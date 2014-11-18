package Server.ChatroomModel;

/**
 * Class to describe a single message in a chat
 */
public class ChatMessage {
    /** The username of the client who sent this message */
    private String username;
    /** The message */
    private String message;
    
    public ChatMessage(String username, String message) {
        this.username = username;
        this.message = message;
    }

    /**
     * Get the username of the client who sent this message
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the messages' text
     * @return
     */
    public String getMessage() {
        return message;
    }
}
