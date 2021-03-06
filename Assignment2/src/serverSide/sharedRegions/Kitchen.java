package serverSide.sharedRegions;

import commInfra.*;
import serverSide.main.*;
import serverSide.entities.*;
import clientSide.entities.*;
import clientSide.stubs.*;
import genclass.GenericIO;

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
	 * Number of portions prepared by the chef
	 */
	private int numberOfPortionsPrepared;

	/**
	 * Number of courses that have been served
	 */
	private int numberOfCoursesServed;

	/**
	 * Reference to General Repositories stub
	 */
	private final GenReposStub repos;

	/**
	 * Kitchen Instantiation
	 * 
	 * @param repos reference to the general repository stub
	 */
	public Kitchen(GenReposStub repos) {
		this.nEntities = 0;
		this.numberOfCoursesServed = 0;
		this.numberOfPortionsCooked = 0;
		this.numberOfPortionsPrepared = 0;
		this.numberOfPortionsServed = 0;
		this.repos = repos;
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
	public synchronized void watchTheNews() {
		KitchenClientProxy chef = ((KitchenClientProxy) Thread.currentThread());
		chef.setChefState(ChefStates.WAITING_FOR_AN_ORDER);
		repos.updateChefState(ChefStates.WAITING_FOR_AN_ORDER);

		try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Operation start preparation, in this operation the chef will start the
	 * preparation of a course
	 */
	public synchronized void startPreparation() {
		KitchenClientProxy chef = ((KitchenClientProxy) Thread.currentThread());
		chef.setChefState(ChefStates.PREPARING_THE_COURSE);
		repos.updateChefState(ChefStates.PREPARING_THE_COURSE);

		notifyAll();
	}

	/**
	 * Operation proceed to presentation, in this operation the chef will dish each
	 * portion
	 */
	public synchronized void proceedToPresentation() {
		KitchenClientProxy chef = ((KitchenClientProxy) Thread.currentThread());
			
		numberOfPortionsPrepared++;
		chef.setChefState(ChefStates.DISHING_THE_PORTIONS);
		repos.updatePortion(numberOfPortionsPrepared, ChefStates.DISHING_THE_PORTIONS);

		numberOfPortionsCooked++;
	}

	/**
	 * Operation have next portion ready, the chef has just served one portion and
	 * needs to dish another one
	 */
	public synchronized void haveNextPortionReady() {
		KitchenClientProxy chef = ((KitchenClientProxy) Thread.currentThread());
	
		numberOfPortionsPrepared++;
		chef.setChefState(ChefStates.DISHING_THE_PORTIONS);
		repos.updatePortion(numberOfPortionsPrepared, ChefStates.DISHING_THE_PORTIONS);

		numberOfPortionsCooked++;

		chef.setChefState(ChefStates.DELIVERING_THE_PORTIONS);
		repos.updateChefState(ChefStates.DELIVERING_THE_PORTIONS);

		notifyAll();
	}

	/**
	 * Operation continue preparation, in this operation the chef has finished one
	 * course and will start preparating another one
	 */

	public synchronized void continuePreparation() {
		KitchenClientProxy chef = ((KitchenClientProxy) Thread.currentThread());
		
		numberOfPortionsPrepared = 0;
		chef.setChefState(ChefStates.PREPARING_THE_COURSE);
		repos.updatePortionAndCourse(numberOfPortionsPrepared, numberOfCoursesServed + 1,
				ChefStates.PREPARING_THE_COURSE);
	}

	/**
	 * Operation have all portions been served, in this operation the chef checks if
	 * all portions have been served before starting preparing another portion
	 *
	 * @return true if all portions have been served, false otherwise
	 */

	public synchronized boolean haveAllPortionsBeenDelivered() {
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

	public synchronized boolean hasOrderBeenCompleted() {
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

	public synchronized void cleanUp() {
		KitchenClientProxy chef = ((KitchenClientProxy) Thread.currentThread());
		chef.setChefState(ChefStates.CLOSING_SERVICE);
		repos.updateChefState(ChefStates.CLOSING_SERVICE);

		// ---------------- DEBUG -----------------
		// System.out.println("Chef has closed Service!");
	}

	/**
	 * 
	 * Waiter Operations
	 * 
	 */

	/**
	 * Operation return to bar, the waiter is in the kitchen and returns to the bar
	 */
	public synchronized void returnToBar() {
		KitchenClientProxy waiter = ((KitchenClientProxy) Thread.currentThread());
		waiter.setWaiterState(WaiterStates.APPRAISING_SITUATION);
		repos.updateWaiterState(WaiterStates.APPRAISING_SITUATION);
	}

	/**
	 * Operation hand note to chef, the waiter hands the order note to the chef and
	 * wakes him up
	 */
	public synchronized void handNoteToChef() {
		KitchenClientProxy waiter = ((KitchenClientProxy) Thread.currentThread());
		waiter.setWaiterState(WaiterStates.PLACING_THE_ORDER);
		repos.updateWaiterState(WaiterStates.PLACING_THE_ORDER);

		notifyAll();

		try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Operation collect portion, the waiter collects the portion and will deliver
	 * it to the client, is signaled by the chef
	 */
	public synchronized void collectPortion() {
		KitchenClientProxy waiter = ((KitchenClientProxy) Thread.currentThread());
		waiter.setWaiterState(WaiterStates.WAITING_FOR_PORTION);
		repos.updateWaiterState(WaiterStates.WAITING_FOR_PORTION);

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

	}

	/**
	 * Operation server shutdown.
	 *
	 * New operation.
	 */
	public synchronized void shutdown() {
		nEntities += 1;
		if (nEntities >= 2)
			KitchenMain.waitConnection = false;
		notifyAll();
	}

}
