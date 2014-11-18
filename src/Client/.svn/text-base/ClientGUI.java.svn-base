package Client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class ClientGUI extends JFrame {

    private JTable userTable;
    private final DefaultTableModel tm = new DefaultTableModel(null, new Object[] {"UserName"}) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JButton chatButton;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem logoutMenuItem;
    private ClientModel clientModel;
    

    /**
     * Initialize all components for ClientGUI and set locations
     * @param clientModel2 
     * @param username 
     * @param clientModel 
     */
    public ClientGUI(String username, ClientModel clientModel) {
        super(username + " Client GUI");
        this.clientModel = clientModel;
        addWindowListener(new ClientGUIWindowListener());
        
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        
        userTable = new JTable();
        userTable.setName("userTable");
        userTable.setModel(tm);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane sp = new JScrollPane(userTable);
        userTable.addMouseListener(new userTableMouseListener());
        
        chatButton = new JButton("Chat");
        chatButton.addActionListener(new ChatButtonActionListener());
        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(Alignment.TRAILING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addGap(5)
                    .addComponent(sp, GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                    .addContainerGap())
                .addGroup(groupLayout.createSequentialGroup()
                    .addGap(89)
                    .addComponent(chatButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(87))
        );
        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addGap(5)
                    .addComponent(sp, GroupLayout.DEFAULT_SIZE, 717, Short.MAX_VALUE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(chatButton)
                    .addGap(13))
        );
        getContentPane().setLayout(groupLayout);
        pack();
        setSize(new Dimension(250,700));
        
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        
        logoutMenuItem = new JMenuItem("Logout");
        logoutMenuItem.addActionListener(new LogoutMenuItemActionListener());
        fileMenu.add(logoutMenuItem);
        setVisible(true);
    }
    
    /**
     * Adds a user that has just logged on to the table in the GUI.
     * @param username, the user that has just logged on
     */
    public void addActiveUser(String username){
        tm.addRow(new Object[] {username});
    }
    
    /**
     * Removes a user that has just logged off from the table in the GUI.
     * @param username, the user that has just logged off
     */
    public void removeActiveUser(String username){
        for (int rowNum = 0; rowNum < tm.getRowCount(); rowNum++){
            if (tm.getValueAt(rowNum, 0).equals(username)){
                tm.removeRow(rowNum);
                break;
            }
        }
    }
    
    /**
     * 
     * Listens for a double click on the table to open up a chat with a user. 
     *
     */
    private class userTableMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent arg0) {
            if(arg0.getClickCount() == 2){
                String otherUsername = (String) userTable.getValueAt(userTable.getSelectedRow(), 0);
                System.out.println(otherUsername);
                clientModel.requestChat(otherUsername);
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
    
    /**
     * 
     * Listens for when the chat button is clicked to open a chat with the selected user. 
     *
     */   
    private class ChatButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            String otherUsername = (String) userTable.getValueAt(userTable.getSelectedRow(), 0);
            System.out.println(otherUsername);
            clientModel.requestChat(otherUsername);
            
        }
    }
    
    /**
     * 
     * Listens for when the logout menu is clicked to terminate the connection. 
     *
     */
    private class LogoutMenuItemActionListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            tryToClose();
        }
    }
    
    /**
     * 
     * listens for when the "x" is clicked on the GUI to terminate the connection.
     *
     */
    private class ClientGUIWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent arg0) {
            tryToClose();
        }
    }
    
    /**
     * Opens a window that asks if the user would like to exit. If user clicks yes, then the connection
     * is terminated. 
     */
    private void tryToClose(){
        int confirmed = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to exit?", "User Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (confirmed == JOptionPane.YES_OPTION){
            clientModel.logout();
        }
    }
    

    public static void closeGUI(ClientGUI gui){
        gui.dispose();
    }
    
}
