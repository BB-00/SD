package clientSide.entities;

import java.rmi.*;
import interfaces.*;
import genclass.GenericIO;
import commInfra.ExecConsts;

/**
 * Student thread.
 *
 * It simulates the student life cycle.
 */
public class Student extends Thread {

	/**
	 * Reference to the Table
	 */
	private final TableInterface table;

	/**
	 * Reference to the Bar
	 */
	private final BarInterface bar;

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
	 * @param studentID student id
	 * @param reference to bar stub
	 * @param reference to table stub
	 */
	public Student(String name, int studentID, int studentState, BarInterface bar, TableInterface table) {
		super(name);
		this.studentID = studentID;
		this.studentState = studentState;
		this.bar = bar;
		this.table = table;
	}

	/**
	 * Life cycle of the student
	 */

	@Override
	public void run() {
		walkABit();
		enter();
		readMenu();

		if (studentID == getFirstToArrive()) {
			prepareOrder();
			do {
				addUpOnesChoices();
			} while (!everybodyHasChosen());
			callWaiter();
			describeOrder();
			joinTalk();
		} else
			informCompanion();
		// do{
		// this.table.startEating();
		// this.table.endEating();
		// while(!this.table.hasEverybodyFinishedEating());
		// if(studentID == this.table.getLastToEat()) this.bar.signalWaiter();
		// }while(!this.table.haveAllCoursesBeenEaten());

		int coursesEatenNum = 0;
		while (!haveAllCoursesBeenEaten()) {
			// if(table.haveAllClientsBeenServed()){
			startEating();
			endEating();
			coursesEatenNum++;

			while (!hasEverybodyFinishedEating())
				;
			if (studentID == getLastToEat() && coursesEatenNum != ExecConsts.M)
				signalWaiter();
			// }
		}

		if (shouldHaveArrivedEarlier()) {
			signalWaiter();
			honourBill();
		}
		exit();
	}

	/**
	 * sleep for a random time
	 */
	private void walkABit() {
		try {
			sleep((long) (1 + 50 * Math.random()));
		} catch (InterruptedException e) {
		}
	}

	public void enter() { // bar
		try {
			studentState = bar.enter(studentID);
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on enter: " + e.getMessage());
			System.exit(1);
		}
	}

	public void readMenu() { // table
		try {
			studentState = table.readMenu(studentID);
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on readMenu: " + e.getMessage());
			System.exit(1);
		}
	}

	public int getFirstToArrive() { // table
		int firstToArrive=-1;
		
		try {
			firstToArrive = table.getFirstToArrive();
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on getFirstToArrive: " + e.getMessage());
			System.exit(1);
		}
		if (firstToArrive == -1)
		{			
			GenericIO.writelnString("Invalid id received in getFirstToArrive");
			System.exit(1);	
		}
		return firstToArrive;
	}

	public void prepareOrder() { // table
		try {
			studentState = table.prepareOrder();
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on prepareOrder: " + e.getMessage());
			System.exit(1);
		}
	}

	public void addUpOnesChoices() { // table
		try {
			table.addUpOnesChoices();
		} catch (RemoteException e) {
			GenericIO
					.writelnString("Student " + studentID + " remote exception on addUpOnesChoices: " + e.getMessage());
			System.exit(1);
		}
	}

	public boolean everybodyHasChosen() { // table
		boolean ret = false; // return value

		try {
			ret = table.everybodyHasChosen();
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on everybodyHasChosen: " + e.getMessage());
			System.exit(1);
		}
		return ret;
	}

	public void callWaiter() { // bar
		try {
			bar.callWaiter(studentID);
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on callWaiter: " + e.getMessage());
			System.exit(1);
		}
	}

	public void describeOrder() { // table
		try {
			table.describeOrder();
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on describeOrder: " + e.getMessage());
			System.exit(1);
		}
	}

	public void joinTalk() { // table
		try {
			studentState = table.joinTalk();
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on joinTalk: " + e.getMessage());
			System.exit(1);
		}
	}

	public void informCompanion() { // table
		try {
			studentState = table.informCompanion(studentID);
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on informCompanion: " + e.getMessage());
			System.exit(1);
		}
	}

	public boolean haveAllCoursesBeenEaten() { // table
		boolean ret = false; // return value

		try {
			ret = table.haveAllCoursesBeenEaten();
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on haveAllCoursesBeenEaten: " + e.getMessage());
			System.exit(1);
		}
		return ret;
	}

	public void startEating() { // table
		try {
			studentState = table.startEating(studentID);
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on startEating: " + e.getMessage());
			System.exit(1);
		}
	}

	public void endEating() { // table
		try {
			studentState = table.endEating(studentID);
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on endEating: " + e.getMessage());
			System.exit(1);
		}
	}

	public boolean hasEverybodyFinishedEating() { // table
		boolean ret = false; // return value

		try {
			ret = table.hasEverybodyFinishedEating(studentID);
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on hasEverybodyFinishedEating: " + e.getMessage());
			System.exit(1);
		}
		return ret;
	}

	public int getLastToEat() { // table
		int ret = -1; // return value

		try {
			ret = table.getLastToEat();
		} catch (RemoteException e) {
			GenericIO
					.writelnString("Student " + studentID + " remote exception on getLastToEat: " + e.getMessage());
			System.exit(1);
		}
		return ret;
	}

	public void signalWaiter() { // bar
		try {
			bar.signalWaiter(studentID, studentState);
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on signalWaiter: " + e.getMessage());
			System.exit(1);
		}
	}

	public boolean shouldHaveArrivedEarlier() { // table
		ReturnBoolean ret = null; // return value

		try {
			ret = table.shouldHaveArrivedEarlier(studentID);
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on shouldHaveArrivedEarlier: " + e.getMessage());
			System.exit(1);
		}
		studentState = ret.getIntStateVal();
		return ret.getBooleanVal();
	}

	public void honourBill() { // table
		try {
			table.honourBill();
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on honourBill: " + e.getMessage());
			System.exit(1);
		}
	}

	public void exit() { // bar
		try {
			studentState = bar.exit(studentID);
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on exit: " + e.getMessage());
			System.exit(1);
		}
	}

}
