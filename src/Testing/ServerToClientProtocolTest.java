package Testing;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import Server.ServerToClientProtocol;

public class ServerToClientProtocolTest {
    @Test
    public void loginTest() {
        String ans = ServerToClientProtocol.LOGIN();
        String required = "connectionaccepted\n";
        assertEquals(ans, required);
    }
    
    @Test
    public void loginSuccessTest() {
        String ans = ServerToClientProtocol.LOGIN_SUCCESS();
        String required = "loginsuccess\n";
        assertEquals(ans, required);
    }
    
    @Test
    public void loginFailTest() {
        String ans = ServerToClientProtocol.LOGIN_FAIL("hhh");
        String required = "loginfailed hhh\n";
        assertEquals(ans, required);
    }
    
    @Test
    public void newUserTest() {
        String ans = ServerToClientProtocol.NEW_USER("kaivan");
        String required = "newuser kaivan\n";
        assertEquals(ans, required);
    }
    
    @Test
    public void logoutTest() {
        String ans = ServerToClientProtocol.LOGOUT("kaivan");
        String required = "userterminated kaivan\n";
        assertEquals(ans, required);
    }
    
    @Test
    public void logoutSuccessTest() {
        String ans = ServerToClientProtocol.LOGOUTSUCCESS();
        String required = "logoutsuccess\n";
        assertEquals(ans, required);
    }
    
    @Test
    public void chatCreationSuccessTest() {
        String ans = ServerToClientProtocol.CHAT_CREATION_SUCCESS(0, "kaivan", "jon");
        String required = "chatcreated 0 kaivan jon\n";
        assertEquals(ans, required);
    }
    
    @Test
    public void chatCreationFailureTest() {
        String ans = ServerToClientProtocol.CHAT_CREATION_FAILURE("user");
        String required = "chatfailed user\n";
        assertEquals(ans, required);
    }
    
    @Test
    public void addChatSuccessTest() {
        String ans = ServerToClientProtocol.ADD_CHAT_USER_SUCCESS();
        String required = "addchatsuccess\n";
        assertEquals(ans, required);
    }
    
    @Test
    public void addChatFailureTest() {
        String ans = ServerToClientProtocol.ADD_CHAT_USER_FAILURE("error");
        String required = "addchatfailure error\n";
        assertEquals(ans, required);
    }
    
    @Test
    public void addChatUserTest() {
        String ans = ServerToClientProtocol.ADDED_TO_CHAT(0, "kaivan");
        String required = "addedtochat 0 kaivan\n";
        assertEquals(ans, required);
    }
    
    @Test
    public void newChatUser() {
        String ans = ServerToClientProtocol.NEW_CHAT_USER(0, "kaivan", "no_text");
        String required = "newchatuser 0 kaivan no_text\n";
        assertEquals(ans, required);
    }
    
    @Test
    public void chatUserExitedTest() {
        String ans = ServerToClientProtocol.CHAT_USER_EXITED(0, "kaivan");
        String required = "chatuserexited 0 kaivan\n";
        assertEquals(ans, required);
    }
    
    @Test
    public void leftChatSuccessTest() {
        String ans = ServerToClientProtocol.LEAVE_CHAT_SUCCESS(0);
        String required = "chatleftsuccess 0\n";
        assertEquals(ans, required);
    }
    
    @Test
    public void chatLeftFailureTest() {
        String ans = ServerToClientProtocol.LEAVE_CHAT_FAILURE("error");
        String required = "chatleftfailure error\n";
        assertEquals(ans, required);
    }
    
    @Test
    public void chatMessageFailTest() {
        String ans = ServerToClientProtocol.SEND_CHATMESSAGE_FAIL("error");
        String required = "chatmessagefail error\n";
        assertEquals(ans, required);
    }
    
    @Test
    public void relayMessageTest() {
        String ans = ServerToClientProtocol.RELAY_MESSAGE(0, "kaivan", "hi wats up");
        String required = "relaymessage 0 kaivan hi wats up\n";
        assertEquals(ans, required);
    }
    
    @Test
    public void typingStatusMessageTest() {
        String ans = ServerToClientProtocol.IS_TYPING(0, "kaivan");
        String required = "typingstatus 0 kaivan is_typing\n";
        assertEquals(ans, required);
        ans = ServerToClientProtocol.NO_TEXT(0, "kaivan");
        required = "typingstatus 0 kaivan no_text\n";
        assertEquals(ans, required);
        ans = ServerToClientProtocol.HAS_TYPED(0, "kaivan");
        required = "typingstatus 0 kaivan has_typed\n";
        assertEquals(ans, required);
    }
}
