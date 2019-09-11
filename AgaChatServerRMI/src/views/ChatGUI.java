/**
 * Class: ChatGUI
 * 
 * @version  1.0
 * 
 * @since 06-09-2019
 * 
 * @autor Aga
 *
 */

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
import java.security.Key;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import component.interfaces.IClientMessage;
import component.serverRMI.AESCryptoUtil;
import controllers.ControllerServerMessage;

/**
 * 
 * Graphical interface for the server to chat with the client
 *
 */

public class ChatGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = -5408404439008276427L;

	private JPanel contentPane;

	private JTextArea jtaChat;
	private JTextArea jtaSendChat;

	private JButton jbSend;

	private IClientMessage iClientMessage;

	private static String textOne = "<html>" +
			"<font size='12' color='green'><strong>You:  </strong></font>" +
			"</html>";

	private static String textTwo = "<html>" +
			"<font size='12' color='green'><strong>Client  :</strong></font>" +
			"</html>";

	private String user;

	private JButton jbCloseSession;

	private Key key;

	/**
	 * Create the frame.
	 */
	public ChatGUI(String user,Key key) {

		this.user = user;
		this.key = key;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 585);
		setTitle("Chat Server");
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

		JLabel jlUser = new JLabel("User: Server");
		jlUser.setFont(new Font("Tahoma", Font.PLAIN, 12));
		jlUser.setForeground(new Color(0, 128, 0));
		jlUser.setBounds(337, 21, 111, 18);
		contentPane.add(jlUser);

		JTabbedPane jtabbedPaneChat = new JTabbedPane(JTabbedPane.TOP);
		jtabbedPaneChat.setBounds(10, 51, 764, 484);

		jtabbedPaneChat.addTab("Chat", getPanelChat());

		contentPane.add(jtabbedPaneChat);

		setVisible(true);
	}

	private JPanel getPanelChat() {

		JPanel jPanel = new JPanel();
		jPanel.setLayout(null);

		jtaChat = new JTextArea();
		jtaChat.setBounds(10, 11, 745, 387);
		jPanel.add(jtaChat);

		jtaSendChat = new JTextArea();
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

		jtaSendChat.setBounds(10, 407, 643, 40);
		jPanel.add(jtaSendChat);

		jbSend = new JButton("Send");
		jbSend.addActionListener(this);
		jbSend.setBounds(655, 407, 100, 40);
		jPanel.add(jbSend);

		return jPanel;

	}


	public void setiClientMessage(IClientMessage iClientMessage) {
		this.iClientMessage = iClientMessage;
	}

	public void addMessage(String message) {

		jtaChat.setText(jtaChat.getText()  + "\n Client " + user + ":" + message);

	}

	private void sendMessager() {

		jtaChat.setText(jtaChat.getText() + "\n You: " + jtaSendChat.getText());

		try {

			//The message is encrypted
			byte[] messageEncrypted = AESCryptoUtil.getTextCipher(jtaSendChat.getText(), key);

			iClientMessage.receiveMessage(messageEncrypted);
			ControllerServerMessage.insertMessageDatabase(jtaSendChat.getText(), "S",user);

		} catch (RemoteException e) {

			e.printStackTrace();
		}

		jtaSendChat.setText("");
		jtaSendChat.grabFocus();

	}

	public void closeSession() {

		JOptionPane.showMessageDialog(this,"Session closed by the client",
				"Information", JOptionPane.INFORMATION_MESSAGE);
		this.dispose();

	}


	public void closeSessionByServer() {

		try {

			
			iClientMessage.closeSession();
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

			closeSessionByServer();

		

	}
}
