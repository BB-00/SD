package serverSide.objects;

import java.rmi.RemoteException;

import clientSide.entities.*;
import commInfra.*;
import interfaces.*;
import serverSide.main.*;

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
public class Bar implements BarInterface {
	/**
	 * Number of entity groups requesting the shutdown.
	 */
	private int nEntities;

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
	 * Reference to the general repository stub
	 */
	private final GenReposInterface repos;

	/**
	 * Auxiliary variable to keep track of the id of the student whose request is
	 * being answered
	 */
	private int studentBeingAnswered;

	/**
	 * Variable to check which students states
	 */
	private final int[] studentsState;

	/**
	 * Array of booleans to keep track of the students which the waiter has already
	 * said goodbye
	 */
	private boolean[] studentsGreeted;

	/**
	 * Reference to the table stub
	 */
	private final TableInterface tab;

	/**
	 * Bar instantiation
	 * 
	 * @param repos reference to the general repository to the table stub
	 * @param tab   referecen the table stub
	 */
	public Bar(GenReposInterface repos, TableInterface tab) {
		this.nEntities = 0;

		// Initialization of the queue of pending requests
		try {
			this.pendingServiceRequestQueue = new MemFIFO<>(new Request[ExecConsts.N * ExecConsts.M]);
		} catch (MemException e) {
			this.pendingServiceRequestQueue = null;
			System.exit(1);
		}

		this.tab = tab;
		this.repos = repos;
		this.courseFinished = true;
		this.studentBeingAnswered = -1;

		this.studentsState = new int[ExecConsts.N];
		this.studentsGreeted = new boolean[ExecConsts.N];
		for (int i = 0; i < ExecConsts.N; i++) {
			this.studentsGreeted[i] = false;
			this.studentsState[i] = StudentStates.GOING_TO_THE_RESTAURANT;
		}
	}

	/**
	 * @return ID of the student whose request is being answered
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	public int getStudentBeingAnswered() throws RemoteException {
		return studentBeingAnswered;
	}

	/**
	 * @return number of students in the restaurant
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	public int getNumberOfStudentsAtRestaurant() throws RemoteException {
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
	 * 
	 * @param studentID
	 * @return state of the student
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	@Override
	public int enter(int studentID) throws RemoteException {
		synchronized (this) {

			// if (student.getStudentState() != StudentStates.GOING_TO_THE_RESTAURANT) {
			// students[studentID].setStudentState(StudentStates.GOING_TO_THE_RESTAURANT);
			// student.setStudentState(StudentStates.GOING_TO_THE_RESTAURANT);
			// repos.updateStudentState(studentID, StudentStates.GOING_TO_THE_RESTAURANT);
			// }
			repos.updateStudentState(studentID, StudentStates.GOING_TO_THE_RESTAURANT);
			studentsState[studentID] = StudentStates.GOING_TO_THE_RESTAURANT;

			numberOfStudentsAtRestaurant++;

			if (numberOfStudentsAtRestaurant == 1)
				tab.setFirstToArrive(studentID);
			else if (numberOfStudentsAtRestaurant == ExecConsts.N)
				tab.setLastToArrive(studentID);

			Request request = new Request(studentID, 'e');
			try {
				pendingServiceRequestQueue.write(request);
			} catch (MemException e) {
				e.printStackTrace();
			}

			numberOfPendingRequests++;

			// if (student.getStudentState() != StudentStates.TAKING_A_SEAT_AT_THE_TABLE) {
			// students[studentID].setStudentState(StudentStates.TAKING_A_SEAT_AT_THE_TABLE);
			// student.setStudentState(StudentStates.TAKING_A_SEAT_AT_THE_TABLE);
			// repos.updateStudentState(studentID,
			// StudentStates.TAKING_A_SEAT_AT_THE_TABLE);
			// }
			studentsState[studentID] = StudentStates.TAKING_A_SEAT_AT_THE_TABLE;
			//repos.updateStudentState(studentID, StudentStates.TAKING_A_SEAT_AT_THE_TABLE);
			repos.updateStudentSeatAndState(studentID, numberOfStudentsAtRestaurant-1, StudentStates.TAKING_A_SEAT_AT_THE_TABLE);

			notifyAll();
		}

		tab.seatAtTable(studentID);

		return studentsState[studentID];

	}

	/**
	 * Operation call The Waiter, called by the student who arrived first, to call
	 * the waiter
	 * 
	 * @param studentID
	 * @return state of the student
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	@Override
	public synchronized void callWaiter(int studentID) throws RemoteException {

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
	 * 
	 * @param studentID
	 * @param studentState
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	@Override
	public synchronized void signalWaiter(int studentID, int studentState) throws RemoteException {

		Request request = new Request(studentID, 's');
		studentsState[studentID] = studentState;

		if (studentsState[studentID] == StudentStates.PAYING_THE_MEAL) {
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
	 * 
	 * @param studentID
	 * @return state of the student
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	@Override
	public synchronized int exit(int studentID) throws RemoteException {

		Request request = new Request(studentID, 'g');

		try {
			pendingServiceRequestQueue.write(request);
		} catch (MemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		numberOfPendingRequests++;

		//repos.updateStudentSeat(studentID, -1);

		// if (student.getStudentState() != StudentStates.GOING_HOME) {
		// students[studentID].setStudentState(StudentStates.GOING_HOME);
		// student.setStudentState(StudentStates.GOING_HOME);
		// repos.updateStudentState(studentID, StudentStates.GOING_HOME);
		// }
		studentsState[studentID] = StudentStates.GOING_HOME;
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
		// -------------- DEBUG ----------------------
		// System.out.println("Student "+studentID+" has left!");

		return studentsState[studentID];
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
	 *         
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	@Override
	public synchronized char lookAround() throws RemoteException {
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
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	@Override
	public synchronized boolean sayGoodbye() throws RemoteException {
		studentsGreeted[studentBeingAnswered] = true;

		notifyAll();

		// Update number of students at the restaurant
		numberOfStudentsAtRestaurant--;
		repos.updateSeatsAtLeaving(studentBeingAnswered);
		studentBeingAnswered = -1;

		repos.updateWaiterState(WaiterStates.APPRAISING_SITUATION);
		
		if (numberOfStudentsAtRestaurant == 0)
			return true;
		return false;

	}

	/**
	 * Operation prepare the Bill
	 * 
	 * It is called the waiter to prepare the bill of the meal eaten by the students
	 * 
	 * @return state of the waiter
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	@Override
	public synchronized int preprareBill() throws RemoteException {
		// if (waiter.getWaiterState() != WaiterStates.PROCESSING_THE_BILL) {
		// waiter.setWaiterState(WaiterStates.PROCESSING_THE_BILL);
		// repos.updateWaiterState(WaiterStates.PROCESSING_THE_BILL);
		// }

		repos.updateWaiterState(WaiterStates.PROCESSING_THE_BILL);

		return WaiterStates.PROCESSING_THE_BILL;
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
	 * 
	 * @return state of the chef
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	@Override
	public synchronized int alertWaiter() throws RemoteException {

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

		// if (chef.getChefState() != ChefStates.DELIVERING_THE_PORTIONS) {
		// chef.setChefState(ChefStates.DELIVERING_THE_PORTIONS);
		// repos.updateChefState(ChefStates.DELIVERING_THE_PORTIONS);
		// }
		repos.updateChefState(ChefStates.DELIVERING_THE_PORTIONS);

		notifyAll();

		return ChefStates.DELIVERING_THE_PORTIONS;
	}

	/**
	 * Operation server shutdown.
	 *
	 * New operation.
	 * 
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	@Override
	public synchronized void shutdown() throws RemoteException {
		nEntities += 1;
		if (nEntities >= 3)
			BarMain.shutdown();
		notifyAll();
	}
}
