package entities;

import sharedRegions.Bar;
import sharedRegions.Table;

/**
 *   Student thread.
 *
 *   It simulates the student life cycle.
 */

public class Student extends Thread{
	
	/**
	 * Reference to the Table
	 */
	private final Table table;

	/**
	 * Reference to the Bar
	 */
	private final Bar bar;
	
	/**
	 * Student ID
	 */
	private  int studentID;
	
	/**
	 * Student States
	 */
	private int studentState;
	
	/**
	 * Instatiation of a student thread
	 * 
	 * @param studentId student id
	 * @param reference to bar
	 * @param reference to table
	 */
	public Student(String name, int studentID, Bar bar, Table table) {
		super(name);
		this.studentID = studentID;
		this.studentState = StudentStates.GOING_TO_THE_RESTAURANT;
		this.bar = bar;
		this.table = table;
	}
	
	/**
	 * Set the studentID
	 * 
	 * @param studentID
	 */
	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}
	
	/**
	 * @return studentID
	 */
	public int getStudentID() {
		return this.studentID;
	}
	
	/**
	 * set the student state
	 * 
	 * @param studentState
	 */
	public void setStudentState(int studentState) {
		this.studentState = studentState;
	}
	
	/**
	 * @return student state
	 */
	public int getStudentState() {
		return this.studentState;
	}
	/**
	 *	Life cycle of the student
	 */

	@Override
	public void run ()
	{
		walkABit();
		this.bar.enter();
		this.table.readMenu();
		
		if(studentID == this.table.getFirstToArrive())
		{
			this.table.prepareOrder();
			do{
				this.table.addUpOnesChoices();
			}while(!this.table.everybodyHasChosen());
			this.bar.callWaiter();
			this.table.describeOrder();
			this.table.joinTalk();
		}
		else	this.table.informCompanion();
		do{
			this.table.startEating();
			this.table.endEating();
			while(!this.table.hasEverybodyFinishedEating());
			if(studentID == this.table.getLastToEat()) this.bar.signalWaiter();
		}while(!this.table.haveAllCoursesBeenEaten());

		if(this.table.shouldHaveArrivedEarlier()) {
			this.bar.signalWaiter();
			this.table.honourBill();
		}
		this.bar.exit();
	}
	
	
	
	private void walkABit()
	{
		try
		{ sleep ((long) (1 + 50 * Math.random ()));
		}
		catch (InterruptedException e) {}
	}
	
}