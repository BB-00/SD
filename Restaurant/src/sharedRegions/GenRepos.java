package sharedRegions;

import genclass.GenericIO;
import genclass.TextFile;
import java.util.Objects;


import entities.*;
import main.*;

public class GenRepos {

	/**
	 * Name of the log file
	 */
	private final String file;

	/**
	 * States of the Students
	 */
	private int [] studentStates;
	
	/**
	 * Seats of the Students;
	 */
	private int [] studentSeats;
	
	/**
	 * State of the Waiter
	 */
	private int waiterState;
	
	/**
	 * State of the Chef
	 */
	private int chefState;
	

	public GenRepos(String file){
		this.file = file;
		
		//set initial states
		studentStates = new int[ExecConsts.N];
		studentSeats = new int[ExecConsts.N];
		for(int i=0 ; i<ExecConsts.N ; i++) {
			studentStates[i] = StudentStates.GOING_TO_THE_RESTAURANT;
			studentSeats[i] = -1;
		}
		waiterState = WaiterStates.APPRAISING_SITUATION;
		chefState = ChefStates.WAITING_FOR_AN_ORDER;
		
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
		studentSeats[studentID] = studentSeat;
	}
	
	/**
	 * Update Waiter State
	 * 
	 * @param newWaiterState
	 */
	public synchronized void updateWaiterState(int newWaiterState) {
		waiterState = newWaiterState;
		reportStatus();
	}
	
	/**
	 * Update Chef State
	 * 
	 * @param newChefState
	 */
	public synchronized void updateChefState(int newChefState) {
		chefState = newChefState;
		reportStatus();
	}
	
	/**
	   *  Print header.
	   */
	   private void reportInitialStatus()
	   {
	      TextFile log = new TextFile();                      // instantiation of a text file handler

	      if (!log.openForWriting(".", file))
	         { GenericIO.writelnString("The operation of creating the file " + file + " failed!");
	           System.exit(1);
	         }
	      log.writelnString("\t\t\t\t\t\t  The Restaurant - Description of the internal state");
	      //log.writelnString ("\nNumber of iterations = " + nIter + "\n");
	      log.writelnString("\n\tChef\tWaiter\tStu0\tStu1\tStu2\tStu3\tStu4\tStu5\tStu6\tNCourse\tNPortion\t\t\tTable\n");
	      log.writelnString("\n\tState\tState\tState\tState\tState\tState\tState\tState\tState\t\t\t\tSeat0\tSeat1\tSeat2\tSeat3\tSeat4\tSeat5\tSeat6\n");
	      if (!log.close())
	         { GenericIO.writelnString("The operation of closing the file " + file + " failed!");
	           System.exit(1);
	         }
	      reportStatus();
	   }
	  /**
	   *  Write the body of the logging file.
	   *
	   */
	   private void reportStatus()
	   {
	        TextFile log = new TextFile();                      // instantiation of a text file handler

	        String lineStatus = "";                              // state line to be printed

	        if (!log.openForAppending(".", file))
	           { GenericIO.writelnString("The operation of opening for appending the file " + file + " failed!");
	             System.exit(1);
	           }
	        switch(chefState){ 
	            case ChefStates.WAITING_FOR_AN_ORDER:  lineStatus += "\tWAFOR";
	                                               break;
	            case ChefStates.PREPARING_THE_COURSE: lineStatus += "\tPRPCS";
	                                               break;
	            case ChefStates.DISHING_THE_PORTIONS:      lineStatus += "\tDSHPT";
	                                               break;
	            case ChefStates.DELIVERING_THE_PORTIONS:    lineStatus += "\tDLVPT";
	                                               break;
	            case ChefStates.CLOSING_SERVICE:    lineStatus += "\tCLSSV";
	                                               break;
	          }
	        switch(waiterState){ 
	            case WaiterStates.APPRAISING_SITUATION:  lineStatus += "\tAPPST";
	                                               break;
	            case WaiterStates.PRESENTING_THE_MENU: lineStatus += "\tPRSMN";  
	                                               break;
	            case WaiterStates.TAKING_THE_ORDER:      lineStatus += "\tTKODR";
	                                               break;
	            case WaiterStates.PLACING_THE_ORDER:    lineStatus += "\tPCODR";
	                                               break;
	            case WaiterStates.WAITING_FOR_PORTION:    lineStatus += "\tWTFPT";
	                                               break;
	            case WaiterStates.PROCESSING_THE_BILL:    lineStatus += "\tPRCBL";
	                                               break;                                              
	            case WaiterStates.RECEIVING_PAYMENT:    lineStatus += "\tRECPM";
	                                               break;                                            
	          }
	        for(int i = 0; i < ExecConsts.N; i++){
	            switch (studentStates[i]){ 
	                case StudentStates.GOING_TO_THE_RESTAURANT:   lineStatus += "\tGGTRT";
	                                            break;
	                case StudentStates.TAKING_A_SEAT_AT_THE_TABLE: lineStatus += "\tTKSTT";
	                                              break;    
	                case StudentStates.SELECTING_THE_COURSES: lineStatus += "\tSELCS";
	                                              break; 
	                case StudentStates.ORGANIZING_THE_ORDER: lineStatus += "\tOGODR";
	                                              break; 
	                case StudentStates.CHATTING_WITH_COMPANIONS: lineStatus += "\tCHTWC";
	                                              break; 
	                case StudentStates.PAYING_THE_MEAL: lineStatus += "\tPYTML";
	                                              break; 
	                case StudentStates.ENJOYING_THE_MEAL: lineStatus += "\tEJTML";
	                                              break; 
	                case StudentStates.GOING_HOME: lineStatus += "\tGGHOM";
	                                              break; 
	                
	            }
	        }
	        lineStatus += "\t"+ExecConsts.M+"\t"+ExecConsts.N;
	        
	        for(int i=0 ; i<ExecConsts.N ; i++)
	        {
	            lineStatus += "\t";
	            if(studentSeats[i] == -1){
	                lineStatus += "-";
	            }
	            else
	            {
	                lineStatus += studentSeats[i];
	            }
	        }
	     
	      log.writelnString (lineStatus);
	      if (!log.close ())
	         { GenericIO.writelnString ("The operation of closing the file " + file + " failed!");
	           System.exit (1);
	         }
	   }
}