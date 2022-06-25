package interfaces;

import java.rmi.registry.*;
import java.rmi.*;
import java.rmi.server.*;
import serverSide.objects.*;
import interfaces.*;
import genclass.GenericIO;

/**
 * General Repository
 *
 * It is responsible to keep the visible internal state of the problem and to
 * provide means for it to be printed in the logging file. It is implemented as
 * an implicit monitor. All public methods are executed in mutual exclusion.
 * There are no internal synchronisation points.
 */

public interface GenReposInterface extends Remote {

	/**
	 * Update Student State
	 * 
	 * @param studentID
	 * @param studentState
	 */
	public void updateStudentState(int studentID, int studentState) throws RemoteException;

	/**
	 * Set Student Seat
	 * 
	 * @param studentID
	 * @param studentSeat
	 */
	public void updateStudentSeat(int studentID, int studentSeat) throws RemoteException;

	/**
	 * Get Student Seat
	 * 
	 * @param studentID
	 * @return number of seat
	 */
	public int getStudentSeat(int studentSeat) throws RemoteException;

	/**
	 * Update Waiter State
	 * 
	 * @param newWaiterState
	 */
	public void updateWaiterState(int waiterState) throws RemoteException;

	/**
	 * Update Chef State
	 * 
	 * @param newChefState
	 */
	public void updateChefState(int chefState) throws RemoteException;

	/**
	 * Operation server shutdown.
	 *
	 * New operation.
	 */
	public void shutdown() throws RemoteException;
}
