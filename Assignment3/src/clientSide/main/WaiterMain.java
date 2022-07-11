package clientSide.main;

import genclass.GenericIO;
import interfaces.*;
import clientSide.entities.*;
import java.rmi.registry.*;
import java.rmi.*;

/**
 *    Client side of the Restaurant problem (waiter).
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on Java RMI.
 */

public class WaiterMain {

    /**
     *  Main method.
     *
     *    @param args runtime arguments
     *        args[0] - name of the platform where is located the RMI registering service
     *        args[1] - port number where the registering service is listening to service requests
     */

    public static void main(String[] args) {

        String rmiRegHostName;                                         // name of the platform where is located the RMI registering service
        int rmiRegPortNumb = -1;                                       // port number where the registering service is listening to service requests


        /* getting problem runtime parameters */

        if (args.length != 2)
        { GenericIO.writelnString ("Wrong number of parameters!");
           System.exit (1);
        }
        rmiRegHostName = args[0];
        try
        { rmiRegPortNumb = Integer.parseInt (args[1]);
        }
        catch (NumberFormatException e)
        { GenericIO.writelnString ("args[1] is not a number!");
            System.exit (1);
        }
        if ((rmiRegPortNumb < 4000) || (rmiRegPortNumb >= 65536))
        { GenericIO.writelnString ("args[1] is not a valid port number!");
           System.exit (1);
        }

        /* problem initialisation */

		String nameEntryGeneralRepos = "GenRepos";	//Public name of the General Repository object
		String nameEntryTable = "Table";				    //Public name of the Table object 
		String nameEntryBar = "Bar";						//Public name of the Bar object
        String nameEntryKitchen = "Kitchen";				//Public name of the Kitchen object 
		GenReposInterface reposStub = null;				    //Remote reference to the General Repository object
        KitchenInterface kitchen = null;					//Remote reference for the Kitchen object
		TableInterface table = null;					    //Remote reference for the Table object
		BarInterface bar = null;						    //Remote reference for the Bar object
		Registry registry = null;							//Remote reference for registration in the RMI Registry service
        Waiter waiter;										//Waiter Thread

        try {
			registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
		} catch (RemoteException e) {
			GenericIO.writelnString ("RMI registry creation exception: " + e.getMessage ());
	        e.printStackTrace ();
	        System.exit (1);			
		}

		try {
			reposStub = (GenReposInterface) registry.lookup(nameEntryGeneralRepos);
		} catch( RemoteException e ) {
			GenericIO.writelnString ("General Repository lookup exception: " + e.getMessage ());
	        e.printStackTrace ();
	        System.exit (1);			
		} catch( NotBoundException e ) {
			GenericIO.writelnString ("General Repository not bound exception: " + e.getMessage ());
	        e.printStackTrace ();
	        System.exit (1);			
		}

        try {
			kitchen = (KitchenInterface) registry.lookup(nameEntryKitchen);
		} catch( RemoteException e ) {
			GenericIO.writelnString ("Kitchen lookup exception: " + e.getMessage ());
	        e.printStackTrace ();
	        System.exit (1);			
		} catch( NotBoundException e ) {
			GenericIO.writelnString ("Kitchen not bound exception: " + e.getMessage ());
	        e.printStackTrace ();
	        System.exit (1);			
		}
		
		try {
			bar = (BarInterface) registry.lookup(nameEntryBar);
		} catch( RemoteException e ) {
			GenericIO.writelnString ("Bar lookup exception: " + e.getMessage ());
	        e.printStackTrace ();
	        System.exit (1);			
		} catch( NotBoundException e ) {
			GenericIO.writelnString ("Bar not bound exception: " + e.getMessage ());
	        e.printStackTrace ();
	        System.exit (1);			
		}

        try {
			table = (TableInterface) registry.lookup(nameEntryTable);
		} catch( RemoteException e ) {
			GenericIO.writelnString ("Table lookup exception: " + e.getMessage ());
	        e.printStackTrace ();
	        System.exit (1);			
		} catch( NotBoundException e ) {
			GenericIO.writelnString ("Table not bound exception: " + e.getMessage ());
	        e.printStackTrace ();
	        System.exit (1);			
		}

        waiter = new Waiter("waiter", WaiterStates.APPRAISING_SITUATION, kitchen, bar, table);
		
		/* start of the simulation */
		waiter.start();
		
		/* waiting for the end of the simulation */
		try {
			waiter.join();
		} catch (InterruptedException e) {}
		GenericIO.writelnString ("The waiter thread has terminated.");

        try {
			kitchen.shutdown();
		} catch(RemoteException e) {
			GenericIO.writelnString ("Waiter generator remote exception on Bar shutdown: " + e.getMessage ());
	        System.exit (1);			
		}

        try {
			bar.shutdown();
		} catch(RemoteException e) {
			GenericIO.writelnString ("Waiter generator remote exception on Kitchen shutdown: " + e.getMessage ());
	        System.exit (1);			
		}

        try {
			table.shutdown();
		} catch(RemoteException e) {
			GenericIO.writelnString ("Waiter generator remote exception on Kitchen shutdown: " + e.getMessage ());
	        System.exit (1);			
		}

        try {
			reposStub.shutdown();
		} catch(RemoteException e) {
			GenericIO.writelnString ("Waiter generator remote exception on GeneralRepos shutdown: " + e.getMessage ());
	        System.exit (1);			
		}
        
    }
    
}
