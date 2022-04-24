package entities;

import sharedRegions.Bar;
import sharedRegions.Kitchen;

/**
 *   Chef thread.
 *
 *   Used to simulate the Chef life cycle.
 */

public class Chef extends Thread{
	
	/**
	 *	Chef state 
	 */
	
	private int chefState;
	
	/**
	 * Reference to the kitchen
	 */
	
	private final Kitchen kitchen;
	
	/**
	 * Reference to the bar
	 */
	
	private final Bar bar;
	
	
	
	/**
	 * 	@param chef state
	 */
	public void setChefState(int chefState)
	{
		this.chefState = chefState;
	}
	
	/**
	 * 	@return chef state
	 */

	public int getChefState()
	{
		return chefState;
	}
	
	/**
	 * 	Instantiation of chef thread
	 * 
	 * 	@param chefState
	 */
	public Chef(String name, Kitchen kit, Bar bar) {
		super(name);
		this.chefState = ChefStates.WAITING_FOR_AN_ORDER;
		this.kitchen = kit;
		this.bar = bar;
	}

	/**
	 * 	Life cycle of the chef
	 */

	@Override
	public void run ()
	{
		boolean firstCourse = true;
		
		this.kitchen.watchTheNews();
		this.kitchen.startPreparation();
		do {
			if(!firstCourse) this.kitchen.continuePreparation();
			else firstCourse = false;
			
			this.kitchen.proceedToPresentation();
			this.bar.alertWaiter();
			
			while(!this.kitchen.haveAllPortionsBeenDelivered()) this.kitchen.haveNextPortionReady();
				
		} while(!this.kitchen.hasOrderBeenCompleted());
		
		this.kitchen.cleanUp();
	}
}