package Client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class LoginModel {
    
    ClientModel clientModel;
    Socket clientSocket;
    LoginGUI gui;
    
    public LoginModel(ClientModel clientModel){
        this.clientModel = clientModel;
        this.gui = new LoginGUI(this);
    }
    
    public boolean connectToServer(String username, String ipAddress, int portNum){
        try {
            clientSocket = new Socket();
            clientSocket.connect(new InetSocketAddress(ipAddress, portNum), 10000);
            clientModel.setUpNetworking(username, clientSocket);
            return true;
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("YO");
            return false;
        }
        
    }
    
    public void closeGUI(){
        gui.dispose();
    }
    
    public void setErrorMessage(String errorMessage){
        gui.setErrorMessage(errorMessage);
    }
    

}
