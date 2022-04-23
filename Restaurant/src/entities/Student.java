package entities;

import sharedRegions.Bar;
import sharedRegions.Table;
import main.ExecConsts;

/**
 *   Student thread.
 *
 *   It simulates the student life cycle.
 */

public class Student extends Thread{
	
	/**
	 * 	Student identification
	 */
	private int studentId;
	
	/**
	 * 	Student state
	 */
	private int studentState;
	
	/**
	 * Reference to the bar
	 */
	
	private final Bar bar;
	
	/**
	 * Reference to the table
	 */
	private final Table tab;
	
	/**
	 * 	@param student id
	 */
	
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	
	/**
	 * 	@return student id
	 */
	
	public int getStudentId() {
		return studentId;
	}

	/**
	 * 	@param student state
	 */
	
	public void setStudentState(int studentState) {
		this.studentState = studentState;
	}
	
	/**
	 * 	@return student state
	 */

	public int getStudentState() {
		return studentState;
	}

	
	
	
	/**
	 * Instatiation of a student thread
	 * 
	 * @param studentId student id
	 * @param studentState student state
	 */
	public Student(String name, int studentId, Bar bar, Table tab) {
		super(name);
		this.studentId = studentId;
		this.studentState = StudentStates.GOING_TO_THE_RESTAURANT;
		this.bar = bar;
		this.tab = tab;
	}

	
	
	
	
	/**
	 *	Life cycle of the student
	 */

	@Override
	public void run ()
	{
		walkABit();
		bar.enter();
		tab.readMenu();
		
		if(studentId == tab.getFirstToArrive())
		{
			tab.prepareOrder();
			do{
				this.tab.addUpOnesChoices();
			}while(!this.tab.everybodyHasChosen());
//			while(!tab.everybodyHasChosen())
//				tab.addUpOnesChoices();
			bar.callWaiter();
			tab.describeOrder();
			tab.joinTalk();
		}
		else
			tab.informCompanion();
		
		do{
			this.tab.startEating();
			this.tab.endEating();
			while(!this.tab.hasEverybodyFinishedEating());
			if(studentId == this.tab.getLastToEat()) this.bar.signalWaiter();
		}while(!this.tab.haveAllCoursesBeenEaten());
//		while(!tab.haveAllCoursesBeenEaten())
//		{
//			tab.startEating();
//			tab.endEating();
//			numCoursesEaten++;
//			
//			while(!tab.hasEverybodyFinishedEating());
//			System.out.println("I FINISHED MY MEAL");
//			if(studentId == tab.getLastToEat() && numCoursesEaten != ExecConsts.M)
//				bar.signalWaiter();
//		}
//		
		if(tab.shouldHaveArrivedEarlier()) 
		{
			bar.signalWaiter();
			tab.honourBill();
		}
		bar.exit();
	}
	
	
	
	private void walkABit()
	{
		try
		{ sleep ((long) (1 + 50 * Math.random ()));
		}
		catch (InterruptedException e) {}
	}
	
}