package entities;

import sharedRegions.Bar;
import sharedRegions.Table;
import commInfra.*;

/**
 * Student thread.
 *
 * It simulates the student life cycle.
 */
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
	private int studentID;

	/**
	 * Student States
	 */
	private int studentState;

	/**
	 * Instatiation of a student thread
	 * 
	 * @param studentID, student id
	 * @param studentState, student state
	 * @param bar, reference to bar
	 * @param table, reference to table
	 */
	public Student(String name, int studentID, int studentState, Bar bar, Table table) {
		super(name);
		this.studentID = studentID;
		this.studentState = studentState;
		this.bar = bar;
		this.table = table;
	}

	/**
	 * Set the studentID
	 * 
	 * @param id, student id
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
	 * Set the student state
	 * 
	 * @param state, student state
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
	 * Life cycle of the student
	 */
	@Override
	public void run() {
		walkABit();
		bar.enter();
		table.readMenu();

		if (studentID == table.getFirstToArrive()) {
			table.prepareOrder();
			do {
				table.addUpOnesChoices();
			} while (!table.everybodyHasChosen());
			bar.callWaiter();
			table.describeOrder();
			table.joinTalk();
		} else
			table.informCompanion();

		int coursesEatenNum=0;
		while (!table.haveAllCoursesBeenEaten()) {
			table.startEating();
			table.endEating();
			coursesEatenNum++;
			
			while (!table.hasEverybodyFinishedEating());
			
			if (studentID == table.getLastToEat() && coursesEatenNum != ExecConsts.M)
				bar.signalWaiter();
		}

		if (table.shouldHaveArrivedEarlier()) {
			bar.signalWaiter();
			table.honourBill();
		}
		bar.exit();
	}

	private void walkABit() {
		try {
			sleep((long) (1 + 50 * Math.random()));
		} catch (InterruptedException e) {
		}
	}

}