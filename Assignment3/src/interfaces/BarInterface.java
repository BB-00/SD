package interfaces;

import commInfra.*;
import serverSide.main.*;
import serverSide.entities.*;
import clientSide.entities.*;
import clientSide.stubs.*;
import genclass.GenericIO;

/**
 *   Operational interface of a remote object of type Bar.
 *
 *     It provides the functionality to access the Bar.
 */
public interface BarInterface extends Remote {
	/**
	 * 
	 * Student Operations
	 * 
	 */

	/**
	 * Operation Enter, is called by the students to signal that he as entered in
	 * the restaurant
	 */
	public void enter() throws RemoteException;

	/**
	 * Operation call The Waiter, called by the student who arrived first, to call
	 * the waiter
	 */
	public void callWaiter() throws RemoteException;

	/**
	 * Operation signal the waiter
	 * 
	 * It is called by the last student to finish eating to signal waiter to bring
	 * next course
	 */
	public void signalWaiter() throws RemoteException;

	/**
	 * Operation Exit, called by the students when they want to leave
	 */
	public void exit() throws RemoteException;

	/**
	 * 
	 * Waiter Operations
	 * 
	 */

	/**
	 * Operation Look Around, called by the waiter checks if there is any pending
	 * request, if not waits
	 * 
	 * @return char that represents type of service to be executed 'e': client has
	 *         arrived therefore need to be presented with menu 'c': waiter will
	 *         take the order and deliver to the chef 'a': portion needs to be
	 *         collected and delivered 's': bill needs to be prepared and presented
	 *         to the client 'g': some student wants to leave and waiter needs to
	 *         say bye bye
	 */
	public ReturnChar lookAround() throws RemoteException;

	/**
	 * Operation Say GoodBye, called by the waiter, to say goodbye to the students
	 * 
	 * @return true if there are no more students at the restaurant, false otherwise
	 */
	public ReturnBoolean sayGoodbye() throws RemoteException;

	/**
	 * Operation prepare the Bill
	 * 
	 * It is called the waiter to prepare the bill of the meal eaten by the students
	 */
	public void preprareBill() throws RemoteException;

	/**
	 * 
	 * Chef Operations
	 * 
	 */

	/**
	 * Operation alert the waiter
	 * 
	 * It is called by the chef to alert the waiter that a portion was dished For
	 * requests the chef id will be N+1
	 */
	public void alertWaiter() throws RemoteException;

	/**
	 * Operation server shutdown.
	 *
	 * New operation.
	 */
	public void shutdown() throws RemoteException;
}
