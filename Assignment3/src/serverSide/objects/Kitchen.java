package serverSide.objects;

import java.rmi.RemoteException;
import clientSide.entities.*;
import interfaces.*;
import serverSide.main.*;

/**
 * Kitchen
 * 
 * It is responsible for keeping track of portions prepared and delivered. Is
 * implemented as an implicit monitor. All public methods are executed in mutual
 * exclusion. Synchronization points include: Chef has to wait for the note that
 * describes the order given by the waiter Chef has to wait for waiter to
 * collect portions Waiter has to wait for chef to start preparing the order
 * Waiter has to wait for portions from the chef
 *
 */
public class Kitchen {
	/**
	 * Number of entity groups requesting the shutdown.
	 */
	private int nEntities;

	/**
	 * Number of portions that are ready to serve
	 */
	private int numberOfPortionsCooked;

	/**
	 * Number of portions that have been served, in each course;
	 */
	private int numberOfPortionsServed;

	/**
	 * Number of courses that have been served
	 */
	private int numberOfCoursesServed;

	/**
	 * Reference to General Repositories Interface
	 */
	private final GenReposInterface repos;

	/**
	 * Kitchen Instantiation
	 * 
	 * @param repos reference to the general repository Interface
	 */
	public Kitchen(GenReposInterface rep) {
		this.nEntities = 0;
		this.numberOfCoursesServed = 0;
		this.numberOfPortionsCooked = 0;
		this.numberOfPortionsServed = 0;
		this.repos = rep;
	}

	/**
	 * 
	 * Chef Operations
	 * 
	 */

	/**
	 * Operation what the news, in this operation the chef is waiting for the waiter
	 * to give in an order
	 */
	@Override
	public synchronized int watchTheNews() throws RemoteException {
		// if (chef.getChefState() != ChefStates.WAITING_FOR_AN_ORDER) {
		// 	chef.setChefState(ChefStates.WAITING_FOR_AN_ORDER);
		// 	repos.updateChefState(ChefStates.WAITING_FOR_AN_ORDER);
		// }
		repos.updateChefState(ChefStates.WAITING_FOR_AN_ORDER); 

		try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ChefStates.WAITING_FOR_AN_ORDER;
	}

	/**
	 * Operation start preparation, in this operation the chef will start the
	 * preparation of a course
	 */
	@Override
	public synchronized int startPreparation() throws RemoteException {

		// if (chef.getChefState() != ChefStates.PREPARING_THE_COURSE) {
		// 	chef.setChefState(ChefStates.PREPARING_THE_COURSE);
		// 	repos.updateChefState(ChefStates.PREPARING_THE_COURSE);
		// }
		repos.updateChefState(ChefStates.PREPARING_THE_COURSE); // setnPortions


		notifyAll();

		return ChefStates.PREPARING_THE_COURSE;
	}

	/**
	 * Operation proceed to presentation, in this operation the chef will dish each
	 * portion
	 */
	@Override
	public synchronized int proceedToPresentation() throws RemoteException {
		// if (chef.getChefState() != ChefStates.DISHING_THE_PORTIONS) {
		// 	chef.setChefState(ChefStates.DISHING_THE_PORTIONS);
		// 	repos.updateChefState(ChefStates.DISHING_THE_PORTIONS);
		// }
		repos.updateChefState(ChefStates.DISHING_THE_PORTIONS); // setnPortions


		numberOfPortionsCooked++;

		return ChefStates.DISHING_THE_PORTIONS;
	}

	/**
	 * Operation have next portion ready, the chef has just served one portion and
	 * needs to dish another one
	 */
	@Override
	public synchronized int haveNextPortionReady() throws RemoteException {
		// if (chef.getChefState() != ChefStates.DISHING_THE_PORTIONS) {
		// 	chef.setChefState(ChefStates.DISHING_THE_PORTIONS);
		// 	repos.updateChefState(ChefStates.DISHING_THE_PORTIONS);
		// }
		repos.updateChefState(ChefStates.DISHING_THE_PORTIONS); // setnPortions


		numberOfPortionsCooked++;

		// if (chef.getChefState() != ChefStates.DELIVERING_THE_PORTIONS) {
		// 	chef.setChefState(ChefStates.DELIVERING_THE_PORTIONS);
		// 	repos.updateChefState(ChefStates.DELIVERING_THE_PORTIONS);
		// }
		repos.updateChefState(ChefStates.DELIVERING_THE_PORTIONS);


		notifyAll();

		return ChefStates.DELIVERING_THE_PORTIONS
	}

	/**
	 * Operation continue preparation, in this operation the chef has finished one
	 * course and will start preparating another one
	 */
	@Override
	public synchronized int continuePreparation() throws RemoteException {
		// if (chef.getChefState() != ChefStates.PREPARING_THE_COURSE) {
		// 	chef.setChefState(ChefStates.PREPARING_THE_COURSE);
		// 	repos.updateChefState(ChefStates.PREPARING_THE_COURSE);
		// }

		repos.updateChefState(ChefStates.PREPARING_THE_COURSE); // setnPortions

		return ChefStates.PREPARING_THE_COURSE;

	}

	/**
	 * Operation have all portions been served, in this operation the chef checks if
	 * all portions have been served before starting preparing another portion
	 *
	 * @return true if all portions have been served, false otherwise
	 */
	@Override
	public synchronized boolean haveAllPortionsBeenDelivered() throws RemoteException {
		while (numberOfPortionsCooked != 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (numberOfPortionsServed == ExecConsts.N) {
			numberOfCoursesServed++;
			return true;
		}
		return false;

	}

	/**
	 * Operation has the order been completed, the chef checks if the order has been
	 * completed or not
	 *
	 * @return true if the order has been completed, false otherwise
	 */
	@Override
	public synchronized boolean hasOrderBeenCompleted() throws RemoteException {
		// ------------------ DEBUG ---------------
		// System.out.println("Courses served: " + numberOfCoursesServed);
		if (numberOfCoursesServed == ExecConsts.M)
			return true;
		return false;
	}

	/**
	 * Operation clean up, the chef has finished the order and starts cleaning the
	 * kitchen
	 */
	@Override
	public synchronized int cleanUp() throws RemoteException {
		// if (chef.getChefState() != ChefStates.CLOSING_SERVICE) {
		// 	chef.setChefState(ChefStates.CLOSING_SERVICE);
		// 	repos.updateChefState(ChefStates.CLOSING_SERVICE);
		// }
		repos.updateChefState(ChefStates.CLOSING_SERVICE);


		// ---------------- DEBUG -----------------
		// System.out.println("Chef has closed Service!");
		return ChefStates.CLOSING_SERVICE;
	}

	/**
	 * 
	 * Waiter Operations
	 * 
	 */

	/**
	 * Operation return to bar, the waiter is in the kitchen and returns to the bar
	 */
	@Override
	public synchronized int returnToBar() throws RemoteException {
		// if (waiter.getWaiterState() != WaiterStates.APPRAISING_SITUATION) {
		// 	waiter.setWaiterState(WaiterStates.APPRAISING_SITUATION);
		// 	repos.updateWaiterState(WaiterStates.APPRAISING_SITUATION);
		// }
		repos.updateWaiterState(WaiterStates.APPRAISING_SITUATION);
		return WaiterStates.APPRAISING_SITUATION;
	}

	/**
	 * Operation hand note to chef, the waiter hands the order note to the chef and
	 * wakes him up
	 */
	@Override
	public synchronized int handNoteToChef() throws RemoteException {
		// if (waiter.getWaiterState() != WaiterStates.PLACING_THE_ORDER) {
		// 	waiter.setWaiterState(WaiterStates.PLACING_THE_ORDER);
		// 	repos.updateWaiterState(WaiterStates.PLACING_THE_ORDER);
		// }
		repos.updateWaiterState(WaiterStates.PLACING_THE_ORDER);


		notifyAll();

		try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return WaiterStates.PLACING_THE_ORDER;

	}

	/**
	 * Operation collect portion, the waiter collects the portion and will deliver
	 * it to the client, is signaled by the chef
	 */
	@Override
	public synchronized int collectPortion() throws RemoteException {
		// if (waiter.getWaiterState() != WaiterStates.WAITING_FOR_PORTION) {
		// 	waiter.setWaiterState(WaiterStates.WAITING_FOR_PORTION);
		// 	repos.updateChefState(WaiterStates.WAITING_FOR_PORTION);
		// }
		repos.updateChefState(WaiterStates.WAITING_FOR_PORTION);


		while (numberOfPortionsCooked == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		numberOfPortionsCooked--;
		numberOfPortionsServed++;
		if (numberOfPortionsServed > ExecConsts.N)
			numberOfPortionsServed = 1;

		notifyAll();

		return WaiterStates.WAITING_FOR_PORTION;

	}

	/**
	 * Operation server shutdown.
	 *
	 * New operation.
	 */
	@Override
	public synchronized void shutdown() throws RemoteException {
		nEntities += 1;
		if (nEntities >= 2)
			KitchenMain.waitConnection = false;
		notifyAll();
	}

}
