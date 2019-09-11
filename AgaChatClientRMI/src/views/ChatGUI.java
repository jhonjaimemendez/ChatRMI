package views;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.Key;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import component.clientRMI.ClientRMI;
import component.interfaces.IClientMessage;
import component.clientRMI.AESCryptoUtil;


public class ChatGUI extends JFrame implements ActionListener,IClientMessage {

	private static final long serialVersionUID = 3237142973716170185L;
	private JPanel contentPane;
	private String user;

	private JTextArea jtaChat;
	private JTextArea jtaSendChat;
	private JButton jbSend;
	private ClientRMI clientRMI;
	private JTextArea jtaChatHistory;
	private JButton jbCloseSession;
	private Key key;

	private static String textOne = "<html>" +
			"<font size='12' color='green'><strong>You:  </strong></font>" +
			"</html>";

	private static String textTwo = "<html>" +
			"<font size='12' color='green'><strong>Client  :</strong></font>" +
			"</html>";


	/**
	 * Create the frame.
	 */
	public ChatGUI(String user,ClientRMI clientRMI) {

		this.user = user;
		this.clientRMI = clientRMI;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 585);
		setTitle("Chat Client");
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		jbCloseSession = new JButton("Close Session");
		jbCloseSession.addActionListener(this);
		jbCloseSession.setBounds(652, 24, 122, 34);
		contentPane.add(jbCloseSession);

		JLabel jlStatus = new JLabel("Status: Connected");
		jlStatus.setFont(new Font("Tahoma", Font.PLAIN, 12));
		jlStatus.setForeground(new Color(0, 128, 0));
		jlStatus.setBounds(10, 22, 111, 18);
		contentPane.add(jlStatus);

		JLabel jlUser = new JLabel("User: " + user);
		jlUser.setFont(new Font("Tahoma", Font.PLAIN, 12));
		jlUser.setForeground(new Color(0, 128, 0));
		jlUser.setBounds(337, 21, 111, 18);
		contentPane.add(jlUser);

		JTabbedPane jtabbedPaneChat = new JTabbedPane(JTabbedPane.TOP);
		jtabbedPaneChat.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				if (jtabbedPaneChat.getSelectedIndex() == 1)

					searchHistorial();
			}
		});
		jtabbedPaneChat.setBounds(10, 51, 764, 484);

		jtabbedPaneChat.addTab("Chat", getPanelChat());
		jtabbedPaneChat.addTab("Chat History", getPanelHistoryChat());

		contentPane.add(jtabbedPaneChat);

		setVisible(true);
		
	
		try {

			clientRMI.getiServerMessage().registerClient((IClientMessage)UnicastRemoteObject.exportObject(this, 0));
			//The encryption key is requested
			key = clientRMI.getiServerMessage().getKeyEncryption();

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private JPanel getPanelChat() {

		JPanel jPanel = new JPanel();
		jPanel.setLayout(null);

		jtaChat = new JTextArea();
		jtaChat.setBounds(10, 11, 745, 387);
		jPanel.add(jtaChat);

		jtaSendChat = new JTextArea();
		jtaSendChat.setBounds(10, 407, 643, 40);
		jtaSendChat.addKeyListener( new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {

				if(e.getKeyCode() == KeyEvent.VK_ENTER)

					sendMessager();

			}
		});
		jPanel.add(jtaSendChat);

		jbSend = new JButton("Send");
		jbSend.addActionListener(this);
		jbSend.setBounds(655, 407, 100, 40);
		jPanel.add(jbSend);

		return jPanel;

	}


	private JPanel getPanelHistoryChat() {

		JPanel jPanel = new JPanel();
		jPanel.setLayout(null);

		jtaChatHistory = new JTextArea();
		jtaChatHistory.setBounds(10, 11, 745, 427);
		jPanel.add(jtaChatHistory);

		return jPanel;

	}

	private void sendMessager() {

		jtaChat.setText(jtaChat.getText() + "\n You: " + jtaSendChat.getText());

		try {
			
			//The message is encrypted
			byte[] messageEncrypted = AESCryptoUtil.getTextCipher(jtaSendChat.getText(), key);

			
			clientRMI.getiServerMessage().addMessage(messageEncrypted, user);

		} catch (RemoteException e) {

			e.printStackTrace();
		}

		jtaSendChat.setText("");
		jtaSendChat.grabFocus();
	}

	public void searchHistorial() {

		try {

			jtaChatHistory.setText(clientRMI.getiServerMessage().getMessageHistory(user));

		} catch (RemoteException e) {

			e.printStackTrace();

		}

	}

	@Override
	public void receiveMessage(byte[] encryptedMessage) throws RemoteException {

		String message = AESCryptoUtil.getTextPlain(encryptedMessage, key);
		
		jtaChat.setText(jtaChat.getText() + "\n Server : " + message);

	}

	@Override
	public void closeSessionByServer() throws RemoteException {

		JOptionPane.showMessageDialog(this,"Session closed by the Server",
				"Information", JOptionPane.INFORMATION_MESSAGE);
		this.dispose();

	}

	public void closeSessionByClient() {
		
		try {
			
			
			clientRMI.getiServerMessage().closeSession();
			this.dispose();
			
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == jbSend) 

			sendMessager();

		else if (e.getSource() == jbCloseSession) 

			closeSessionByClient();
		

	}


}
