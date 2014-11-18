package Server.ChatroomModel;

import Server.ClientHandler;

public interface ChatRoom {
    
    /**
     * Method to create a new chat with the two clients.
     * @param host - The client who started the chat
     * @param other - The client with whom the host wants to chat
     * @return
     */
    boolean create(ClientHandler host, ClientHandler other);
    /**
     * Adds a client to the conversation
     * @param host - The client who is adding another user to the chat
     * @param client - The user being added to the chat
     * @return
     */
    boolean addClient(ClientHandler host, ClientHandler client);
    
    /**
     * Removes a client from the conversation
     * @param client
     * @return
     */
    boolean leaveClient(ClientHandler client);
    
    /**
     * Adds a message to the conversation
     * @param message
     */
    void addClientMessage(ChatMessage message);
    
    /**
     * Method to set the typing status of a client
     * @param client
     * @param typingStatus
     */
    void setTypingState(ClientHandler client, String typingStatus);
}
