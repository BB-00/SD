package sharedRegions;

import entities.*;
import main.*;


public class Kitchen {
	 /**
	 *	Number of portions that are ready to serve
	 */
    private int numberOfPortionsCooked;

    /**
     *  Number of portions that have been served, in each course;
     */
    private int numberOfPortionsServed;

    /**
     *  Number of courses that have been served
     */
    private int numberOfCoursesServed;

    /**
     *  Reference to General Repositories
     */
    private final GenRepos repos;


    /**
	 * Kitchen Instantiation
	 * 
	 * @param repos
	 */
    public Kitchen(GenRepos rep){
        this.numberOfCoursesServed=0;
        this.numberOfPortionsCooked=0;
        this.numberOfPortionsServed=0;
        this.repos = rep;
    }

	/**
     * 
     * Chef Operations
     * 
     */

     /**
      *  Operation what the news, in this operation the chef is waiting for the waiter to give in an order
      */
	public synchronized void watchTheNews() {
		((Chef) Thread.currentThread()).setChefState(ChefStates.WAITING_FOR_AN_ORDER);
		repos.updateChefState(ChefStates.WAITING_FOR_AN_ORDER);
	
		try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
     * Operation proceed to presentation, in this operation the chef will dish each portion
     */
	public synchronized void proceedToPresentation() {
		((Chef) Thread.currentThread()).setChefState(ChefStates.DISHING_THE_PORTIONS);
		repos.updateChefState(ChefStates.DISHING_THE_PORTIONS);
		
		numberOfPortionsCooked++;
	}

	 /**
     * Operation start preparation, in this operation the chef will start the preparation of a course
     */
	public synchronized void startPreparation() {
		((Chef) Thread.currentThread()).setChefState(ChefStates.PREPARING_THE_COURSE);
		repos.updateChefState(ChefStates.PREPARING_THE_COURSE);
		
		notifyAll();
	}

	/**
     * Operation continue preparation, in this operation the chef has finished one course and will start preparating another one
     */

	public synchronized void continuePreparation() {
		((Chef) Thread.currentThread()).setChefState(ChefStates.PREPARING_THE_COURSE);
		repos.updateChefState(ChefStates.PREPARING_THE_COURSE);
	}
	
	
	/**
     * Operation have all portions been served, in this operation the chef checks if all portions have been served before starting preparing another portion
     *
     * @return true if all portions have been served, false otherwise
     */

	public synchronized boolean haveAllPortionsBeenDelivered() {
		while( numberOfPortionsCooked != 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(numberOfPortionsServed == ExecConsts.N) {
			numberOfCoursesServed++;
			//ExecConsts.M--;
			return true;
		}
		return false;

	}
	
	/**
     * Operation has the order been completed, the chef checks if the order has been completed or not
     *
     * @return true if the order has been completed, false otherwise
     */

	public synchronized boolean hasOrderBeenCompleted() {
		System.out.println("Courses served: "+numberOfCoursesServed);
		if (numberOfCoursesServed == ExecConsts.M) return true;
		return false;
	}
	
	/**
     * Operation have next portion ready, the chef has just served on portion and needs to dish another one
     */

	public synchronized void haveNextPortionReady() {	
		((Chef) Thread.currentThread()).setChefState(ChefStates.DISHING_THE_PORTIONS);
		repos.updateChefState(ChefStates.DISHING_THE_PORTIONS);
		
		numberOfPortionsCooked++;
		
		((Chef) Thread.currentThread()).setChefState(ChefStates.DELIVERING_THE_PORTIONS);
		repos.updateChefState(ChefStates.DELIVERING_THE_PORTIONS);
		
		notifyAll();
	}
	
	/**
     * Operation clean up, the chef has finished the order and starts cleaning the kitchen
     */

	public synchronized void cleanUp() {	
		((Chef) Thread.currentThread()).setChefState(ChefStates.CLOSING_SERVICE);
		repos.updateChefState(ChefStates.CLOSING_SERVICE);
		
		System.out.println("Chef has closed Service!");
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
		((Waiter) Thread.currentThread()).setWaiterState(WaiterStates.APPRAISING_SITUATION);
		repos.updateWaiterState(WaiterStates.APPRAISING_SITUATION);
	}

	/**
     * Operation hand note to chef, the waiter hands the order note to the chef and wakes him up
     */
	
	public synchronized void handNoteToChef() {		
		((Waiter) Thread.currentThread()).setWaiterState(WaiterStates.PLACING_THE_ORDER);
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
     * Operation collect portion, the waiter collects the portion and will deliver it to the client, is signaled by the chef
     */
	public synchronized void collectPortion() {
		((Waiter) Thread.currentThread()).setWaiterState(WaiterStates.WAITING_FOR_PORTION);
		repos.updateWaiterState(WaiterStates.WAITING_FOR_PORTION);
		
		while ( numberOfPortionsCooked == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		numberOfPortionsCooked--;
		numberOfPortionsServed++;
		if(numberOfPortionsServed > ExecConsts.N) numberOfPortionsServed = 1;
		
		notifyAll();
		
	}

}