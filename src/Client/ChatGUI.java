package Client;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


@SuppressWarnings("serial")
public class ChatGUI extends JFrame {
//    private final DefaultTableModel tm = new DefaultTableModel(null, new Object[]{"Message"});
    private final DefaultTableModel statusTM = new DefaultTableModel(null, new Object[] {"Users"}) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JButton SendButton;
    private JScrollPane chatMessagesScrollPane;
    private JTextPane textArea;
    private JComboBox addUserMenu;
    private ChatModel chatModel;
    private JTextPane messageTable;
    private JPanel wrappingPanel;
    String status = "no_text";
    Timer timer;
    int delay = 5000;
    //Should be available users from Chat Model. 
    private String[] offlineUsers = {"Add User: "};
    private JTable statusTable;
    private JScrollPane statusScrollPane;
    

    /**
     * Initialize all components for ClientGUI and set locations
     * @param chatModel 
     */
    public ChatGUI(String otherUsername, ChatModel chatModel) {
        super(otherUsername);
        this.chatModel = chatModel;
        addWindowListener(new ChatGUIWindowListener());
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        wrappingPanel = new JPanel();
        
        SendButton = new JButton("Send");
        SendButton.addActionListener(new SendButtonActionListener());
        SendButton.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
        
        textArea = new JTextPane();
        JScrollPane scrollpane = new JScrollPane(textArea);
        textArea.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        textArea.addKeyListener(new TextAreaKeyListener());
        textArea.getDocument().addDocumentListener(new TextAreaDocumentListener());
        
        //Changes status to has_typed if user has been idle for 5 seconds.
        ActionListener isIdle = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                hasTypedCheck();
            }
        };
        timer = new Timer(delay, isIdle);

        
        //Creates a dropDown menu to add users. 
        addUserMenu = new JComboBox(offlineUsers);
        addUserMenu.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
        addUserMenu.addActionListener(new addUserMenuListener());
        
        messageTable = new JTextPane();
        messageTable.setEditable(false);
        chatMessagesScrollPane = new JScrollPane();
        chatMessagesScrollPane.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
        chatMessagesScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        chatMessagesScrollPane.setViewportView(messageTable);
        
        statusTable = new JTable();
        statusTable.setModel(statusTM);
        statusTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        statusTable.addMouseListener(new statusTableMouseListener());
        statusScrollPane = new JScrollPane(statusTable);
        statusScrollPane.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
        
        GroupLayout layout = new GroupLayout(wrappingPanel);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.TRAILING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(Alignment.LEADING)
                        .addComponent(scrollpane, GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE)
                        .addComponent(chatMessagesScrollPane, GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE))
                    .addGap(6)
                    .addGroup(layout.createParallelGroup(Alignment.LEADING)
                        .addComponent(SendButton, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                        .addComponent(statusScrollPane, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                        .addComponent(addUserMenu, 0, 149, Short.MAX_VALUE))
                    .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(Alignment.LEADING)
                        .addComponent(statusScrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                        .addComponent(chatMessagesScrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(SendButton, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(addUserMenu, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addComponent(scrollpane, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
                    .addContainerGap())
        );
        wrappingPanel.setLayout(layout);
        
        getContentPane().add(wrappingPanel);
        pack();
        setVisible(true);
    }
    
    // Listens for events that happen to the dropDown menu and sends the request to the server.
    private class addUserMenuListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String username = (String) addUserMenu.getSelectedItem();
            if(!(username.equals("Add User: "))){
                chatModel.requestAddUserToChat(username);
                System.out.println("Adding " + username + " to chatID number " + chatModel.chatID);
            }
        }

       
    }
    
    private class TextAreaDocumentListener implements DocumentListener{

        //Not sure if we need to use this...
        @Override
        public void changedUpdate(DocumentEvent arg0) {
            
        }

        //Checks to see if user is typing into textArea
        @Override
        public void insertUpdate(DocumentEvent arg0) {
            timer.restart();
            status = "is_typing";
            chatModel.sendStatusToServer(status);
            System.out.println(status);
        }

        //Checks if user is deleting text from textArea
        @Override
        public void removeUpdate(DocumentEvent arg0) {
            timer.restart();
            status = "is_typing";
            chatModel.sendStatusToServer(status);
            System.out.println(status);
        }
        
    }
    
    //Sends message to server
    private class TextAreaKeyListener implements KeyListener {
        @Override
        public void keyPressed(KeyEvent arg0) {
            //Don't Need
        }

        @Override
        public void keyReleased(KeyEvent arg0) {
            int key = arg0.getKeyCode();
            
            if(key == KeyEvent.VK_ENTER) {
                String text = textArea.getText();
                System.out.println(text);
                chatModel.clientToServerChatMessage(text);
                textArea.grabFocus();
                textArea.setText("");
                
                  
                status = "no_text";
                chatModel.sendStatusToServer(status);
                System.out.println(status);
            } 
            
        }

        @Override
        public void keyTyped(KeyEvent arg0) {
            // Don't Need
            
        }
    }
    
    public String[] arrayListToArray(ArrayList<String> arraylist){
        String[] newlist = new String[arraylist.size()];
        for(int i = 0; i < newlist.length; i++){
            newlist[i] = arraylist.get(i);
        }
        return newlist;
    }
    
    public JTable getOtherUserStatus(){
        return statusTable;
    }
    
    public void addUserToDropDown(String username){
        addUserMenu.addItem(username);
        //addUserMenu.
    }
    
    public JComboBox getAddUserMenu(){
        return addUserMenu;
    }
    
    public void removeUserFromDropDown(String username){
        addUserMenu.removeItem(username);
    }
    
    public void addMessageToUserTable(String message){
        messageTable.setText(messageTable.getText().concat(message+"\n"));
        messageTable.scrollRectToVisible(new Rectangle(0, this.messageTable.getHeight() - 2, 1, 1));
    }
    
    public void addUserExitedMessage(String username){
        messageTable.setText(messageTable.getText().concat(username+" has exited the chat.\n"));
        messageTable.scrollRectToVisible(new Rectangle(0, this.messageTable.getHeight() - 2, 1, 1));
    }
    
    public void addUserToChat(String otherUsername) {
        messageTable.setText(messageTable.getText().concat(otherUsername+" has joined the chat.\n"));
        messageTable.scrollRectToVisible(new Rectangle(0, this.messageTable.getHeight() - 2, 1, 1));   
    }
    
    public void addUserLoggedOutMessage(String username){
        messageTable.setText(messageTable.getText().concat(username+" has logged off.\n"));
        messageTable.scrollRectToVisible(new Rectangle(0, this.messageTable.getHeight() - 2, 1, 1));
    }
    
    public void userLoggedOnMessage(String username) {
        messageTable.setText(messageTable.getText().concat(username+" has logged on.\n"));
        messageTable.scrollRectToVisible(new Rectangle(0, this.messageTable.getHeight() - 2, 1, 1));
    }

    
    private class SendButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            String text = textArea.getText();
            System.out.println(text);
            chatModel.clientToServerChatMessage(text);
            textArea.setText("");
            textArea.grabFocus();
            
            status = "no_text";
            chatModel.sendStatusToServer(status);
            System.out.println(status);
            
        }
    }
    private class ChatGUIWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent arg0) {
            chatModel.leaveChat();
        }
    }
    
    protected void hasTypedCheck(){
        if(status.equals("is_typing")){
            status = "has_typed";
            chatModel.sendStatusToServer(status);
            System.out.println(status);
        }
    }
    
    private class statusTableMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent arg0) {
            if(arg0.getClickCount() == 2){
                String userStatus = (String) statusTable.getValueAt(statusTable.getSelectedRow(), 0);
                String otherUsername = userStatus.split(":")[0];
                chatModel.requestChat(otherUsername);
            }     
        }

        @Override
        public void mouseEntered(MouseEvent arg0) {
            
        }

        @Override
        public void mouseExited(MouseEvent arg0) {
   
        }

        @Override
        public void mousePressed(MouseEvent arg0) {

        }

        @Override
        public void mouseReleased(MouseEvent arg0) {

        }
        
    }


    
}
