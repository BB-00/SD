package serverSide.main;

import java.rmi.registry.*;
import java.rmi.*;
import java.rmi.server.*;
import serverSide.objects.*;
import interfaces.*;
import genclass.GenericIO;

/**
 * Instantiation and registering of a kitchen object.
 *
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on Java RMI.
 */

public class KitchenMain {
	/**
	 * Flag signaling the end of operations.
	 */

	private static boolean end = false;

	/**
	 * Main method.
	 *
	 * args[0] - port number for listening to service requests args[1] - name of the
	 * platform where is located the RMI registering service args[2] - port number
	 * where the registering service is listening to service requests
	 */

	public static void main(String[] args) {
		int portNumb = -1; // port number for listening to service requests
		String rmiRegHostName; // name of the platform where is located the RMI registering service
		int rmiRegPortNumb = -1; // port number where the registering service is listening to service requests

		if (args.length != 3) {
			GenericIO.writelnString("Wrong number of parameters!");
			System.exit(1);
		}
		try {
			portNumb = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			GenericIO.writelnString("args[0] is not a number!");
			System.exit(1);
		}
		if ((portNumb < 4000) || (portNumb >= 65536)) {
			GenericIO.writelnString("args[0] is not a valid port number!");
			System.exit(1);
		}
		rmiRegHostName = args[1];
		try {
			rmiRegPortNumb = Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			GenericIO.writelnString("args[2] is not a number!");
			System.exit(1);
		}
		if ((rmiRegPortNumb < 4000) || (rmiRegPortNumb >= 65536)) {
			GenericIO.writelnString("args[2] is not a valid port number!");
			System.exit(1);
		}

		/* create and install the security manager */

		if (System.getSecurityManager() == null)
			System.setSecurityManager(new SecurityManager());
		GenericIO.writelnString("Security manager was installed!");

		/* get a remote reference to the general repository object */

		String nameEntryGeneralRepos = "GeneralRepository"; // public name of the general repository object
		GenReposInterface reposStub = null; // remote reference to the general repository object
		Registry registry = null; // remote reference for registration in the RMI registry service

		try {
			registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
		} catch (RemoteException e) {
			GenericIO.writelnString("RMI registry creation exception: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		GenericIO.writelnString("RMI registry was created!");

		try {
			reposStub = (GenReposInterface) registry.lookup(nameEntryGeneralRepos);
		} catch (RemoteException e) {
			GenericIO.writelnString("GeneralRepos lookup exception: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		} catch (NotBoundException e) {
			GenericIO.writelnString("GeneralRepos not bound exception: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}

		/* instantiate a kitchen object */

		Kitchen kit = new Kitchen(reposStub); // kit object
		KitchenInterface kitStub = null; // remote reference to the kit object

		try {
			kitStub = (KitchenInterface) UnicastRemoteObject.exportObject(kit, portNumb);
		} catch (RemoteException e) {
			GenericIO.writelnString("Kitchen stub generation exception: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		GenericIO.writelnString("Stub was generated!");

		/* register it with the general registry service */

		String nameEntryBase = "RegisterHandler"; // public name of the object that enables the registration
		// of other remote objects
		String nameEntryObject = "Kitchen"; // public name of the bar object
		Register reg = null; // remote reference to the object that enables the registration
		// of other remote objects

		try {
			reg = (Register) registry.lookup(nameEntryBase);
		} catch (RemoteException e) {
			GenericIO.writelnString("RegisterRemoteObject lookup exception: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		} catch (NotBoundException e) {
			GenericIO.writelnString("RegisterRemoteObject not bound exception: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}

		try {
			reg.bind(nameEntryObject, kitStub);
		} catch (RemoteException e) {
			GenericIO.writelnString("Kitchen registration exception: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		} catch (AlreadyBoundException e) {
			GenericIO.writelnString("Kitchen already bound exception: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		GenericIO.writelnString("Kitchen object was registered!");

		/* wait for the end of operations */

		GenericIO.writelnString("Kitchen is in operation!");
		try {
			while (!end)
				synchronized (Class.forName("serverSide.main.ServerRestaurantKitchen")) {
					try {
						(Class.forName("serverSide.main.ServerRestaurantKitchen")).wait();
					} catch (InterruptedException e) {
						GenericIO.writelnString("Kitchen main thread was interrupted!");
					}
				}
		} catch (ClassNotFoundException e) {
			GenericIO.writelnString("The data type ServerRestaurantKitchen was not found (blocking)!");
			e.printStackTrace();
			System.exit(1);
		}

		/* server shutdown */

		boolean shutdownDone = false; // flag signaling the shutdown of the Kitchen service

		try {
			reg.unbind(nameEntryObject);
		} catch (RemoteException e) {
			GenericIO.writelnString("Kitchen deregistration exception: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		} catch (NotBoundException e) {
			GenericIO.writelnString("Kitchen not bound exception: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		GenericIO.writelnString("Kitchen was deregistered!");

		try {
			shutdownDone = UnicastRemoteObject.unexportObject(kit, true);
		} catch (NoSuchObjectException e) {
			GenericIO.writelnString("Kitchen unexport exception: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}

		if (shutdownDone)
			GenericIO.writelnString("Kitchen was shutdown!");
	}

	/**
	 * Close of operations.
	 */

	public static void shutdown() {
		end = true;
		try {
			synchronized (Class.forName("serverSide.main.ServerRestaurantKitchen")) {
				(Class.forName("serverSide.main.ServerRestaurantKitchen")).notify();
			}
		} catch (ClassNotFoundException e) {
			GenericIO.writelnString("The data type ServerRestaurantKitchen was not found (waking up)!");
			e.printStackTrace();
			System.exit(1);
		}
	}
}
