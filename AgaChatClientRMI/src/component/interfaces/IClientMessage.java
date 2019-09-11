/**
 * Interface: IClientMessage
 * 
 * @version  1.0
 * 
 * @since 06-09-2019
 * 
 * @autor Aga
 *
 */

package component.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote methods are specified which the server can executes and offers the client
 */

public interface IClientMessage extends Remote {

	/**
	 * Client method to wait for server messages
	 * 
	 * @param message Message sent from the server to the client
	 * @throws RemoteException
	 */
	public void receiveMessage(byte[] encryptedMessage) throws RemoteException;
	
	/**
	 * Method to close the session
	 * @throws RemoteException
	 */
	public void closeSessionByServer() throws RemoteException;
	
}
