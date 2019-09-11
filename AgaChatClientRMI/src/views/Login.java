/**
 * Class: Login
 * 
 * @version  1.0
 * 
 * @since 07-09-2019
 * 
 * @autor Aga
 *
 */

package views;

/**
 * Class that allows users to log in to communicate with the server
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import component.clientRMI.ClientRMI;

public class Login extends JFrame implements ActionListener{


	private static final long serialVersionUID = 134534L;
	private JPanel contentPane;
	private JTextField jtUser;
	private JPasswordField jtPassword;
	private JButton jbLogin;
	private JButton jbCancel;
	private ClientRMI clientRMI;


	/**
	 * Create the frame.
	 */
	public Login(ClientRMI clientRMI) {

		this.clientRMI = clientRMI;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 522, 277);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Login Client");
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		jtUser = new JTextField();
		jtUser.setBounds(163, 52, 212, 28);
		contentPane.add(jtUser);
		jtUser.setColumns(10);

		JLabel jlUser = new JLabel("User");
		jlUser.setBounds(131, 59, 35, 14);
		contentPane.add(jlUser);

		JLabel jlPassword = new JLabel("Password");
		jlPassword.setBounds(100, 114, 80, 14);
		contentPane.add(jlPassword);

		jtPassword = new JPasswordField();
		jtPassword.setColumns(10);
		jtPassword.setBounds(165, 107, 212, 28);
		jtPassword.addActionListener(this);
		contentPane.add(jtPassword);

		jbLogin = new JButton("Log In");
		jbLogin.setMnemonic('L');
		jbLogin.addActionListener(this);
		jbLogin.setBounds(131, 167, 107, 34);
		contentPane.add(jbLogin);

		jbCancel = new JButton("Cancel");
		jbCancel.setMnemonic('C');
		jbCancel.addActionListener(this);
		jbCancel.setBounds(292, 167, 107, 34);
		contentPane.add(jbCancel);


		setVisible(true);

	}

	private boolean isValidateForm() {

		boolean result = true;

		if (jtUser.getText().isEmpty()) {

			JOptionPane.showMessageDialog(this, "Enter the userid");
			result = false;
			jtUser.grabFocus();

		} else if (jtPassword.getText().isEmpty()) {

			JOptionPane.showMessageDialog(this, "Enter the password of the userid","Error", JOptionPane.ERROR_MESSAGE);
			result = false;
			jtPassword.grabFocus();
		} 


		return result;
	}

	private boolean isLoginCorrect(String user, String password) {

		boolean result = true;

		try {

			if (!clientRMI.getiServerMessage().openSession(user, password)) {

				JOptionPane.showMessageDialog(this, "Incorrect username or password","Error Login", JOptionPane.ERROR_MESSAGE);
				clearForm();
				result = false;
			}

		} catch (RemoteException e) {

			e.printStackTrace();
		}

		return result;

	}

	private void clearForm() {

		jtUser.setText("");
		jtPassword.setText("");
		jtUser.grabFocus();

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == jbCancel) {

			this.dispose();

		} else 	if (e.getSource() == jbLogin || e.getSource() == jtPassword) {

			if (isValidateForm() && isLoginCorrect(jtUser.getText(), jtPassword.getText())) {

				new ChatGUI(jtUser.getText(),clientRMI);
				this.dispose();

			}
		}

	}




}
