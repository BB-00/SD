package sharedRegions;

import entities.*;
import commInfra.*;;

/**
 * Table
 * 
 * It is responsible for keeping track of the courses being eaten. Implemented
 * as an implicit monitor Public methods executed in mutual exclusion
 * Synchronization points for the waiter include: If saluting a student waiter
 * must wait for him to seat at table and then wait for him to read menu Waiter
 * has to wait for first student to arrive to describe him the order Waiter
 * blocks waiting for student to pay the bill Synchronization points for the
 * student include: Student waits for waiter to bring menu specifically to him
 * First student to arrive blocks if everybody has not chosen yet and while
 * companions are not describing their choices First student to arrive waits for
 * waiter to come with the pad If some student is informing about his choice,
 * then a student must wait for his companion to finish telling his preference
 * Students must wait that everybody is served before they can start eating When
 * a student finishes his course must wait for his companions to finish Student
 * that was last to eat must wait for his companions to woken up before he can
 * signal waiter to bring next course Last student to arrive must wait for
 * waiter to bring him the bill
 * 
 */
public class Table {
	/**
	 * Array of Students Threads
	 */
	private final Student[] students;

	/**
	 * number of Students that are seated at the table
	 */
	private int numberOfStudentsAtTable;

	/**
	 * number of Students that have already chosen the courses
	 */
	private int numberOfStudentsThatHasChosen;

	/**
	 * number of Students that have already finish eat a course
	 */
	private int numberOfStudentsThatHasFinishEat;

	/**
	 * number of Students that have already been served on specific course
	 */
	private int numberOfStudentsServed;

	/**
	 * number of Courses that have already been eaten
	 */
	private int numberOfCoursesEaten;

	/**
	 * ID of the student that arrived first
	 */
	private int firstToArriveID;

	/**
	 * ID of the student that arrived last
	 */
	private int lastToArriveID;

	/**
	 * ID of the student that has finished eat last
	 */
	private int lastToEatID;

	/**
	 * ID of the student that is being answered by the waiter
	 */
	private int studentBeingAnsweredID;

	/**
	 * Number of students that have been woke up
	 */
	private int numberOfStudentsWokenUp;

	/**
	 * Variable to check if the waiter is presenting the menu
	 */
	private boolean presentingTheMenu;

	/**
	 * Variable to check if student is informing his companion about the order
	 */
	private boolean informingCompanion;

	/**
	 * Variable to check if student is informing the waiter about the order
	 */
	private boolean takingTheOrder;

	/**
	 * Variable to check if waiter is processing the bill
	 */
	private boolean processingTheBill;

	/**
	 * Variable to check which students are seated
	 */
	private boolean[] studentsSeated;

	/**
	 * Variable to check which students have read the menu
	 */
	private boolean[] studentsThatHaveReadTheMenu;

	/**
	 * Reference to General Repositories
	 */
	private final GenRepos repos;

	/**
	 * Table Instantiation
	 * 
	 * @param repos
	 */
	public Table(GenRepos repos) {
		// Init students threads
		students = new Student[ExecConsts.N];
		for (int i = 0; i < ExecConsts.N; i++) {
			students[i] = null;
		}

		this.numberOfStudentsThatHasChosen = 0;
		this.numberOfStudentsThatHasFinishEat = 0;
		this.numberOfStudentsServed = 0;
		this.numberOfCoursesEaten = 0;
		this.firstToArriveID = -1;
		this.lastToArriveID = -1;
		this.lastToEatID = -1;
		this.studentBeingAnsweredID = -1;
		this.numberOfStudentsWokenUp = 0;
		this.presentingTheMenu = false;
		this.informingCompanion = false;
		this.takingTheOrder = false;
		this.processingTheBill = false;

		studentsSeated = new boolean[ExecConsts.N];
		studentsThatHaveReadTheMenu = new boolean[ExecConsts.N];

		for (int i = 0; i < ExecConsts.N; i++) {
			studentsSeated[i] = false;
			studentsThatHaveReadTheMenu[i] = false;
		}

		this.repos = repos;
	}

	/**
	 * @return id of the first student to arrive at the restaurant
	 */
	public int getFirstToArrive() {
		return firstToArriveID;
	}

	/**
	 * 
	 * @return id of the last student to finish eating a meal
	 */
	public int getLastToEat() {
		return lastToEatID;
	}

	/**
	 * @param firstToArrive id of the first student to arrive
	 */
	public void setFirstToArrive(int firstToArrive) {
		this.firstToArriveID = firstToArrive;
	}

	/**
	 * @param lastToArrive if of the last student to arrive to the restaurant
	 */
	public void setLastToArrive(int lastToArrive) {
		this.lastToArriveID = lastToArrive;
	}

	/**
	 * 
	 * Waiter Operations
	 * 
	 */

	/**
	 * Operation salute the client
	 * 
	 * It is called by the waiter when a student enters the restaurant
	 */
	public synchronized void saluteClient(int studentIDBeingAnswered) {
		Waiter waiter = ((Waiter) Thread.currentThread());
		studentBeingAnsweredID = studentIDBeingAnswered;

		waiter.setWaiterState(WaiterStates.PRESENTING_THE_MENU);
		repos.updateWaiterState(WaiterStates.PRESENTING_THE_MENU);

		presentingTheMenu = true;

		while (studentsSeated[studentBeingAnsweredID] == false) {
			try {
				wait();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		notifyAll();
		// ------------------ DEBUG ----------------
		// System.out.println("Waiter Saluting student " + studentBeingAnsweredID);

		while (studentsThatHaveReadTheMenu[studentBeingAnsweredID] == false) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		studentBeingAnsweredID = -1;
		presentingTheMenu = false;
	}

	/**
	 * Operation return to the bar
	 * 
	 * It is called by the waiter to change to return to the bar appraising
	 * situation
	 */
	public synchronized void returnBar() {
		Waiter waiter = ((Waiter) Thread.currentThread());
		waiter.setWaiterState(WaiterStates.APPRAISING_SITUATION);
		repos.updateWaiterState(WaiterStates.APPRAISING_SITUATION);
	}

	/**
	 * Operation get the pad
	 * 
	 * It is called by the waiter when an order is going to be described by the
	 * first student to arrive Waiter Blocks waiting for student to describe him the
	 * order
	 */
	public synchronized void getThePad() {
		Waiter waiter = ((Waiter) Thread.currentThread());
		if (waiter.getWaiterState() != WaiterStates.TAKING_THE_ORDER) {
			waiter.setWaiterState(WaiterStates.TAKING_THE_ORDER);
			repos.updateWaiterState(WaiterStates.TAKING_THE_ORDER);
		}

		takingTheOrder = true;

		notifyAll();

		while (takingTheOrder) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// -------------- DEBUG ----------------------
		// System.out.println("Waiter got the order");

	}

	/**
	 * Operation have all clients been served
	 * 
	 * Called by the waiter to check if all clients have been served or not
	 * 
	 * @return true if all clients have been served, false otherwise
	 */
	public synchronized boolean haveAllClientsBeenServed() {
		if (numberOfStudentsServed == ExecConsts.N) {
			// -------------- DEBUG ----------------------
			// System.out.println("Everyone has been served!");
			lastToEatID = -1;
			numberOfStudentsWokenUp = 0;

			notifyAll();
			return true;
		}
		return false;

	}

	/**
	 * Operation deliver portion
	 * 
	 * Called by the waiter, when a portion is delivered at the table
	 */
	public synchronized void deliverPortion() {
		numberOfStudentsServed++;
	}

	/**
	 * Operation present the bill
	 * 
	 * Called by the waiter to present the bill to the last student to arrive
	 */
	public synchronized void presentBill() {
		Waiter waiter = ((Waiter) Thread.currentThread());
		processingTheBill = true;

		notifyAll();

		waiter.setWaiterState(WaiterStates.RECEIVING_PAYMENT);
		repos.updateWaiterState(WaiterStates.RECEIVING_PAYMENT);

		try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * Student Operations
	 * 
	 */

	/**
	 * Operation siting at the table
	 * 
	 * Student comes in the table and sits (blocks) waiting for waiter to bring him
	 * the menu Called by the student (inside enter method in the bar)
	 */
	public synchronized void seatAtTable() {
		Student student = ((Student) Thread.currentThread());

		int studentID = student.getStudentID();

		students[studentID] = student;
		students[studentID].setStudentState(StudentStates.TAKING_A_SEAT_AT_THE_TABLE);
		repos.updateStudentState(studentID, StudentStates.TAKING_A_SEAT_AT_THE_TABLE);

		// repos.updateStudentSeat(studentID, numberOfStudentsAtTable);
		// numberOfStudentsAtTable++;

		// -------------- DEBUG ----------------------
		// System.out.println("Student " + studentID + " took a seat!");

		studentsSeated[studentID] = true;

		notifyAll();

		do {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// --------------------- DEUBG -----------------
			// System.out.println("Student "+studentID+" was waken up");
		} while (studentID != studentBeingAnsweredID && presentingTheMenu == false);

		// --------------------- DEUBG -----------------
		// System.out.println("Student "+studentID+ " was presented with the menu");
	}

	/**
	 * Operation read the menu
	 * 
	 * Called by the student to read a menu, wakes up waiter to signal that he
	 * already read the menu
	 */
	public synchronized void readMenu() {
		Student student = ((Student) Thread.currentThread());

		int studentID = student.getStudentID();

		students[studentID].setStudentState(StudentStates.SELECTING_THE_COURSES);
		repos.updateStudentState(studentID, StudentStates.SELECTING_THE_COURSES);

		studentsThatHaveReadTheMenu[studentID] = true;
		notifyAll();

		// --------------------- DEUBG -----------------
		// System.out.println("Student " + studentID + " read the menu (" +
		// studentBeingAnsweredID + ")");
	}

	/**
	 * Operation prepare the order
	 * 
	 * Called by the student to begin the preparation of the order,
	 */
	public synchronized void prepareOrder() {
		numberOfStudentsThatHasChosen++;

		students[firstToArriveID].setStudentState(StudentStates.ORGANIZING_THE_ORDER);
		repos.updateStudentState(firstToArriveID, StudentStates.ORGANIZING_THE_ORDER);
	}

	/**
	 * Operation everybody has chosen
	 * 
	 * Called by the first student to arrive to check if all his companions have
	 * choose or not Blocks if not.
	 * 
	 * @return true if has everybody choosen, false otherwise
	 */
	public synchronized boolean everybodyHasChosen() {
		if (numberOfStudentsThatHasChosen == ExecConsts.N)
			return true;
		else {
			while (informingCompanion == false) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return false;
		}

	}

	/**
	 * Operation add up ones choices
	 * 
	 * Called by the first student to arrive to add up a companions choice to the
	 * order
	 */
	public synchronized void addUpOnesChoices() {
		numberOfStudentsThatHasChosen++;
		informingCompanion = false;

		notifyAll();
	}

	/**
	 * Operation describe the order
	 * 
	 * Called by the first student to arrive to describe the order to the waiter
	 * Blocks waiting for waiter to come with pad Wake waiter up so he can take the
	 * order
	 */
	public synchronized void describeOrder() {

		while (takingTheOrder == false) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// --------------------- DEUBG -----------------
		// System.out.println("Student " + firstToArriveID + " described the order");
		takingTheOrder = false;

		notifyAll();
	}

	/**
	 * Operation join the talk
	 * 
	 * Called by the first student to arrive so he can join his companions while
	 * waiting for the courses
	 */
	public synchronized void joinTalk() {
		students[firstToArriveID].setStudentState(StudentStates.CHATTING_WITH_COMPANIONS);
		repos.updateStudentState(firstToArriveID, StudentStates.CHATTING_WITH_COMPANIONS);
	}

	/**
	 * Operation inform companion
	 * 
	 * Called by a student to inform the first student to arrive about his
	 * preferences Blocks waiting for courses
	 */
	public synchronized void informCompanion() {
		Student student = ((Student) Thread.currentThread());
		int studentID = student.getStudentID();

		while (informingCompanion) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		informingCompanion = true;
		notifyAll();

		students[studentID].setStudentState(StudentStates.CHATTING_WITH_COMPANIONS);
		repos.updateStudentState(studentID, StudentStates.CHATTING_WITH_COMPANIONS);
	}

	/**
	 * Operation start eating
	 * 
	 * Called by the student to start eating the meal (During random time)
	 */
	public synchronized void startEating() {
		Student student = ((Student) Thread.currentThread());
		int studentID = student.getStudentID();

		synchronized(this) {			
			students[studentID].setStudentState(StudentStates.ENJOYING_THE_MEAL);
			repos.updateStudentState(studentID, StudentStates.ENJOYING_THE_MEAL);
		}

		try {
			Thread.sleep((long) (1 + 100 * Math.random()));
		} catch (InterruptedException e) {}
	}

	/**
	 * Operation end eating
	 * 
	 * Called by the student to signal that he has finished eating his meal
	 */
	public synchronized void endEating() {
		Student student = ((Student) Thread.currentThread());
		int studentID = student.getStudentID();

		numberOfStudentsThatHasFinishEat++;
		// --------------------- DEUBG -----------------
		// System.out.println("Student " + studentID + " finished");

		if (numberOfStudentsThatHasFinishEat == ExecConsts.N) {
			numberOfCoursesEaten++;
			lastToEatID = studentID;
		}

		students[studentID].setStudentState(StudentStates.CHATTING_WITH_COMPANIONS);
		repos.updateStudentState(studentID, StudentStates.CHATTING_WITH_COMPANIONS);
	}

	/**
	 * Operation has everybody finished eating
	 * 
	 * Called by to student to wait for his companions to finish eating
	 */
	public synchronized boolean hasEverybodyFinishedEating() {
		int studentID = ((Student) Thread.currentThread()).getStudentID();

		if (studentID == lastToEatID) {
			numberOfStudentsThatHasFinishEat = 0;
			numberOfStudentsServed = 0;
			numberOfStudentsWokenUp++;
			notifyAll();
			while (numberOfStudentsWokenUp != ExecConsts.N) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		while (numberOfStudentsThatHasFinishEat != 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		numberOfStudentsWokenUp++;
		if (numberOfStudentsWokenUp == ExecConsts.N)
			notifyAll();

		return true;
	}

	/**
	 * Operation honour the bill
	 * 
	 * Called by the student to pay the bill Student blocks waiting for bill to be
	 * presented and signals waiter when it's time to pay it
	 */
	public synchronized void honourBill() {

		while (!processingTheBill) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// --------------------- DEUBG -----------------
		// System.out.println("The bill is payed");
		notifyAll();
	}

	/**
	 * Operation have all courses been eaten
	 * 
	 * Called by the student to check if there are more courses to be eaten Student
	 * blocks waiting for the course to be served
	 * 
	 * @return true if all courses have been eaten, false otherwise
	 */
	public synchronized boolean haveAllCoursesBeenEaten() {
		if (numberOfCoursesEaten == ExecConsts.M)
			return true;
		else {
			while (numberOfStudentsServed != ExecConsts.N) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			// --------------------- DEUBG -----------------
			// System.out.println("All portions have been served, course " + numberOfCoursesEaten);
			return false;
		}

	}

	/**
	 * Operation should have arrived earlier
	 * 
	 * Called by the student to check wich one was last to arrive
	 * 
	 * @return True if current student was the last to arrive, false otherwise
	 */
	public synchronized boolean shouldHaveArrivedEarlier() {
		Student student = ((Student) Thread.currentThread());

		int studentID = student.getStudentID();

		if (studentID == lastToArriveID) {
			students[studentID].setStudentState(StudentStates.PAYING_THE_MEAL);
			repos.updateStudentState(studentID, StudentStates.PAYING_THE_MEAL);			return true;
		} else
			return false;
	}
}
