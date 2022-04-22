package entities;

import sharedRegions.Table;
import sharedRegions.Bar;

public class Student extends Thread {
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
	
	
	@Override
	public void run() {
		walkABit();
		this.bar.enter();
		this.table.readTheMenu();
		if(this.studentID == this.table.getFirstToArrive()){
			this.table.prepareTheOrder();
			do{
				this.table.addUpOnesChoices();
			}while(!this.table.hasEverybodyChosen());
			this.table.callTheWaiter();
			this.table.describeTheOrder();
			this.table.joinTheTalk();
		}else{
			this.table.informCompanion();
		}
		do{
			this.table.startEating();
			this.table.endEating();
			while(!this.table.hasEverybodyFinishedEating());
			if(studentID == this.table.getLastToFinishEat()) this.table.signalTheWaiter();
		}while(!this.table.haveAllCoursesBeenServed());
		if(studentID == this.table.getLastToArrive()){
			this.table.shouldHaveArrivedEarlier();
			this.table.honourTheBill();
		}
		this.bar.exit();
	}
	
	/**
	 * Operation walk a bit, called by students to walk before entering the restaurant
	 */
	private void walkABit() {
		try
		{ sleep ((long) (1 + 50 * Math.random ()));
		}
		catch (InterruptedException e) {}
	}
}
