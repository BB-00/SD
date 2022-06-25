package interfaces;

import java.rmi.registry.*;
import java.rmi.*;
import java.rmi.server.*;
import serverSide.objects.*;
import interfaces.*;
import genclass.GenericIO;

/**
 * Operational interface of a remote object of type Table.
 *
 * It provides the functionality to access the Table.
 */
public interface TableInterface extends Remote {

	/**
	 * @return id of the first student to arrive at the restaurant
	 */
	public int getFirstToArrive() throws RemoteException;

	/**
	 * 
	 * @return id of the last student to finish eating a meal
	 */
	public int getLastToEat() throws RemoteException;

	/**
	 * 
	 * @param firstToArrive id of the first student to arrive
	 */
	public void setFirstToArrive(int firstToArrive) throws RemoteException;

	/**
	 * 
	 * @param lastToArrive if of the last student to arrive to the restaurant
	 */
	public void setLastToArrive(int lastToArrive) throws RemoteException;

	/**
	 * 
	 * Waiter Operations
	 * 
	 */

	/**
	 * Operation salute the client
	 * 
	 * It is called by the waiter when a student enters the restaurant
	 */
	public int saluteClient(int studentIDBeingAnswered) throws RemoteException;

	/**
	 * Operation return to the bar
	 * 
	 * It is called by the waiter to change to return to the bar appraising
	 * situation
	 */
	public int returnBar() throws RemoteException;

	/**
	 * Operation get the pad
	 * 
	 * It is called by the waiter when an order is going to be described by the
	 * first student to arrive Waiter Blocks waiting for student to describe him the
	 * order
	 */
	public int getThePad() throws RemoteException;

	/**
	 * Operation have all clients been served
	 * 
	 * Called by the waiter to check if all clients have been served or not
	 * 
	 * @return true if all clients have been served, false otherwise
	 */
	public boolean haveAllClientsBeenServed() throws RemoteException;

	/**
	 * Operation deliver portion
	 * 
	 * Called by the waiter, when a portion is delivered at the table
	 */
	public void deliverPortion() throws RemoteException;

	/**
	 * Operation present the bill
	 * 
	 * Called by the waiter to present the bill to the last student to arrive
	 */
	public int presentBill() throws RemoteException;

	/**
	 * 
	 * Student Operations
	 * 
	 */

	/**
	 * Operation siting at the table
	 * 
	 * Student comes in the table and sits (blocks) waiting for waiter to bring him
	 * the menu Called by the student (inside enter method in the bar)
	 */
	public void seatAtTable(int studentID) throws RemoteException;

	/**
	 * Operation read the menu
	 * 
	 * Called by the student to read a menu, wakes up waiter to signal that he
	 * already read the menu
	 */
	public int readMenu(int studentID) throws RemoteException;

	/**
	 * Operation prepare the order
	 * 
	 * Called by the student to begin the preparation of the order,
	 */
	public int prepareOrder() throws RemoteException;

	/**
	 * Operation everybody has chosen
	 * 
	 * Called by the first student to arrive to check if all his companions have
	 * choose or not Blocks if not.
	 * 
	 * @return true if has everybody choosen, false otherwise
	 */
	public boolean everybodyHasChosen() throws RemoteException;

	/**
	 * Operation add up ones choices
	 * 
	 * Called by the first student to arrive to add up a companions choice to the
	 * order
	 */
	public void addUpOnesChoices() throws RemoteException;

	/**
	 * Operation describe the order
	 * 
	 * Called by the first student to arrive to describe the order to the waiter
	 * Blocks waiting for waiter to come with pad Wake waiter up so he can take the
	 * order
	 */
	public void describeOrder() throws RemoteException;

	/**
	 * Operation join the talk
	 * 
	 * Called by the first student to arrive so he can join his companions while
	 * waiting for the courses
	 */
	public int joinTalk() throws RemoteException;

	/**
	 * Operation inform companion
	 * 
	 * Called by a student to inform the first student to arrive about his
	 * preferences Blocks waiting for courses
	 */
	public int informCompanion(int studentID) throws RemoteException;

	/**
	 * Operation start eating
	 * 
	 * Called by the student to start eating the meal (During random time)
	 */
	public int startEating(int studentID) throws RemoteException;

	/**
	 * Operation end eating
	 * 
	 * Called by the student to signal that he has finished eating his meal
	 */
	public int endEating(int studentID) throws RemoteException;

	/**
	 * Operation has everybody finished eating
	 * 
	 * Called by to student to wait for his companions to finish eating
	 */
	public boolean hasEverybodyFinishedEating(int studentID) throws RemoteException;

	/**
	 * Operation honour the bill
	 * 
	 * Called by the student to pay the bill Student blocks waiting for bill to be
	 * presented and signals waiter when it's time to pay it
	 */
	public void honourBill() throws RemoteException;

	/**
	 * Operation have all courses been eaten
	 * 
	 * Called by the student to check if there are more courses to be eaten Student
	 * blocks waiting for the course to be served
	 * 
	 * @return true if all courses have been eaten, false otherwise
	 */
	public boolean haveAllCoursesBeenEaten() throws RemoteException;

	/**
	 * Operation should have arrived earlier
	 * 
	 * Called by the student to check wich one was last to arrive
	 * 
	 * @return True if current student was the last to arrive, false otherwise
	 */
	public ReturnBoolean shouldHaveArrivedEarlier(int studentID) throws RemoteException;

	/**
	 * Operation server shutdown.
	 *
	 * New operation.
	 */
	public void shutdown() throws RemoteException;
}
