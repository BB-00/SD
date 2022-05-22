package sharedRegions;

import entities.*;
import main.ExecConsts;


public class Table {
	/**
	 * Array of Students Threads
	 */
	private final Student [] students;
	
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
	private boolean [] studentsSeated;
	
	/**
	 * Variable to check which students have read the menu
	 */
	private boolean [] studentsThatHaveReadTheMenu;
	
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
		//Init students threads
		students = new Student[ExecConsts.N];
		for(int i=0 ; i<ExecConsts.N ; i++) {
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
		
		for(int i=0 ; i<ExecConsts.N ; i++)
    	{
    		studentsSeated[i] = false;
    		studentsThatHaveReadTheMenu[i] = false;
    	}
		
		this.repos = repos;		
	}
    

    /**
     * @return id of the first student to arrive at the restaurant
     */
    public int getFirstToArrive() { return firstToArriveID; }
    
    /**
     * 
     * @return id of the last student to finish eating a meal
     */
    public int getLastToEat() { return lastToEatID; }
    
    /**
     * 
     * @param firstToArrive id of the first student to arrive
     */
    public void setFirstToArrive(int firstToArrive) { this.firstToArriveID = firstToArrive; }
    
    /**
     * 
     * @param lastToArrive if of the last student to arrive to the restaurant
     */
    public void setLastToArrive(int lastToArrive) { this.lastToArriveID = lastToArrive; }

    
    /**
     * Operation salute the client
     * 
     * It is called by the waiter when a student enters the restaurant
     */
    public synchronized void saluteClient(int studentIdBeingAnswered) {
    	studentBeingAnsweredID = studentIdBeingAnswered;
    	
    	((Waiter) Thread.currentThread()).setWaiterState(WaiterStates.PRESENTING_THE_MENU);
    	repos.updateWaiterState(WaiterStates.PRESENTING_THE_MENU);
    	
    	presentingTheMenu = true;
    	
    	while(studentsSeated[studentBeingAnsweredID] == false)
    	{
			try {
				wait();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	}
    	
    	notifyAll();
    	System.out.println("Waiter Saluting student "+studentBeingAnsweredID);

    	while(studentsThatHaveReadTheMenu[studentBeingAnsweredID] == false) {
	    	try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    
    	}
    	
    	studentBeingAnsweredID = -1;
    	presentingTheMenu  = false;
    }

    /**
     * Operation return to the bar
     * 
     * It is called by the waiter to change to return to the bar appraising situation
     */
    public synchronized void returnBar() {
    	((Waiter) Thread.currentThread()).setWaiterState(WaiterStates.APPRAISING_SITUATION);
    	repos.updateWaiterState(WaiterStates.APPRAISING_SITUATION);    	
    }

    /**
     * Operation get the pad
     * 
     * It is called by the waiter when an order is going to be described by the first student to arrive
     * Waiter Blocks waiting for student to describe him the order
     */
    public synchronized void getThePad() {
    	((Waiter) Thread.currentThread()).setWaiterState(WaiterStates.TAKING_THE_ORDER);
    	repos.updateWaiterState(WaiterStates.TAKING_THE_ORDER);
    	
    	takingTheOrder = true;
    	
    	notifyAll();
    	
    	while(takingTheOrder) {
	    	try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	System.out.println("Waiter got the order");
    	
    }
    
    /**
     * Operation have all clients been served
     * 
     * Called by the waiter to check if all clients have been served or not
     * @return true if all clients have been served, false otherwise
     */
    public synchronized boolean haveAllClientsBeenServed() {    	
    	if(numberOfStudentsServed == ExecConsts.N) {
    		System.out.println("Everyone has been served!");
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
    	processingTheBill = true;
    	
    	notifyAll();
    	
    	((Waiter) Thread.currentThread()).setWaiterState(WaiterStates.RECEIVING_PAYMENT);
    	repos.updateWaiterState(WaiterStates.RECEIVING_PAYMENT);
    	try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    /**
     * Operation siting at the table
     * 
     * Student comes in the table and sits (blocks) waiting for waiter to bring him the menu
     * Called by the student (inside enter method in the bar)
     */
    public synchronized void seatAtTable() {
    	int studentId = ((Student) Thread.currentThread()).getStudentID();
    	
		students[studentId] = ((Student) Thread.currentThread());
		students[studentId].setStudentState(StudentStates.TAKING_A_SEAT_AT_THE_TABLE);
		repos.updateStudentState(studentId, StudentStates.TAKING_A_SEAT_AT_THE_TABLE);
		repos.updateStudentSeat(studentId, numberOfStudentsAtTable);
		numberOfStudentsAtTable++;
    	
    	System.out.println("Student "+studentId+" took a seat!");

    	studentsSeated[studentId] = true;
    	
    	notifyAll();
    	
    	//while (true) {
	    	try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	System.out.println("Student "+studentId+" was waken up");
	    	if (studentId == studentBeingAnsweredID && presentingTheMenu == true) {
	    		System.out.println("Student "+studentId+" Can Proceed");
	    		//break;
	    	}
	    //}
    	System.out.println("Student "+studentId+ " was presented with the menu");
    	
    	
    }
    
    /**
     * Operation read the menu
     * 
     * Called by the student to read a menu, wakes up waiter to signal that he already read the menu
     */
    public synchronized void readMenu() {
    	int studentId = ((Student) Thread.currentThread()).getStudentID();
    	
    	students[studentId].setStudentState(StudentStates.SELECTING_THE_COURSES);
    	repos.updateStudentState(studentId, StudentStates.SELECTING_THE_COURSES);
    	
    	studentsThatHaveReadTheMenu[studentId] = true;
    	notifyAll();
    	
    	System.out.println("Student "+studentId+ " read the menu ("+studentBeingAnsweredID+")");
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
     * Called by the first student to arrive to check if all his companions have choose or not
     * Blocks if not.
     * @return true if has everybody choosen, false otherwise
     */
    public synchronized boolean everybodyHasChosen() {
    	if(numberOfStudentsThatHasChosen == ExecConsts.N) return true;
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
     * Operation add up ones choices
     * 
     * Called by the first student to arrive to add up a companions choice to the order
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
     * Blocks waiting for waiter to come with pad
     * Wake waiter up so he can take the order
     */
    public synchronized void describeOrder() {

    	while(takingTheOrder == false) {
	    	try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	System.out.println("Student "+firstToArriveID+" described the order");
    	takingTheOrder = false;

    	notifyAll();
    }
    
    /**
     * Operation join the talk
     * 
     * Called by the first student to arrive so he can join his companions while waiting for the courses 
     */
    public synchronized void joinTalk()
    {
    	students[firstToArriveID].setStudentState(StudentStates.CHATTING_WITH_COMPANIONS);
    	repos.updateStudentState(firstToArriveID, StudentStates.CHATTING_WITH_COMPANIONS);   
    }
    
    /**
     * Operation inform companion
     * 
     * Called by a student to inform the first student to arrive about his preferences 
     * Blocks waiting for courses
     */
    public synchronized void informCompanion() {
    	int studentId = ((Student) Thread.currentThread()).getStudentID();
    	
    	while(informingCompanion) {
    		try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	informingCompanion = true;
    	notifyAll();
    	
    	students[studentId].setStudentState(StudentStates.CHATTING_WITH_COMPANIONS);
    	repos.updateStudentState(studentId, StudentStates.CHATTING_WITH_COMPANIONS);    	
    }

    /**
     * Operation start eating
     * 
     * Called by the student to start eating the meal (During random time)
     */    
    public synchronized void startEating() {
    	int studentId = ((Student) Thread.currentThread()).getStudentID();
    	 
    	students[studentId].setStudentState(StudentStates.ENJOYING_THE_MEAL);
    	repos.updateStudentState(studentId, StudentStates.ENJOYING_THE_MEAL);
    	
        try {
        	Thread.sleep ((long) (1 + 100 * Math.random ()));
        } catch (InterruptedException e) {}
    }

	/**
     * Operation end eating
     * 
     * Called by the student to signal that he has finished eating his meal
     */
    public synchronized void endEating() {
    	int studentId = ((Student) Thread.currentThread()).getStudentID();
    	
    	numberOfStudentsThatHasFinishEat++;
    	System.out.println("Student "+studentId+" finished");
    	
    	if(numberOfStudentsThatHasFinishEat == ExecConsts.N) {
    		numberOfCoursesEaten++;
    		lastToEatID = studentId;
    	}
    	
    	students[studentId].setStudentState(StudentStates.CHATTING_WITH_COMPANIONS);
    	repos.updateStudentState(studentId, StudentStates.CHATTING_WITH_COMPANIONS);
    }
    
    /**
     * Operation has everybody finished eating
     * 
     * Called by to student to wait for his companions to finish eating
     */
    public synchronized boolean hasEverybodyFinishedEating() {
    	int studentId = ((Student) Thread.currentThread()).getStudentID();
    	
    	if(studentId == lastToEatID) {
    		numberOfStudentsThatHasFinishEat = 0;
    		numberOfStudentsServed = 0;
    		numberOfStudentsWokenUp++;
    		notifyAll();
    		while(numberOfStudentsWokenUp != ExecConsts.N) {
    			try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    	
    	while(numberOfStudentsThatHasFinishEat != 0) {
    		try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	numberOfStudentsWokenUp++;
    	if(numberOfStudentsWokenUp == ExecConsts.N) notifyAll();
    	
    	return true;
    }
    
    /**
     * Operation honour the bill
     * 
     * Called by the student to pay the bill
     * Student blocks waiting for bill to be presented and signals waiter when it's time to pay it
     */
    public synchronized void honourBill() {    	
    	
		
		while(!processingTheBill) {
    		try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
	    	
    	System.out.println("The bill is payed");
    	notifyAll();
    }
    
    /**
     * Operation have all courses been eaten
     * 
     * Called by the student to check if there are more courses to be eaten
     * 	Student blocks waiting for the course to be served
     * 	@return true if all courses have been eaten, false otherwise
     */
    public synchronized boolean haveAllCoursesBeenEaten() {
    	if(numberOfCoursesEaten == ExecConsts.M) return true;
		else {
    		while(numberOfStudentsServed != ExecConsts.N) {
	    		try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		System.out.println("All portions have been served, course "+ numberOfCoursesEaten);
	    	return false;
    	}
    	
    }
    
    /**
     * Operation should have arrived earlier
     * 
     * Called by the student to check wich one was last to arrive
     * @return True if current student was the last to arrive, false otherwise
     */
    public synchronized boolean shouldHaveArrivedEarlier() {
    	int studentId = ((Student) Thread.currentThread()).getStudentID();

    	if(studentId == lastToArriveID) {
	    	students[studentId].setStudentState(StudentStates.PAYING_THE_MEAL);
	    	repos.updateStudentState(studentId, StudentStates.PAYING_THE_MEAL);
	    	return true;
    	}
    	else return false;
    }
}
