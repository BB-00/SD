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

		int coursesEatenNum = 0;
		while (!haveAllCoursesBeenEaten()) {
			startEating();
			endEating();
			coursesEatenNum++;

			while (!hasEverybodyFinishedEating());
			
			if (studentID == getLastToEat() && coursesEatenNum != ExecConsts.M)
				signalWaiter();
		}

		if (shouldHaveArrivedEarlier()) {
			signalWaiter();
			honourBill();
		}
		exit();
	}

	/**
	 * Sleep for a random time Internal operation.
	 */
	private void walkABit() {
		try {
			sleep((long) (1 + 50 * Math.random()));
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Operation enter the restaurant Remote operation. It is called by the student
	 * to signal that he is entering the restaurant
	 */
	public void enter() { // bar
		try {
			studentState = bar.enter(studentID);
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on enter: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Operation read the menu Remote Operation. Called by the student to read a
	 * menu, wakes up waiter to signal that he already read the menu
	 */
	public void readMenu() { // table
		try {
			studentState = table.readMenu(studentID);
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on readMenu: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Obtain id of the first student to arrive Remote operation.
	 * 
	 * @return id of the first student to arrive at the restaurant
	 */
	public int getFirstToArrive() { // table
		int firstToArrive = -1;

		try {
			firstToArrive = table.getFirstToArrive();
		} catch (RemoteException e) {
			GenericIO
					.writelnString("Student " + studentID + " remote exception on getFirstToArrive: " + e.getMessage());
			System.exit(1);
		}
		if (firstToArrive == -1) {
			GenericIO.writelnString("Invalid id received in getFirstToArrive");
			System.exit(1);
		}
		return firstToArrive;
	}

	/**
	 * Operation prepare the order Remote operation. Called by the student to begin
	 * the preparation of the order (options of his companions)
	 */
	public void prepareOrder() { // table
		try {
			studentState = table.prepareOrder();
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on prepareOrder: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Operation add up ones choices Remote operation. Called by the first student
	 * to arrive to add up a companions choice to the order
	 */
	public void addUpOnesChoices() { // table
		try {
			table.addUpOnesChoices();
		} catch (RemoteException e) {
			GenericIO
					.writelnString("Student " + studentID + " remote exception on addUpOnesChoices: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Operation everybody has chosen Remote operation. Called by the first student
	 * to arrive to check if all his companions have choose or not Blocks if not
	 * waiting to be waker up be a companion to give him his preference
	 * 
	 * @return true if everybody choose their course choice, false otherwise
	 */
	public boolean everybodyHasChosen() { // table
		boolean ret = false; // return value

		try {
			ret = table.everybodyHasChosen();
		} catch (RemoteException e) {
			GenericIO.writelnString(
					"Student " + studentID + " remote exception on everybodyHasChosen: " + e.getMessage());
			System.exit(1);
		}
		return ret;
	}

	/**
	 * Operation call the waiter Remote operation. It is called by the first student
	 * to arrive the restaurant to call the waiter to describe the order
	 *
	 */
	public void callWaiter() { // bar
		try {
			bar.callWaiter(studentID);
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on callWaiter: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Operation describe the order Remote operation. Called by the first student to
	 * arrive to describe the order to the waiter Blocks waiting for waiter to come
	 * with pad Wake waiter up so he can take the order
	 */
	public void describeOrder() { // table
		try {
			table.describeOrder();
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on describeOrder: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Operation join the talk Remote operation. Called by the first student to
	 * arrive so he can join his companions while waiting for the courses to be
	 * delivered
	 */
	public void joinTalk() { // table
		try {
			studentState = table.joinTalk();
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on joinTalk: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Operation inform companion Remote Operation. Called by a student to inform
	 * the first student to arrive about their preferences Blocks if someone else is
	 * informing at the same time
	 */
	public void informCompanion() { // table
		try {
			studentState = table.informCompanion(studentID);
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on informCompanion: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Operation have all courses been eaten Remote operation. Called by the student
	 * to check if there are more courses to be eaten Student blocks waiting for the
	 * course to be served
	 * 
	 * @return true if all courses have been eaten, false otherwise
	 */
	public boolean haveAllCoursesBeenEaten() { // table
		boolean ret = false; // return value

		try {
			ret = table.haveAllCoursesBeenEaten();
		} catch (RemoteException e) {
			GenericIO.writelnString(
					"Student " + studentID + " remote exception on haveAllCoursesBeenEaten: " + e.getMessage());
			System.exit(1);
		}
		return ret;
	}

	/**
	 * Operation start eating Remote operation. Called by the student to start
	 * eating the meal (During random time)
	 */
	public void startEating() { // table
		try {
			studentState = table.startEating(studentID);
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on startEating: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Operation end eating Remote operation. Called by the student to signal that
	 * he has finished eating his meal
	 */
	public void endEating() { // table
		try {
			studentState = table.endEating(studentID);
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on endEating: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Operation has everybody finished eating Remote operation. Called by the
	 * student to wait for his companions to finish eating
	 * 
	 * @return true if everybody has finished eating, false otherwise
	 */
	public boolean hasEverybodyFinishedEating() { // table
		boolean ret = false; // return value

		try {
			ret = table.hasEverybodyFinishedEating(studentID);
		} catch (RemoteException e) {
			GenericIO.writelnString(
					"Student " + studentID + " remote exception on hasEverybodyFinishedEating: " + e.getMessage());
			System.exit(1);
		}
		return ret;
	}

	/**
	 * Obtain id of the last student to arrive Remote operation.
	 * 
	 * @return id of the last student to finish eating a meal
	 */
	public int getLastToEat() { // table
		int ret = -1; // return value

		try {
			ret = table.getLastToEat();
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on getLastToEat: " + e.getMessage());
			System.exit(1);
		}
		return ret;
	}

	/**
	 * Operation signal the waiter Remote operation. It is called by the last
	 * student to finish eating that next course can be brought signal chef that he
	 * can put request in the queue and waiter that he proceed his executing to
	 * collect portions It is also used by last student to arrive to signal that he
	 * wishes to pay the bill
	 */
	public void signalWaiter() { // bar
		try {
			bar.signalWaiter(studentID, studentState);
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on signalWaiter: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Operation should have arrived earlier Remote operation. Called by the student
	 * to check which one was last to arrive
	 * 
	 * @return True if current student was the last to arrive, false otherwise
	 */
	public boolean shouldHaveArrivedEarlier() { // table
		ReturnBoolean ret = null; // return value

		try {
			ret = table.shouldHaveArrivedEarlier(studentID);
		} catch (RemoteException e) {
			GenericIO.writelnString(
					"Student " + studentID + " remote exception on shouldHaveArrivedEarlier: " + e.getMessage());
			System.exit(1);
		}
		studentState = ret.getIntStateVal();
		return ret.getBooleanVal();
	}

	/**
	 * Operation honour the bill Remote operation. Called by the student to pay the
	 * bill Student blocks waiting for bill to be presented and signals waiter when
	 * it's time to pay it
	 */
	public void honourBill() { // table
		try {
			table.honourBill();
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on honourBill: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Operation exit the restaurant Remote operation. It is called by a student
	 * when he leaves the restaurant
	 */
	public void exit() { // bar
		try {
			studentState = bar.exit(studentID);
		} catch (RemoteException e) {
			GenericIO.writelnString("Student " + studentID + " remote exception on exit: " + e.getMessage());
			System.exit(1);
		}
	}

}
