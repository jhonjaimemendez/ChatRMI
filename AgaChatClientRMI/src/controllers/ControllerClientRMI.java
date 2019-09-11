/**
 * Class: ControllerClientRMI
 * 
 * @version  1.0
 * 
 * @since 06-09-2019
 * 
 * @autor Aga
 *
 */

package controllers;

import javax.swing.JOptionPane;

/**
 * Start the login and chat process with the server
 */

import component.clientRMI.ClientRMI;
import views.Login;

public class ControllerClientRMI {

	public ControllerClientRMI() {
		
		try {
			
			ClientRMI clientRMI = new ClientRMI("127.0.0.1");

			new Login(clientRMI);

		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, "RMI server is not deployed","Error", JOptionPane.ERROR_MESSAGE);
		}

	}

}
