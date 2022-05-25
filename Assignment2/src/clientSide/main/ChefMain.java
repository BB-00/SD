package clientSide.main;

import clientSide.entities.Chef;
import clientSide.stubs.BarStub;
import clientSide.stubs.KitchenStub;

public class ChefMain {
		
	public static void main(String[] args) {
		Chef chef;
		
		//Initialization
		BarStub bar;
		KitchenStub kitchen;
		
		bar = new BarStub();
		kitchen = new KitchenStub();
		
		chef = new Chef("Chef", kitchen, bar);
		
		
		// Start of simulation
		chef.start();
		
		try {
			chef.join();
	    } catch (InterruptedException e) {
	        System.out.print("Error occured while executing Chef");
	    }
	}
}
