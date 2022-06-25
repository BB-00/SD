package serverSide.objects;

import commInfra.*;
import serverSide.main.*;
import serverSide.entities.*;
import clientSide.entities.*;
import clientSide.stubs.*;
import genclass.GenericIO;


/**
 * 	Table
 * 
 *  It is responsible for keeping track of the courses being eaten.
 *  Implemented as an implicit monitor
 *	Public methods executed in mutual exclusion
 *	Synchronization points for the waiter include:
 *		If saluting a student waiter must wait for him to seat at table and then wait for him to read menu
 *		Waiter has to wait for first student to arrive to describe him the order
 *		Waiter blocks waiting for student to pay the bill
 *	Synchronization points for the student include:	
 *		Student waits for waiter to bring menu specifically to him
 *		First student to arrive blocks if everybody has not chosen yet and while companions are not describing their choices
 *		First student to arrive waits for waiter to come with the pad
 *		If some student is informing about his choice, then a student must wait for his companion to finish telling his preference
 *		Students must wait that everybody is served before they can start eating
 *		When a student finishes his course must wait for his companions to finish
 *		Student that was last to eat must wait for his companions to woken up before he can signal waiter to bring next course
 *		Last student to arrive must wait for waiter to bring him the bill	
 *		
 */
public class Table implements TableInterface {
	/**
	 *   Number of entity groups requesting the shutdown.
	 */
	private int nEntities;
	
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
	 * Variable to check which students states
	 */
	private final int [] studentsState;
	
	/**
	 * Variable to check which students are seated
	 */
	private boolean [] studentsSeated;
	
	/**
	 * Variable to check which students have read the menu
	 */
	private boolean [] studentsThatHaveReadTheMenu;
	
	/**
	 * Reference to General Repositories Interface
	 */
	private final GenReposInterface repos;
		
	/**
	 * Table Instantiation
	 * 
	 * @param repos reference to the General Repository Interface
	 */
	public Table(GenReposInterface repos) {
		this.nEntities = 0;
		
		
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
		
		this.studentsSeated = new boolean[ExecConsts.N];
		this.studentsThatHaveReadTheMenu = new boolean[ExecConsts.N];
		this.studentsState = new int[ExecConsts.N];
		
		for(int i=0 ; i<ExecConsts.N ; i++)
    	{
    		this.studentsSeated[i] = false;
    		this.studentsThatHaveReadTheMenu[i] = false;
			this.studentsState[i] = -1;
    	}
		
		this.repos = repos;		
	}
    

    /**
     * @return id of the first student to arrive at the restaurant
     */
	@Override
    public int getFirstToArrive() throws RemoteException { return firstToArriveID; }
    
    /**
     * 
     * @return id of the last student to finish eating a meal
     */
	@Override
    public int getLastToEat() throws RemoteException { return lastToEatID; }
    
    /**
     * 
     * @param firstToArrive id of the first student to arrive
     */
	@Override
    public synchronized void setFirstToArrive (int firstToArrive) throws RemoteException {
    	//System.out.println("Student "+firstToArrive+" was first to arrive!");
    	this.firstToArriveID = firstToArrive; }
    
    /**
     * 
     * @param lastToArrive if of the last student to arrive to the restaurant
     */
	@Override
    public synchronized void setLastToArrive(int lastToArrive) throws RemoteException { this.lastToArriveID = lastToArrive; }

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
	@Override
    public synchronized int saluteClient(int studentIDBeingAnswered) throws RemoteException {
    	
    	studentBeingAnsweredID = studentIDBeingAnswered;
    	
		// if(waiter.getWaiterState() != WaiterStates.PRESENTING_THE_MENU) {
		// 	waiter.setWaiterState(WaiterStates.PRESENTING_THE_MENU);
		// 	repos.updateWaiterState(WaiterStates.PRESENTING_THE_MENU);
		// }

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
    	// ------------------ DEBUG ----------------
    	// System.out.println("Waiter Saluting student "+studentBeingAnsweredID);

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

		return WaiterStates.PRESENTING_THE_MENU;
    }

    /**
     * Operation return to the bar
     * 
     * It is called by the waiter to change to return to the bar appraising situation
     */
	@Override
    public synchronized int returnBar() throws RemoteException {

		// if(waiter.getWaiterState() != WaiterStates.APPRAISING_SITUATION) {
		// 	waiter.setWaiterState(WaiterStates.APPRAISING_SITUATION);
		// 	repos.updateWaiterState(WaiterStates.APPRAISING_SITUATION);
		// }

		repos.updateWaiterState(WaiterStates.APPRAISING_SITUATION);

		return WaiterStates.APPRAISING_SITUATION;

    }

    /**
     * Operation get the pad
     * 
     * It is called by the waiter when an order is going to be described by the first student to arrive
     * Waiter Blocks waiting for student to describe him the order
     */
	@Override
    public synchronized int getThePad() throws RemoteException {

		// if(waiter.getWaiterState() != WaiterStates.TAKING_THE_ORDER) {
		// 	waiter.setWaiterState(WaiterStates.TAKING_THE_ORDER);
		// 	repos.updateWaiterState(WaiterStates.TAKING_THE_ORDER);
		// }
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
    	
    	// -------------- DEBUG ----------------------
    	// System.out.println("Waiter got the order");

		return WaiterStates.TAKING_THE_ORDER;
    	
    }
    
    /**
     * Operation have all clients been served
     * 
     * Called by the waiter to check if all clients have been served or not
     * @return true if all clients have been served, false otherwise
     */
	@Override
    public synchronized boolean haveAllClientsBeenServed() throws RemoteException {    	
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
	@Override
    public synchronized void deliverPortion() throws RemoteException {
    	numberOfStudentsServed++; 
    }
    
    /**
     * Operation present the bill
     * 
     * Called by the waiter to present the bill to the last student to arrive
     */
	@Override
    public synchronized int presentBill() throws RemoteException {

		processingTheBill = true;
    	
    	notifyAll();
    	
    	// if(waiter.getWaiterState() != WaiterStates.RECEIVING_PAYMENT) {
		// 	waiter.setWaiterState(WaiterStates.RECEIVING_PAYMENT);
		// 	repos.updateWaiterState(WaiterStates.RECEIVING_PAYMENT);
		// }
		repos.updateWaiterState(WaiterStates.RECEIVING_PAYMENT);

    	
    	try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return WaiterStates.RECEIVING_PAYMENT;
    	
    }
    
    /**
     * 
     * Student Operations
     * 
     */
    
    /**
     * Operation siting at the table
     * 
     * Student comes in the table and sits (blocks) waiting for waiter to bring him the menu
     * Called by the student (inside enter method in the bar)
     */
	@Override
    public synchronized void seatAtTable(int studentID) throws RemoteException {
    	
		studentsState[studentID] = StudentStates.TAKING_A_SEAT_AT_THE_TABLE;
    	
		// ----------------- DEBUG ------------------
    	// System.out.println("Student "+studentID+" took a seat!");

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
	    	//System.out.println("Student "+studentID+" was waken up");
	    } while(studentID != studentBeingAnsweredID && presentingTheMenu == false);
    	
    	// --------------------- DEUBG -----------------
    	// System.out.println("Student "+studentID+ " was presented with the menu");
    }
    
    /**
     * Operation read the menu
     * 
     * Called by the student to read a menu, wakes up waiter to signal that he already read the menu
     */
	@Override
    public synchronized int readMenu(int studentID) throws RemoteException {
    	    	    	
		// if(((TableClientProxy) Thread.currentThread()).getStudentState() != StudentStates.SELECTING_THE_COURSES) {
    	// 	students[studentID].setStudentState(StudentStates.SELECTING_THE_COURSES);
    	// 	student.setStudentState(StudentStates.SELECTING_THE_COURSES);
    	// 	repos.updateStudentState(studentID, StudentStates.SELECTING_THE_COURSES);
		// }
		studentsState[studentID] = StudentStates.SELECTING_THE_COURSES;
		repos.updateStudentState(studentID, StudentStates.SELECTING_THE_COURSES);

    			
    	studentsThatHaveReadTheMenu[studentID] = true;
    	notifyAll();

		return studentsState[studentID];
    	
    	// --------------------- DEUBG -----------------
    	// System.out.println("Student "+studentID+ " read the menu ("+studentBeingAnsweredID+")");
    }
    
    /**
     * Operation prepare the order
     * 
     * Called by the student to begin the preparation of the order, 
     */
	@Override
    public synchronized int prepareOrder() throws RemoteException {    	
    	numberOfStudentsThatHasChosen++;
    	    	    	
		// if(student.getStudentState() != StudentStates.ORGANIZING_THE_ORDER) {
    	// 	students[firstToArriveID].setStudentState(StudentStates.ORGANIZING_THE_ORDER);
    	// 	student.setStudentState(StudentStates.ORGANIZING_THE_ORDER);
    	// 	repos.updateStudentState(firstToArriveID, StudentStates.ORGANIZING_THE_ORDER);
		// }

		studentsState[firstToArriveID] = StudentStates.SELECTING_THE_COURSES;
		repos.updateStudentState(firstToArriveID, StudentStates.ORGANIZING_THE_ORDER);

		return studentsState[firstToArriveID];
    }
    
    /**
     * Operation everybody has chosen
     * 
     * Called by the first student to arrive to check if all his companions have choose or not
     * Blocks if not.
     * @return true if has everybody choosen, false otherwise
     */
	@Override
    public synchronized boolean everybodyHasChosen() throws RemoteException {
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
	@Override
    public synchronized void addUpOnesChoices() throws RemoteException {
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
	@Override
    public synchronized void describeOrder() throws RemoteException {

    	while(takingTheOrder == false) {
	    	try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	// --------------------- DEUBG -----------------
    	// System.out.println("Student "+firstToArriveID+" described the order");
    	takingTheOrder = false;

    	notifyAll();
    }
    
    /**
     * Operation join the talk
     * 
     * Called by the first student to arrive so he can join his companions while waiting for the courses 
     */
	@Override
    public synchronized int joinTalk() throws RemoteException {
    	    	
		// if(student.getStudentState() != StudentStates.CHATTING_WITH_COMPANIONS) {
    	// 	students[firstToArriveID].setStudentState(StudentStates.CHATTING_WITH_COMPANIONS);
    	// 	student.setStudentState(StudentStates.CHATTING_WITH_COMPANIONS);
    	// 	repos.updateStudentState(firstToArriveID, StudentStates.CHATTING_WITH_COMPANIONS);
		// } 
		repos.updateStudentState(firstToArriveID, StudentStates.CHATTING_WITH_COMPANIONS);
		studentsState[firstToArriveID] = StudentStates.CHATTING_WITH_COMPANIONS;

		return studentsState[firstToArriveID];

    }
    
    /**
     * Operation inform companion
     * 
     * Called by a student to inform the first student to arrive about his preferences 
     * Blocks waiting for courses
     */
	@Override
    public synchronized int informCompanion(int studentID) throws RemoteException  {
    	
    	    	
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
    	
    	// if(student.getStudentState() != StudentStates.CHATTING_WITH_COMPANIONS) {
    	// 	students[studentID].setStudentState(StudentStates.CHATTING_WITH_COMPANIONS);
    	// 	student.setStudentState(StudentStates.CHATTING_WITH_COMPANIONS);
    	// 	repos.updateStudentState(studentID, StudentStates.CHATTING_WITH_COMPANIONS);
    	// }

		studentsState[studentID] = StudentStates.CHATTING_WITH_COMPANIONS;
		repos.updateStudentState(studentID, StudentStates.CHATTING_WITH_COMPANIONS);

		return studentsState[studentID];

    }

    /**
     * Operation start eating
     * 
     * Called by the student to start eating the meal (During random time)
     */    
	@Override
    public synchronized int startEating(int studentID) throws RemoteException  {
    	
		// if(student.getStudentState() != StudentStates.ENJOYING_THE_MEAL) {
    	// 	students[studentID].setStudentState(StudentStates.ENJOYING_THE_MEAL);
    	// 	student.setStudentState(StudentStates.ENJOYING_THE_MEAL);
    	// 	repos.updateStudentState(studentID, StudentStates.ENJOYING_THE_MEAL);
		// }

		synchronized(this){
			repos.updateStudentState(studentID, StudentStates.ENJOYING_THE_MEAL);
			studentsState[studentID] = StudentStates.ENJOYING_THE_MEAL;
		}
    	
        try {
        	Thread.sleep ((long) (1 + 100 * Math.random ()));
        } catch (InterruptedException e) {}

		return studentsState[studentID];
    }

	/**
     * Operation end eating
     * 
     * Called by the student to signal that he has finished eating his meal
     */
	@Override
    public synchronized int endEating(int studentID) throws RemoteException {
    	
    	    	
    	numberOfStudentsThatHasFinishEat++;
    	
    	// --------------------- DEUBG -----------------
    	// System.out.println("Student "+studentID+" finished course "+numberOfCoursesEaten+1+"!");
    	
    	if(numberOfStudentsThatHasFinishEat == ExecConsts.N) {
    		numberOfCoursesEaten++;
    		lastToEatID = studentID;
    	}
    	
    	// if(student.getStudentState() != StudentStates.CHATTING_WITH_COMPANIONS) {
    	// 	students[studentID].setStudentState(StudentStates.CHATTING_WITH_COMPANIONS);
    	// 	student.setStudentState(StudentStates.CHATTING_WITH_COMPANIONS);
    	// 	repos.updateStudentState(studentID, StudentStates.CHATTING_WITH_COMPANIONS);
		// }
		repos.updateStudentState(studentID, StudentStates.CHATTING_WITH_COMPANIONS);
		studentsState[studentID] = StudentStates.CHATTING_WITH_COMPANIONS;

		return studentsState[studentID];
    }
    
    /**
     * Operation has everybody finished eating
     * 
     * Called by to student to wait for his companions to finish eating
     */
	@Override
    public synchronized boolean hasEverybodyFinishedEating(int studentID) throws RemoteException {
    	
    	if(studentID == lastToEatID) {
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
	@Override
    public synchronized void honourBill() throws RemoteException {    	
    	
		
		while(!processingTheBill) {
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
     * Called by the student to check if there are more courses to be eaten
     * 	Student blocks waiting for the course to be served
     * 	@return true if all courses have been eaten, false otherwise
     */
	@Override
    public synchronized boolean haveAllCoursesBeenEaten() throws RemoteException {
    	if(numberOfCoursesEaten == ExecConsts.M) {
    		//System.out.println("All portions have been served, course "+ numberOfCoursesEaten);
    		return true;
    	}
		else {
    		while(numberOfStudentsServed != ExecConsts.N) {
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
     * Operation should have arrived earlier
     * 
     * Called by the student to check wich one was last to arrive
     * @return True if current student was the last to arrive, false otherwise
     */
	@Override
    public synchronized ReturnBoolean shouldHaveArrivedEarlier(int studentID) throws RemoteException {
    	
		
    	if(studentID == lastToArriveID) {
    		// if(student.getStudentState() != StudentStates.PAYING_THE_MEAL) {
        	// 	students[studentID].setStudentState(StudentStates.PAYING_THE_MEAL);
        	// 	student.setStudentState(StudentStates.PAYING_THE_MEAL);
        	// 	repos.updateStudentState(studentID, StudentStates.PAYING_THE_MEAL);
    		// }
			repos.updateStudentState(studentID, StudentStates.PAYING_THE_MEAL);
			studentsState[studentID] = StudentStates.PAYING_THE_MEAL;

	    	return new ReturnBoolean(true, studentsState[studentID]);
    	}
    	else return new ReturnBoolean(false, studentsState[studentID]);;
    }
    
    /**
	 *   Operation server shutdown.
	 *
	 *   New operation.
	 */
	@Override
	 public synchronized void shutdown() throws RemoteException {
		 nEntities += 1;
	     if (nEntities >= 2)
	    	 TableMain.waitConnection = false;
	     notifyAll();
	 }
}
