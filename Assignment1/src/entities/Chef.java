package entities;

import sharedRegions.Bar;
import sharedRegions.Kitchen;

/**
 * Chef thread.
 *
 * Used to simulate the Chef life cycle.
 */
public class Chef extends Thread {

	/**
	 * Chef state
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
	 * Set Chef State
	 * 
	 * @param chef state
	 */
	public void setChefState(int state) {
		chefState = state;
	}

	/**
	 * @return chef state
	 */
	public int getChefState() {
		return chefState;
	}

	/**
	 * Instantiation of chef thread
	 * 
	 * @param name thread name
	 * @param kit  reference to the kitchen stub
	 * @param bar  reference to the bar stub
	 */
	public Chef(String name, int chefState, Kitchen kit, Bar bar) {
		super(name);
		this.chefState = chefState;
		this.kitchen = kit;
		this.bar = bar;
	}

	/**
	 * Life cycle of the chef
	 */
	@Override
	public void run() {
		boolean firstCourse = true;

		kitchen.watchTheNews();
		kitchen.startPreparation();
		do {
			if (!firstCourse)
				kitchen.continuePreparation();
			else
				firstCourse = false;

			kitchen.proceedToPresentation();
			bar.alertWaiter();

			while (!kitchen.haveAllPortionsBeenDelivered()) {
				kitchen.haveNextPortionReady();
			}

		} while (!kitchen.hasOrderBeenCompleted());

		kitchen.cleanUp();
	}
}