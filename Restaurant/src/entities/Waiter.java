package entities;

import sharedRegions.*;


/**
 *   Waiter thread.
 *
 *   Used to simulate the Waiter life cycle.
 */

public class Waiter extends Thread{
	/**
	 * Reference to the kitchen
	 */
	
	private final Kitchen kitchen;
	
	/**
	 * Reference to the bar
	 */
	
	private final Bar bar;
	
	/**
	 * Reference to the table
	 */
	private final Table table;

	/**
	 * 	Waiter state
	 */
	
	private int waiterState;
	
	/**
	 * 	@param waiter state
	 */
	
	public void setWaiterState(int waiterState) {
		this.waiterState = waiterState;
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
	
	public Waiter(String name, Kitchen kit, Bar bar, Table tab) {
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
			request = this.bar.lookAround();
			
			switch(request)
			{
				case 'e':	//Client arriving, needs to be presented with the menu
					this.table.saluteClient(bar.getStudentBeingAnswered());
					this.table.returnBar();
					break;
				case 'c':	//Order will be described to the waiter
					this.table.getThePad();
					this.kitchen.handNoteToChef();
					this.kitchen.returnToBar();
					break;
				case 'a':	//Portions need to be collected and delivered
					while(!this.table.haveAllClientsBeenServed()) {
						this.kitchen.collectPortion();
						this.table.deliverPortion();
					}
					this.table.returnBar();
					break;
				case 's':	//Bill needs to be prepared so it can be payed by the student
					this.bar.preprareBill();
					this.table.presentBill();
					this.table.returnBar();
					break;
				case 'g':	//Goodbye needs to be said to a student
					exit = this.bar.sayGoodbye();
					break;
			}
			//If the last student has left the restaurant, life cycle may terminate
		}while (!exit);
	}
}