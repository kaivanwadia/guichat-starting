package Client;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.DefaultTableModel;

/**
 * 
 * Class representing the chat model in the MVC system.
 * Holds a list of users and their typing statuses. 
 *
 */
public class ChatModel{
    String username;
    HashMap<String, String> conversationHistory;
    int chatID;
    PrintWriter toServer;
    Socket chatSocket;
    ChatGUI gui;
    ClientModel clientModel;
    ArrayList<String> availableUsers;
    HashMap<String, String> userStatuses;
    
    /**
     * Creates a new chatModel that relays and receives messages from the server and modifies the chatGUI accordingly.
     * @param toServer, PrintWriter that sends messages to the server.
     * @param chatID, the unique ID of a specific chat window. 
     * @param username
     * @param otherUsername
     * @param availableUsers
     * @param clientModel
     * @param addedToChat
     */
    public ChatModel(PrintWriter toServer, int chatID, String username, String otherUsername, ArrayList<String> availableUsers, ClientModel clientModel, boolean addedToChat){
        this.username = username;
        this.toServer = toServer;
        this.chatID = chatID;
        this.clientModel = clientModel;
        this.availableUsers = availableUsers;
        this.userStatuses = new HashMap<String, String>();
        String guiTitle = addedToChat ? "" : " " + otherUsername + " ";
        gui = new ChatGUI(guiTitle, this);
        for (String user : this.availableUsers){
            gui.addUserToDropDown(user);
        }
        addTypingStatus(username, "no_text");
        addTypingStatus(otherUsername, "no_text");
        
    }
    
    /**
     * Adds a user's typing status to the chat window.
     * @param user
     * @param status
     */
    public void addTypingStatus(String user, String status){
        if (status.equals("is_typing")) {
            userStatuses.put(user, "Typing");
        } else if (status.equals("has_typed")) {
            userStatuses.put(user, "Has typed");
        } else if (status.equals("no_text")) {
            userStatuses.put(user, "No Text");
        }
        refreshTypingStatuses(user);
    }
    
    /**
     * Refreshes the typing statuses each time a new typing status is received. 
     * @param refreshedUser
     */
    public void refreshTypingStatuses(String refreshedUser) {
        if (refreshedUser.equals(username)) return;
        
        DefaultTableModel otherUserStatus = (DefaultTableModel) gui.getOtherUserStatus().getModel();
        
        String tStatus = userStatuses.get(refreshedUser);
        
        String status = refreshedUser + ": " + tStatus;
        
        for (int row = 0; row < otherUserStatus.getRowCount(); row++){
            String userstatus = otherUserStatus.getValueAt(row, 0).toString();
            if (userstatus.contains(refreshedUser + ": ")){
                if (tStatus == null){
                    otherUserStatus.removeRow(row);
                }
                else{
                    otherUserStatus.setValueAt(status, row, 0);
                }
                return;
            }
        }
        otherUserStatus.addRow(new Object[] {status});
        
    }
    
    /**
     * Adds a message to the chat window. 
     * @param username
     * @param message
     */
    public void addMessageToUserTable(String username, String message){
        gui.addMessageToUserTable(username + ": " + message);
    }
    
    /**
     * Sends the status of the connected user to the server. 
     * @param status
     */
    public void sendStatusToServer(String status){
        System.out.println("typingstatus" + chatID + username + " " + status);
        toServer.println("typingstatus"+ " " + chatID + " " + username + " " + status);
        toServer.flush();
    }
    
    /**
     * Sends the message of the connected user to the server. 
     * @param message
     */
    public void clientToServerChatMessage(String message){
        String chatMessage = message;
        System.out.println("chatmessage" + chatID + " " + chatMessage);
        toServer.println("chatmessage" + chatID + " " + chatMessage);
        toServer.flush();
    }
    
    /**
     * Sends a request to add a user to the chat to the server. 
     * @param otherUsername
     */
    public void requestAddUserToChat(String otherUsername){
        System.out.println("addtochat" + " " + chatID + " " + otherUsername);
        toServer.println("addtochat" + " " + chatID + " " + otherUsername);
        toServer.flush();
    }
    
    /**
     * Adds a user to the chat. Removes the user from the "add user" dropDown menu and add's their typing status.
     * @param otherUsername
     */
    public void addUserToChat(String otherUsername){
        String guiTitle = gui.getTitle();
        
        guiTitle += " " + otherUsername + " ";
        gui.setTitle(guiTitle);
        gui.removeUserFromDropDown(otherUsername);
        addTypingStatus(otherUsername, "no_text");
        gui.addUserToChat(otherUsername);
    }

    /**
     * Removes a user from the chat, adds them to the "add user" dropDown menu, and removes their status from the chat 
     * window. 
     * @param otherUsername
     */
    public void removeUserFromChat(String otherUsername){
        String guiTitle = gui.getTitle();
        guiTitle = guiTitle.replace(" " + otherUsername + " ", "");
        gui.setTitle(guiTitle);
        availableUsers.add(username);
        gui.addUserToDropDown(otherUsername);
        userStatuses.remove(otherUsername);
        refreshTypingStatuses(otherUsername);
        gui.addUserExitedMessage(otherUsername);
    }
    
    /**
     * Removes a user that has logged out from the "add user" dropDown menu, removes their status, and appends 
     * a "logged out" message to the chat window. 
     * @param otherUsername
     */
    public void otherUserLoggedOut(String otherUsername){
        gui.removeUserFromDropDown(otherUsername);
        
        DefaultTableModel otherUserStatus = (DefaultTableModel) gui.getOtherUserStatus().getModel();
        String tStatus = userStatuses.get(otherUsername);
 
        for (int row = 0; row < otherUserStatus.getRowCount(); row++){
            String userstatus = otherUserStatus.getValueAt(row, 0).toString();
            if (userstatus.contains(otherUsername + ": ")){
                if (tStatus == null){
                    otherUserStatus.removeRow(row);
                }
                return;
            }
        }

        userStatuses.remove(otherUsername);
        gui.addUserLoggedOutMessage(otherUsername);
    }
    
    /**
     * Sends a request to start a chat with a user to the server. 
     * @param otherUsername
     */
    public void requestChat(String otherUsername) {
        System.out.println("Requesting chat with " + otherUsername);
        toServer.println("newchat" + " " + username + " " + otherUsername);
        toServer.flush();
    }
    
    /**
     * Sends a request to the server to remove the connected user from the chat. 
     */
    public void leaveChat(){
        toServer.println("leavechat" + " " + chatID + " " + username);
        toServer.flush();
        gui.dispose();
        clientModel.activeChats.remove(chatID);
    }
    
}
