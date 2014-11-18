package Testing;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import Server.ClientHandler;
import Server.ChatroomModel.Chat;
import static org.junit.Assert.assertEquals;;

public class EndChatTests {
    @Test
    public void endChatTest() {
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
        
        // Add client to chat
        cl2.handleClientRequest("addtochat 0 tayo");
        
        assertEquals(cl2.getMessagePassingHistory().get(1), "addchatsuccess\n");
        assertEquals(cl2.getMessagePassingHistory().get(2), "newchatuser 0 tayo no_text\n");
        
        assertEquals(cl1.getMessagePassingHistory().get(1), "newchatuser 0 tayo no_text\n");
        
        // CLient leaves chat
        cl2.handleClientRequest("leavechat 0 jon");
        
        assertEquals(cl2.getMessagePassingHistory().get(3), "chatleftsuccess 0\n");
        
        assertEquals(cl1.getMessagePassingHistory().get(2), "chatuserexited 0 jon\n");
        assertEquals(cl3.getMessagePassingHistory().get(3), "chatuserexited 0 jon\n");
    }
}
