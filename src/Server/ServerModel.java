package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import Server.ChatroomModel.Chat;

/**
 * Class used to declare the server and handle the client connections
 */
public class ServerModel {
    /** The socket being used by the server */
    private ServerSocket serverSocket;
    /** The HashMap of online clients */
    public Map<String , ClientHandler> onlineClients;
    /** The HashMap of active chats */
    public Map<Integer, Chat> ongoingChats;
    
    /**
     * Make a ServerModel that listens for connections on port. <br>
     * @param port - port number, requires 0 <= port <= 65535. <br>
     */
    public ServerModel(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.onlineClients = new HashMap<String, ClientHandler>();
        this.ongoingChats = new HashMap<Integer, Chat>();
    }
    
    /**
     * Method to handle the client connections. <br>
     * Never returns unless an exception is thrown. <br>
     * @throws IOException - Main server socket is broken <br>
     */
    public void serve() throws IOException{
        while(true){
            Socket clientSocket = serverSocket.accept();
            System.out.println("Socket Connected");
            handleClient(clientSocket);
        }
    }
    
    /**
     * Method to start a thread to handle a single client <br>
     * @param clientSocket <br>
     * @throws IOException 
     */
    public void handleClient(Socket clientSocket) {
        ClientHandler clientRunnable;
        try {
            clientRunnable = new ClientHandler(clientSocket, onlineClients, ongoingChats);
            Thread clientThread = new Thread(clientRunnable);
            clientThread.start();
        } catch (IOException e) {
            System.err.println("Error creating a clientHandler");
            e.printStackTrace();
        }
    }
    
    public void newClientAccepted(){
        
    }

    public void inviteToChat(){
        
    }
    
    public void createChat(String user1, String user2){
        
    }
    
    public void serverToClientMessage(){
        
    }
    
    public void endClientConnection(String username){
        
    }
}
