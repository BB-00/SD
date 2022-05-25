package clientSide.entities;

import clientSide.stubs.KitchenStub;
import clientSide.stubs.BarStub;
import clientSide.stubs.TableStub;

/**
 *   Waiter thread.
 *
 *   Used to simulate the Waiter life cycle.
 */

public class Waiter extends Thread{
	/**
	 * Reference to the kitchen
	 */
	
	private final KitchenStub kitchen;
	
	/**
	 * Reference to the bar
	 */
	
	private final BarStub bar;
	
	/**
	 * Reference to the table
	 */
	private final TableStub table;

	/**
	 * 	Waiter state
	 */
	
	private int waiterState;
	
	/**
	 * 	@param waiter state
	 */
	
	public void setWaiterState(int state) {
		waiterState = state;
	}
	
	/**
	 * 	@return waiter state
	 */

	public int getWaiterState() {
		return waiterState;
	}
	
	/**
	 * 	Instantiation of waiter thread
	 * 
	 * 	@param waiterState
	 */
	
	public Waiter(String name, KitchenStub kit, BarStub bar, TableStub tab) {
		super(name);
		this.waiterState = WaiterStates.APPRAISING_SITUATION;
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
			request = bar.lookAround();
			
			switch(request)
			{
				case 'e':	//Client arriving, needs to be presented with the menu
					table.saluteClient(bar.getStudentBeingAnswered());
					table.returnBar();
					break;
				case 'c':	//Order will be described to the waiter
					table.getThePad();
					kitchen.handNoteToChef();
					kitchen.returnToBar();
					break;
				case 'a':	//Portions need to be collected and delivered
					while(!table.haveAllClientsBeenServed()) {
						kitchen.collectPortion();
						table.deliverPortion();
					}
					table.returnBar();
					break;
				case 's':	//Bill needs to be prepared so it can be payed by the student
					bar.preprareBill();
					table.presentBill();
					table.returnBar();
					break;
				case 'g':	//Goodbye needs to be said to a student
					exit = bar.sayGoodbye();
					break;
			}
			//If the last student has left the restaurant, life cycle may terminate
		}while (!exit);
	}
}