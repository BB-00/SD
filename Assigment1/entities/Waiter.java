package entities;

import sharedRegions.*;

public class Waiter extends Thread {

    private int waiter_state;

    private final Kitchen kitchen;

    private final Bar bar;

    private final Table table;

    public void set_state(int state){
        this.waiter_state = state;
    }

    public int get_state(){
        return this.waiter_state;
    }

    public Waiter(String name, Kitchen kit, Bar b, Table tab){
        super(name);
        this.kitchen=kit;
        this.bar=b;
        this.table=tab;
        this.waiter_state = WaiterStates.APPRAISING_SITUATION;
    }

    @Override
    public void run(){

        boolean exit = false;

        do{
            char aux = this.bar.lookAround();
            switch (aux) {
                case 'e':
                    this.table.saluteTheClient();
                    this.table.returnToBar();
                    break;
                case 'c':
                    this.table.getThePad();
                    this.kitchen.handNoteToChef();
                    this.kitchen.returnToBar();
                    break;
                case 'a':
                    do{
                        this.kitchen.collectPortion();
                        this.table.deliverPortion();
                    }while(this.table.haveAllPortionsBeenDelivered);
                    this.table.returnToBar();
                    break;
                case 's':
                    this.bar.prepareTheBill();
                    this.table.presentTheBill();
                    this.kitchen.returnToBar();
                case 'g':
                    exit = this.bar.sayGoodBye();
                    break;
            }
        } while(!exit);

    }
    
}
