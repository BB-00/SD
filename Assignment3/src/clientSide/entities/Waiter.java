package clientSide.entities;

import java.rmi.*;
import interfaces.*;
import genclass.GenericIO;

/**
 *   Waiter thread.
 *
 *   Used to simulate the Waiter life cycle.
 */

public class Waiter extends Thread{
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
	 * 	Waiter state
	 */
	
	private int waiterState;
	
	/**
	 * 	Instantiation of waiter thread
	 * 
	 * 	@param waiterState
	 *  @param kit reference to the kitchen stub
	 *  @param bar reference to the kitchen stub
	 *  @param tab reference to the table stub
	 */
	
	public Waiter(String name, int waiterState, KitchenInterface kit, BarInterface bar, TableInterface tab) {
		super(name);
		this.waiterState = waiterState;
		this.kitchen = kit;
		this.bar = bar;
		this.table = tab;
	}
	
	/**
	 *	Life cycle of the waiter
	 */
	@Override
	public void run ()
	{
		char request;
		boolean exit = false;
		
		do {
			request = lookAround();
			
			switch(request)
			{
				case 'e':	//Client arriving, needs to be presented with the menu
					saluteClient(getStudentBeingAnswered());
					returnBar();
					break;
				case 'c':	//Order will be described to the waiter
					getThePad();
					handNoteToChef();
					returnToBar();
					break;
				case 'a':	//Portions need to be collected and delivered
					while(!haveAllClientsBeenServed()) {
						collectPortion();
						deliverPortion();
					}
					returnBar();
					break;
				case 's':	//Bill needs to be prepared so it can be payed by the student
					preprareBill();
					presentBill();
					returnBar();
					break;
				case 'g':	//Goodbye needs to be said to a student
					exit = sayGoodbye();
					break;
			}
			//If the last student has left the restaurant, life cycle may terminate
		}while (!exit);
	}
	
	public char lookAround() { // bar
		char c = '\0'; // return value

		try {
			c = bar.lookAround();
		} catch (RemoteException e) {
			GenericIO.writelnString("Waiter remote exception on lookAround: " + e.getMessage());
			System.exit(1);
		}
		if(c!='e' && c!='c' && c!='a' && c!='s' && c!='g')
		{
			GenericIO.writelnString("Invalid service type!");
			System.exit(1);			
		}
		return c;
	}
	
	public void saluteClient(int studentID) { // table
		try {
			waiterState = table.saluteClient(studentID);
		} catch (RemoteException e) {
			GenericIO.writelnString("Waiter remote exception on saluteClient: " + e.getMessage());
			System.exit(1);
		}
	}
	
	public int getStudentBeingAnswered() { // bar
		int ret = -1; // return value

		try {
			ret = bar.getStudentBeingAnswered();
		} catch (RemoteException e) {
			GenericIO.writelnString("Waiter remote exception on getStudentBeingAnswered: " + e.getMessage());
			System.exit(1);
		}
		if(ret == -1)
		{
			GenericIO.writelnString("Invalid student id!");
			System.exit(1);			
		}
		return ret;
	}
	
	public void returnBar() { // table
		try {
			waiterState = table.returnBar();
		} catch (RemoteException e) {
			GenericIO.writelnString("Waiter remote exception on returnBar: " + e.getMessage());
			System.exit(1);
		}
	}
	
	public void getThePad() { // table
		try {
			waiterState = table.getThePad();
		} catch (RemoteException e) {
			GenericIO.writelnString("Waiter remote exception on getThePad: " + e.getMessage());
			System.exit(1);
		}
	}
	
	public void handNoteToChef() { // kitchen
		try {
			waiterState = kitchen.handNoteToChef();
		} catch (RemoteException e) {
			GenericIO.writelnString("Waiter remote exception on handNoteToChef: " + e.getMessage());
			System.exit(1);
		}
	}
	
	public void returnToBar() { // kitchen
		try {
			waiterState = kitchen.returnToBar();
		} catch (RemoteException e) {
			GenericIO.writelnString("Waiter remote exception on returnToBar: " + e.getMessage());
			System.exit(1);
		}
	}
	
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
	
	public void collectPortion() { // kitchen
		try {
			waiterState = kitchen.collectPortion();
		} catch (RemoteException e) {
			GenericIO.writelnString("Waiter remote exception on collectPortion: " + e.getMessage());
			System.exit(1);
		}
	}
	
	public void deliverPortion() { // table
		try {
			table.deliverPortion();
		} catch (RemoteException e) {
			GenericIO.writelnString("Waiter remote exception on deliverPortion: " + e.getMessage());
			System.exit(1);
		}
	}
	
	public void preprareBill() { // bar
		try {
			waiterState = bar.preprareBill();
		} catch (RemoteException e) {
			GenericIO.writelnString("Waiter remote exception on preprareBill: " + e.getMessage());
			System.exit(1);
		}
	}
	
	public void presentBill() { // table
		try {
			waiterState = table.presentBill();
		} catch (RemoteException e) {
			GenericIO.writelnString("Waiter remote exception on presentBill: " + e.getMessage());
			System.exit(1);
		}
	}
	
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
