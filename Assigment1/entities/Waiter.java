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
        this.waiter_state = WaiterStates.APRAISING_SITUATION;
    }

    @Override
    public void run(){

        boolean exit = false;

        do{
            char aux = Bar.lookAround();
            switch (aux) {
                case 'e':
                    Table.saluteTheClient();
                    Table.returnToBar();
                    break;
                case 'c':
                    Table.getThePad();
                    Kitchen.handNoteToChef();
                    Kitchen.returnToBar();
                    break;
                case 'a':
                    do{
                        Kitchen.collectPortion();
                        Table.deliverPortion();
                    }while(Table.haveAllPortionsBeenDelivered);
                    Table.returnToBar();
                    break;
                case 's':
                    Bar.prepareTheBill();
                    Table.presentTheBill();
                    Table.returnToBar();
                case 'g':
                    exit = Bar.sayGoodBye();
                    break;
            }
        } while(!exit);

    }
    
}
