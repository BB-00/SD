package serverSide.objects;

import clientSide.entities.*;
import java.util.Objects;
import java.rmi.RemoteException;
import genclass.GenericIO;
import genclass.TextFile;
import interfaces.*;
import serverSide.main.*;
import commInfra.ExecConsts;

/**
 * General Repository
 *
 * It is responsible to keep the visible internal state of the problem and to
 * provide means for it to be printed in the logging file. It is implemented as
 * an implicit monitor. All public methods are executed in mutual exclusion.
 * There are no internal synchronisation points.
 */
public class GenRepos implements GenReposInterface {
	/**
	 * Number of entity groups requesting the shutdown.
	 */
	private int nEntities;

	/**
	 * Name of the log file
	 */
	private String logFileName;

	/**
	 * States of the Students
	 */
	private int[] studentStates;

	/**
	 * Seats of the Students;
	 */
	private int[] seats;

	/**
	 * State of the Waiter
	 */
	private int waiterState;

	/**
	 * State of the Chef
	 */
	private int chefState;

	/**
	 * Number of courses delivered
	 */
	private int nCourse = 0;

	/**
	 * Number of Portions delivered
	 */
	private int nPortion = 0;

	/**
	 * Instantiation of a general repository object.
	 */
	public GenRepos() {
		this.nEntities = 0;

		this.logFileName = "log";

		// set initial states
		this.studentStates = new int[ExecConsts.N];
		this.seats = new int[ExecConsts.N];
		for (int i = 0; i < ExecConsts.N; i++) {
			this.studentStates[i] = StudentStates.GOING_TO_THE_RESTAURANT;
			this.seats[i] = -1;
		}
		this.waiterState = WaiterStates.APPRAISING_SITUATION;
		this.chefState = ChefStates.WAITING_FOR_AN_ORDER;

		this.nCourse = 0;
		this.nPortion = 0;

		reportInitialStatus();
	}

	/**
	 * Update Student State
	 * 
	 * @param studentID
	 * @param studentState
	 * @throws RemoteException if either the invocation of the remote method, or the
	 *                         communication with the registry service fails
	 */
	@Override
	public synchronized void updateStudentState(int studentID, int studentState) throws RemoteException {
		studentStates[studentID] = studentState;
		reportStatus();
	}

	/**
	 * Set Student Seat
	 * 
	 * @param studentID
	 * @param studentSeat
	 * @throws RemoteException if either the invocation of the remote method, or the
	 *                         communication with the registry service fails
	 */
	@Override
	public synchronized void updateStudentSeat(int studentID, int studentSeat) throws RemoteException {
		seats[studentID] = studentSeat;
		reportStatus();
	}

	/**
	 * Set Student Seat
	 * 
	 * @param studentID
	 * @param studentSeat
	 * @param studentState
	 * @throws RemoteException if either the invocation of the remote method, or the
	 *                         communication with the registry service fails
	 */
	@Override
	public synchronized void updateStudentSeatAndState(int studentID, int studentSeat, int studentState)
			throws RemoteException {
		studentStates[studentID] = studentState;
		seats[studentSeat] = studentID;
		reportStatus();
	}

	/**
	 * Get Student Seat
	 * 
	 * @param studentID
	 * @return number of seat
	 * @throws RemoteException if either the invocation of the remote method, or the
	 *                         communication with the registry service fails
	 */
	@Override
	public synchronized int getStudentSeat(int studentSeat) throws RemoteException {
		for (int i = 0; i < seats.length; i++) {
			if (seats[i] == studentSeat) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Update the leaving of a student in the seats of the table
	 * 
	 * @param id student id to leave table
	 * @throws RemoteException if either the invocation of the remote method, or the
	 *                         communication with the registry service fails
	 */
	@Override
	public synchronized void updateSeatsAtLeaving(int id) throws RemoteException {
		int seat = 0;

		for (int i = 0; i < this.seats.length; i++) {
			if (this.seats[i] == id)
				seat = i;
		}

		this.seats[seat] = -1;
	}

	/**
	 * Update Waiter State
	 * 
	 * @param WaiterState
	 * @throws RemoteException if either the invocation of the remote method, or the
	 *                         communication with the registry service fails
	 */
	@Override
	public synchronized void updateWaiterState(int waiterState) throws RemoteException {
		this.waiterState = waiterState;
		reportStatus();
	}

	/**
	 * Update Chef State
	 * 
	 * @param newChefState
	 * @throws RemoteException if either the invocation of the remote method, or the
	 *                         communication with the registry service fails
	 */
	@Override
	public synchronized void updateChefState(int chefState) throws RemoteException {
		this.chefState = chefState;
		reportStatus();
	}

	/**
	 * Set variable nCourses and report status in the logging file
	 * 
	 * @param nCourse
	 * @param chefState
	 * @throws RemoteException if either the invocation of the remote method, or the
	 *                         communication with the registry service fails
	 */
	@Override
	public synchronized void updateCourse(int nCourse, int chefState) throws RemoteException {
		this.chefState = chefState;
		this.nCourse = nCourse;
		reportStatus();
	}

	/**
	 * Write the portion value in the logging file
	 * 
	 * @param value     nPortions value to set
	 * @param chefState
	 * @throws RemoteException if either the invocation of the remote method, or the
	 *                         communication with the registry service fails
	 */
	@Override
	public synchronized void updatePortion(int nPortion, int chefState) throws RemoteException {
		this.chefState = chefState;
		this.nPortion = nPortion;
		reportStatus();
	}

	/**
	 * Update the chef state, the nPortion and nCourse values
	 * 
	 * @param nPortion  number of the portion to be set
	 * @param nCourse   number of the course to be set
	 * @param chefState chef state
	 * @throws RemoteException if either the invocation of the remote method, or the
	 *                         communication with the registry service fails
	 */
	@Override
	public synchronized void updatePortionAndCourse(int nPortion, int nCourse, int chefState) throws RemoteException {
		this.chefState = chefState;
		this.nPortion = nPortion;
		this.nCourse = nCourse;
		reportStatus();
	}

	/**
	 * Print header.
	 */
	private void reportInitialStatus() {
		TextFile log = new TextFile(); // instantiation of a text file handler

		if (!log.openForWriting(".", logFileName)) {
			GenericIO.writelnString("The operation of creating the file " + logFileName + " failed!");
			System.exit(1);
		}
		log.writelnString("\t\t\t\t\t\t  The Restaurant - Description of the internal state");
		log.writelnString("\nChef\tWaiter\tStu0\tStu1\tStu2\tStu3\tStu4\tStu5\tStu6\tNCourse\tNPortion\t\t\t\tTable\n");
		log.writelnString(
				"\nState\tState\tState\tState\tState\tState\tState\tState\tState\t\t\t\t\t Seat0\tSeat1\tSeat2\tSeat3\tSeat4\tSeat5\tSeat6\n");
		if (!log.close()) {
			GenericIO.writelnString("The operation of closing the file " + logFileName + " failed!");
			System.exit(1);
		}
		reportStatus();
	}

	/**
	 * Write the body of the logging file.
	 *
	 */
	private void reportStatus() {
		TextFile log = new TextFile(); // instantiation of a text file handler

		String lineStatus = ""; // state line to be printed

		if (!log.openForAppending(".", logFileName)) {
			GenericIO.writelnString("The operation of opening for appending the file " + logFileName + " failed!");
			System.exit(1);
		}

		switch (chefState) {
		case ChefStates.WAITING_FOR_AN_ORDER:
			lineStatus += "WAFOR\t";
			break;
		case ChefStates.PREPARING_THE_COURSE:
			lineStatus += "PRPCS\t";
			break;
		case ChefStates.DISHING_THE_PORTIONS:
			lineStatus += "DSHPT\t";
			break;
		case ChefStates.DELIVERING_THE_PORTIONS:
			lineStatus += "DLVPT\t";
			break;
		case ChefStates.CLOSING_SERVICE:
			lineStatus += "CLSSV\t";
			break;
		}

		switch (waiterState) {
		case WaiterStates.APPRAISING_SITUATION:
			lineStatus += "APPST\t";
			break;
		case WaiterStates.PRESENTING_THE_MENU:
			lineStatus += "PRSMN\t";
			break;
		case WaiterStates.TAKING_THE_ORDER:
			lineStatus += "TKODR\t";
			break;
		case WaiterStates.PLACING_THE_ORDER:
			lineStatus += "PCODR\t";
			break;
		case WaiterStates.WAITING_FOR_PORTION:
			lineStatus += "WTFPT\t";
			break;
		case WaiterStates.PROCESSING_THE_BILL:
			lineStatus += "PRCBL\t";
			break;
		case WaiterStates.RECEIVING_PAYMENT:
			lineStatus += "RECPM\t";
			break;
		}

		for (int i = 0; i < ExecConsts.N; i++) {
			switch (studentStates[i]) {
			case StudentStates.GOING_TO_THE_RESTAURANT:
				lineStatus += "GGTRT\t";
				break;
			case StudentStates.TAKING_A_SEAT_AT_THE_TABLE:
				lineStatus += "TKSTT\t";
				break;
			case StudentStates.SELECTING_THE_COURSES:
				lineStatus += "SELCS\t";
				break;
			case StudentStates.ORGANIZING_THE_ORDER:
				lineStatus += "OGODR\t";
				break;
			case StudentStates.CHATTING_WITH_COMPANIONS:
				lineStatus += "CHTWC\t";
				break;
			case StudentStates.PAYING_THE_MEAL:
				lineStatus += "PYTML\t";
				break;
			case StudentStates.ENJOYING_THE_MEAL:
				lineStatus += "EJTML\t";
				break;
			case StudentStates.GOING_HOME:
				lineStatus += "GGHOM\t";
				break;
			}
		}

		lineStatus += this.nCourse + "\t\t" + this.nPortion + "\t";

		for (int i = 0; i < ExecConsts.N; i++) {
			lineStatus += "\t";
			if (seats[i] == -1) {
				lineStatus += " -";
			} else {
				lineStatus += " " + seats[i];
			}
		}

		log.writelnString(lineStatus);
		if (!log.close()) {
			GenericIO.writelnString("The operation of closing the file " + logFileName + " failed!");
			System.exit(1);
		}
	}

	/**
	 * Operation server shutdown.
	 *
	 * New operation.
	 * 
	 * @throws RemoteException if either the invocation of the remote method, or the
	 *                         communication with the registry service fails
	 */
	public synchronized void shutdown() {
		nEntities += 1;
		if (nEntities >= 3)
			GenReposMain.shutdown();
	}
}
