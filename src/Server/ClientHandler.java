package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Server.ChatroomModel.Chat;
import Server.ChatroomModel.ChatMessage;
import Server.ClientToServerProtocol.CToSMessages;

/**
 * Class which handles a single client connected to the server and all its requests
 */
public class ClientHandler implements Runnable {
    /** The username of the client */
    private String username;
    /** The buffered reader to read from the client */
    private final BufferedReader inFromClient;
    /** The print writer to write to the client */
    private final PrintWriter outToClient;
    /** The socket number of the connection */
    private final Socket socket;
    /** The global list of online clients */
    private final Map<String, ClientHandler> globalOnlineClients;
    /** The global list of ongoing chats */
    private final Map<Integer, Chat> globalOngoingChats;
    /** The list of chats the client is active in */
    private final Map<Integer, Chat> activeChats;
    
    /** Testing mode flag */
    private boolean testing;
    /** History of messages sent for testing */
    private List<String> messagePassingHistory;
    
    
    /**
     * Constructor to create a new instance of a client handler
     * @param clientSocket - The socket of the client
     * @param activeUsers - A Map of the active users to their threads
     * @param activeChats - A Map of the active chats to their instances
     * @param testing - A boolean to denote whether in testing mode
     * @throws IOException - Cannot connect to the socket
     */
    public ClientHandler(Socket clientSocket, Map<String, ClientHandler> globalOnlineClients, Map<Integer, Chat> globalOngoingChats) throws IOException{
        this.socket = clientSocket;
        this.inFromClient = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.outToClient = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        this.activeChats = new HashMap<Integer, Chat>();
        this.globalOnlineClients = globalOnlineClients;
        this.globalOngoingChats = globalOngoingChats;
        this.username = "";
        
        this.testing = false;
        this.messagePassingHistory = new ArrayList<String>();
    }
    
    /**
     * Constructor used for testing puroses only
     * @param string
     * @param globalOnlineClients
     * @param globalOngoingChats
     * @param testing
     */
    public ClientHandler(String string, Map<String, ClientHandler> globalOnlineClients, 
            Map<Integer, Chat> globalOngoingChats, boolean testing) {
        this.username = string;
        this.globalOngoingChats = globalOngoingChats;
        this.globalOnlineClients = globalOnlineClients;
        this.testing = testing;
        this.socket = null;
        this.inFromClient = null;
        this.outToClient = null;
        this.messagePassingHistory = new ArrayList<String>();
        this.activeChats = new HashMap<Integer, Chat>();
    }

    /**
     * Method to handle the request from the client
     * @param request
     * @return
     */
    public boolean handleClientRequest(String request) {
        CToSMessages protocol = ClientToServerProtocol.messageToProtocol(request);
        
        switch(protocol) {
        case LOGOUT:
            synchronized (globalOnlineClients) {
                for (Integer chatID : activeChats.keySet()) {
                    Chat chat = activeChats.get(chatID);
                    chat.leaveClient(this);
                }
                
                globalOnlineClients.remove(username);
                // Inform all the other clients of this clients exit
                for (String usern : globalOnlineClients.keySet()) {
                    ClientHandler client = globalOnlineClients.get(usern);
                    client.transmitMessageToClientThread(ServerToClientProtocol.LOGOUT(username));
                }
                return true;
            }
        case NEW_CHAT:
            synchronized (globalOnlineClients) {
                synchronized (globalOngoingChats) {
                    String args[] = request.split(" ");
                    String hostName = args[1];
                    ClientHandler host = globalOnlineClients.get(hostName);
                    String otherName = args[2];
                    ClientHandler otherClient = globalOnlineClients.get(otherName);
                    for (Integer cID : activeChats.keySet()) {
                        Chat c = activeChats.get(cID);
                        if (c.numberOfUsers()==2) {
                            if (c.checkForClient(host) && c.checkForClient(otherClient)) {
                                this.transmitMessageToClient(ServerToClientProtocol.CHAT_CREATION_FAILURE("Chat already exists"));
                                return false;
                            }
                        }
                    }
                    Chat chat = new Chat();
                    globalOngoingChats.put(chat.getChatID(), chat);
                    chat.create(host, otherClient);
                    this.addChat(chat.getChatID(), chat);
                    
                    // Adding the chat to second user
                    otherClient.addChat(chat.getChatID(), chat);
                    return false;
                }
            }
        case ADD_CHAT:
            synchronized (globalOnlineClients) {
                synchronized (globalOngoingChats) {
                    String args[] = request.split(" ");
                    int chatID = Integer.parseInt(args[1]);
                    String clientName = args[2];
                    if (globalOnlineClients.containsKey(clientName) && globalOngoingChats.containsKey(chatID)) {
                        ClientHandler client = globalOnlineClients.get(clientName);
                        Chat chat = globalOngoingChats.get(chatID);
                        chat.addClient(this, client);
                        client.activeChats.put(chat.getChatID(), chat);
                        return false;
                    } else {
                        if (!globalOnlineClients.containsKey(clientName)) {
                            transmitMessageToClient(ServerToClientProtocol.ADD_CHAT_USER_FAILURE("user not found"));
                        } else {
                            transmitMessageToClient(ServerToClientProtocol.ADD_CHAT_USER_FAILURE("chat not found"));
                        }
                        return false;
                    }
                }
            }
        case LEAVE_CHAT:
            synchronized (globalOngoingChats) {
                String args[] = request.split(" ");
                int chatID = Integer.parseInt(args[1]);
                if (globalOngoingChats.containsKey(chatID)) {
                    Chat chat = globalOngoingChats.get(chatID);
                    if (chat.leaveClient(this)) {
                        activeChats.remove(chat.getChatID());
                        transmitMessageToClientThread(ServerToClientProtocol.LEAVE_CHAT_SUCCESS(chat.getChatID()));
                        if (chat.numberOfUsers()==0) {
                            globalOngoingChats.remove(chat.getChatID());
                        }
                        return false;
                    } else {
                        transmitMessageToClientThread(ServerToClientProtocol.GENERAL_ERROR_MESSAGE("user not in the chat"));
                        return false;
                    }
                } else {
                    transmitMessageToClientThread(ServerToClientProtocol.GENERAL_ERROR_MESSAGE("chat does not exist"));
                    return false;
                }
            }
        case TYPING_STATUS:
            synchronized (globalOngoingChats) {
                String args[] = request.split(" ");
                int chatID = Integer.parseInt(args[1]);
                String username = args[2];
                String typing_status = args[3];
                if (!globalOngoingChats.containsKey(chatID)) {
                    transmitMessageToClient(ServerToClientProtocol.GENERAL_ERROR_MESSAGE("ERROR : Chat does not exist"));
                    return false;
                }
                if (!activeChats.containsKey(chatID)) {
                    transmitMessageToClient(ServerToClientProtocol.GENERAL_ERROR_MESSAGE("ERROR : User not in chat"));
                    return false;
                }
                ClientHandler client = globalOnlineClients.get(username);
                Chat chat = globalOngoingChats.get(chatID);
                chat.setTypingState(client, typing_status);
                return false;
            }
        case CHAT_MESSAGE:
            synchronized (globalOngoingChats) {
                String id = request.split(" ")[0].substring(11);
                int chatID = Integer.parseInt(id);
                String chatMessage = request.substring(request.indexOf(" ") + 1);
                if (globalOngoingChats.containsKey(chatID)) {
                    Chat chat = globalOngoingChats.get(chatID);
                    if (chat.checkForClient(this)) {
                        ChatMessage cm = new ChatMessage(username, chatMessage);
                        chat.addClientMessage(cm);
                        return false;
                    } else {
                        transmitMessageToClientThread(ServerToClientProtocol.SEND_CHATMESSAGE_FAIL("User not in chat"));
                        return false;
                    }
                } else {
                    transmitMessageToClientThread(ServerToClientProtocol.SEND_CHATMESSAGE_FAIL("Chat does not exist"));
                    return false;
                }
            }
        default:
            transmitMessageToClientThread(ServerToClientProtocol.GENERAL_ERROR_MESSAGE("invalidaction"));
            return false;
        }
    }
    
    /**
     * Method to send a single message to the Client
     * @param message - The message to be sent to the Client
     */
    public void transmitMessageToClient(final String message) {
        if (testing) {
            messagePassingHistory.add(message);
            return;
        }
        
        synchronized (outToClient) {
            outToClient.print(message);
            outToClient.flush();
        }
    }
    
    /**
     * Method to send more than one messages to the Client
     * @param messages - An array of the messages to send
     */
    public void transmitMessageToClient(final String[] messages) {
        if (testing) {
            for (String message : messages) {
                messagePassingHistory.add(message);
            }
            return;
        }
        
        synchronized (outToClient) {
            for (String message : messages) {
                outToClient.print(message);
                outToClient.flush();
            }
        }
    }
    
    /**
     * Method to spawn a thread to send multiple messages to the client
     * @param message - An array of messages to send to the client
     */
    public void transmitMessageToClientThread(final String[] messages) {
        if (testing) {
            for (String message : messages) {
                messagePassingHistory.add(message);
            }
            return;
        }
        
        new Thread(new Runnable() {
            public void run() {
                synchronized (outToClient) {
                    for (String message : messages) {
                        outToClient.print(message);
                        outToClient.flush();
                    }
                }
            }
        }).start();
    }
    
    /**
     * Method to spawn a thread to send a message to the client
     * @param message - the message to send to the client
     */
    public void transmitMessageToClientThread(final String message) {
        if (testing) {
            messagePassingHistory.add(message);
            return;
        }
        
        new Thread(new Runnable() {
            public void run() {
                synchronized (outToClient) {
                    outToClient.print(message);
                    outToClient.flush();
                }
            }
        }).start();
    }

    @Override
    public void run() {
        String clientMessage;
        try {
            this.updateInitialInformation();
            while ((clientMessage=inFromClient.readLine()) != null) {
                boolean logout = this.handleClientRequest(clientMessage);
                if (logout) {
                    break;
                }
            }
        } catch (IOException e) {
            this.handleClientRequest("terminateconnection");
            e.printStackTrace();
        } finally {
            if (globalOnlineClients.containsKey(username)) {
                this.handleClientRequest("terminateconnection");
            }
            synchronized (outToClient) {
                this.outToClient.close();
                try {
                    inFromClient.close();
                    socket.close();
                } catch (IOException e) {
                    System.err.println("Error in closing client connection");
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Method to get the initial information (username) from the user.
     * @throws IOException - Exception caused due to reading from client
     */
    private void updateInitialInformation() throws IOException {
        this.transmitMessageToClientThread(ServerToClientProtocol.LOGIN());
        String clientMessageIn;
        while (this.username.isEmpty()) {
            clientMessageIn = inFromClient.readLine();
            String username = clientMessageIn.replace("login ", "");
            if (ClientToServerProtocol.messageToProtocol(clientMessageIn)==CToSMessages.LOGIN) {
                if (!this.globalOnlineClients.containsKey(username)) {
                    this.username = username;
                    synchronized (this.globalOnlineClients) {
                        this.globalOnlineClients.put(username, this);
                    }
                    this.transmitMessageToClient(ServerToClientProtocol.LOGIN_SUCCESS());
                    // Sending the list of online clients to the new client.. And also sending a broadcast that a new client has joined
                    for (String clientName : this.globalOnlineClients.keySet()) {
                         if (clientName.equals(username)) continue;
                         ClientHandler client = this.globalOnlineClients.get(clientName);
                         client.transmitMessageToClientThread(ServerToClientProtocol.NEW_USER(username));
                         this.transmitMessageToClient(ServerToClientProtocol.NEW_USER(clientName));
                    }
                } else {
                    this.transmitMessageToClientThread(ServerToClientProtocol.LOGIN_FAIL("duplicate username"));
                }
            }
        }
    }

    /**
     * Method to get the username of the client
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Method to get the history of messages sent to the Client
     * @return
     */
    public List<String> getMessagePassingHistory() {
        return messagePassingHistory;
    }
    
    /**
     * Method to add a chat to the active chats list of the client
     * @param chatID - The chat ID of the chat
     * @param chat - The chat model of the chat
     */
    public void addChat(int chatID, Chat chat) {
        activeChats.put(chatID, chat);
    }
}
