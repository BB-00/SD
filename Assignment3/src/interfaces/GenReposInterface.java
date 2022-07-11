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
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	public void updateStudentState(int studentID, int studentState) throws RemoteException;

	/**
	 * Set Student Seat
	 * 
	 * @param studentID
	 * @param studentSeat
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	public void updateStudentSeat(int studentID, int studentSeat) throws RemoteException;
	
	/**
	 * Set Student Seat and State
	 * 
	 * @param studentID
	 * @param studentSeat
	 * @param studentState
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	public void updateStudentSeatAndState(int studentID, int studentSeat, int studentState) throws RemoteException;

	/**
	 * Set Student Seat at leaving
	 * 
	 * @param studentID
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	public void updateSeatsAtLeaving(int studentId) throws RemoteException;

	/**
	 * Get Student Seat
	 * 
	 * @param studentID
	 * @return number of seat
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	public int getStudentSeat(int studentSeat) throws RemoteException;

	/**
	 * Update Waiter State
	 * 
	 * @param WaiterState
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	public void updateWaiterState(int waiterState) throws RemoteException;

	/**
	 * Update Chef State
	 * 
	 * @param newChefState
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	public void updateChefState(int chefState) throws RemoteException;
	
	/**
	 * Set number of course and chef state
	 * 
	 * @param nCourse
	 * @param chefState
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	public void updateCourse(int nCourse, int chefState) throws RemoteException;
	
	/**
	 * Set number of portion and chef state
	 * 
	 * @param nPortion
	 * @param chefState
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	public void updatePortion(int nPortion, int chefState) throws RemoteException;
	
	/**
	 * Set number of course, number of portion and chef state
	 * 
	 * @param nPortion
	 * @param nCourse
	 * @param chefState
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	public void updatePortionAndCourse(int nPortion, int nCourse, int chefState) throws RemoteException;

	/**
	 * Operation server shutdown.
	 *
	 * New operation.
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
	 */
	public void shutdown() throws RemoteException;
}
