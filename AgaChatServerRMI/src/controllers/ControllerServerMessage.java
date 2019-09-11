/**
 * Class: ImpServerMessage
 * 
 * @version  1.0
 * 
 * @since 06-09-2019
 * 
 * @autor Aga
 *
 */

package controllers;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.Key;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import component.database.ConnectMySQL;
import component.interfaces.IClientMessage;
import component.interfaces.IServerMessage;
import component.serverRMI.AESCryptoUtil;
import views.ChatGUI;

/**
 *  Class that defines the implementation of the remote methods 
 *  specified in the interface
 * 
 */

public class ControllerServerMessage extends UnicastRemoteObject implements IServerMessage {

	private ChatGUI chatGUI;
	private Key key;

	private static final long serialVersionUID = 154546767L;

	/**
	 * Constructor that receives the database connection parameters
	 * 
	 * @param serverName Server name or IP address
	 * @param nameBD database name
	 * @param user Authorized user
	 * @param password
	 */
	public ControllerServerMessage(String serverName, String nameBD, 
			String user, String password)  throws RemoteException {

		try {

			new ConnectMySQL(serverName, nameBD, user, password);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	/**
	 * addMessage: Add messages to user history
	 *
	 * @param message: Message that is added to the history and displayed in the conversation
	 * @param user: User who maintains communication and messages are added for your history
	 *
	 * return boolean true if it was added correctly and false if there were problems adding to the database
	 */
	@Override
	public boolean addMessage(byte[] encryptedMessage, String user) throws RemoteException {

		String message = AESCryptoUtil.getTextPlain(encryptedMessage, key);
		
		boolean result = insertMessageDatabase(message, "U",user);

		chatGUI.addMessage(message);

		return result;

	}

	/**
	 * getMessageHistory: Returns customer message history
	 *
	 * @param user: User who maintains communication and messages are added for your history
	 *
	 * return Vector<String> Collection containing the message history
	 */
	@Override
	public String getMessageHistory(String user) throws RemoteException {

		StringBuffer messageBuffer = new StringBuffer();

		try {

			PreparedStatement preparedStatement = ConnectMySQL.getConnection().
					prepareStatement("Select sender,message from messages where userid = ?");

			preparedStatement.setString(1, user);

			ResultSet resultSet = preparedStatement.executeQuery();


			while (resultSet.next()) {

				messageBuffer.append(resultSet.getString(1).contentEquals("S") ? "Server: " : "You:  ");
				messageBuffer.append(resultSet.getString(2));
				messageBuffer.append("\n");

			}


		} catch (SQLException e) {


			e.printStackTrace();
		}

		return messageBuffer.toString();
	}

	/**
	 * openSession: Open the session by validating the user to the database
	 *
	 * @param user: Registered user of the database 
	 * @param password: Password of the user registered in the database
	 *
	 * return Vector<String> Collection containing the message history
	 */
	@Override
	public boolean openSession(String user, String password) throws RemoteException {

		boolean result = false;

		try {

			PreparedStatement preparedStatement = ConnectMySQL.getConnection().
					prepareStatement("Select count(1) from users where userid = ? and password = ?");

			preparedStatement.setString(1, user);
			preparedStatement.setString(2, password);

			ResultSet resultSet = preparedStatement.executeQuery();

			resultSet.next();

			int count = resultSet.getInt(1);

			if (count == 1) {

				//The encryption key is generated
				key = AESCryptoUtil.getKey(password);
				
				//Open the GUI for the server to chat with the client
				chatGUI = new ChatGUI(user,key);
				
				result = true;

			}	

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return result;
	}

	/**
	 * registerClient: Method that receives the remote object from the client to be able to 
	 *                 send messages from the server to the client
	 * 
	 * param clientMessage: Remote client object to be able to send messages from the server
	 */
	@Override
	public void registerClient(IClientMessage clientMessage) throws RemoteException {

		chatGUI.setiClientMessage(clientMessage);


	}

	public static boolean  insertMessageDatabase(String message, String sender,String user) {

		boolean result = true;

		try {

			PreparedStatement preparedStatement = ConnectMySQL.getConnection().
					prepareStatement("insert into messages values(?,?,?,?,?)");

			preparedStatement.setString(1, null);
			preparedStatement.setString(2, message);
			preparedStatement.setString(3,  DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()));
			preparedStatement.setString(4,  sender);
			preparedStatement.setString(5,  user);
			preparedStatement.execute();

		} catch (SQLException e) {

			result = false;

			e.printStackTrace();
		}

		return result;

	}

	/**
	 * Method to close the session
	 * @throws RemoteException
	 */
	@Override
	public void closeSession() throws RemoteException {
		
		chatGUI.closeSession();
		
	}

	

	/**
	 * Method that returns the encryption key generated by the server and shared to the client
	 * 
	 * @return Encryption key
	 * @throws RemoteException
	 */
	@Override
	public Key getKeyEncryption() throws RemoteException {
		
		return key;
		
	}

}
