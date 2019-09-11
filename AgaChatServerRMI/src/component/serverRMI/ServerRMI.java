/**
 * Class: ServerRMI
 * 
 * @version  1.0
 * 
 * @since 06-09-2019
 * 
 * @autor Aga
 *
 */

package component.serverRMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import controllers.ControllerServerMessage;

/**
 * 
 * Class that deploys the RMI server
 *
 */

public class ServerRMI {

	public ServerRMI() {

		try {

			Registry registry = LocateRegistry.createRegistry(1099);


			registry.rebind("ServerMessage", new ControllerServerMessage("localhost", "Agachat", "root", ""));
			System.out.println("------------------------------------------------------------------\n");
			System.out.println("Message server Deployed\n");
			System.out.println("------------------------------------------------------------------\n");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

}
