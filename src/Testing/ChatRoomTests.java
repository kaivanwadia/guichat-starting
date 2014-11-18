package Testing;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import Server.ClientHandler;
import Server.ChatroomModel.Chat;
import static org.junit.Assert.assertEquals;

public class ChatRoomTests {
    @Test
    public void createChatTest() throws IOException {
        Map<String, ClientHandler> globalOnlineClients = new HashMap<String, ClientHandler>();
        Map<Integer, Chat> globalOngoingChats = new HashMap<Integer, Chat>();
        
        ClientHandler cl1 = new ClientHandler("kaivan", globalOnlineClients, globalOngoingChats, true);
        globalOnlineClients.put(cl1.getUsername(), cl1);
        
        ClientHandler cl2 = new ClientHandler("jon", globalOnlineClients, globalOngoingChats, true);
        globalOnlineClients.put(cl2.getUsername(), cl2);
        
        ClientHandler cl3 = new ClientHandler("tayo", globalOnlineClients, globalOngoingChats, true);
        globalOnlineClients.put(cl3.getUsername(), cl3);
        
        // Creating new chat
        cl2.handleClientRequest("newchat jon kaivan");
        
        assertEquals(cl1.getMessagePassingHistory().get(0), "chatcreated 0 jon kaivan\n");
        
        assertEquals(cl2.getMessagePassingHistory().get(0), "chatcreated 0 jon kaivan\n");
        
        // Duplicate chat
        cl2.handleClientRequest("newchat jon kaivan");
        
        assertEquals(cl2.getMessagePassingHistory().get(1), "chatfailed Chat already exists\n");
        
        // Add client to chat
        cl2.handleClientRequest("addtochat 0 tayo");
        
        assertEquals(cl2.getMessagePassingHistory().get(2), "addchatsuccess\n");
        assertEquals(cl2.getMessagePassingHistory().get(3), "newchatuser 0 tayo no_text\n");
        
        assertEquals(cl1.getMessagePassingHistory().get(1), "newchatuser 0 tayo no_text\n");
        
        assertEquals(cl3.getMessagePassingHistory().get(0), "addedtochat 0 jon\n");
        assertEquals(cl3.getMessagePassingHistory().get(1), "newchatuser 0 kaivan no_text\n");
        assertEquals(cl3.getMessagePassingHistory().get(2), "newchatuser 0 jon no_text\n");
        
        // Send chat message
        cl2.handleClientRequest("chatmessage0 hi bye");
        
        assertEquals(cl1.getMessagePassingHistory().get(2), "relaymessage 0 jon hi bye\n");
        assertEquals(cl2.getMessagePassingHistory().get(4), "relaymessage 0 jon hi bye\n");
        assertEquals(cl3.getMessagePassingHistory().get(3), "relaymessage 0 jon hi bye\n");
        
        // Try to add existing user to chat
        cl2.handleClientRequest("addtochat 0 tayo");
        
        assertEquals(cl2.getMessagePassingHistory().get(5), "addchatfailure user already in chat\n");
        
        // Send typing status
        cl2.handleClientRequest("typingstatus 0 jon is_typing");
        
        assertEquals(cl2.getMessagePassingHistory().get(6), "typingstatus 0 jon is_typing\n");
        assertEquals(cl3.getMessagePassingHistory().get(4), "typingstatus 0 jon is_typing\n");
        assertEquals(cl1.getMessagePassingHistory().get(3), "typingstatus 0 jon is_typing\n");
    }
}
