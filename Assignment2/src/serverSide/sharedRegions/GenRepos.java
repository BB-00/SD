package serverSide.sharedRegions;

import genclass.GenericIO;
import genclass.TextFile;
import java.util.Objects;
import java.util.Arrays;

import serverSide.main.*;
import clientSide.entities.*;
import commInfra.*;



public class GenRepos {
	/**
	 *   Number of entity groups requesting the shutdown.
	 */
	private int nEntities;
	
	/**
	 * Name of the log file
	 */
	private String logFileName;

	/**
	 * States of the Students
	 */
	private int [] studentStates;
	
	/**
	 * Seats of the Students;
	 */
	private int [] seats;
	
	/**
	 * State of the Waiter
	 */
	private int waiterState;
	
	/**
	 * State of the Chef
	 */
	private int chefState;

	public GenRepos(){
		this.nEntities =  0;
		
		this.logFileName = "log.txt";
		
		//set initial states
		this.studentStates = new int[ExecConsts.N];
		this.seats = new int[ExecConsts.N];
		for(int i=0 ; i<ExecConsts.N ; i++) {
			this.studentStates[i] = StudentStates.GOING_TO_THE_RESTAURANT;
			this.seats[i] = -1;
		}
		this.waiterState = WaiterStates.APPRAISING_SITUATION;
		this.chefState = ChefStates.WAITING_FOR_AN_ORDER;
		
		reportInitialStatus();
	}
	
	/**
	 * Update Student State
	 * 
	 * @param studentID
	 * @param studentState
	 */
	public synchronized void updateStudentState(int studentID, int studentState) {
		studentStates[studentID] = studentState;
		reportStatus();
	}
	
	/**
	 * Set Student Seat
	 * 
	 * @param studentID
	 * @param studentSeat
	 */
	public synchronized void updateStudentSeat(int studentID, int studentSeat) {
		seats[studentID] = studentSeat;
		reportStatus();
	}
	
	/**
	 * Get Student Seat
	 * 
	 * @param studentID
	 * @return number of seat
	 */
	public synchronized int getStudentSeat(int studentSeat){
		for(int i=0 ; i<seats.length ; i++) {
			if (seats[i] == studentSeat) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Update Waiter State
	 * 
	 * @param newWaiterState
	 */
	public synchronized void updateWaiterState(int waiterState) {
		this.waiterState = waiterState;
		reportStatus();
	}
	
	/**
	 * Update Chef State
	 * 
	 * @param newChefState
	 */
	public synchronized void updateChefState(int chefState) {
		this.chefState = chefState;
		reportStatus();
	}
	
	/**
	 *  Print header.
	 */
	private void reportInitialStatus(){
	    TextFile log = new TextFile();                      // instantiation of a text file handler

	    if (!log.openForWriting(".", logFileName)) {
	    	GenericIO.writelnString("The operation of creating the file "+logFileName+" failed!");
	        System.exit(1);
	    }
	    log.writelnString("\t\t\t\t\t\t  The Restaurant - Description of the internal state");
	    log.writelnString("\nChef\tWaiter\tStu0\tStu1\tStu2\tStu3\tStu4\tStu5\tStu6\tNCourse\tNPortion\t\t\t\tTable\n");
	    log.writelnString("\nState\tState\tState\tState\tState\tState\tState\tState\tState\t\t\t\t\t Seat0\tSeat1\tSeat2\tSeat3\tSeat4\tSeat5\tSeat6\n");
	    if (!log.close()) {
	    	GenericIO.writelnString("The operation of closing the file "+logFileName+" failed!");
	        System.exit(1);
	    }
	    reportStatus();
	}
	
	/**
	 *  Write the body of the logging file.
	 *
	 */
	private void reportStatus() {
		TextFile log = new TextFile();                      // instantiation of a text file handler

	    String lineStatus = "";                              // state line to be printed

	    if (!log.openForAppending(".", logFileName)) {
	    	GenericIO.writelnString("The operation of opening for appending the file "+logFileName+" failed!");
	        System.exit(1);
	    }
	    
	    switch(chefState) { 
	    	case ChefStates.WAITING_FOR_AN_ORDER:
	    		lineStatus += "WAFOR\t";
	            break;
	        case ChefStates.PREPARING_THE_COURSE:
	        	lineStatus += "PRPCS\t";
	            break;
	        case ChefStates.DISHING_THE_PORTIONS:
	        	lineStatus += "DSHPT\t";
	            break;
	        case ChefStates.DELIVERING_THE_PORTIONS:
	        	lineStatus += "DLVPT\t";
	            break;
	        case ChefStates.CLOSING_SERVICE:
	        	lineStatus += "CLSSV\t";
	            break;
        }
	    
	    switch(waiterState) { 
	    	case WaiterStates.APPRAISING_SITUATION:
	    		lineStatus += "APPST\t";
	            break;
	        case WaiterStates.PRESENTING_THE_MENU:
	        	lineStatus += "PRSMN\t";  
	            break;
	        case WaiterStates.TAKING_THE_ORDER:
	        	lineStatus += "TKODR\t";
	            break;
	        case WaiterStates.PLACING_THE_ORDER:
	        	lineStatus += "PCODR\t";
	            break;
	        case WaiterStates.WAITING_FOR_PORTION:
	        	lineStatus += "WTFPT\t";
	            break;
	        case WaiterStates.PROCESSING_THE_BILL:
	        	lineStatus += "PRCBL\t";
	            break;                                              
	        case WaiterStates.RECEIVING_PAYMENT:
	        	lineStatus += "RECPM\t";
	            break;                                            
	    }
	    
	    for(int i=0 ; i<ExecConsts.N ; i++) {
	    	switch(studentStates[i]) { 
	        	case StudentStates.GOING_TO_THE_RESTAURANT:
	        		lineStatus += "GGTRT\t";
	                break;
	            case StudentStates.TAKING_A_SEAT_AT_THE_TABLE:
	            	lineStatus += "TKSTT\t";
	                break;    
	            case StudentStates.SELECTING_THE_COURSES:
	            	lineStatus += "SELCS\t";
	                break; 
	            case StudentStates.ORGANIZING_THE_ORDER:
	            	lineStatus += "OGODR\t";
	                break; 
	            case StudentStates.CHATTING_WITH_COMPANIONS:
	            	lineStatus += "CHTWC\t";
	                break; 
	            case StudentStates.PAYING_THE_MEAL:
	            	lineStatus += "PYTML\t";
	                break; 
	            case StudentStates.ENJOYING_THE_MEAL:
	            	lineStatus += "EJTML\t";
	                break; 
	            case StudentStates.GOING_HOME:
	            	lineStatus += "GGHOM\t";
	                break; 
	    	}
	    }
	    
	    lineStatus += ExecConsts.M+"\t\t"+ExecConsts.N+"\t";
	        
	    for(int i=0 ; i<ExecConsts.N ; i++) {
	    	lineStatus += "\t";
	        if(seats[i] == -1) {
	        	lineStatus += " -";
	        } else {
	            lineStatus += " "+seats[i];
	        }
	    }
	     
	    log.writelnString (lineStatus);
	    if (!log.close()) {
	    	GenericIO.writelnString("The operation of closing the file "+logFileName+" failed!");
	        System.exit (1);
	    }
	}
	   
	/**
	 *   Operation server shutdown.
	 *
	 *   New operation.
	 */
	public synchronized void shutdown() {
		nEntities += 1;
	    if (nEntities >= 3)
	    	GenReposMain.waitConnection = false;
	}
}
