package entities;
import sharedRegions.Bar;
import sharedRegions.Kitchen;

public class Chef extends Thread{
    
    private int chef_state;

    private Kitchen kitchen;
    
    private Bar bar;

    public void set_state(int state){
        this.chef_state = state;
    }

    public int get_state(){
        return chef_state;
    }

    public Chef(String name, Kitchen kit, Bar b){

        super(name);
        this.kitchen = kit;
        this.bar = b;
        this.chef_state = ChefStates.WAITING_FOR_AN_ORDER;

    }


    @Override
    public void run(){

        this.kitchen.watchNews();
        this.kitchen.startPreparation();
        boolean firstCourse = true;

        do {

            if (!firstCourse) this.kitchen.continuePreparation();
            else firstCourse = false;

            this.kitchen.proceedToPreparation();
            this.bar.alertTheWaiter();

            while(!this.kitchen.haveAllPortionsBeenServed()) {
                this.kitchen.haveNextPortionReady();
                this.bar.alertTheWaiter();
            }

        } while(!this.kitchen.hasTheOrderBeenCompleted());

        this.kitchen.cleanUp();

    }
    
}