package entities;

/**
 * Definition of the internal states of the student during his life cycle
 */
public final class StudentStates {
	/**
	 * student arrives the restaurant at a random time
	 */
	public static final int GOING_TO_THE_RESTAURANT = 0;
	
	/**
	 * student waits for waiter to salute him
	 */
	public static final int TAKING_A_SEAT_AT_THE_TABLE = 1;
	
	/**
	 * student select the courses from the menu
	 */
	public static final int SELECTING_THE_COURSES = 2;
	
	/**
	 * first student to arrive collects the order from other companions
	 */
	public static final int ORGANIZING_THE_ORDER = 3;
	
	/**
	 * student chats with companions while waiting for courses to be served
	 */
	public static final int CHATTING_WITH_COMPANIONS = 4;
	
	/**
	 * student eats the meal during random time
	 */
	public static final int ENJOYING_THE_MEAL = 5;
	
	/**
	 * last student to arrive waits for waiter to present the bill
	 */
	public static final int PAYING_THE_MEAL = 6;
	
	/**
	 * final state life cycle ending
	 */
	public static final int GOING_HOME = 7;
}