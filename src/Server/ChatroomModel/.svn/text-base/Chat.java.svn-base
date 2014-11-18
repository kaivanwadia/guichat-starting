package Server.ChatroomModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Server.ClientHandler;
import Server.ServerToClientProtocol;

/**
 * This class represents a chat between two or more clients.
 * Each Chat has a specific chatID. The Chat also has a list of all
 * currently active users and a history of all the chat messages.
 * Every time a user logs in or logs off the the remaining users will 
 * be notified of that change.
 */
public class Chat implements ChatRoom {
    /** A hidden counter to give unique ID's to the various Chats */
    private static int chatCount = 0;
    /** A unique identifier for this chat */
    private final int ID;
    /** A List of all the chat messages which have been sent so far */
    private final List<ChatMessage> chatMessages;
    /** A HashMap of the client to its current typing status */
    private final Map<ClientHandler, String> clients;
    
    /**
     * Creates a new chat with the specified user who created it.
     * @param username
     */
    public Chat() {
        this.ID = chatCount++;
        this.chatMessages = new ArrayList<ChatMessage>();
        this.clients = new HashMap<ClientHandler, String>();
    }
    
    @Override
    public synchronized boolean create(ClientHandler host, ClientHandler other) {
        if (clients.containsKey(host)) {
            host.transmitMessageToClientThread(ServerToClientProtocol.CHAT_CREATION_FAILURE("user already in chat"));
            return false;
        } else if (clients.containsKey(other)) {
            other.transmitMessageToClientThread(ServerToClientProtocol.CHAT_CREATION_FAILURE("user already in chat"));
            return false;
        } else {
            clients.put(host, "no_text");
            clients.put(other, "no_text");
            String chatCreationMessage = ServerToClientProtocol.CHAT_CREATION_SUCCESS(ID, host.getUsername(), other.getUsername());
            host.transmitMessageToClient(chatCreationMessage);
            other.transmitMessageToClient(chatCreationMessage);
            return true;
        }
    }

    @Override
    public synchronized boolean addClient(ClientHandler host, ClientHandler client) {
        if (clients.containsKey(client)) {
            host.transmitMessageToClientThread(ServerToClientProtocol.ADD_CHAT_USER_FAILURE("user already in chat"));
            return false;
        } else {
            clients.put(client, "no_text");
            host.transmitMessageToClient(ServerToClientProtocol.ADD_CHAT_USER_SUCCESS());
            client.transmitMessageToClient(ServerToClientProtocol.ADDED_TO_CHAT(ID, host.getUsername()));
            newUserBroadcast(client);
            
            for (ClientHandler c : clients.keySet()) {
                if (c.getUsername().equals(client.getUsername())) continue;
                String m = ServerToClientProtocol.NEW_CHAT_USER(ID, c.getUsername(), clients.get(c));
                client.transmitMessageToClient(m);
            }
            return true;
        }
    }

    @Override
    public synchronized boolean leaveClient(ClientHandler client) {
        if (!clients.containsKey(client)) {
            return false;
        } else {
            clients.remove(client);
            userLeftBroadcast(client);
            return true;
        }
    }

    @Override
    public synchronized void addClientMessage(ChatMessage message) {
        this.chatMessages.add(message);
        chatMessageBroadcast(message);
    }
    
    @Override
    public synchronized void setTypingState(ClientHandler client, String typingStatus) {
        if (clients.containsKey(client)) {
            if (typingStatus.equals("is_typing")) {
                messageBroadcast(ServerToClientProtocol.IS_TYPING(ID, client.getUsername()));
            } else if (typingStatus.endsWith("no_text")) {
                messageBroadcast(ServerToClientProtocol.NO_TEXT(ID, client.getUsername()));
            } else if (typingStatus.equals("has_typed")) {
                messageBroadcast(ServerToClientProtocol.HAS_TYPED(ID, client.getUsername()));
            } else {
                client.transmitMessageToClientThread(ServerToClientProtocol.GENERAL_ERROR_MESSAGE("Illegal typing status"));
                return;
            }
            clients.put(client, typingStatus);
        }
    }
    
    /**
     * Method to send a broadcast to all users in the chat that a new user has joined
     * @param client - The new user
     */
    private void newUserBroadcast(ClientHandler client) {
        String message = ServerToClientProtocol.NEW_CHAT_USER(ID, client.getUsername(), "no_text");
        for (ClientHandler c : clients.keySet()) {
            if (c.getUsername().equals(client.getUsername())) continue;
            c.transmitMessageToClientThread(message);
        }
    }
    
    /**
     * Method to send a broadcast to all users in the chat that a user has left the chat
     * @param client - The client who has left the chat
     */
    private void userLeftBroadcast(ClientHandler client) {
        String message = ServerToClientProtocol.CHAT_USER_EXITED(ID, client.getUsername());
        for (ClientHandler c : clients.keySet()) {
            if (c.getUsername().equals(client.getUsername())) continue;
            c.transmitMessageToClientThread(message);
        }
    }
    
    /**
     * Method to send a broadcast to all users in the chat for a new chat message
     * @param chatMessage - The new chat message
     */
    private void chatMessageBroadcast(ChatMessage chatMessage) {
        String message = ServerToClientProtocol.RELAY_MESSAGE(ID, chatMessage.getUsername(), chatMessage.getMessage());
        for (ClientHandler c : clients.keySet()) {
            c.transmitMessageToClientThread(message);
        }
    }
    
    /**
     * Method to send a specified message to all the users in the chat
     * @param message - The message to be sent
     */
    private synchronized void messageBroadcast(String message) {
        for (ClientHandler c : clients.keySet()) {
            c.transmitMessageToClientThread(message);
        }
    }
    
    /**
     * Method to check if the client is in the chat
     * @param client - The client to check for
     * @return
     */
    public boolean checkForClient(ClientHandler client) {
        return clients.containsKey(client);
    }
    
    /**
     * Method to return the number of clients in the chat
     * @return - The number of clients in the chat
     */
    public int numberOfUsers() {
        return clients.size();
    }
    
    /**
     * Method to get the ID of this chat
     * @return
     */
    public int getChatID() {
        return this.ID;
    }
}
