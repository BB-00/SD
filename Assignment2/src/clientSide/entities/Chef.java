package clientSide.entities;

import clientSide.stubs.BarStub;
import clientSide.stubs.KitchenStub;

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
	private final KitchenStub kitchen;
	
	/**
	 * Reference to the bar
	 */
	private final BarStub bar;
	
	
	
	/**
	 * 	@param chef state
	 */
	public void setChefState(int state) {
		this.chefState = state;
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
	 * 	@param name thread name
	 *  @param kit reference to the kitchen stub
	 *  @param bar reference to the bar stub
	 */
	public Chef(String name, KitchenStub kit, BarStub bar) {
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
		
		kitchen.watchTheNews();
		kitchen.startPreparation();
		do {
			if(!firstCourse) kitchen.continuePreparation();
			else firstCourse = false;
			
			kitchen.proceedToPresentation();
			bar.alertWaiter();
			
			while(!kitchen.haveAllPortionsBeenDelivered()) {
				kitchen.haveNextPortionReady();
				//this.bar.alertWaiter();
			}
				
		} while(!kitchen.hasOrderBeenCompleted());
		
		kitchen.cleanUp();
	}
}