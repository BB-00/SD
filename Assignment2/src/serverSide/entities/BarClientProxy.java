package serverSide.entities;

import serverSide.sharedRegions.*;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

/**
 * Service provider agent for access to the Bar.
 *
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 */
public class BarClientProxy extends Thread implements StudentCloning, WaiterCloning, ChefCloning {
	/**
	 * Number of instantiated threads.
	 */
	private static int nProxy = 0;

	/**
	 * Communication channel.
	 */
	private ServerCom sconi;

	/**
	 * Interface to the Bar.
	 */
	private BarInterface barInterface;

	/**
	 * Student identification.
	 */
	private int studentID = -1;

	/**
	 * Student state.
	 */
	private int studentState = 0;

	/**
	 * Chef state.
	 */
	private int chefState = 0;

	/**
	 * Waiter state.
	 */
	private int waiterState = 0;

	/**
	 * Instantiation of a client proxy.
	 *
	 * @param sconi    communication channel
	 * @param barInter interface to the bar
	 */
	public BarClientProxy(ServerCom sconi, BarInterface barInter) {
		super("BarProxy_" + BarClientProxy.getProxyId());
		this.sconi = sconi;
		this.barInterface = barInter;
	}

	/**
	 * Generation of the instantiation identifier.
	 *
	 * @return instantiation identifier
	 */
	private static int getProxyId() {
		Class<?> cl = null; // representation of the BarClientProxy object in JVM
		int proxyId; // instantiation identifier

		try {
			cl = Class.forName("serverSide.entities.BarClientProxy");
		} catch (ClassNotFoundException e) {
			GenericIO.writelnString("Data type BarClientProxy was not found!");
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
			outMessage = barInterface.processAndReply(inMessage); // process it
		} catch (MessageException e) {
			GenericIO.writelnString("Thread " + getName() + ": " + e.getMessage() + "!");
			GenericIO.writelnString(e.getMessageVal().toString());
			System.exit(1);
		}

		sconi.writeObject(outMessage); // send service reply
		sconi.close(); // close the communication channel
	}

	/**
	 * Set student id.
	 *
	 * @param id student id
	 */
	public void setStudentID(int id) {
		studentID = id;
	}

	/**
	 * Get student id.
	 *
	 * @return student id
	 */
	public int getStudentID() {
		return studentID;
	}

	/**
	 * Set student state.
	 *
	 * @param state new student state
	 */
	public void setStudentState(int state) {
		studentState = state;
	}

	/**
	 * Get student state.
	 *
	 * @return student state
	 */
	public int getStudentState() {
		return studentState;
	}

	/**
	 * Set waiter state.
	 *
	 * @param state new waiter state
	 */
	public void setWaiterState(int state) {
		// System.out.println("BarProxy WAITER STATE: "+ state);
		waiterState = state;
	}

	/**
	 * Get waiter state.
	 *
	 * @return waiter state
	 */
	public int getWaiterState() {
		return waiterState;
	}

	/**
	 * Set chef state.
	 *
	 * @param state new chef state
	 */
	public void setChefState(int state) {
		chefState = state;
	}

	/**
	 * Get chef state.
	 *
	 * @return chef state
	 */
	public int getChefState() {
		return chefState;
	}
}