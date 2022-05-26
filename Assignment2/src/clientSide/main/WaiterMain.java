package clientSide.main;

import clientSide.entities.Chef;
import clientSide.entities.Student;
import clientSide.entities.Waiter;
import clientSide.stubs.BarStub;
import clientSide.stubs.GenReposStub;
import clientSide.stubs.KitchenStub;
import clientSide.stubs.TableStub;
import commInfra.ExecConsts;
import genclass.GenericIO;


public class WaiterMain {
	
	public static void main(String[] args) {
		
		
		String barServerHostName;                               // name of the platform where is located the barber shop server
	    int barServerPortNum = -1;                             // port number for listening to service requests
	    String tableServerHostName;
	    int tableServerPortNum = -1;
	    String genReposServerHostName;                                 // name of the platform where is located the general repository server
	    int genReposServerPortNum = -1;                               // port number for listening to service requests                                    
	    String kitchenServerHostName;                                 // name of the platform where is located the kitchen server
	    int kitchenServerPortNum = -1;                               // port number for listening to service requests
	    BarStub bar;													// remote reference to the bar
		TableStub table;
		KitchenStub kitchen;
		Waiter waiter;
		GenReposStub genReposStub;								// remote reference to the general repository
	
	
		/* getting problem runtime parameters */
		
	      if (args.length != 8)
	         { GenericIO.writelnString("Wrong number of parameters!");
	           System.exit (1);
	         }
	      barServerHostName = args[0];
	      try
	      { barServerPortNum = Integer.parseInt (args[1]);
	      }
	      catch (NumberFormatException e)
	      { GenericIO.writelnString ("args[1] is not a number!");
	        System.exit (1);
	      }
	      if ((barServerPortNum < 4000) || (barServerPortNum >= 65536))
	         { GenericIO.writelnString ("args[1] is not a valid port number!");
	           System.exit(1);
	         }
	      
	      tableServerHostName = args[2];
	      try {
	    	  tableServerPortNum = Integer.parseInt(args[3]);
	      } catch(NumberFormatException e) {
	    	  GenericIO.writelnString("args[3] is not a valid port number!");
	    	  System.exit(1);
	      }
	      if ((tableServerPortNum < 4000) || (tableServerPortNum >= 65536)) {
		    	GenericIO.writelnString ("args[3] is not a valid port number!");
		        System.exit (1);
		  }
	      
	      kitchenServerHostName = args[4];
	      try {
	    	  kitchenServerPortNum = Integer.parseInt(args[5]);
	      } catch(NumberFormatException e) {
	    	  GenericIO.writelnString("args[5] is not a valid port number!");
	    	  System.exit(1);
	      }
	      if ((kitchenServerPortNum < 4000) || (kitchenServerPortNum >= 65536)) {
		    	GenericIO.writelnString ("args[5] is not a valid port number!");
		        System.exit (1);
		  }
	      
	      genReposServerHostName = args[6];
	      try
	      { genReposServerPortNum = Integer.parseInt (args[7]);
	      }
	      catch (NumberFormatException e)
	      { GenericIO.writelnString ("args[7] is not a number!");
	        System.exit (1);
	      }
	    if ((genReposServerPortNum < 4000) || (genReposServerPortNum >= 65536)) {
	    	GenericIO.writelnString ("args[7] is not a valid port number!");
	        System.exit (1);
	    }
		
		//Initialization
		
		bar = new BarStub(barServerHostName, barServerPortNum);
		table = new TableStub(tableServerHostName, tableServerPortNum);
		kitchen = new KitchenStub(kitchenServerHostName, kitchenServerPortNum);
		genReposStub = new GenReposStub(genReposServerHostName, genReposServerPortNum);
		
		
		waiter = new Waiter("Waiter", kitchen, bar, table);
		
		
		// Start of simulation
		waiter.start();
		
		GenericIO.writelnString();
		while(waiter.isAlive()) {
			
//			kitchen.endOperation();
//			bar.endOperation();
//			table.endOperation();
			Thread.yield();
			
			try {
				waiter.join();
		    } catch (InterruptedException e) {
		        System.out.print("Error occured while executing Waiter");
		    }
			GenericIO.writelnString("The waiter has terminated.");
		}
		GenericIO.writelnString();
		kitchen.shutdown();
		table.shutdown();
		bar.shutdown();
		genReposStub.shutdown();

	}

}
