package clientSide.entities;

import java.rmi.*;
import interfaces.*;
import genclass.GenericIO;

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
	private final KitchenInterface kitchen;

	/**
	 * Reference to the bar
	 */
	private final BarInterface bar;

	/**
	 * Instantiation of chef thread
	 * 
	 * @param name thread name
	 * @param kit  reference to the kitchen stub
	 * @param bar  reference to the bar stub
	 */
	public Chef(String name, int chefState, KitchenInterface kit, BarInterface bar) {
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

		watchTheNews();
		startPreparation();
		do {
			if (!firstCourse)
				continuePreparation();
			else
				firstCourse = false;

			proceedToPresentation();
			alertWaiter();

			while (!haveAllPortionsBeenDelivered()) {
				haveNextPortionReady();
			}

		} while (!hasOrderBeenCompleted());

		cleanUp();
	}

	/**
	 * 	Operation watch the news
	 * 	Remote operation.
	 * 	It is called by the chef, he waits for waiter to notify him of the order
	 */
	public void watchTheNews() { // kitchen
		try {
			chefState = kitchen.watchTheNews();
		} catch (RemoteException e) {
			GenericIO.writelnString("Chef remote exception on watchTheNews: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * 	Operation start presentation
	 * 	Remote operation.
	 * 	It is called by the chef after waiter has notified him of the order to be prepared 
	 * 	to signal that preparation of the course has started
	 */
	public void startPreparation() { // kitchen
		try {
			chefState = kitchen.startPreparation();
		} catch (RemoteException e) {
			GenericIO.writelnString("Chef remote exception on startPreparation: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * 	Operation continue preparation
	 *  Remote operation.
	 * 	It is called by the chef when all portions have been delivered, but the course has not been completed yet
	 */
	public void continuePreparation() { // kitchen
		try {
			chefState = kitchen.continuePreparation();
		} catch (RemoteException e) {
			GenericIO.writelnString("Chef remote exception on continuePreparation: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * 	Operation proceed presentation
	 *  Remote operation.
	 * 	It is called by the chef when a portion needs to be prepared
	 */
	public void proceedToPresentation() { // kitchen
		try {
			chefState = kitchen.proceedToPresentation();
		} catch (RemoteException e) {
			GenericIO.writelnString("Chef remote exception on proceedToPresentation: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Operation alert the waiter
	 * Remote operation.
	 * It is called by the chef to alert the waiter that a portion was dished
	 * 	A request is putted in the queue (chef id will be N+1)
	 */
	public void alertWaiter() { // bar
		try {
			chefState = bar.alertWaiter();
		} catch (RemoteException e) {
			GenericIO.writelnString("Chef remote exception on alertWaiter: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * 	Operation have all portions been delivered
	 *  Remote operation.
	 * 	It is called by the chef when he finishes a portion and checks if another one needs to be prepared or not
	 * 	It is also here were the chef blocks waiting for waiter do deliver the current portion
	 * 	@return true if all portions have been delivered, false otherwise
	 */
	public boolean haveAllPortionsBeenDelivered() { // kitchen
		boolean ret = false; // return value

		try {
			ret = kitchen.haveAllPortionsBeenDelivered();
		} catch (RemoteException e) {
			GenericIO.writelnString("Chef remote exception on haveAllPortionsBeenDelivered: " + e.getMessage());
			System.exit(1);
		}
		return ret;
	}

	/**
	 * Operation have next portion ready
	 * Remote operation.
	 * It is called by the chef after a portion has been delivered and another one needs to be prepared
	 */
	public void haveNextPortionReady() { // kitchen
		try {
			chefState = kitchen.haveNextPortionReady();
		} catch (RemoteException e) {
			GenericIO.writelnString("Chef remote exception on haveNextPortionReady: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 *	Operation has order been completed
	 *  Remote operation.
	 * 	It is called by the chef when he finishes preparing all courses to check if order has been completed or not
	 * 	@return true if all courses have been completed, false or not
	 */
	public boolean hasOrderBeenCompleted() { // kitchen
		boolean ret = false; // return value

		try {
			ret = kitchen.hasOrderBeenCompleted();
		} catch (RemoteException e) {
			GenericIO.writelnString("Chef remote exception on hasOrderBeenCompleted: " + e.getMessage());
			System.exit(1);
		}
		return ret;
	}

	/**
	 * Operation clean up
	 * Remote operation.
	 * It is called by the chef when he finishes the order, to close service
	 */
	public void cleanUp() { // kitchen
		try {
			chefState = kitchen.cleanUp();
		} catch (RemoteException e) {
			GenericIO.writelnString("Chef remote exception on cleanUp: " + e.getMessage());
			System.exit(1);
		}
	}
}
