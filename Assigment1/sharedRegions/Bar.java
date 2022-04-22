package sharedRegions;

import entities.Student;
import entities.StudentStates;
import entities.Waiter;
import entities.WaiterStates;

public class Bar {
    
    private int numberOfPendingRequests;

    private int numberOfStudentsInRestaurant;

    /**
	 * ID of the student that is being answered by the waiter
	 */
	private int studentBeingAnsweredID;

    /**
     * Array to check if student has been greeted
     */
    private boolean [] studentsGreeted;

    /**
     * Reference to the table
     */
    private final Table table;
    
    /**
     * Reference to General Repositories
     */
    private final GenRepos repos;
    
    private FIFO <Request> pendingServiceRequestQueue;

    public Bar(GenRepos repos, Table table){
    	this.numberOfPendingRequests = 0;
    	this.numberOfStudentsInRestaurant = 0;
    	this.table = table;
    	this.repos = repos;
    }
    
    
    /**
     * 
     * Student Operations
     *
     */
    
    /**
     * Operation Enter, is called by the students to signal that he as entered in the restaurant
     */
    public void enter() {
    	synchronized(this) {
    		//get student id
    		int studentID = ((Student) Thread.currentThread()).getStudentID();
    		
    		//Update Student State
			((Student) Thread.currentThread()).setStudentState(StudentStates.GOING_TO_THE_RESTAURANT);
			repos.updateStudentState(studentID, StudentStates.GOING_TO_THE_RESTAURANT);
			
			//update number of students in the restaurant
			numberOfStudentsInRestaurant++;
			
			//Register first and last student to arrive
			if(numberOfStudentsInRestaurant == 1) table.setFirstToArrive(studentID);
			else if (numberOfStudentsInRestaurant == 7)
				table.setLastToArrive(studentID);
			
			//Add a new request to queue and update number of request
			try {
				pendingServiceRequestQueue.write(new Request(studentID, 'c'));
			} catch (MemException e) {
				e.printStackTrace();
			}
			numberOfPendingRequests++;
			
			//Update Student State
			((Student) Thread.currentThread()).setStudentState(StudentStates.TAKING_A_SEAT_AT_THE_TABLE);
			repos.updateStudentState(studentID, StudentStates.TAKING_A_SEAT_AT_THE_TABLE);
			
			//Register seat number
			repos.updateStudentSeat(studentID, numberOfStudentsInRestaurant-1);
			
			//Signal waiter of pending request
			notifyAll();
    	}
    	
    	//Seat at table
    	table.takeASeatAtTable();
    }
    
    /**
     * Operation Exit, called by the students when they want to leave
     */
    public void exit() {
    	//get student id
    	int studentID = ((Student) Thread.currentThread()).getStudentID();
    	
    	// create new request to say goodbye
    	Request request = new Request(studentID, 'g');
    	
    	//Add a new request to queue and update number of requests
    	try {
			pendingServiceRequestQueue.write(request);
		} catch (MemException e) {
			e.printStackTrace();
		}
    	numberOfPendingRequests++;
    	
    	//notify waiter that is ready to leave
    	notifyAll();
    	
    	//Update Student State
		((Student) Thread.currentThread()).setStudentState(StudentStates.GOING_HOME);
		repos.updateStudentState(studentID, StudentStates.GOING_HOME);
		
		//Block until waiter salutes them
		while(studentsGreeted[studentID] == false) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("Student "+studentID+" wants to leave");
		}
    }
    
    /**
     * 
     * Waiter Operations
     *
     */
    
    /**
     * Operation Look Around, called by the waiter checks if there is any pending request, if not waits
     * 
     * @return char that represents type of service to be executed
     * 				'c': client has arrived therefore need to be presented with menu
     * 				'o': waiter will take the order and deliver to the chef
     * 				'p': portion needs to be collected and delivered
     * 				'b': bill needs to be prepared and presented to the client
     * 				'g': some student wants to leave and waiter needs to say bye bye
     */
    public synchronized char lookAround() {
    	Request request;
    	
    	//waiter blocks if there are no pending requests
    	while(numberOfPendingRequests == 0) {
    		try {
    			wait();
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    	
    	try {
    		//take request from queue and update number of pending requests
    		request = pendingServiceRequestQueue.read();
    		numberOfPendingRequests--;
    	} catch (MemException e) {
    		e.printStackTrace();
    		return 0;
    	}
    	
    	//Register student being answered
    	System.out.println("Attending Student "+request.id);
    	studentBeingAnsweredID = request.id;
    	
    	return request.type;
    }
    
    /**
     * Operation Prepare The Bill, called by waiter to prepare the bill of the meal
     */
    public synchronized void prepareTheBill() {
    	//Update Waiter State
    	((Waiter) Thread.currentThread()).set_state(WaiterStates.PROCESSING_THE_BILL);
    	repos.updateWaiterState(WaiterStates.PROCESSING_THE_BILL);
    }
    
    
    public synchronized boolean sayGoodBye() {
    	//update value of students greeted
    	studentsGreeted[studentBeingAnsweredID] = true;
    	
    	//wake student that is waiting to be greeted
    	notifyAll();
    	
    	//Update number of students at restaurant
    	numberOfStudentsInRestaurant--;
    	studentBeingAnsweredID = -1;
    	
    	////Update Waiter State
    	repos.updateWaiterState(((Waiter) Thread.currentThread()).get_state());
    	
    	//if there are no more students in restaurant terminate
    	if(numberOfStudentsInRestaurant == 0) return true;
    	
    	return false;
    }
}

/**
 * 
 * Request Data type
 *
 */
class Request {
	
	/**
	 * Id of the author of the request
	 */
	public int id;
	
	/**
	 * Request type
	 */
	public char type;
	
	
	/**
	 * Request Instantiation
	 * @param id of the request
	 * @param type of the request
	 */
	public Request(int id, char type)
	{
		this.id = id;
		this.type = type;
	}
}
