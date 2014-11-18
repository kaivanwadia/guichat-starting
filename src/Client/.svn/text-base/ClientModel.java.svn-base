package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

public class ClientModel implements Runnable{
    Socket clientSocket;
    InetAddress serverIP;
    int serverPort;
    String username;
    InputStreamReader stream;
    BufferedReader fromServer;
    PrintWriter toServer;
    ArrayList<String> activeUsers = new ArrayList<String>();
    Map<Integer, ChatModel> activeChats = new HashMap<Integer, ChatModel>();
    ClientGUI gui;
    LoginModel loginModel;
    boolean userLoggedOut = false;
    
    public ClientModel(){
        loginModel = new LoginModel(this);
    }
    
    
    public void initializeGUI(){
        System.out.println("Initializing GUI");
        gui = new ClientGUI(username, this);
        for(String user : activeUsers){
            gui.addActiveUser(user);
        }
        
    }
    
    public void sendUsername(){
        System.out.println("Sending Username");
        toServer.println("login" + " " + username);
        toServer.flush();
    }
    
    public void requestChat(String otherUsername) {
        System.out.println("Requesting chat with " + otherUsername);
        toServer.println("newchat" + " " + username + " " + otherUsername);
        toServer.flush();
    }
    
    public void createNewChat(int chatID, String otherUsername){
        System.out.println("Creating chat with " + otherUsername + " that has chatID " + chatID);
        ArrayList<String> tempList = new ArrayList<String>();
        for (String user : activeUsers){
            if (user.equals(username) || user.equals(otherUsername)){
                continue;
            }
            tempList.add(user);
        }
        ChatModel chatModel = new ChatModel(toServer, chatID, username, otherUsername, tempList, this, false);
        activeChats.put(chatID, chatModel);
    }
    
    public ChatModel getChat(int chatID){
        if (!activeChats.containsKey(chatID)){
            System.out.println(chatID + " is not in active chats list");
            return null;
        }
        return activeChats.get(chatID);
    }
    
    public void addActiveUser(String username){
        System.out.println("Trying to add " + username + " to active users list");
        if (activeUsers.contains(username)){
            System.out.println(username + " is already in active users list");
            return;
        }
        activeUsers.add(username);
        System.out.println(username + "added to active users list");
        gui.addActiveUser(username);
        for (ChatModel chat : activeChats.values()){
            chat.gui.addUserToDropDown(username);
            chat.gui.userLoggedOnMessage(username);
        }
        
    }

    public void removeActiveUser(String username){
        System.out.println("Tryping to remove " + username + " from active users list");
        if (!activeUsers.contains(username)){
            System.out.println(username + " is not in active users list");
            return;
        }
        activeUsers.remove(username);
        System.out.println(username + "removed from active users list");
        gui.removeActiveUser(username);
        for (ChatModel chat : activeChats.values()){
            chat.otherUserLoggedOut(username);
        }
        
    }
    
    public void addToChat(int chatID, String username, String typing_status){
        ChatModel chat = getChat(chatID);
        chat.addUserToChat(username);
    }
    
    public void recieveChatMessage(int chatID, String username, String message){
        ChatModel chat = getChat(chatID);
        chat.addMessageToUserTable(username, message);
    }
    
    public void recieveTypingStatus(int chatID, String username, String status){
        ChatModel chat = getChat(chatID);
        chat.addTypingStatus(username, status);
    }
    
    public void removeFromChat(int chatID, String username){
        ChatModel chat = getChat(chatID);
        chat.removeUserFromChat(username);
    }
    
    public void logout(){
        System.out.println("Ending session");
        userLoggedOut = true;
        try {
            
            for (ChatModel chat : activeChats.values()){
                chat.leaveChat();
            }
            toServer.println("terminateconnection");
            toServer.flush();
            ClientGUI.closeGUI(gui);
            clientSocket.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String message;
        try {
            while((message = fromServer.readLine()) != null){
                System.out.println("Server:" + message);
                String[] serverMessage = message.split(" ");
                
                if (serverMessage.length == 1){
                    if (serverMessage[0].equals("connectionaccepted")){
                        sendUsername();
                        
                    } else if (serverMessage[0].equals("loginsuccess")){
                           initializeGUI();
                           loginModel.closeGUI();
                           
                    } else if (serverMessage[0].equals("addchatsuccess")){
                        continue;
                         
                    } else if (serverMessage[0].equals("invalidaction")){
                        System.out.println("Invalid Action!");
                    }
                
                    
                } else if (serverMessage.length == 2){
                    if (serverMessage[0].equals("loginfailed")){
                        String errorMessage = serverMessage[1];
                        loginModel.setErrorMessage(errorMessage);
                        
                    } else if (serverMessage[0].equals("newuser")){
                        addActiveUser(serverMessage[1]);
                        
                    } else if (serverMessage[0].equals("chatfailed")){
                        String errorMessage = serverMessage[1];
                        JOptionPane.showMessageDialog(gui, errorMessage);
                        
                    } else if (serverMessage[0].equals("userterminated")){
                        removeActiveUser(serverMessage[1]);
                        
                    } else if (serverMessage[0].equals("addchatfailure")){
                        String errorMessage = serverMessage[1];
                        JOptionPane.showMessageDialog(gui, errorMessage);
                        
                    } else if (serverMessage[0].equals("chatleftfailure")){
                        String errorMessage = serverMessage[1];
                        JOptionPane.showMessageDialog(gui, errorMessage);
                        
                    } else if (serverMessage[0].equals("chatmessagefail")){
                        String errorMessage = serverMessage[1];
                        JOptionPane.showMessageDialog(gui, errorMessage);
                    }  else if (serverMessage[0].equals("chatleftsuccesss")){
                        int chatID = Integer.parseInt(serverMessage[1]);
                        activeChats.remove(chatID);
                        System.out.println("Removed chatID " + chatID + " from active chats list");
                    }
                    
                    
                } else if (serverMessage.length == 3){
                    if (serverMessage[0].equals("chatuserexited")){
                        int chatID = Integer.parseInt(serverMessage[1]);
                        removeFromChat(chatID, serverMessage[2]);
                        
                    } else if (serverMessage[0].equals("addedtochat")){
                        int chatID = Integer.parseInt(serverMessage[1]);
                        addedToChat(chatID, serverMessage[2]);
                    }
                    
                } else if (serverMessage.length >= 4){
                    if (serverMessage[0].equals("chatcreated")){
                        int chatID = Integer.parseInt(serverMessage[1]);
                        String otherUsername = serverMessage[2].equals(username) ? serverMessage[3] : serverMessage[2];
                        createNewChat(chatID, otherUsername);
                        
                    } else if (serverMessage[0].equals("newchatuser")){
                        int chatID = Integer.parseInt(serverMessage[1]);
                        addToChat(chatID, serverMessage[2], serverMessage[3]);
                        
                    } else if (serverMessage[0].equals("relaymessage")){
                        String[] tempMessage = message.split(" ", 4);
                        int chatID = Integer.parseInt(tempMessage[1]);
                        recieveChatMessage(chatID, tempMessage[2], tempMessage[3]);
                    }
                    
                    else if (serverMessage[0].equals("typingstatus")){
                        int chatID = Integer.parseInt(serverMessage[1]);
                        recieveTypingStatus(chatID, serverMessage[2], serverMessage[3]);
                    }
                    
                } else{
                    toServer.println("Invalid message from server");
                    toServer.flush();
                }
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            
            if (!userLoggedOut){
                //Close all chat windows
                for (ChatModel chat : activeChats.values()){
                    chat.gui.dispose();
                }
                //Close client window
                JOptionPane.showMessageDialog(gui, "Server is down for maintenance");
                gui.dispose();
            }
        }
        
    }

    private void addedToChat(int chatID, String otherUsername) {
        System.out.println("Added to chatID " + chatID + " by " + username);
        ArrayList<String> tempList = new ArrayList<String>();
        for (String user : activeUsers){
            if (user.equals(username) || user.equals(otherUsername)){
                continue;
            }
            tempList.add(user);
        }
        ChatModel chatModel = new ChatModel(toServer, chatID, username, otherUsername, tempList, this, true);
        activeChats.put(chatID, chatModel);
        
    }
    
    public void setUpNetworking(String username, Socket clientSocket) {
        try {
            this.username = username;
            this.clientSocket = clientSocket;
            this.serverPort = this.clientSocket.getPort();
            this.serverIP = this.clientSocket.getInetAddress();
            stream = new InputStreamReader(this.clientSocket.getInputStream());
            fromServer = new BufferedReader(stream);
            toServer = new PrintWriter(this.clientSocket.getOutputStream());
            System.out.println("Networking Established for " + username);
            Thread clientThread = new Thread(this);
            clientThread.start();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    
}
