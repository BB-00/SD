package sharedRegions;

public class GenRepos {
	/**
	 * States of the Students
	 */
	private int [] studentStates;
	
	/**
	 * Seats of the Students;
	 */
	private int [] studentSeats;
	
	/**
	 * State of the Waiter
	 */
	private int waiterState;
	
	/**
	 * Set Student State
	 * 
	 * @param studentID
	 * @param studentState
	 */
	public synchronized void updateStudentState(int studentID, int studentState) {
		studentStates[studentID] = studentState;
	}
	
	/**
	 * Set Student Seat
	 * 
	 * @param studentID
	 * @param studentSeat
	 */
	public synchronized void updateStudentSeat(int studentID, int studentSeat) {
		studentSeats[studentID] = studentSeat;
	}
	
	public synchronized void updateWaiterState(int newWaiterState) {
		waiterState = newWaiterState;
	}
}
