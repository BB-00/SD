package serverSide.main;

import genclass.GenericIO;
import serverSide.sharedRegions.*;
import serverSide.entities.*;
import clientSide.stubs.*;
import commInfra.*;
import java.net.*;

/**
 * Server side of the General Repository of Information.
 *
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 */
public class BarMain {

	/**
	 * Flag signaling the service is active.
	 */
	public static boolean waitConnection;

	/**
	 * Main method.
	 *
	 * @param args runtime arguments args[0] - port number for listening to service
	 *             requests args[1] - name of the platform where is located the
	 *             server for the general repository args[2] - port number where the
	 *             server for the general repository is listening to service
	 *             requests args[3] - name of the platform where is located the
	 *             server for the table args[4] - port number where the server for
	 *             the table is listening to service requests
	 */
	public static void main(String[] args) {

		Bar bar; // Bar (service to be rendered)
		BarInterface barInterface; // interface to the bar
		GenReposStub reposStub; // stub to the general repository
		TableStub tableStub; // stub to the table
		ServerCom scon, sconi; // communication channels
		int portNum = -1; // port number for listening to service requests
		String reposServerName; // name of the platform where is located the server for the general repository
		int reposPortNum = -1; // port number where the server for the general repository is listening to
								// service requests
		String tableServerName; // name of the platform where is located the server for the table
		int tablePortNum = -1; // port number where the server for the table is listening to service requests

		/* getting problem runtime parameters */
		if (args.length != 5) {
			GenericIO.writelnString("Wrong number of parameters!");
			System.exit(1);
		}

		try {
			portNum = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			GenericIO.writelnString("args[0] is not a number!");
			System.exit(1);
		}
		if ((portNum < 4000) || (portNum >= 65536)) {
			GenericIO.writelnString("args[0] is not a valid port number!");
			System.exit(1);
		}

		reposServerName = args[1];
		try {
			reposPortNum = Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			GenericIO.writelnString("args[2] is not a number!");
			System.exit(1);
		}
		if ((reposPortNum < 4000) || (reposPortNum >= 65536)) {
			GenericIO.writelnString("args[2] is not a valid port number!");
			System.exit(1);
		}

		tableServerName = args[3];
		try {
			tablePortNum = Integer.parseInt(args[4]);
		} catch (NumberFormatException e) {
			GenericIO.writelnString("args[2] is not a number!");
			System.exit(1);
		}
		if ((tablePortNum < 4000) || (tablePortNum >= 65536)) {
			GenericIO.writelnString("args[2] is not a valid port number!");
			System.exit(1);
		}

		/* service is established */

		reposStub = new GenReposStub(reposServerName, reposPortNum); // communication to the general repository is
																		// instantiated
		tableStub = new TableStub(tableServerName, tablePortNum); // communication to the table is instatiated
		bar = new Bar(reposStub, tableStub); // service is instantiated
		barInterface = new BarInterface(bar); // interface to the service is instantiated
		scon = new ServerCom(portNum); // listening channel at the public port is established
		scon.start();
		GenericIO.writelnString("Service is established!");
		GenericIO.writelnString("Server is listening for service requests.");

		/* service request processing */

		BarClientProxy cliProxy; // service provider agent

		waitConnection = true;
		while (waitConnection) {
			try {
				sconi = scon.accept(); // enter listening procedure
				cliProxy = new BarClientProxy(sconi, barInterface); // start a service provider agent to address
				cliProxy.start(); // the request of service
			} catch (SocketTimeoutException e) {
			}
		}
		scon.end(); // operations termination
		GenericIO.writelnString("Server was shutdown.");
	}
}