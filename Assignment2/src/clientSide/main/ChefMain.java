package clientSide.main;

import clientSide.entities.Chef;
import clientSide.stubs.*;
import genclass.GenericIO;

public class ChefMain {

	public static void main(String[] args) {
		
		String barServerHostName;                               // name of the platform where is located the barber shop server
	    int barServerPortNum = -1;                             // port number for listening to service requests
	    String kitchenServerHostName;                                 // name of the platform where is located the kitchen server
	    int kitchenServerPortNum = -1;                               // port number for listening to service requests
	    BarStub bar;													// remote reference to the bar
		KitchenStub kitchen;
		
		Chef chef;
	
	    /* getting problem runtime parameters */
	
	    if (args.length != 4) {
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
		
		
		//Initialization
		
		
		bar = new BarStub(barServerHostName, barServerPortNum);
		kitchen = new KitchenStub(kitchenServerHostName, kitchenServerPortNum);
		
		chef = new Chef("Chef", kitchen, bar);
		
		
		// Start of simulation
		chef.start();
        GenericIO.writelnString("Chef thread"+Thread.currentThread().getName()+" Started");


        try {
            chef.join();
        } catch(InterruptedException e) {
        	e.printStackTrace();
        }
        
        bar.shutdown();
        kitchen.shutdown();
	}
}
