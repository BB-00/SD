package interfaces;

import java.rmi.*;

/**
 *   Operational interface of a remote object of type Bar.
 *
 *     It provides the functionality to access the Bar.
 */
public interface BarInterface extends Remote {
	/**
	 * @return ID of the student whose request is being answered
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	public int getStudentBeingAnswered() throws RemoteException;

	/**
	 * @return number of students in the restaurant
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	public int getNumberOfStudentsAtRestaurant() throws RemoteException;
	
	/**
	 * 
	 * Student Operations
	 * 
	 */

	/**
	 * Operation Enter, is called by the students to signal that he as entered in
	 * the restaurant
	 * 
	 * @param studentID
	 * @return state of the student
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	public int enter(int studentID) throws RemoteException;

	/**
	 * Operation call The Waiter, called by the student who arrived first, to call
	 * the waiter
	 * 
	 * @param studentID
	 * @return state of the student
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	public void callWaiter(int studentID) throws RemoteException;

	/**
	 * Operation signal the waiter
	 * 
	 * It is called by the last student to finish eating to signal waiter to bring
	 * next course
	 * 
	 * @param studentID
	 * @param studentState
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	public void signalWaiter(int studentID, int studentState) throws RemoteException;

	/**
	 * Operation Exit, called by the students when they want to leave
	 * 
	 * @param studentID
	 * @return state of the student
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	public int exit(int studentID) throws RemoteException;

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
	 *         
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	public char lookAround() throws RemoteException;

	/**
	 * Operation Say GoodBye, called by the waiter, to say goodbye to the students
	 * 
	 * @return true if there are no more students at the restaurant, false otherwise
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	public boolean sayGoodbye() throws RemoteException;

	/**
	 * Operation prepare the Bill
	 * 
	 * It is called the waiter to prepare the bill of the meal eaten by the students
	 * 
	 * @return state of the waiter
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	public int preprareBill() throws RemoteException;

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
	 * 
	 * @return state of the chef
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	public int alertWaiter() throws RemoteException;

	/**
	 * Operation server shutdown.
	 *
	 * New operation.
	 * 
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	public void shutdown() throws RemoteException;
}
