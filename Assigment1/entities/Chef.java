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

        Kitchen.watchNews();
        Kitchen.startPreparation();
        boolean firstCourse = true;

        do {

            if (!firstCourse) Kitchen.continuePreparation();
            else firstCourse = false;

            Kitchen.proceedToPreparation();
            Bar.alertTheWaiter();

            while(!Kitchen.haveAllPortionsBeenDelivered()) {
                Kitchen.haveNextPortionReady();
                Bar.alertTheWaiter();
            }

        } while(!Kitchen.hasTheOrderBeenCompleted());

        Kitchen.cleanUp();

    }
    
}