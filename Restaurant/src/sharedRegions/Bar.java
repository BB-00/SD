package sharedRegions;

import commInfra.*;
import entities.*;
import main.*;


public class Bar 
{
	/**
	 *	Number of students present in the restaurant
	 */
	
	private int numberOfStudentsAtRestaurant;
	
	/**
	 *  Number of pending requests to be answered by the waiter
	 */
	private int numberOfPendingRequests;
	
	/**
	 * Boolean variable used to store if a course was finished or not
	 */
	private boolean courseFinished;
	
	/**
	 * Queue of pending Requests
	 */
	private MemFIFO<Request> pendingServiceRequestQueue;
	
	/**
	 * Reference to the student threads
	 */
	private final Student [] students;
	
	/**
	 * Reference to the general repository
	 */
	private final GenRepos repo;
	
	/**
	 * Auxiliary variable to keep track of the id of the student whose request is being answered
	 */
	private int studentBeingAnswered;
	
	/**
	 * Array of booleans to keep track of the students which the waiter has already said goodbye
	 */
	private boolean[] studentsGreeted;
	
	/**
	 * Reference to the table
	 */
	private final Table tab;
	
	
	
	/**
	 * Bar instantiation
	 * 
	 * @param repo reference to the general repository 
	 */
	public Bar(GenRepos repo, Table tab) {
		//Initizalization of students thread
		students = new Student[ExecConsts.N];
		for(int i = 0; i < ExecConsts.N; i++ ) 
			students[i] = null;
		
		//Initialization of the queue of pending requests
		try {
			pendingServiceRequestQueue = new MemFIFO<> (new Request [ExecConsts.N * ExecConsts.M]);
		} catch (MemException e) {
			pendingServiceRequestQueue = null;
		    System.exit(1);
		}
	
		this.tab = tab;
		this.repo = repo;
		this.courseFinished = true;
		this.studentBeingAnswered = -1;
		
		this.studentsGreeted = new boolean[ExecConsts.N];
		for(int i = 0 ;i < ExecConsts.N; i++)
			studentsGreeted[i] = false;
	}
	
	/**
	 * @return Id of the student whose request is being answered
	 */
	public int getStudentBeingAnswered() {
		return studentBeingAnswered;
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
			int studentId = ((Student) Thread.currentThread()).getStudentID();
			Request request = new Request(studentId,'e');
			
			students[studentId] = ((Student) Thread.currentThread());
			students[studentId].setStudentState(StudentStates.GOING_TO_THE_RESTAURANT);
			
			numberOfStudentsAtRestaurant++;

			if(numberOfStudentsAtRestaurant == 1)
				tab.setFirstToArrive(studentId);
			else if (numberOfStudentsAtRestaurant == ExecConsts.N)
				tab.setLastToArrive(studentId);
			
			try {
				pendingServiceRequestQueue.write(request);
			} catch (MemException e) {
				e.printStackTrace();
			}

			numberOfPendingRequests++;
			
			students[studentId].setStudentState(StudentStates.TAKING_A_SEAT_AT_THE_TABLE);
			repo.updateStudentState(studentId, StudentStates.TAKING_A_SEAT_AT_THE_TABLE);

			repo.updateStudentSeat(numberOfStudentsAtRestaurant-1, studentId);
			
			notifyAll();
		}
		
		tab.seatAtTable();

	}
	
	/**
	 * Operation call The Waiter, called by the student who arrived first, to call the waiter
	 */
	public synchronized void callWaiter() {
		int studentId = ((Student) Thread.currentThread()).getStudentID();
		Request request = new Request(studentId,'c');
		
		try {
			pendingServiceRequestQueue.write(request);
		} catch (MemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		numberOfPendingRequests++;	
		notifyAll();
	}
	
	/**
	 * Operation signal the waiter
	 * 
	 * It is called by the last student to finish eating to signal waiter to bring next course
	 */
	public synchronized void signalWaiter() {
		int studentId = ((Student) Thread.currentThread()).getStudentID();
		Request request = new Request(studentId,'s');

		if(((Student) Thread.currentThread()).getStudentState() == StudentStates.PAYING_THE_MEAL) {		
			try {
				pendingServiceRequestQueue.write(request);
			} catch (MemException e) {
				e.printStackTrace();
			}

			numberOfPendingRequests++;	
			
			notifyAll();
			
		} else {
			courseFinished = true;		

			notifyAll();
		}
	}
	
	/**
     * Operation Exit, called by the students when they want to leave
     */
	public synchronized void exit() {
		int studentId = ((Student) Thread.currentThread()).getStudentID();
		Request request = new Request(studentId,'g');
		
		try {
			pendingServiceRequestQueue.write(request);
		} catch (MemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		numberOfPendingRequests++;

		notifyAll();
		
		students[studentId].setStudentState(StudentStates.GOING_HOME);
		repo.updateStudentState(studentId, StudentStates.GOING_HOME);
		
		while(studentsGreeted[studentId] == false) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		System.out.println("Student "+studentId+" is leaving!");
		
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
     * 				'e': client has arrived therefore need to be presented with menu
     * 				'c': waiter will take the order and deliver to the chef
     * 				'a': portion needs to be collected and delivered
     * 				's': bill needs to be prepared and presented to the client
     * 				'g': some student wants to leave and waiter needs to say bye bye
     */
	public synchronized char lookAround() {
		Request request;
		
		while(numberOfPendingRequests == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			request = pendingServiceRequestQueue.read();
			numberOfPendingRequests--;
		} catch (MemException e) {	
			e.printStackTrace();
			return 0;
		}		
		studentBeingAnswered = request.id;
		
		return request.type;
	}
	
	/**
     * Operation Say GoodBye, called by the waiter, to say goodbye to the students
     * 
     * @return true if there are no more students at the restaurant, false otherwise
     */
	public synchronized boolean sayGoodbye() {
		studentsGreeted[studentBeingAnswered] = true;

		notifyAll();
		
		//Update number of students at the restaurant
		numberOfStudentsAtRestaurant--;
		studentBeingAnswered = -1;
		
		repo.updateWaiterState(((Waiter) Thread.currentThread()).getWaiterState());
		
		if(numberOfStudentsAtRestaurant == 0)
			return true;
		return false;
		
	}
	
	/**
	 * Operation prepare the Bill
	 * 
	 * It is called the waiter to prepare the bill of the meal eaten by the students
	 */
	public synchronized void preprareBill() {
		((Waiter) Thread.currentThread()).setWaiterState(WaiterStates.PROCESSING_THE_BILL);
		repo.updateWaiterState(WaiterStates.PROCESSING_THE_BILL);
	}
	
	/**
	 * 
	 * Chef Operations
	 * 
	 */
	
	/**
	 * Operation alert the waiter
	 * 
	 * It is called by the chef to alert the waiter that a portion was dished
	 * 	For requests the chef id will be N+1
	 */
	public synchronized void alertWaiter() {
		while(!courseFinished)
		{
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Request request = new Request(ExecConsts.N+1,'a');
		
		try {
			pendingServiceRequestQueue.write(request);
		} catch (MemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		numberOfPendingRequests++;
		courseFinished = false;
		
		((Chef) Thread.currentThread()).setChefState(ChefStates.DELIVERING_THE_PORTIONS);
		repo.updateChefState(ChefStates.DELIVERING_THE_PORTIONS);
		
		notifyAll();
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
