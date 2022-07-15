package clientSide.stubs;

import clientSide.entities.Chef;
import clientSide.entities.Waiter;
import clientSide.entities.ChefStates;
import clientSide.entities.WaiterStates;
import genclass.GenericIO;
import commInfra.*;

/**
 * Stub to the Kitchen.
 *
 * It instantiates a remote reference to the barber shop. Implementation of a
 * client-server model of type 2 (server replication). Communication is based on
 * a communication channel under the TCP protocol.
 */
public class KitchenStub {
	/**
	 * Name of the platform where is located the kitchen Server
	 */
	private String serverHostName;

	/**
	 * Port number for listening to service requests
	 */
	private int serverPortNum;

	/**
	 * Instantiation of a stub to the kitchen
	 * 
	 * @param serverHostName name of the platform where is located the kitchen
	 *                       Server
	 * @param serverPortNum  port number for listening to service requests
	 */
	public KitchenStub(String serverHostName, int serverPortNum) {
		this.serverHostName = serverHostName;
		this.serverPortNum = serverPortNum;
	}

	/**
	 * Operation watch the news
	 * 
	 * It is called by the chef, he waits for waiter to notify him of the order
	 */
	public void watchTheNews() {
		ClientCom com; // communication channel
		Message outMessage, // outgoing message
				inMessage; // incoming message

		com = new ClientCom(serverHostName, serverPortNum);
		while (!com.open()) {
			try {
				Thread.currentThread().sleep((long) (10));
			} catch (InterruptedException e) {
			}
		}

		// MESSAGES
		outMessage = new Message(MessageType.REQWTN, ((Chef) Thread.currentThread()).getChefState());

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.WTNDONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		if (inMessage.getChefState() < ChefStates.WAITING_FOR_AN_ORDER
				|| inMessage.getChefState() > ChefStates.CLOSING_SERVICE) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Chef State!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		((Chef) Thread.currentThread()).setChefState(inMessage.getChefState());
		com.close();

	}

	/**
	 * Operation start presentation
	 * 
	 * It is called by the chef after waiter has notified him of the order to be
	 * prepared to signal that preparation of the course has started
	 */
	public void startPreparation() {
		ClientCom com; // communication channel
		Message outMessage, // outgoing message
				inMessage; // incoming message

		com = new ClientCom(serverHostName, serverPortNum);
		while (!com.open()) {
			try {
				Thread.currentThread().sleep((long) (10));
			} catch (InterruptedException e) {
			}
		}

		// MESSAGES
		outMessage = new Message(MessageType.REQSTP, ((Chef) Thread.currentThread()).getChefState());

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.STPDONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		if (inMessage.getChefState() < ChefStates.WAITING_FOR_AN_ORDER
				|| inMessage.getChefState() > ChefStates.CLOSING_SERVICE) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Chef State!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		((Chef) Thread.currentThread()).setChefState(inMessage.getChefState());
		com.close();
	}

	/**
	 * Operation proceed presentation
	 * 
	 * It is called by the chef when a portion needs to be prepared
	 */
	public void proceedToPresentation() {
		ClientCom com; // communication channel
		Message outMessage, // outgoing message
				inMessage; // incoming message

		com = new ClientCom(serverHostName, serverPortNum);
		while (!com.open()) {
			try {
				Thread.currentThread().sleep((long) (10));
			} catch (InterruptedException e) {
			}
		}

		// MESSAGES
		outMessage = new Message(MessageType.REQPTP, ((Chef) Thread.currentThread()).getChefState());

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.PTPDONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		if (inMessage.getChefState() < ChefStates.WAITING_FOR_AN_ORDER
				|| inMessage.getChefState() > ChefStates.CLOSING_SERVICE) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Chef State!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		((Chef) Thread.currentThread()).setChefState(inMessage.getChefState());
		com.close();
	}

	/**
	 * Operation have next portion ready
	 * 
	 * It is called by the chef after a portion has been delivered and another one
	 * needs to be prepared
	 */
	public void haveNextPortionReady() {
		ClientCom com; // communication channel
		Message outMessage, // outgoing message
				inMessage; // incoming message

		com = new ClientCom(serverHostName, serverPortNum);
		while (!com.open()) {
			try {
				Thread.currentThread().sleep((long) (10));
			} catch (InterruptedException e) {
			}
		}

		// MESSAGES
		outMessage = new Message(MessageType.REQHNPR, ((Chef) Thread.currentThread()).getChefState());

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.HNPRDONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		if ((inMessage.getChefState() < ChefStates.WAITING_FOR_AN_ORDER
				|| inMessage.getChefState() > ChefStates.CLOSING_SERVICE)
				|| (inMessage.getChefState() != ChefStates.DELIVERING_THE_PORTIONS)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Chef State!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		((Chef) Thread.currentThread()).setChefState(inMessage.getChefState());
		com.close();
	}

	/**
	 * Operation continue preparation
	 * 
	 * It is called by the chef when all portions have been delivered, but the
	 * course has not been completed yet
	 */
	public void continuePreparation() {
		ClientCom com; // communication channel
		Message outMessage, // outgoing message
				inMessage; // incoming message

		com = new ClientCom(serverHostName, serverPortNum);
		while (!com.open()) {
			try {
				Thread.currentThread().sleep((long) (10));
			} catch (InterruptedException e) {
			}
		}

		// MESSAGES
		outMessage = new Message(MessageType.REQCP, ((Chef) Thread.currentThread()).getChefState());

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		// TODO Message Types - enter
		if ((inMessage.getMsgType() != MessageType.CPDONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		if (inMessage.getChefState() < ChefStates.WAITING_FOR_AN_ORDER
				|| inMessage.getChefState() > ChefStates.CLOSING_SERVICE) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Chef State!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		((Chef) Thread.currentThread()).setChefState(inMessage.getChefState());
		com.close();
	}

	/**
	 * Operation have all portions been delivered
	 * 
	 * It is called by the chef when he finishes a portion and checks if another one
	 * needs to be prepared or not It is also here were the chef blocks waiting for
	 * waiter do deliver the current portion
	 * 
	 * @return true if all portions have been delivered, false otherwise
	 */
	public boolean haveAllPortionsBeenDelivered() {
		ClientCom com; // communication channel
		Message outMessage, // outgoing message
				inMessage; // incoming message

		com = new ClientCom(serverHostName, serverPortNum);
		while (!com.open()) {
			try {
				Thread.currentThread().sleep((long) (10));
			} catch (InterruptedException e) {
			}
		}

		// MESSAGES
		outMessage = new Message(MessageType.REQHAPBD);

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.HAPBDDONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		com.close();

		return inMessage.getAllPortionsBeenDelivered();
	}

	/**
	 * Operation has order been completed
	 * 
	 * It is called by the chef when he finishes preparing all courses to check if
	 * order has been completed or not
	 * 
	 * @return true if all courses have been completed, false or not
	 */
	public boolean hasOrderBeenCompleted() {
		ClientCom com; // communication channel
		Message outMessage, // outgoing message
				inMessage; // incoming message

		com = new ClientCom(serverHostName, serverPortNum);
		while (!com.open()) {
			try {
				Thread.currentThread().sleep((long) (10));
			} catch (InterruptedException e) {
			}
		}

		// MESSAGES
		outMessage = new Message(MessageType.REQHOBC);

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.HOBCDONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		com.close();

		return inMessage.getHasOrderBeenCompleted();
	}

	/**
	 * Operation clean up
	 * 
	 * It is called by the chef when he finishes the order, to close service
	 */
	public void cleanUp() {
		ClientCom com; // communication channel
		Message outMessage, // outgoing message
				inMessage; // incoming message

		com = new ClientCom(serverHostName, serverPortNum);
		while (!com.open()) {
			try {
				Thread.currentThread().sleep((long) (10));
			} catch (InterruptedException e) {
			}
		}

		// MESSAGES
		outMessage = new Message(MessageType.REQCU, ((Chef) Thread.currentThread()).getChefState());

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.CUDONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		if (inMessage.getChefState() < ChefStates.WAITING_FOR_AN_ORDER
				|| inMessage.getChefState() > ChefStates.CLOSING_SERVICE) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Chef State!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		((Chef) Thread.currentThread()).setChefState(inMessage.getChefState());
		com.close();
	}

	/**
	 * Operation return to the bar
	 * 
	 * Called by the waiter when he is the kitchen and returns to the bar
	 */
	public void returnToBar() {
		ClientCom com; // communication channel
		Message outMessage, // outgoing message
				inMessage; // incoming message

		com = new ClientCom(serverHostName, serverPortNum);
		while (!com.open()) {
			try {
				Thread.currentThread().sleep((long) (10));
			} catch (InterruptedException e) {
			}
		}

		// MESSAGES
		outMessage = new Message(MessageType.REQRTB, ((Waiter) Thread.currentThread()).getWaiterState());

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.RTBDONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		if (inMessage.getWaiterState() < WaiterStates.APPRAISING_SITUATION
				|| inMessage.getWaiterState() > WaiterStates.RECEIVING_PAYMENT) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Waiter State!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		((Waiter) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
		com.close();
	}

	/**
	 * Operation hand note to chef
	 * 
	 * Called by the waiter to wake chef up chef to give him the description of the
	 * order
	 */
	public void handNoteToChef() {
		ClientCom com; // communication channel
		Message outMessage, // outgoing message
				inMessage; // incoming message

		com = new ClientCom(serverHostName, serverPortNum);
		while (!com.open()) {
			try {
				Thread.currentThread().sleep((long) (10));
			} catch (InterruptedException e) {
			}
		}

		// MESSAGES
		outMessage = new Message(MessageType.REQHNTC, ((Waiter) Thread.currentThread()).getWaiterState());

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.HNTCDONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		if (inMessage.getWaiterState() < WaiterStates.APPRAISING_SITUATION
				|| inMessage.getWaiterState() > WaiterStates.RECEIVING_PAYMENT) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Waiter State!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		((Waiter) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
		com.close();
	}

	/**
	 * Operation collect portion
	 * 
	 * Called by the waiter when there is a portion to be delivered. Collect and
	 * signal chef that the portion was delivered
	 */
	public void collectPortion() {
		ClientCom com; // communication channel
		Message outMessage, // outgoing message
				inMessage; // incoming message

		com = new ClientCom(serverHostName, serverPortNum);
		while (!com.open()) {
			try {
				Thread.currentThread().sleep((long) (10));
			} catch (InterruptedException e) {
			}
		}

		// MESSAGES
		outMessage = new Message(MessageType.REQCPOR, ((Waiter) Thread.currentThread()).getWaiterState());

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.CPORDONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		if (inMessage.getWaiterState() < WaiterStates.APPRAISING_SITUATION
				|| inMessage.getWaiterState() > WaiterStates.RECEIVING_PAYMENT) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Waiter State!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		((Waiter) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
		com.close();
	}

	/**
	 * Operation server shutdown.
	 *
	 * New operation.
	 */
	public void shutdown() {
		ClientCom com; // communication channel
		Message outMessage, // outgoing message
				inMessage; // incoming message

		com = new ClientCom(serverHostName, serverPortNum);
		while (!com.open()) {
			try {
				Thread.sleep((long) (1000));
			} catch (InterruptedException e) {
			}
		}

		// MESSAGES
		outMessage = new Message(MessageType.SHUT);

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if (inMessage.getMsgType() != MessageType.SHUTDONE) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}
		com.close();
	}
}
