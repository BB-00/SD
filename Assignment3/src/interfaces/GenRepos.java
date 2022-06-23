package interfaces;

import genclass.GenericIO;
import genclass.TextFile;
import java.util.Objects;
import java.util.Arrays;

import serverSide.main.*;
import clientSide.entities.*;
import commInfra.*;

/**
 * General Repository
 *
 * It is responsible to keep the visible internal state of the problem and to
 * provide means for it to be printed in the logging file. It is implemented as
 * an implicit monitor. All public methods are executed in mutual exclusion.
 * There are no internal synchronisation points.
 */

public interface GenRepos extends Remote {

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
	public ReturnInt getStudentSeat(int studentSeat) throws RemoteException;

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
