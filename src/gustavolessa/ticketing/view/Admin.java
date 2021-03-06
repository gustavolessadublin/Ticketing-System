package gustavolessa.ticketing.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

//import gustavolessa.ticketing.controller.Controller;

@SuppressWarnings("serial")
public class Admin extends JFrame implements ItemListener{

	private gustavolessa.ticketing.controller.Controller controller = new gustavolessa.ticketing.controller.Controller(this);
	private JTextField addUsernameField;
	private JPasswordField addPasswordField;
	private JComboBox addUserTypesList;
	private JComboBox updateInfoUserTypesList;
	private JComboBox updateInfoUserId;
	private String[] userTypes = {"Admin", "Tech Support", "Manager"};
	private String loggedUserId;
	private JTextField updateInfoUsernameField;
	private JPasswordField updateInfoPasswordField;
	private JPasswordField addUserConfirmPasswordField;
	private JPasswordField updateInfoConfirmPasswordField;
	private String[][] users;
	private String[] userIds;

	//Create getters for private variables
	public String getNewUserType() {
		String selected = "";
		try {
			selected = (String)addUserTypesList.getSelectedItem();


			if(selected.equals("Tech Support")) {
				return "Tech";
			} else {
				return selected;
			}
		} catch (NullPointerException e) {
		}
		return selected;
	}
	public String getChangePassUserType(){
		String selected = "";
		try {
			selected = (String)updateInfoUserTypesList.getSelectedItem();
			if(selected.equals("Tech Support")) {
				return "Tech";
			} else {
				return selected;
			}
		} catch (NullPointerException e) {
		}
		return selected;
	}
	public String getUpdateInfoUserId() {
		String selected = (String)updateInfoUserId.getSelectedItem();
		return selected;
	}
	public String getAddUsernameField() {
		return addUsernameField.getText();
	}
	public String getAddPasswordField() {
		return new String(addPasswordField.getPassword());
	}
	public String getChangePassUsernameField() {
		return updateInfoUsernameField.getText();
	}
	public String getChangePassPasswordField() {
		return new String(updateInfoPasswordField.getPassword());
	}
	public String getChangePassConfirmPasswordField() {
		return new String(updateInfoConfirmPasswordField.getPassword());
	}
	public String getAddUserConfirmPasswordField() {
		return new String(addUserConfirmPasswordField.getPassword());
	}
	public String getLoggedUserId() {
		return loggedUserId;
	}
	
	/**
	 * Method to update user type JComboBox using information retrieved from database.
	 * @param type to be selected.
	 */
	private void setUpdateInfoUserType(String type) {
		switch(type) {
		case "Admin":
			updateInfoUserTypesList.setSelectedIndex(0);
			break;
		case "Tech":
			updateInfoUserTypesList.setSelectedIndex(1);
			break;
		case "Manager":
			updateInfoUserTypesList.setSelectedIndex(2);
			break;
		default:
			updateInfoUserTypesList.setSelectedIndex(-1);
			break;
			//stem.out.println("Couldn't determine user type to select");
		}		
	}
	
	//Constructor that takes userID as parameter
	public Admin(String loggedUserId){
		this.loggedUserId = loggedUserId;
		setSize(640,300);
		this.setLayout(new BorderLayout());
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setTitle("Admin Dashboard");
		this.setLocationRelativeTo(null);
		retrieveUserData();
		this.addWindowListener(controller);

		//Add Menu containing File -> Close
		if (System.getProperty("os.name").contains("Mac")) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		}
		JMenuBar topBar = new JMenuBar();
		this.setJMenuBar(topBar);
		JMenu file = new JMenu("File");
		topBar.add(file);
		JMenuItem close = new JMenuItem("Close");
		file.add(close);
		close.addActionListener(controller);
		close.setActionCommand("close");

		//Create center panel with GridLayout              
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(1,2));
                
		//Create panel "Add User" and its components.
		JPanel addUserPanel = new JPanel(){
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(300, 350);
            }
        };
        JPanel addUserFields = new JPanel();
		JLabel addUserTypeLabel = new JLabel("User type: ");
        JLabel addUsernameLabel = new JLabel("Username: ");
		addUsernameField = new JTextField();
		addPasswordField = new JPasswordField();
		JLabel addUserPassLabel = new JLabel("Password: ");
		JLabel addUserConfirmPassLabel = new JLabel("Confirm password: ");
		addUserConfirmPasswordField = new JPasswordField();
		addUserTypesList = new JComboBox(userTypes);
		addUserTypesList.setSelectedIndex(-1);
		JButton addUserButton = new JButton("Register");
		
		//Add components to addUserPanel
		addUserPanel.setLayout(new BorderLayout());
		addUserFields.setLayout(new GridLayout(4,2));
		addUserFields.add(addUserTypeLabel);
		addUserFields.add(addUserTypesList);
		addUserFields.add(addUsernameLabel);
		addUserFields.add(addUsernameField);
		addUserFields.add(addUserPassLabel);
		addUserFields.add(addPasswordField);
		addUserFields.add(addUserConfirmPassLabel);
		addUserFields.add(addUserConfirmPasswordField);
		addUserPanel.add(addUserFields, BorderLayout.CENTER);
		addUserPanel.add(addUserButton, BorderLayout.SOUTH);
		
		//Create panel "Update Info", sub-panel and its components.
		JPanel updateInfoPanel = new JPanel(){
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(350, 350);
            }
        };
        JPanel updateInfoFields = new JPanel();
		JLabel updateInfoIdLabel = new JLabel("Choose User ID: ");
		updateInfoUserId = new JComboBox(userIds);
		updateInfoUserId.setSelectedIndex(-1);
		updateInfoUserId.addItemListener(this);
		JLabel updateInfoUserTypeLabel = new JLabel("User type: ");
		updateInfoUserTypesList = new JComboBox(userTypes);
		updateInfoUserTypesList.setSelectedIndex(-1);
		JLabel updateInfoUsernameLabel = new JLabel("Username: ");
		updateInfoUsernameField = new JTextField();
		JLabel updateInfoPassLabel = new JLabel("New password: ");
		updateInfoPasswordField = new JPasswordField();
		JLabel updateInfoConfirmPassLabel = new JLabel("Confirm new password: ");
		updateInfoConfirmPasswordField = new JPasswordField();
		JButton updateInfoButton = new JButton("Update");
		
		//Add components to updateInfoPanel
		updateInfoPanel.setLayout(new BorderLayout());
		updateInfoFields.setLayout(new GridLayout(5,2));
		updateInfoFields.add(updateInfoIdLabel);
		updateInfoFields.add(updateInfoUserId);
		updateInfoFields.add(updateInfoUserTypeLabel);
		updateInfoFields.add(updateInfoUserTypesList);
		updateInfoFields.add(updateInfoUsernameLabel);
		updateInfoFields.add(updateInfoUsernameField);
		updateInfoFields.add(updateInfoPassLabel);
		updateInfoFields.add(updateInfoPasswordField);
		updateInfoFields.add(updateInfoConfirmPassLabel);
		updateInfoFields.add(updateInfoConfirmPasswordField);
		updateInfoPanel.add(updateInfoFields, BorderLayout.CENTER);
		updateInfoPanel.add(updateInfoButton, BorderLayout.SOUTH);
		
		//Add ActionListener and ActionCommands for buttons
		addUserButton.addActionListener(controller);
		addUserButton.setActionCommand("addUserButton");
		updateInfoButton.addActionListener(controller);
		updateInfoButton.setActionCommand("changePassButton");
		
		//Create and set borders with title for both panels
		TitledBorder addUserBorder = BorderFactory.createTitledBorder("Add User");
		addUserBorder.setTitleJustification(TitledBorder.CENTER);
		addUserPanel.setBorder(addUserBorder);
		TitledBorder changePassBorder = BorderFactory.createTitledBorder("Update Info");
		changePassBorder.setTitleJustification(TitledBorder.CENTER);
		updateInfoPanel.setBorder(changePassBorder);
		
		//Create panel and button to Logout
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout());
		JButton refresh = new JButton("Refresh");
		refresh.addActionListener(controller);
		refresh.setActionCommand("refreshAdmin");
		JButton logout = new JButton("Logout");
		logout.addActionListener(controller);
		logout.setActionCommand("adminLogout");
		buttonsPanel.add(refresh);
		buttonsPanel.add(logout);
	
		//Add panel to frame
		centerPanel.add(addUserPanel);
		centerPanel.add(updateInfoPanel);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(buttonsPanel, BorderLayout.SOUTH);	
		
		validate();
		repaint();
		setVisible(true);
	}

	
	/**
	 * Method to retrieve users info, populating global variable userIds. 
	 */
	public void retrieveUserData() {
		try {
			users = controller.getUsersInfo();
		} catch (SQLException e) {
			System.out.println("Error retriving users -> retrieveUserData");
			e.printStackTrace();
		}
		userIds = new String[users.length];
		for(int x=0;x<users.length;x++) {
			userIds[x] = users[x][0];
		}	
	}
	
	/**
	 * Method that displays user's info according to the info stored in the 2D array "users".
	 * @param selectedId
	 */
	public void setUpdateInfo(String selectedId) {
		updateInfoPasswordField.setText("");
		updateInfoConfirmPasswordField.setText("");
		if(selectedId.equals("-1")) {
			updateInfoUsernameField.setText("");
			setUpdateInfoUserType("");
			updateInfoUserId.setSelectedIndex(-1);
		} else {
			for(int x=0;x<users.length;x++) {
				if(users[x][0] == selectedId) {
					updateInfoUsernameField.setText(users[x][1]);
					setUpdateInfoUserType(users[x][3]);
				}
			}		
		}
	}
	
	/**
	 * Method from ItemListener that detects when JComboBox had an item selected.
	 */
    @Override
    public void itemStateChanged(ItemEvent event) {
       if (event.getStateChange() == ItemEvent.SELECTED) {
          String selectedId = (String)event.getItem();
          setUpdateInfo(selectedId);
       }
    } 
}