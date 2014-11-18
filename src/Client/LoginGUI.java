package Client;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

public class LoginGUI extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private JLabel error;
    private JLabel name;
    private JLabel ip;
    private JLabel port;
    private JTextField nameField;
    private JTextField ipField;
    private JTextField portField;
    private JButton login;
    private LoginModel loginModel;
    
    public LoginGUI(LoginModel loginModel) {
        super("Login GUI");
        this.loginModel = loginModel;
        error = new JLabel("");
        name = new JLabel("Name:");
        ip = new JLabel("IP:");
        port = new JLabel("Port:");
        nameField = new JTextField();
        ipField = new JTextField();
        portField = new JTextField();
        login = new JButton("Login");
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        Container cp = this.getContentPane();
        
        GroupLayout layout = new GroupLayout(cp);
        setSize(300,200);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(false);

        cp.setLayout(layout);
        
        ParallelGroup hGroup1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        SequentialGroup hGroup2 = layout
                .createSequentialGroup();
        
        ParallelGroup h1 = layout.createParallelGroup();
        ParallelGroup h2 = layout.createParallelGroup();
        hGroup1.addComponent(error);
        h1.addComponent(name);
        h1.addComponent(ip);
        h1.addComponent(port);
        hGroup2.addGroup(h1);
        h2.addComponent(nameField);
        h2.addComponent(ipField);
        h2.addComponent(portField);
        h2.addComponent(login);
        hGroup2.addGroup(h2);
        hGroup1.addGroup(hGroup2);
        layout.setHorizontalGroup(hGroup1);
        
        
        ParallelGroup v1 = layout
                .createParallelGroup(GroupLayout.Alignment.BASELINE);
        ParallelGroup v2 = layout
                .createParallelGroup(GroupLayout.Alignment.BASELINE);
        ParallelGroup v3 = layout
                .createParallelGroup(GroupLayout.Alignment.BASELINE);
        ParallelGroup v4 = layout
                .createParallelGroup(GroupLayout.Alignment.BASELINE);
        ParallelGroup v5 = layout
                .createParallelGroup(GroupLayout.Alignment.BASELINE);
        SequentialGroup vGroup = layout.createSequentialGroup();
        v1.addComponent(error);
        vGroup.addGroup(v1);
        v2.addComponent(name);
        v2.addComponent(nameField);
        vGroup.addGroup(v2);
        v3.addComponent(ip);
        v3.addComponent(ipField);
        vGroup.addGroup(v3);
        v4.addComponent(port);
        v4.addComponent(portField);
        vGroup.addGroup(v4);
        v5.addComponent(login);
        vGroup.addGroup(v5);
        layout.setVerticalGroup(vGroup); 
        
        login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }
    
    private void handleLogin(){
        
        error.setText("");
        String username = nameField.getText();
        String uNameRegEx = "\\w+";
        String ipAddress = ipField.getText();
        String ipRegEx = "(\\d{1,3}\\.){3}\\d{1,3}";
        int portNum = -1;
        
        try{
            portNum = Integer.parseInt(portField.getText());
        } catch (NumberFormatException e){
                error.setText("Error: Missing Informaion");
                return;
        }
        
       if(!(ipAddress.matches(ipRegEx)) && portNum < 0){
            error.setText("Error: Invalid IP Address and Port Number");
            ipField.selectAll(); 
            ipField.grabFocus();
            portField.selectAll();
       } else if(!(username.matches(uNameRegEx))){
           error.setText("Error: Invalid User Name");
           nameField.grabFocus();
           nameField.selectAll();
       } else if(!(ipAddress.matches(ipRegEx))){
            error.setText("Error: Invalid IP Address");
            ipField.selectAll();   
            ipField.grabFocus();
       } else if(portNum < 0 || portNum > 65535){
            error.setText("Error: Invalid Port Number");
            portField.selectAll();
            portField.grabFocus(); 
       } else{
           if (!loginModel.connectToServer(username, ipAddress, portNum)){
               error.setText("Error: Problem Connecting to Host. Try Again");
           }
       }
        
    }
    
    public void setErrorMessage(String errorMessage){
        error.setText(errorMessage);
        nameField.grabFocus();
        nameField.selectAll();
    }
    
}
