package serverSide.entities;

import commInfra.Message;
import commInfra.MessageException;
import commInfra.ServerCom;
import genclass.GenericIO;
import serverSide.sharedRegions.GenReposInterface;

/**
 * Service provider agent for access to the General Repository of Information.
 *
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 */
public class GenReposClientProxy extends Thread {
	/**
	 * Number of instantiated threads.
	 */
	private static int nProxy = 0;

	/**
	 * Communication channel.
	 */
	private ServerCom sconi;

	/**
	 * Interface to the General Repository of Information.
	 */
	private GenReposInterface reposInterface;

	/**
	 * Chef State
	 */
	private int chefState;

	/**
	 * Waiter State
	 */
	private int waiterState;

	/**
	 * Student state
	 */
	private int studentState;

	/**
	 * Student id
	 */
	private int studentID;

	/**
	 * Student State
	 */
	private int studentSeat;

	/**
	 * Number of Portions
	 */
	private int nPortion;

	/**
	 * Number of Courses;
	 */
	private int nCourse;

	/**
	 * Instantiation of a client proxy.
	 *
	 * @param sconi      communication channel
	 * @param reposInter interface to the general repository of information
	 */
	public GenReposClientProxy(ServerCom sconi, GenReposInterface reposInter) {
		super("GenReposProxy_" + GenReposClientProxy.getProxyId());
		this.sconi = sconi;
		this.reposInterface = reposInter;
	}

	/**
	 * Generation of the instantiation identifier.
	 *
	 * @return instantiation identifier
	 */
	private static int getProxyId() {
		Class<?> cl = null; // representation of the GeneralReposClientProxy object in JVM
		int proxyId; // instantiation identifier

		try {
			cl = Class.forName("serverSide.entities.GenReposClientProxy");
		} catch (ClassNotFoundException e) {
			GenericIO.writelnString("Data type GenReposClientProxy was not found!");
			e.printStackTrace();
			System.exit(1);
		}

		synchronized (cl) {
			proxyId = nProxy;
			nProxy += 1;
		}

		return proxyId;
	}

	/**
	 * Life cycle of the service provider agent.
	 */
	@Override
	public void run() {
		Message inMessage = null, // service request
				outMessage = null; // service reply

		/* service providing */

		inMessage = (Message) sconi.readObject(); // get service request

		try {
			outMessage = reposInterface.processAndReply(inMessage); // process it
		} catch (MessageException e) {
			GenericIO.writelnString("Thread " + getName() + ": " + e.getMessage() + "!");
			GenericIO.writelnString(e.getMessageVal().toString());
			System.exit(1);
		}

		sconi.writeObject(outMessage); // send service reply
		sconi.close(); // close the communication channel
	}

	/**
	 * Get chef state
	 * 
	 * @return chef state
	 */
	public int getChefState() {
		return chefState;
	}

	/**
	 * Set chef state
	 * 
	 * @param chefState state of the chef
	 */
	public void setChefState(int chefState) {
		this.chefState = chefState;
	}

	/**
	 * Get waiter state
	 * 
	 * @return waiter state
	 */
	public int getWaiterState() {
		return waiterState;
	}

	/**
	 * Set waiter state
	 * 
	 * @param waiterState state of the waiter
	 */
	public void setWaiterState(int waiterState) {
		this.waiterState = waiterState;
	}

	/**
	 * Get student state
	 * 
	 * @return student state
	 */
	public int getStudentState() {
		return studentState;
	}

	/**
	 * Set student state
	 * 
	 * @param studentState state of the student
	 */
	public void setStudentState(int studentState) {
		this.studentState = studentState;
	}

	/**
	 * Get student id
	 * 
	 * @return id of the student
	 */
	public int getStudentID() {
		return studentID;
	}

	/**
	 * Set student id
	 * 
	 * @param studentId id of the student
	 */
	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}

	/**
	 * Get student seat
	 * 
	 * @return seat of the student
	 */
	public int getStudentSeat() {
		return studentSeat;
	}

	/**
	 * Set student seat
	 * 
	 * @param studentSeat seat of the student
	 */
	public void setStudentSeat(int studentSeat) {
		this.studentSeat = studentSeat;
	}

	/**
	 * Get number of Portion
	 * 
	 * @return number of Portion
	 */
	public int getNPortion() {
		return nPortion;
	}

	/**
	 * Set number of Portion
	 * 
	 * @param nPortion number of Portion
	 */
	public void setNPortion(int nPortion) {
		this.nPortion = nPortion;
	}
	
	/**
	 * Get number of Course
	 * 
	 * @return number of Course
	 */
	public int getNCourse() {
		return nCourse;
	}

	/**
	 * Set number of Course
	 * 
	 * @param nCourse number of Course
	 */
	public void setNCourse(int nCourse) {
		this.nCourse = nCourse;
	}
}
