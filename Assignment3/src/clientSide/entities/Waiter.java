package clientSide.entities;

import java.rmi.*;
import interfaces.*;
import genclass.GenericIO;

/**
 * Waiter thread.
 *
 * Used to simulate the Waiter life cycle.
 */
public class Waiter extends Thread {
	/**
	 * Reference to the kitchen
	 */
	private final KitchenInterface kitchen;

	/**
	 * Reference to the bar
	 */
	private final BarInterface bar;

	/**
	 * Reference to the table
	 */
	private final TableInterface table;

	/**
	 * Waiter state
	 */
	private int waiterState;

	/**
	 * Instantiation of waiter thread
	 * 
	 * @param waiterState
	 * @param kit         reference to the kitchen stub
	 * @param bar         reference to the kitchen stub
	 * @param tab         reference to the table stub
	 */
	public Waiter(String name, int waiterState, KitchenInterface kit, BarInterface bar, TableInterface tab) {
		super(name);
		this.waiterState = waiterState;
		this.kitchen = kit;
		this.bar = bar;
		this.table = tab;
	}

	/**
	 * Life cycle of the waiter
	 */
	@Override
	public void run() {
		char request;
		boolean exit = false;

		do {
			request = lookAround();

			switch (request) {
			case 'e': // Client arriving, needs to be presented with the menu
				saluteClient(getStudentBeingAnswered());
				returnBar();
				break;
			case 'c': // Order will be described to the waiter
				getThePad();
				handNoteToChef();
				returnToBar();
				break;
			case 'a': // Portions need to be collected and delivered
				while (!haveAllClientsBeenServed()) {
					collectPortion();
					deliverPortion();
				}
				returnBar();
				break;
			case 's': // Bill needs to be prepared so it can be payed by the student
				preprareBill();
				presentBill();
				returnBar();
				break;
			case 'g': // Goodbye needs to be said to a student
				exit = sayGoodbye();
				break;
			}
			// If the last student has left the restaurant, life cycle may terminate
		} while (!exit);
	}

	/**
	 * Operation look Around Remote operation. It is called by the waiter, he checks
	 * for pending service requests and if not waits for them
	 * 
	 * @return Character that represents the service to be executed 'e' : means a
	 *         client has arrived therefore needs to be presented with the menu by
	 *         the waiter 'c' : means that the waiter will hear the order and
	 *         deliver it to the chef 'a' : means that a portion needs to be
	 *         delivered by the waiter 's' : means that the bill needs to be
	 *         prepared and presented by the waiter 'g' : means that some student
	 *         wants to leave and waiter needs to say goodbye
	 */
	public char lookAround() { // bar
		char c = '\0'; // return value

		try {
			c = bar.lookAround();
		} catch (RemoteException e) {
			GenericIO.writelnString("Waiter remote exception on lookAround: " + e.getMessage());
			System.exit(1);
		}
		if (c != 'e' && c != 'c' && c != 'a' && c != 's' && c != 'g') {
			GenericIO.writelnString("Invalid service type!");
			System.exit(1);
		}
		return c;
	}

	/**
	 * Operation salute the client Remote operation. It is called by the waiter when
	 * a student enters the restaurant and needs to be saluted Waiter waits for the
	 * student to take a seat (if he hasn't done it yet) Waiter waits for student to
	 * finish reading the menu
	 * 
	 * @param studentIdBeingAnswered id of the student whose request is being
	 *                               answered
	 */
	public void saluteClient(int studentID) { // table
		try {
			waiterState = table.saluteClient(studentID);
		} catch (RemoteException e) {
			GenericIO.writelnString("Waiter remote exception on saluteClient: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Return id of the student whose request is being answered Remote operation.
	 * 
	 * @return Id of the student whose request is being answered
	 */
	public int getStudentBeingAnswered() { // bar
		int ret = -1; // return value

		try {
			ret = bar.getStudentBeingAnswered();
		} catch (RemoteException e) {
			GenericIO.writelnString("Waiter remote exception on getStudentBeingAnswered: " + e.getMessage());
			System.exit(1);
		}
		if (ret == -1) {
			GenericIO.writelnString("Invalid student id!");
			System.exit(1);
		}
		return ret;
	}

	/**
	 * Operation return to the bar Remote operation. Called by the waiter when he is
	 * in the table and returns to the bar
	 */
	public void returnBar() { // table
		try {
			waiterState = table.returnBar();
		} catch (RemoteException e) {
			GenericIO.writelnString("Waiter remote exception on returnBar: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Operation get the pad Remote operation. It is called by the waiter when an
	 * order is going to be described by the first student to arrive Waiter Blocks
	 * waiting for student to describe him the order
	 */
	public void getThePad() { // table
		try {
			waiterState = table.getThePad();
		} catch (RemoteException e) {
			GenericIO.writelnString("Waiter remote exception on getThePad: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Operation hand note to chef Remote operation. Called by the waiter to wake
	 * chef up chef to give him the description of the order
	 */
	public void handNoteToChef() { // kitchen
		try {
			waiterState = kitchen.handNoteToChef();
		} catch (RemoteException e) {
			GenericIO.writelnString("Waiter remote exception on handNoteToChef: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Operation return to the bar Remote operation. Called by the waiter when he is
	 * in the kitchen and returns to the bar
	 */
	public void returnToBar() { // kitchen
		try {
			waiterState = kitchen.returnToBar();
		} catch (RemoteException e) {
			GenericIO.writelnString("Waiter remote exception on returnToBar: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Operation have all clients been served Remote operation. Called by the waiter
	 * to check if all clients have been served or not
	 * 
	 * @return true if all clients have been served, false otherwise
	 */
	public boolean haveAllClientsBeenServed() { // table
		boolean ret = false; // return value

		try {
			ret = table.haveAllClientsBeenServed();
		} catch (RemoteException e) {
			GenericIO.writelnString("Waiter remote exception on haveAllClientsBeenServed: " + e.getMessage());
			System.exit(1);
		}
		return ret;
	}

	/**
	 * Operation collect portion Remote operation. Called by the waiter when there
	 * is a portion to be delivered. Collect and signal chef that the portion was
	 * delivered
	 */
	public void collectPortion() { // kitchen
		try {
			waiterState = kitchen.collectPortion();
		} catch (RemoteException e) {
			GenericIO.writelnString("Waiter remote exception on collectPortion: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Operation deliver portion Remote operation. Called by the waiter, to deliver
	 * a portion
	 */
	public void deliverPortion() { // table
		try {
			table.deliverPortion();
		} catch (RemoteException e) {
			GenericIO.writelnString("Waiter remote exception on deliverPortion: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Operation prepare the Bill Remote operation. It is called the waiter to
	 * prepare the bill of the meal eaten by the students
	 */
	public void preprareBill() { // bar
		try {
			waiterState = bar.preprareBill();
		} catch (RemoteException e) {
			GenericIO.writelnString("Waiter remote exception on preprareBill: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Operation present the bill Remote operation. Called by the waiter to present
	 * the bill to the last student to arrive
	 */
	public void presentBill() { // table
		try {
			waiterState = table.presentBill();
		} catch (RemoteException e) {
			GenericIO.writelnString("Waiter remote exception on presentBill: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Operation say Goodbye Remote operation It is called by the waiter to say
	 * goodbye to a student that's leaving the restaurant
	 * 
	 * @return true if there are no more students at the restaurant, false otherwise
	 */
	public boolean sayGoodbye() { // bar
		boolean ret = false; // return value

		try {
			ret = bar.sayGoodbye();
		} catch (RemoteException e) {
			GenericIO.writelnString("Waiter remote exception on sayGoodbye: " + e.getMessage());
			System.exit(1);
		}
		return ret;
	}
}
