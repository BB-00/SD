package sharedRegions;

import entities.*;
import main.*;

public class Kitchen {
    
    private int numberOfPortionsCooked;

    private int numberOfPortionsServed;

    private int numberOfCoursesServed;

    private final GenRepos repos;

    public Kitchen(GenRepos rep){
        this.numberOfCoursesServed=0;
        this.numberOfPortionsCooked=0;
        this.numberOfPortionsServed=0;
        this.repos = rep;
    }

    public synchronized void watchNews(){

        //Set chef state
		((Chef) Thread.currentThread()).set_state(ChefStates.WAITING_FOR_AN_ORDER);
		repos.set_state(ChefStates.WAITING_FOR_AN_ORDER);

        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void startPreparation(){

        ((Chef) Thread.currentThread()).set_state(ChefStates.PREPARING_THE_COURSE);
		repos.set_state(ChefStates.PREPARING_THE_COURSE);

        notifyAll();

    }

    public synchronized void proceedToPreparation(){

        ((Chef) Thread.currentThread()).set_state(ChefStates.DISHING_THE_PORTIONS);
		repos.set_state(ChefStates.DISHING_THE_PORTIONS);

        numberOfPortionsCooked++;

    }

    public synchronized void continuePreparation(){

        ((Chef) Thread.currentThread()).set_state(ChefStates.PREPARING_THE_COURSE);
		repos.set_state(ChefStates.PREPARING_THE_COURSE);

    }

    public synchronized boolean haveAllPortionsBeenServed(){

        System.out.println("Number of Portions Served: "+numberOfPortionsServed);
        if(numberOfPortionsServed == 7){
            numberOfCoursesServed++;
            return true;
        }

        return false;

    }

    public synchronized boolean hasTheOrderBeenCompleted(){

        System.out.println("Number of courses Served: "+numberOfCoursesServed);
        
        if(numberOfCoursesServed == 3){
            return true;
        }

        return false;

    }

    public synchronized void haveNextPortionReady(){

        ((Chef) Thread.currentThread()).set_state(ChefStates.DISHING_THE_PORTIONS);
		repos.set_state(ChefStates.DISHING_THE_PORTIONS);

        numberOfPortionsCooked++;

        ((Chef) Thread.currentThread()).set_state(ChefStates.DELIVERING_THE_PORTIONS);
		repos.set_state(ChefStates.DELIVERING_THE_PORTIONS);

        notifyAll();

    }

    public synchronized void cleanUp(){

        ((Chef) Thread.currentThread()).set_state(ChefStates.CLOSING_SERVICE);
		repos.set_state(ChefStates.CLOSING_SERVICE);

    }

    public synchronized void returnToBar(){

        ((Waiter) Thread.currentThread()).set_state(WaiterStates.APPRAISING_SITUATION);
		repos.set_state(WaiterStates.APPRAISING_SITUATION);

    }

    public synchronized void handNoteToChef(){

        ((Waiter) Thread.currentThread()).set_state(WaiterStates.PLACING_THE_ORDER);
		repos.set_state(WaiterStates.PLACING_THE_ORDER);

        notifyAll();
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

    }

    public synchronized void collectPortion(){

        ((Waiter) Thread.currentThread()).set_state(WaiterStates.WAITING_FOR_PORTION);
		repos.set_state(WaiterStates.WAITING_FOR_PORTION);

        while (this.numberOfPortionsCooked == 0) {
            try{
                wait();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        numberOfPortionsCooked--;
        numberOfPortionsServed++;

        if (numberOfPortionsServed==7) numberOfPortionsServed=0;

        notifyAll();


    }


}
