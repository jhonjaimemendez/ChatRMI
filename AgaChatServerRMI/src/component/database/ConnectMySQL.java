/**
 * Class: ConnectMySQL
 * 
 * @version  1.0
 * 
 * @since 06-09-2019
 * 
 * @autor Aga
 *
 */

package component.database;

import java.sql.Connection;
import java.sql.DriverManager;


/**
 * This class allows to connect to a MySQL database
 */

final public class ConnectMySQL {


	private static Connection connection;


	/**
		* General constructor that connects to the database depending on the
		* parameters
		*
		* @param serverName Server name or IP address
		* @param nameBD database name
		* @param user Authorized user
		* @param password
		*
	*/
	public ConnectMySQL(String serverName, String nameBD,
			             String user, String password) throws Exception {

		//The driver is loaded to connect to the database
		Class.forName("com.mysql.jdbc.Driver").newInstance();

		//A URL is created to the database server
		String url = "jdbc:mysql://" + serverName + "/" + nameBD;
		
		//The database connection is created
		connection = DriverManager.getConnection(url, user, password);

	}

	/**
	 * Returns the configured connection
	 */
	public static Connection getConnection() {

		return connection;
		
	}

}
