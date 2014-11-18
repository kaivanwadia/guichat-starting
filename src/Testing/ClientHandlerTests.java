package Testing;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import Server.ClientHandler;
import Server.ChatroomModel.Chat;
import static org.junit.Assert.assertEquals;;

public class ClientHandlerTests {
    @Test
    public void logoutTest() {
        Map<String, ClientHandler> globalOnlineClients = new HashMap<String, ClientHandler>();
        Map<Integer, Chat> globalOngoingChats = new HashMap<Integer, Chat>();
        
        ClientHandler cl1 = new ClientHandler("kaivan", globalOnlineClients, globalOngoingChats, true);
        globalOnlineClients.put(cl1.getUsername(), cl1);
        
        ClientHandler cl2 = new ClientHandler("jon", globalOnlineClients, globalOngoingChats, true);
        globalOnlineClients.put(cl2.getUsername(), cl2);
        
        ClientHandler cl3 = new ClientHandler("tayo", globalOnlineClients, globalOngoingChats, true);
        globalOnlineClients.put(cl3.getUsername(), cl3);
        
        cl1.handleClientRequest("terminateconnection");
        
        assertEquals(cl2.getMessagePassingHistory().get(0), "userterminated kaivan\n");
        assertEquals(cl3.getMessagePassingHistory().get(0), "userterminated kaivan\n");
    }
}
