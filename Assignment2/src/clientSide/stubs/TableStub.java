package clientSide.stubs;

import clientSide.entities.*;
import genclass.GenericIO;
import commInfra.*;
import serverSide.entities.*;

/**
 * Stub to the Table.
 *
 * It instantiates a remote reference to the barber shop. Implementation of a
 * client-server model of type 2 (server replication). Communication is based on
 * a communication channel under the TCP protocol.
 */
public class TableStub {
	/**
	 * Name of the platform where is located the table Server
	 */
	private String serverHostName;

	/**
	 * Port number for listening to service requests
	 */
	private int serverPortNum;

	/**
	 * Instantiation of a stub to the Table
	 * 
	 * @param serverHostName name of the platform where is located the table Server
	 * @param serverPortNum  port number for listening to service requests
	 */
	public TableStub(String serverHostName, int serverPortNum) {
		this.serverHostName = serverHostName;
		this.serverPortNum = serverPortNum;
	}

	/**
	 * Set id of the first student to arrive
	 * 
	 * @param firstToArrive id of the first student to arrive
	 */
	public void setFirstToArrive(int studentID) {
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
		outMessage = new Message(MessageType.REQSFTA, studentID);

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.SFTADONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		com.close();
	}

	/**
	 * Obtain id of the first student to arrive
	 * 
	 * @return id of the first student to arrive at the restaurant
	 */
	public int getFirstToArrive() {
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
		outMessage = new Message(MessageType.REQGFTA);

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.GFTADONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		com.close();
		return inMessage.getFirstToArrive();
	}

	/**
	 * Set id of the last student to arrive
	 * 
	 * @param lastToArrive if of the last student to arrive to the restaurant
	 */
	public void setLastToArrive(int studentID) {
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
		outMessage = new Message(MessageType.REQSLTA, studentID);

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.SLTADONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		com.close();
	}

	/**
	 * Obtain id of the last student to arrive
	 * 
	 * @return id of the last student to finish eating a meal
	 */
	public int getLastToEat() {
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
		outMessage = new Message(MessageType.REQGLTE);

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.GLTEDONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		com.close();
		return inMessage.getLastToEat();
	}

	/**
	 * Operation salute the client
	 * 
	 * It is called by the waiter when a student enters the restaurant and needs to
	 * be saluted Waiter waits for the student to take a seat (if he hasn't done it
	 * yet) Waiter waits for student to finish reading the menu
	 */
	public void saluteClient(int studentIDBeingAnswered) {
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
		outMessage = new Message(MessageType.REQSC, studentIDBeingAnswered,
				((Waiter) Thread.currentThread()).getWaiterState());

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if (inMessage.getMsgType() != MessageType.SCDONE) {
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

		if (inMessage.getStudentBeingAnswered() != -1) {
			GenericIO.writelnString("Thread Student" + inMessage.getStudentID() + ": Invalid Student ID!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		((Waiter) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
		com.close();
	}

	/**
	 * Operation return to the bar
	 * 
	 * It is called by the waiter to return to the bar to the appraising situation
	 * state
	 */
	public void returnBar() {
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
		outMessage = new Message(MessageType.REQRB, ((Waiter) Thread.currentThread()).getWaiterState());

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.RBDONE)) {
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
	 * Operation get the pad
	 * 
	 * It is called by the waiter when an order is going to be described by the
	 * first student to arrive Waiter Blocks waiting for student to describe him the
	 * order
	 */
	public void getThePad() {
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
		outMessage = new Message(MessageType.REQGP, ((Waiter) Thread.currentThread()).getWaiterState());

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.GPDONE)) {
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
	 * Operation have all clients been served
	 * 
	 * Called by the waiter to check if all clients have been served or not
	 * 
	 * @return true if all clients have been served, false otherwise
	 */
	public boolean haveAllClientsBeenServed() {
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
		outMessage = new Message(MessageType.REQHACBS);

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.HACBSDONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		com.close();

		return inMessage.getAllClientsBeenServed();
	}

	/**
	 * Operation deliver portion
	 * 
	 * Called by the waiter, to deliver a portion
	 */
	public void deliverPortion() {
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
		outMessage = new Message(MessageType.REQDP);

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.DPDONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		com.close();
	}

	/**
	 * Operation present the bill
	 * 
	 * Called by the waiter to present the bill to the last student to arrive
	 */
	public void presentBill() {
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
		outMessage = new Message(MessageType.REQPB, ((Waiter) Thread.currentThread()).getWaiterState());

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.PBDONE)) {
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
	 * Operation siting at the table
	 * 
	 * Student comes in the table and sits (blocks) waiting for waiter to bring him
	 * the menu Called by the student (inside enter method in the bar)
	 */
	public void seatAtTable() {
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
		outMessage = new Message(MessageType.REQSAT, ((StudentCloning) Thread.currentThread()).getStudentID(),
				((StudentCloning) Thread.currentThread()).getStudentState());

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.SATDONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		if (inMessage.getStudentID() != ((StudentCloning) Thread.currentThread()).getStudentID()) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Student ID!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		if (inMessage.getStudentState() < StudentStates.GOING_TO_THE_RESTAURANT
				|| inMessage.getStudentState() > StudentStates.GOING_HOME) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Student State!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		((StudentCloning) Thread.currentThread()).setStudentState(inMessage.getStudentState());
		com.close();
	}

	/**
	 * Operation read the menu
	 * 
	 * Called by the student to read a menu, wakes up waiter to signal that he
	 * already read the menu
	 */
	public void readMenu() {
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
		outMessage = new Message(MessageType.REQRM, ((Student) Thread.currentThread()).getStudentID(),
				((Student) Thread.currentThread()).getStudentState());

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.RMDONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		if (inMessage.getStudentID() != ((Student) Thread.currentThread()).getStudentID()) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Student ID!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		if (inMessage.getStudentState() < StudentStates.GOING_TO_THE_RESTAURANT
				|| inMessage.getStudentState() > StudentStates.GOING_HOME) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Student State!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
		com.close();
	}

	/**
	 * Operation prepare the order
	 * 
	 * Called by the student to begin the preparation of the order (options of his
	 * companions)
	 */
	public void prepareOrder() {
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
		outMessage = new Message(MessageType.REQPO, ((Student) Thread.currentThread()).getStudentState());

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.PODONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}
		if (inMessage.getStudentState() < StudentStates.GOING_TO_THE_RESTAURANT
				|| inMessage.getStudentState() > StudentStates.GOING_HOME) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Student State!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
		com.close();
	}

	/**
	 * Operation everybody has chosen
	 * 
	 * Called by the first student to arrive to check if all his companions have
	 * choose or not Blocks if not waiting to be waker up be a companion to give him
	 * his preference
	 * 
	 * @return true if everybody choose their course choice, false otherwise
	 */
	public boolean everybodyHasChosen() {
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
		outMessage = new Message(MessageType.REQEHC);

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.EHCDONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		com.close();

		return inMessage.getEverybodyHasChosen();
	}

	/**
	 * Operation add up ones choices
	 * 
	 * Called by the first student to arrive to add up a companions choice to the
	 * order
	 */
	public void addUpOnesChoices() {
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
		outMessage = new Message(MessageType.REQAUOC);

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.AUOCDONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		com.close();
	}

	/**
	 * Operation describe the order
	 * 
	 * Called by the first student to arrive to describe the order to the waiter
	 * Blocks waiting for waiter to come with pad Wake waiter up so he can take the
	 * order
	 */
	public void describeOrder() {
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
		outMessage = new Message(MessageType.REQDO);

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.DODONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		com.close();
	}

	/**
	 * Operation join the talk
	 * 
	 * Called by the first student to arrive so he can join his companions while
	 * waiting for the courses to be delivered
	 */
	public void joinTalk() {
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
		outMessage = new Message(MessageType.REQJT, ((Student) Thread.currentThread()).getStudentState());

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.JTDONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		if (inMessage.getStudentState() < StudentStates.GOING_TO_THE_RESTAURANT
				|| inMessage.getStudentState() > StudentStates.GOING_HOME) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Student State!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
		com.close();
	}

	/**
	 * Operation inform companion
	 * 
	 * Called by a student to inform the first student to arrive about their
	 * preferences Blocks if someone else is informing at the same time
	 */
	public void informCompanion() {
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
		outMessage = new Message(MessageType.REQIC, ((Student) Thread.currentThread()).getStudentID(),
				((Student) Thread.currentThread()).getStudentState());

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.ICDONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		if (inMessage.getStudentID() != ((Student) Thread.currentThread()).getStudentID()) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Student ID!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		if (inMessage.getStudentState() < StudentStates.GOING_TO_THE_RESTAURANT
				|| inMessage.getStudentState() > StudentStates.GOING_HOME) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Student State!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
		com.close();
	}

	/**
	 * Operation start eating
	 * 
	 * Called by the student to start eating the meal (During random time)
	 */
	public void startEating() {
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
		outMessage = new Message(MessageType.REQSE, ((Student) Thread.currentThread()).getStudentID(),
				((Student) Thread.currentThread()).getStudentState());

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.SEDONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		if (inMessage.getStudentID() != ((Student) Thread.currentThread()).getStudentID()) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Student ID!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		if (inMessage.getStudentState() < StudentStates.GOING_TO_THE_RESTAURANT
				|| inMessage.getStudentState() > StudentStates.GOING_HOME) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Student State!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
		com.close();
	}

	/**
	 * Operation end eating
	 * 
	 * Called by the student to signal that he has finished eating his meal
	 */
	public void endEating() {
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
		outMessage = new Message(MessageType.REQEE, ((Student) Thread.currentThread()).getStudentID(),
				((Student) Thread.currentThread()).getStudentState());

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.EEDONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		if (inMessage.getStudentID() != ((Student) Thread.currentThread()).getStudentID()) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Student ID!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		if (inMessage.getStudentState() < StudentStates.GOING_TO_THE_RESTAURANT
				|| inMessage.getStudentState() > StudentStates.GOING_HOME) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Student State!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
		com.close();
	}

	/**
	 * Operation has everybody finished eating
	 * 
	 * Called by the student to wait for his companions to finish eating
	 * 
	 * @return true if everybody has finished eating a course, else false
	 */
	public boolean hasEverybodyFinishedEating() {
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
		outMessage = new Message(MessageType.REQHEFE, ((Student) Thread.currentThread()).getStudentID());

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.HEFEDONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		if (inMessage.getStudentID() != ((Student) Thread.currentThread()).getStudentID()) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Student ID!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		com.close();

		return inMessage.getHasEverybodyFinishedEating();
	}

	/**
	 * Operation honour the bill
	 * 
	 * Called by the student to pay the bill Student blocks waiting for bill to be
	 * presented and signals waiter when it's time to pay it
	 */
	public void honourBill() {
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
		outMessage = new Message(MessageType.REQHB);

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.HBDONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		com.close();
	}

	/**
	 * Operation have all courses been eaten
	 * 
	 * Called by the student to check if there are more courses to be eaten Student
	 * blocks waiting for the course to be served
	 * 
	 * @return true if all courses have been eaten, false otherwise
	 */
	public boolean haveAllCoursesBeenEaten() {
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
		outMessage = new Message(MessageType.REQHACBE);

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.HACBEDONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		com.close();

		return inMessage.getAllCoursesEaten();
	}

	/**
	 * Operation should have arrived earlier
	 * 
	 * Called by the student to check which one was last to arrive
	 * 
	 * @return True if current student was the last to arrive, false otherwise
	 */
	public boolean shouldHaveArrivedEarlier() {
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
		outMessage = new Message(MessageType.REQSHAE, ((Student) Thread.currentThread()).getStudentID(),
				((Student) Thread.currentThread()).getStudentState());

		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if ((inMessage.getMsgType() != MessageType.SHAEDONE)) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		if (inMessage.getStudentID() != ((Student) Thread.currentThread()).getStudentID()) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Student ID!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		if (inMessage.getStudentState() < StudentStates.GOING_TO_THE_RESTAURANT
				|| inMessage.getStudentState() > StudentStates.GOING_HOME) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Student State!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}

		((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
		com.close();

		return inMessage.getArrivedEarlier();
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
