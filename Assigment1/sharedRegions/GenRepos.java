package sharedRegions;

public class GenRepos {
	/**
	 * States of the Students
	 */
	private int [] studentStates;
	
	/**
	 * Set Student State
	 * 
	 * @param studentID
	 * @param studentState
	 */
	public synchronized void updateStudentState(int studentID, int studentState) {
		studentStates[studentID] = studentState;
	}
	
	

}
