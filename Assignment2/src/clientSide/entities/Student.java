package clientSide.entities;

import clientSide.stubs.TableStub;
import commInfra.ExecConsts;
import clientSide.stubs.BarStub;

/**
 *   Student thread.
 *
 *   It simulates the student life cycle.
 */
public class Student extends Thread{
	
	/**
	 * Reference to the Table
	 */
	private final TableStub table;

	/**
	 * Reference to the Bar
	 */
	private final BarStub bar;
	
	/**
	 * Student ID
	 */
	private  int studentID;
	
	/**
	 * Student States
	 */
	private int studentState;
	
	/**
	 * Instatiation of a student thread
	 * 
	 * @param studentID student id
	 * @param reference to bar stub
	 * @param reference to table stub
	 */
	public Student(String name, int studentID, BarStub bar, TableStub table) {
		super(name);
		this.studentID = studentID;
		this.studentState = StudentStates.GOING_TO_THE_RESTAURANT;
		this.bar = bar;
		this.table = table;
	}
	
	/**
	 * Set the studentID
	 * 
	 * @param studentID
	 */
	public void setStudentID(int id) {
		this.studentID = id;
	}
	
	/**
	 * @return studentID
	 */
	public int getStudentID() {
		return this.studentID;
	}
	
	/**
	 * set the student state
	 * 
	 * @param studentState
	 */
	public void setStudentState(int state) {
		this.studentState = state;
	}
	
	/**
	 * @return student state
	 */
	public int getStudentState() {
		return this.studentState;
	}
	/**
	 *	Life cycle of the student
	 */

	@Override
	public void run ()
	{
		walkABit();
		bar.enter();
		table.readMenu();
		
		if(studentID == table.getFirstToArrive())
		{
			table.prepareOrder();
			do{
				table.addUpOnesChoices();
			}while(!table.everybodyHasChosen());
			bar.callWaiter();
			table.describeOrder();
			table.joinTalk();
		}
		else table.informCompanion();
		// do{
		// 	this.table.startEating();
		// 	this.table.endEating();
		// 	while(!this.table.hasEverybodyFinishedEating());
		// 	if(studentID == this.table.getLastToEat()) this.bar.signalWaiter();
		// }while(!this.table.haveAllCoursesBeenEaten());

		int coursesEatenNum = 0;
		while(!table.haveAllCoursesBeenEaten()){
			//if(table.haveAllClientsBeenServed()){
				table.startEating();
				table.endEating();
				coursesEatenNum++;
				
				while(!table.hasEverybodyFinishedEating());
				if(studentID == table.getLastToEat() && coursesEatenNum != ExecConsts.M) bar.signalWaiter();
			//}
		}

		if(table.shouldHaveArrivedEarlier()) {
			bar.signalWaiter();
			table.honourBill();
		}
		bar.exit();
	}
	
	
	private void walkABit(){
		try{ 
			sleep ((long) (1 + 50 * Math.random ()));
		} catch (InterruptedException e) {}
	}
	
}