package Testing;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import Server.ClientToServerProtocol;
import Server.ClientToServerProtocol.CToSMessages;

public class ClientToServerProtocolTest {
    @Test
    public void loginTest() {
        CToSMessages ans = ClientToServerProtocol.messageToProtocol("login kaivan");
        CToSMessages required = CToSMessages.LOGIN;
        assertEquals(ans,required);
    }
    
    @Test
    public void logoutTest() {
        CToSMessages ans = ClientToServerProtocol.messageToProtocol("terminateconnection");
        CToSMessages required = CToSMessages.LOGOUT;
        assertEquals(ans,required);
    }
    
    @Test
    public void unknownTest() {
        CToSMessages ans = ClientToServerProtocol.messageToProtocol("sdfdsn");
        CToSMessages required = CToSMessages.UNKNOWN;
        assertEquals(ans,required);
    }
    
    @Test
    public void newChatTest() {
        CToSMessages ans = ClientToServerProtocol.messageToProtocol("newchat kaivan jonathan");
        CToSMessages required = CToSMessages.NEW_CHAT;
        assertEquals(ans,required);
    }
    
    @Test
    public void addChatTest() {
        CToSMessages ans = ClientToServerProtocol.messageToProtocol("addtochat 2 kaivan");
        CToSMessages required = CToSMessages.ADD_CHAT;
        assertEquals(ans,required);
    }
    
    @Test
    public void leaveChatTest() {
        CToSMessages ans = ClientToServerProtocol.messageToProtocol("leavechat 2 kaivan");
        CToSMessages required = CToSMessages.LEAVE_CHAT;
        assertEquals(ans,required);
    }
    
    @Test
    public void typingStatusTest() {
        CToSMessages ans = ClientToServerProtocol.messageToProtocol("typingstatus 2 kaivan is_typing");
        CToSMessages required = CToSMessages.TYPING_STATUS;
        assertEquals(ans,required);
        ans = ClientToServerProtocol.messageToProtocol("typingstatus 2 kaivan no_text");
        required = CToSMessages.TYPING_STATUS;
        assertEquals(ans,required);
        ans = ClientToServerProtocol.messageToProtocol("typingstatus 2 kaivan has_typed");
        required = CToSMessages.TYPING_STATUS;
        assertEquals(ans,required);
    }
    
    @Test
    public void chatMessageTest() {
        CToSMessages ans = ClientToServerProtocol.messageToProtocol("chatmessage2 hi how are you?");
        CToSMessages required = CToSMessages.CHAT_MESSAGE;
        assertEquals(ans,required);
    }
}
