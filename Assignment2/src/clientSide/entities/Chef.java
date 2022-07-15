package clientSide.entities;

import clientSide.stubs.BarStub;
import clientSide.stubs.KitchenStub;

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
	private final KitchenStub kitchen;

	/**
	 * Reference to the bar
	 */
	private final BarStub bar;

	/**
	 * Set chef state.
	 *
	 * @param state new chef state
	 */
	public void setChefState(int state) {
		this.chefState = state;
	}

	/**
	 * Get chef state.
	 *
	 * @return chef state
	 */
	public int getChefState() {
		return chefState;
	}

	/**
	 * Instantiation of chef thread
	 * 
	 * @param name    thread name
	 * @param kitchen reference to the kitchen
	 * @param bar     reference to the bar
	 */
	public Chef(String name, int chefState, KitchenStub kitchen, BarStub bar) {
		super(name);
		this.chefState = chefState;
		this.kitchen = kitchen;
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