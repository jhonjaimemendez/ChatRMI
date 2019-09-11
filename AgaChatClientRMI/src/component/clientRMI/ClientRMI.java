/**
 * Class: ClientRMI
 * 
 * @version  1.0
 * 
 * @since 06-09-2019
 * 
 * @autor Aga
 *
 */

package component.clientRMI;

import java.rmi.Naming;

import component.interfaces.IServerMessage;

/**
 * 
 * Class that defines the connection of the client to the server
 *
 */

public class ClientRMI  {

	private String url; 
	private IServerMessage iServerMessage = null;

	public ClientRMI(String server) throws Exception {

		url = "rmi://"+server+"/ServerMessage";

		iServerMessage = (IServerMessage) Naming.lookup(url);



	}

	public IServerMessage getiServerMessage() {
		return iServerMessage;
	}

}
