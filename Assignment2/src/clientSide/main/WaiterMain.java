package clientSide.main;

import clientSide.entities.Waiter;
import clientSide.stubs.BarStub;
import clientSide.stubs.KitchenStub;
import clientSide.stubs.TableStub;


public class WaiterMain {
	
	public static void main(String[] args) {
		Waiter waiter;
		
		//Initialization
		BarStub bar;
		TableStub table;
		KitchenStub kitchen;
		
		bar = new BarStub();
		table = new TableStub();
		kitchen = new KitchenStub();
		
		waiter = new Waiter("Waiter", kitchen, bar, table);
		
		
		// Start of simulation
		waiter.start();
		
		try {
            waiter.join();
        } catch (InterruptedException e) {
            System.out.print("Error occured while executing Waiter");
        }
	}

}
