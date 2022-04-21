package sharedRegions;

import entities.Student;
import entities.StudentStates;

public class Table {
	/**
	 * number of Students already seated
	 */
	private int numberOfStudentsAtTheTable;
	
	/**
	 * number of Students that have already chosen the courses
	 */
	private int numberOfStudentsThasHasChosen;
	
	/**
	 * number of Students that have already finish eat a course
	 */
	private int numberOfStudentsThatHasFinishEat;
	
	/**
	 * number of Students that have already been served on specific course
	 */
	private int numberOfStudentsServed;
	
	/**
	 * number of Courses that have already been served
	 */
	private int numberOfCoursesServed;
	
	/**
	 * ID of the student that arrived first
	 */
	private int firstToArriveID;
	
	/**
	 * Reference to General Repositories
	 */
	private final GenRepos repos;
	
	/**
	 * Table Instantiation
	 * 
	 * @param repos
	 */
	public Table(GenRepos repos) {
		this.numberOfStudentsAtTheTable = 0;
		this.numberOfStudentsThasHasChosen = 0;
		this.numberOfStudentsThatHasFinishEat = 0;
		this.numberOfStudentsServed = 0;
		this.numberOfCoursesServed = 0;
		this.repos = repos;
	}
	
	
	public void isFirstToArrive() {
		
	}
	
	/**
	 * Operation read the menu, performed by student when seats at the table. Wakes up Waiter after read the menu
	 */
	public void readTheMenu() {
		// get student ID
		int studentID = ((Student) Thread.currentThread()).getStudentID();
		
		//update Student State
		((Student) Thread.currentThread()).setStudentState(StudentStates.SELECTING_THE_COURSES);
		repos.updateStudentState(studentID, StudentStates.SELECTING_THE_COURSES);
		
		notifyAll();
		System.out.println("Student "+studentID+" read the menu");
	}
	
    
}
