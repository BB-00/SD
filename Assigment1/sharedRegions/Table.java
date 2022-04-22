package sharedRegions;

import entities.Student;
import entities.StudentStates;

public class Table {
	
	/**
	 * number of Students already seated
	 */
	private int numberOfStudentsAtTheTable;
	
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
	 * number of Courses that have already been served
	 */
	private int numberOfCoursesServed;
	
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
	 * Number of pending request for the waiter
	 */
	private int numberOfPendingRequests;
	
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
	 * Reference to General Repositories
	 */
	private final GenRepos repos;
	
	/**
	 * Queue of pending Requests
	 */
	private FIFO<Request> pendingServiceRequestQueue;
	
	/**
	 * Table Instantiation
	 * 
	 * @param repos
	 */
	public Table(GenRepos repos) {
		this.numberOfStudentsAtTheTable = 0;
		this.numberOfStudentsThatHasChosen = 0;
		this.numberOfStudentsThatHasFinishEat = 0;
		this.numberOfStudentsServed = 0;
		this.numberOfCoursesServed = 0;
		this.numberOfCoursesEaten = 0;
		this.firstToArriveID = -1;
		this.lastToArriveID = -1;
		this.lastToEatID = -1;
		this.studentBeingAnsweredID = -1;
		this.numberOfPendingRequests = 0;
		this.informingCompanion = false;
		this.takingTheOrder = false;
		this.processingTheBill = false;
		this.repos = repos;
	}
	
	/**
	 * @return ID of the first student to arrive
	 */
	public int getFirstToArrive() {
		return firstToArriveID;
	}
	
	/**
	 * set the ID of the first student to arrive
	 * 
	 * @param studentID
	 */
	public void setFirstToArrive(int studentID) {
		this.firstToArriveID = studentID;
	}
	
	/**
	 * @return ID of the last student to arrive
	 */
	public int getLastToArrive() {
		return lastToArriveID;
	}
	
	/**
	 * set the ID of the last student to arrive
	 * 
	 * @param studentID
	 */
	public void setLastToArrive(int studentID) {
		this.lastToArriveID = studentID;
	}
	
	/**
	 * @return ID of the last student to eat
	 */
	public int getLastToFinishEat() {
		return lastToEatID;
	}
	
	
	/**
	 * 
	 * Students Operations
	 *
	 */
	
	/**
	 * Operation Take A Seat At The Table, called by automatically by students after entering in the restaurant
	 */
	public synchronized void takeASeatAtTable() {
		//get student ID
		int studentID = ((Student) Thread.currentThread()).getStudentID();
		
		System.out.println("Student "+studentID+" has seated");
		
		//notify waiter that has seated
		notifyAll();
		
		//Block to wait for the menu
		while(true) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("Student "+studentID+" was woken up");
			
			if(studentID == studentBeingAnsweredID && presentingTheMenu== true) {
				System.out.println("Student "+studentID+" can proceed");
				break;
			}
		}
		
		System.out.println("Student "+studentID+" was presented with the menu -"+studentBeingAnsweredID);
	}
	
	/**
	 * Operation read the menu, called by student when he seats at the table. Wakes up Waiter after read the menu
	 */
	public void readTheMenu() {
		//get Student ID
		int studentID = ((Student) Thread.currentThread()).getStudentID();
		
		//update Student State
		((Student) Thread.currentThread()).setStudentState(StudentStates.SELECTING_THE_COURSES);
		repos.updateStudentState(studentID, StudentStates.SELECTING_THE_COURSES);
		
		notifyAll();
		System.out.println("Student "+studentID+" read the menu");
	}
	
	/**
	 * Operation prepare the order, called by the student who arrived first, to begin preparation of the order
	 */
	public void prepareTheOrder() {
		// add the order of first to arrive
		numberOfStudentsThatHasChosen++;
		
		//update Student State
		((Student) Thread.currentThread()).setStudentState(StudentStates.ORGANIZING_THE_ORDER);
		repos.updateStudentState(firstToArriveID, StudentStates.ORGANIZING_THE_ORDER);
	}
	
	/**
	 * Operation add up ones choices, called by the student who arrived first to add up companion orders
	 */
	public synchronized void addUpOnesChoices(){
		// update number of students that have chosen the courses
		numberOfStudentsThatHasChosen++;
		informingCompanion = false;
		
		notifyAll();
	}
	
	/**
	 * Operation has everybody chosen, called by the student who arrived first, to check if companion has chose or not
	 * Blocks if not
	 * 
	 * @return true if has everybody chosen, false otherwise
	 */
	public synchronized boolean hasEverybodyChosen() {
		if(numberOfStudentsThatHasChosen == 7) return true;
		else {
			while(informingCompanion == false) {
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
	 * Operation call The Waiter, called by the student who arrived first, to call the waiter
	 */
	public synchronized void callTheWaiter() {
		Request request = new Request(((Student) Thread.currentThread()).getStudentID(), 'o');
		
		// Add request to queue
		try {
			pendingServiceRequestQueue.write(request);
		} catch (MemException e){
			e.printStackTrace();
		}
		
		// update the number of pending requests
		numberOfPendingRequests++;
		
		// notify waiter of new pending request
		notifyAll();
	}
	
	/**
	 * Operation Describe The Order, called by the student who arrived first to describe the order to the waiter
	 * Blocks to wait for the waiter to get the pad
	 * Wake waiter so he can take the order
	 */
	public synchronized void describeTheOrder() {
		while(takingTheOrder = false) {
			try {
				wait();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Student"+firstToArriveID+" described the order");
		takingTheOrder = false;
		notifyAll();
	}
	
	/**
	 * Operation Join The Talk, called by the student who arrived first, after having described the order to join
	 * talk with his companions while waiting for the courses
	 */
	public synchronized void joinTheTalk() {
		// Update Student State
		((Student) Thread.currentThread()).setStudentState(StudentStates.CHATTING_WITH_COMPANIONS);
		repos.updateStudentState(firstToArriveID, StudentStates.CHATTING_WITH_COMPANIONS);
	}
	
	/**
	 * Operation Inform Companion, called by students to inform the first to arrive about his preferences
	 */
	public synchronized void informCompanion() {
		while(informingCompanion) {
			try{
				wait();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		informingCompanion = true;
		notifyAll();
		
		((Student) Thread.currentThread()).setStudentState(StudentStates.CHATTING_WITH_COMPANIONS);
		repos.updateStudentState(firstToArriveID, StudentStates.CHATTING_WITH_COMPANIONS);
	}
	
	/**
	 * Operation Start Eating, called by the students when they start eating their meal
	 */
	public synchronized void startEating() {
		//update Student State
		((Student) Thread.currentThread()).setStudentState(StudentStates.ENJOYING_THE_MEAL);
		repos.updateStudentState(((Student) Thread.currentThread()).getStudentID(), StudentStates.ENJOYING_THE_MEAL);
		
		//enjoy meal
		try {
			Thread.sleep((long) (1+200*Math.random()));
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Operation End Eating, called by the students to signal that has finished eating the course
	 */
	public synchronized void endEating() {
		//get Student ID
		int studentID = ((Student) Thread.currentThread()).getStudentID();
		
		//Update number of students that have eaten
		numberOfStudentsThatHasFinishEat++;
		System.out.println("Student "+studentID+" finished eating course "+numberOfCoursesEaten);
		
		//Update number of courses eaten, if all students have eaten
		if(numberOfStudentsThatHasFinishEat == 7) {
			numberOfCoursesEaten++;
			lastToEatID = studentID;
		}
		
		//update student state
		((Student) Thread.currentThread()).setStudentState(StudentStates.CHATTING_WITH_COMPANIONS);
		repos.updateStudentState(studentID, StudentStates.CHATTING_WITH_COMPANIONS);
	}
    
	/**
	 * Operation Has Everybody Finished Eating, called by the students to wait for the others students to finish eat
	 * 
	 * @return true when all have finished eat
	 */
	public synchronized boolean hasEverybodyFinishedEating() {
		// get student id
		int studentID = ((Student) Thread.currentThread()).getStudentID();
		
		// ATENCAO -----------------------------------------------------------------------------------------------------------------------------------------------------
		int numberOfStudentsWokenUp=0;
		//-------------------------------------------------------------------------------------------------
		
		//notify all students that the last has finished eaten
		if(studentID == lastToEatID) {
			numberOfStudentsThatHasFinishEat = 0;
			numberOfStudentsServed = 0;
			numberOfStudentsWokenUp++;
			while(numberOfStudentsWokenUp != 7) {
    			try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
		}
		
		//wait while not all students have finished
		while(numberOfStudentsThatHasFinishEat != 0) {
			try {
				wait();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Student "+studentID+" woke after course "+(numberOfCoursesEaten-1));
		}
		
		// update number of students woken up
		numberOfStudentsWokenUp++;
		if(numberOfStudentsWokenUp == 7) notifyAll();
		
		return true;
	}
	
	/**
	 * Operation Signal The Waiter, called by the last student to finished eat, and called the waiter to bring next course
	 */
	public synchronized void signalTheWaiter() {
		//notify waiter and chef
		notifyAll();
	}
	
	/**
	 * Operation have all courses been served, called by the students to check if there are more courses
	 * Student blocks waiting for the course to be served
	 * 
	 * @return true if all courses have been eaten, false otherwise
	 */
	public synchronized boolean haveAllCoursesBeenServed() {
		if(numberOfCoursesServed == 3) {
			return true;
		} else {
			//students block waiting for all students to be served
			while(numberOfStudentsServed == 7) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("All Students served!! Course "+numberOfCoursesEaten);
			return false;
		}
	}
	
	/**
	 * Operation Should Have Arrived Earlier, called by the students to check which one arrived at last
	 * 
	 * @return true if current student was last to arrive, false otherwise
	 */
	public synchronized boolean shouldHaveArrivedEarlier() {
		//gget student id
		int studentID = ((Student) Thread.currentThread()).getStudentID();
		
		if(studentID == lastToArriveID) {
			//Update Student State
			((Student) Thread.currentThread()).setStudentState(StudentStates.PAYING_THE_MEAL);
			repos.updateStudentState(studentID, StudentStates.PAYING_THE_MEAL);
			
			return true;
		} else {
			return false;
		}	
	}
	
	/**
	 * Operation Honour The Bill, called by the last student to arrive to pay the bill
	 * Blocks to wait for the waiter to present the bill
	 */
	public void honourTheBill() {
		// block waiting for waiter to present the bill
		while(!processingTheBill) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Student "+((Student) Thread.currentThread()).getStudentID()+" payed the bill");
		
		//signal waiter that bill was paid
		notifyAll();
	}
}
