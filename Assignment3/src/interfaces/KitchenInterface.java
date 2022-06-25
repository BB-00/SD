package interfaces;

import java.rmi.registry.*;
import java.rmi.*;
import java.rmi.server.*;
import serverSide.objects.*;
import interfaces.*;
import genclass.GenericIO;

/**
 *   Operational interface of a remote object of type Kitchen.
 *
 *     It provides the functionality to access the Kitchen.
 */
public interface KitchenInterface extends Remote {

	/**
	 * 
	 * Chef Operations
	 * 
	 */

	/**
	 * Operation what the news, in this operation the chef is waiting for the waiter
	 * to give in an order
	 */
	public int watchTheNews() throws RemoteException;

	/**
	 * Operation start preparation, in this operation the chef will start the
	 * preparation of a course
	 */
	public int startPreparation() throws RemoteException;

	/**
	 * Operation proceed to presentation, in this operation the chef will dish each
	 * portion
	 */
	public int proceedToPresentation() throws RemoteException;

	/**
	 * Operation have next portion ready, the chef has just served one portion and
	 * needs to dish another one
	 */
	public int haveNextPortionReady() throws RemoteException;

	/**
	 * Operation continue preparation, in this operation the chef has finished one
	 * course and will start preparating another one
	 */

	public int continuePreparation() throws RemoteException;

	/**
	 * Operation have all portions been served, in this operation the chef checks if
	 * all portions have been served before starting preparing another portion
	 *
	 * @return true if all portions have been served, false otherwise
	 */

	public boolean haveAllPortionsBeenDelivered() throws RemoteException;

	/**
	 * Operation has the order been completed, the chef checks if the order has been
	 * completed or not
	 *
	 * @return true if the order has been completed, false otherwise
	 */

	public boolean hasOrderBeenCompleted() throws RemoteException;

	/**
	 * Operation clean up, the chef has finished the order and starts cleaning the
	 * kitchen
	 */

	public int cleanUp() throws RemoteException;

	/**
	 * 
	 * Waiter Operations
	 * 
	 */

	/**
	 * Operation return to bar, the waiter is in the kitchen and returns to the bar
	 */
	public int returnToBar() throws RemoteException;

	/**
	 * Operation hand note to chef, the waiter hands the order note to the chef and
	 * wakes him up
	 */
	public int handNoteToChef() throws RemoteException;

	/**
	 * Operation collect portion, the waiter collects the portion and will deliver
	 * it to the client, is signaled by the chef
	 */
	public int collectPortion() throws RemoteException;

	/**
	 * Operation server shutdown.
	 *
	 * New operation.
	 */
	public void shutdown() throws RemoteException;

}
