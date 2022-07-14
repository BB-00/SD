package sharedRegions;

import commInfra.*;
import entities.*;

/**
 * 
 * Bar
 *
 * It is responsible for keeping track of the several requests that must be full
 * filled by the waiter Implemented as an implicit monitor. Public methods
 * executed in mutual exclusion Synchronization points include: Waiter waits for
 * pending requests if there are none When a student has to wait for the waiter
 * to say goodbye to him so he can leave the restaurant Chef must wait for
 * everybody to eat before alerting the waiter
 */
public class Bar {
	/**
	 * Number of students present in the restaurant
	 */
	private int numberOfStudentsAtRestaurant;

	/**
	 * Number of pending requests to be answered by the waiter
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
	private final Student[] students;

	/**
	 * Reference to the general repository
	 */
	private final GenRepos repos;

	/**
	 * Auxiliary variable to keep track of the id of the student whose request is
	 * being answered
	 */
	private int studentBeingAnswered;

	/**
	 * Array of booleans to keep track of the students which the waiter has already
	 * said goodbye
	 */
	private boolean[] studentsGreeted;

	/**
	 * Reference to the table
	 */
	private final Table table;

	/**
	 * Bar instantiation
	 * 
	 * @param repos reference to the general repository
	 */
	public Bar(GenRepos repos, Table table) {
		// Initizalization of students thread
		students = new Student[ExecConsts.N];
		for (int i = 0; i < ExecConsts.N; i++)
			students[i] = null;

		// Initialization of the queue of pending requests
		try {
			pendingServiceRequestQueue = new MemFIFO<>(new Request[ExecConsts.N * ExecConsts.M]);
		} catch (MemException e) {
			pendingServiceRequestQueue = null;
			System.exit(1);
		}

		this.table = table;
		this.repos = repos;
		this.courseFinished = true;
		this.studentBeingAnswered = -1;

		this.studentsGreeted = new boolean[ExecConsts.N];
		for (int i = 0; i < ExecConsts.N; i++)
			studentsGreeted[i] = false;
	}

	/**
	 * @return Id of the student whose request is being answered
	 */
	public int getStudentBeingAnswered() {
		return studentBeingAnswered;
	}

	/**
	 * @return number of students in the restaurant
	 */
	public int getNumberOfStudentsAtRestaurant() {
		return numberOfStudentsAtRestaurant;
	}

	/**
	 * 
	 * Student Operations
	 * 
	 */

	/**
	 * Operation Enter, is called by the students to signal that he as entered in
	 * the restaurant
	 */
	public void enter() {
		synchronized (this) {
			Student student = ((Student) Thread.currentThread());

			int studentID = student.getStudentID();

			students[studentID] = student;

			students[studentID].setStudentState(StudentStates.GOING_TO_THE_RESTAURANT);
			repos.updateStudentState(studentID, StudentStates.GOING_TO_THE_RESTAURANT);

			numberOfStudentsAtRestaurant++;

			if (numberOfStudentsAtRestaurant == 1)
				table.setFirstToArrive(studentID);
			else if (numberOfStudentsAtRestaurant == ExecConsts.N)
				table.setLastToArrive(studentID);

			Request request = new Request(studentID, 'e');
			try {
				pendingServiceRequestQueue.write(request);
			} catch (MemException e) {
				e.printStackTrace();
			}

			numberOfPendingRequests++;

			repos.updateStudentSeatAndState(studentID, numberOfStudentsAtRestaurant - 1, StudentStates.TAKING_A_SEAT_AT_THE_TABLE);

			notifyAll();
		}

		table.seatAtTable();

	}

	/**
	 * Operation call The Waiter, called by the student who arrived first, to call
	 * the waiter
	 */
	public synchronized void callWaiter() {
		int studentID = ((Student) Thread.currentThread()).getStudentID();
		Request request = new Request(studentID, 'c');
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
	 * It is called by the last student to finish eating to signal waiter to bring
	 * next course
	 */
	public synchronized void signalWaiter() {
		int studentID = ((Student) Thread.currentThread()).getStudentID();
		Request request = new Request(studentID, 's');

		if (((Student) Thread.currentThread()).getStudentState() == StudentStates.PAYING_THE_MEAL) {
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
		Student student = ((Student) Thread.currentThread());

		int studentID = student.getStudentID();

		Request request = new Request(studentID, 'g');

		try {
			pendingServiceRequestQueue.write(request);
		} catch (MemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		numberOfPendingRequests++;
		
		students[studentID].setStudentState(StudentStates.GOING_HOME);
		repos.updateStudentState(studentID, StudentStates.GOING_HOME);

		notifyAll();

		while (studentsGreeted[studentID] == false) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		//repos.updateStudentSeat(studentID, -1);
		
		// -------------- DEBUG ----------------------
		// System.out.println("Student " + studentID + " has left!");

	}

	/**
	 * 
	 * Waiter Operations
	 * 
	 */

	/**
	 * Operation Look Around, called by the waiter checks if there is any pending
	 * request, if not waits
	 * 
	 * @return char that represents type of service to be executed 'e': client has
	 *         arrived therefore need to be presented with menu 'c': waiter will
	 *         take the order and deliver to the chef 'a': portion needs to be
	 *         collected and delivered 's': bill needs to be prepared and presented
	 *         to the client 'g': some student wants to leave and waiter needs to
	 *         say bye bye
	 */
	public synchronized char lookAround() {
		Request request;

		while (numberOfPendingRequests == 0) {
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
		Waiter waiter = ((Waiter) Thread.currentThread());
		studentsGreeted[studentBeingAnswered] = true;

		notifyAll();

		// Update number of students at the restaurant
		numberOfStudentsAtRestaurant--;
		repos.updateSeatsAtLeaving(studentBeingAnswered);
		studentBeingAnswered = -1;

		waiter.setWaiterState(WaiterStates.APPRAISING_SITUATION);
		repos.updateWaiterState(WaiterStates.APPRAISING_SITUATION);

		if (numberOfStudentsAtRestaurant == 0)
			return true;
		return false;

	}

	/**
	 * Operation prepare the Bill
	 * 
	 * It is called the waiter to prepare the bill of the meal eaten by the students
	 */
	public synchronized void preprareBill() {
		Waiter waiter = ((Waiter) Thread.currentThread());
		waiter.setWaiterState(WaiterStates.PROCESSING_THE_BILL);
		repos.updateWaiterState(WaiterStates.PROCESSING_THE_BILL);
	}

	/**
	 * 
	 * Chef Operations
	 * 
	 */

	/**
	 * Operation alert the waiter
	 * 
	 * It is called by the chef to alert the waiter that a portion was dished For
	 * requests the chef id will be N+1
	 */
	public synchronized void alertWaiter() {
		Chef chef = ((Chef) Thread.currentThread());

		while (!courseFinished) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Request request = new Request(ExecConsts.N + 1, 'a');

		try {
			pendingServiceRequestQueue.write(request);
		} catch (MemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		numberOfPendingRequests++;
		courseFinished = false;

		chef.setChefState(ChefStates.DELIVERING_THE_PORTIONS);
		repos.updateChefState(ChefStates.DELIVERING_THE_PORTIONS);

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
	 * 
	 * @param id   of the request
	 * @param type of the request
	 */
	public Request(int id, char type) {
		this.id = id;
		this.type = type;
	}
}
