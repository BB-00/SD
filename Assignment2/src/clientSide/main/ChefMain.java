package clientSide.main;

import clientSide.entities.Chef;
import clientSide.entities.ChefStates;
import clientSide.stubs.*;
import genclass.GenericIO;

/**
 *  Client side of the Restaurant problem (chef).
 *
 *	Implementation of a client-server model of type 2 (server replication).
 *	Communication is based on a communication channel under the TCP protocol.
 */
public class ChefMain {
	
	/**
	 *  Main method.
	 *
	 *    @param args runtime arguments
	 *        args[0] - name of the platform where is located the bar server
	 *        args[1] - port number for listening to service requests
	 *        args[2] - name of the platform where is located the kitchen server
	 *        args[3] - port number for listening to service requests
     *		  args[4] - name of the platform where is located the general repository server
	 *        args[5] - port number for listening to service requests
	 */
	public static void main(String[] args) {
		
		String barServerHostName;						// name of the platform where is located the bar server
		int barServerPortNum = -1; 						// port number for listening to service requests
		String kitchenServerHostName;					// name of the platform where is located the kitchen server
		int kitchenServerPortNum = -1; 					// port number for listening to service requests
		String genReposServerHostName;					// name of the platform where is located the general repository server
		int genReposServerPortNum = -1;					// port number for listening to service requests
		BarStub bar;									// remote reference to the bar stub
		KitchenStub kitchen;							// remote reference to the kitchen stub
		GenReposStub genRepos; 							// remote reference to the general repository stub

		Chef chef;										// Chef thread
	
		
	    /* getting problem runtime parameters */
	    if (args.length != 6) {
	    	GenericIO.writelnString("Wrong number of parameters!");
	        System.exit (1);
	    }
	    
	    barServerHostName = args[0];
	    try {
	    	barServerPortNum = Integer.parseInt (args[1]);
	    }
	    catch (NumberFormatException e) {
	    	GenericIO.writelnString ("args[1] is not a number!");
	        System.exit (1);
	    }
	    if ((barServerPortNum < 4000) || (barServerPortNum >= 65536)) {
	    	GenericIO.writelnString ("args[1] is not a valid port number!");
	        System.exit(1);
	    }
	      
	    kitchenServerHostName = args[2];
	    try {
	    	kitchenServerPortNum = Integer.parseInt(args[3]);
	    } catch(NumberFormatException e) {
	    	GenericIO.writelnString("args[3] is not a valid port number!");
	    	System.exit(1);
	    }
	    if ((kitchenServerPortNum < 4000) || (kitchenServerPortNum >= 65536)) {
		    GenericIO.writelnString("args[3] is not a valid port number!");
		    System.exit (1);
		}
		
	    genReposServerHostName = args[4];
	    try {
	    	genReposServerPortNum = Integer.parseInt(args[5]);
	    } catch(NumberFormatException e) {
	    	GenericIO.writelnString("args[5] is not a valid port number!");
	    	System.exit(1);
	    }
	    if ((genReposServerPortNum < 4000) || (genReposServerPortNum >= 65536)) {
		    GenericIO.writelnString("args[5] is not a valid port number!");
		    System.exit (1);
		}
		
	    
		//Initialization
		bar = new BarStub(barServerHostName, barServerPortNum);
		kitchen = new KitchenStub(kitchenServerHostName, kitchenServerPortNum);
		genRepos = new GenReposStub(genReposServerHostName, genReposServerPortNum);
		
		chef = new Chef("Chef", ChefStates.WAITING_FOR_AN_ORDER, kitchen, bar);
		
		
		// Start of simulation
		chef.start();
        GenericIO.writelnString("Chef thread "+Thread.currentThread().getName()+" started!");

        try {
            chef.join();
        } catch(InterruptedException e) {
        	e.printStackTrace();
        }
        
        bar.shutdown();
        kitchen.shutdown();
        genRepos.shutdown();
        
        GenericIO.writelnString("Chef thread "+Thread.currentThread().getName()+" finished!");
	}
}
