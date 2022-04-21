package entities;

import sharedRegions.Table;

public class Student extends Thread {
	/**
	 * Reference to the Table
	 */
	private final Table table;
//	private final Bar bar;
	/**
	 * Student ID
	 */
	private  int studentID;
	/**
	 * Student States
	 * 
	 */
	private int studentState;
	/**
	 * Seat Number where Student is seated
	 */
	private int seatNum;
	
	public Student(String name, int studentID, Table table) {
		super(name);
		this.table = table;
		this.studentID = studentID;
		this.studentState = StudentStates.GOING_TO_THE_RESTAURANT;
		this.seatNum = -1;
	}
	
	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}
	
	public int getStudentID() {
		return this.studentID;
	}
	
	public void setStudentState(int studentState) {
		this.studentState = studentState;
	}
	
	public int getStudentState() {
		return this.studentState;
	}
	
	
	@Override
	public void run() {
		walkABit();
		enter();
		Table.readTheMenu();
		if(Table.isFirstToArrive()){
			Table.prepareTheOrder();
			do{
				Table.addUpOnesChoices();
			}while(!Table.hasEverybodyChoices());
			Table.callTheWaiter();
			Table.describeTheOrder();
			Table.jointTheTalk();
		}else{
			Table.informCompanion();
		}
		do{
			Table.startEating();
			Table.endEating();
			if(Table.isLastToFinishEating()) Table.signalTheWaiter();
		}while(!Table.haveAllCoursesBeenServed());
		if(Table.isLastToArrive()){
			Table.shouldHaveArrivedEarlier();
			Table.honourTheBill();
		}
		Table.exit();
	}
	
	private void walkABit() {
		try
		{ sleep ((long) (1 + 50 * Math.random ()));
		}
		catch (InterruptedException e) {}
	}
	
	private void enter() {
		
	}
}
