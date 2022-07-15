package clientSide.main;

import clientSide.entities.Student;
import clientSide.entities.StudentStates;
import clientSide.stubs.*;
import commInfra.ExecConsts;
import genclass.GenericIO;

/**
 *  Client side of the Restaurant problem (student).
 *
 *	Implementation of a client-server model of type 2 (server replication).
 *	Communication is based on a communication channel under the TCP protocol.
 */
public class StudentMain {
	
	/**
	 *  Main method.
	 *
	 *    @param args runtime arguments
	 *        args[0] - name of the platform where is located the bar server
	 *        args[1] - port number for listening to service requests
	 *        args[2] - name of the platform where is located the table server
	 *        args[3] - port number for listening to service requests
     *		  args[4] - name of the platform where is located the general repository server
	 *        args[5] - port number for listening to service requests
	 */
	public static void main(String[] args) {
		
		String barServerHostName;						// name of the platform where is located the bar server
		int barServerPortNum = -1; 						// port number for listening to service requests
		String tableServerHostName;						// name of the platform where is located the table server
		int tableServerPortNum = -1;					// port number for listening to service requests
		String genReposServerHostName;					// name of the platform where is located the general repository server
		int genReposServerPortNum = -1;					// port number for listening to service requests
		BarStub bar;									// remote reference to the bar stub
		TableStub table;								// remote reference to the table stub
		GenReposStub genRepos; 							// remote reference to the general repository stub

		Student[] students = new Student[ExecConsts.N];	// Student Threads
	
		
		/* getting problem runtime parameters */
	    if (args.length != 6) {
	    	GenericIO.writelnString("Wrong number of parameters!");
	        System.exit (1);
	    }
	    
	    barServerHostName = args[0];
	    try {
	    	barServerPortNum = Integer.parseInt (args[1]);
	    } catch (NumberFormatException e) {
	    	GenericIO.writelnString ("args[1] is not a number!");
	        System.exit (1);
	    }
	    if ((barServerPortNum < 4000) || (barServerPortNum >= 65536)) {
	    	GenericIO.writelnString ("args[1] is not a valid port number!");
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
	    
	    genReposServerHostName = args[4];
	    try {
	    	genReposServerPortNum = Integer.parseInt (args[5]);
	    } catch (NumberFormatException e) {
	    	GenericIO.writelnString ("args[5] is not a number!");
	        System.exit (1);
	    }
	    if ((genReposServerPortNum < 4000) || (genReposServerPortNum >= 65536)) {
	    	GenericIO.writelnString ("args[5] is not a valid port number!");
	        System.exit (1);
	    }
	    
	    
		//Initialization
		bar = new BarStub(barServerHostName, barServerPortNum);
		table = new TableStub(tableServerHostName, tableServerPortNum);
		genRepos = new GenReposStub(genReposServerHostName, genReposServerPortNum);
		
		for(int i=0; i<ExecConsts.N; i++){
            students[i] = new Student("Student_"+i, i, StudentStates.GOING_TO_THE_RESTAURANT, bar, table);
        }
		
		
		// Start of simulation
		for(int i=0; i<ExecConsts.N; i++){
            students[i].start();
            GenericIO.writelnString("Student thread "+i+" started!");
        }
		
		
		for (int i=0; i<ExecConsts.N; i++){
			try {
                students[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
		
		bar.shutdown();
		table.shutdown();
		genRepos.shutdown();
		
		for(int i=0; i<ExecConsts.N; i++){
            students[i].start();
            GenericIO.writelnString("Student thread "+i+" finished!");
        }
	}
}
