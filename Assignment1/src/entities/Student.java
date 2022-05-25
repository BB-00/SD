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
	public void setStudentID(int id) {
		studentID = id;
	}
	
	/**
	 * @return studentID
	 */
	public int getStudentID() {
		return studentID;
	}
	
	/**
	 * set the student state
	 * 
	 * @param studentState
	 */
	public void setStudentState(int state) {
		studentState = state;
	}
	
	/**
	 * @return student state
	 */
	public int getStudentState() {
		return studentState;
	}
	/**
	 *	Life cycle of the student
	 */

	@Override
	public void run ()
	{
		walkABit();
		bar.enter();
		table.readMenu();
		
		if(studentID == table.getFirstToArrive())
		{
			table.prepareOrder();
			do{
				table.addUpOnesChoices();
			}while(!table.everybodyHasChosen());
			bar.callWaiter();
			table.describeOrder();
			table.joinTalk();
		}
		else table.informCompanion();
		// do{
		// 	this.table.startEating();
		// 	this.table.endEating();
		// 	while(!this.table.hasEverybodyFinishedEating());
		// 	if(studentID == this.table.getLastToEat()) this.bar.signalWaiter();
		// }while(!this.table.haveAllCoursesBeenEaten());

		while(!table.haveAllCoursesBeenEaten()){
			if(table.haveAllClientsBeenServed()){
				table.startEating();
				table.endEating();
				while(!table.hasEverybodyFinishedEating());
				if(studentID == table.getLastToEat()) bar.signalWaiter();
			}
		}

		if(table.shouldHaveArrivedEarlier()) {
			bar.signalWaiter();
			table.honourBill();
		}
		bar.exit();
	}
	
	
	private void walkABit(){
		try{ 
			sleep ((long) (1 + 50 * Math.random ()));
		} catch (InterruptedException e) {}
	}
	
}