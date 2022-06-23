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
				// this.bar.alertWaiter();
			}

		} while (!hasOrderBeenCompleted());

		cleanUp();
	}

	public void watchTheNews() { // kitchen
		try {
			chefState = kitchen.watchTheNews();
		} catch (RemoteException e) {
			GenericIO.writelnString("Chef remote exception on watchTheNews: " + e.getMessage());
			System.exit(1);
		}
	}

	public void startPreparation() { // kitchen
		try {
			chefState = kitchen.startPreparation();
		} catch (RemoteException e) {
			GenericIO.writelnString("Chef remote exception on startPreparation: " + e.getMessage());
			System.exit(1);
		}
	}

	public void continuePreparation() { // kitchen
		try {
			chefState = kitchen.continuePreparation();
		} catch (RemoteException e) {
			GenericIO.writelnString("Chef remote exception on continuePreparation: " + e.getMessage());
			System.exit(1);
		}
	}

	public void proceedToPresentation() { // kitchen
		try {
			chefState = kitchen.proceedToPresentation();
		} catch (RemoteException e) {
			GenericIO.writelnString("Chef remote exception on proceedToPresentation: " + e.getMessage());
			System.exit(1);
		}
	}

	public void alertWaiter() { // bar
		try {
			chefState = bar.alertWaiter();
		} catch (RemoteException e) {
			GenericIO.writelnString("Chef remote exception on alertWaiter: " + e.getMessage());
			System.exit(1);
		}
	}

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

	public void haveNextPortionReady() { // kitchen
		try {
			chefState = kitchen.haveNextPortionReady();
		} catch (RemoteException e) {
			GenericIO.writelnString("Chef remote exception on haveNextPortionReady: " + e.getMessage());
			System.exit(1);
		}
	}

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

	public void cleanUp() { // kitchen
		try {
			chefState = kitchen.cleanUp();
		} catch (RemoteException e) {
			GenericIO.writelnString("Chef remote exception on cleanUp: " + e.getMessage());
			System.exit(1);
		}
	}
}
