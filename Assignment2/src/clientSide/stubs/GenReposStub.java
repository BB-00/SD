package clientSide.stubs;

import commInfra.*;
import genclass.GenericIO;

/**
 * Stub to the General Repository.
 *
 * It instantiates a remote reference to the general repository. Implementation
 * of a client-server model of type 2 (server replication). Communication is
 * based on a communication channel under the TCP protocol.
 */
public class GenReposStub {
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
	public GenReposStub(String serverHostName, int serverPortNum) {
		this.serverHostName = serverHostName;
		this.serverPortNum = serverPortNum;
	}

	/**
	 * Write in the logging file the updated student state
	 * 
	 * @param studentID    student id
	 * @param studentState student state to be set
	 */
	public void updateStudentState(int studentID, int studentState) {
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

		outMessage = new Message(MessageType.SETUSSTATE, studentID, studentState);
		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if (inMessage.getMsgType() != MessageType.SACK) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}
		com.close();
	}

	/**
	 * Write in the logging file the updated seats value at the table
	 * 
	 * @param studentID   student id to sit
	 * @param studentSeat student seat to be set
	 */
	public void updateStudentSeat(int studentID, int studentSeat) {
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

		outMessage = new Message(MessageType.SETUSSEAT, studentID, studentSeat);
		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if (inMessage.getMsgType() != MessageType.SACK) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}
		com.close();
	}

	/**
	 * Write in the logging file the updated student seat and state
	 * 
	 * @param studentID student id
	 * @param studentSeat student seat to be set
	 * @param studentState student state to be set
	 */
	public void updateStudentSeatAndState(int studentID, int studentSeat, int studentState) {
		ClientCom com; // Client communication
		Message outMessage, inMessage; // outGoing and inGoing messages

		com = new ClientCom(serverHostName, serverPortNum);
		// Wait for a connection to be established
		while (!com.open()) {
			try {
				Thread.currentThread().sleep((long) (10));
			} catch (InterruptedException e) {
			}
		}

		outMessage = new Message(MessageType.SETUSSEATANDSTATE, studentID, studentSeat, studentState);
		com.writeObject(outMessage); // Write outGoing message in the communication channel
		inMessage = (Message) com.readObject(); // Read inGoing message

		// Validate inGoing message type and arguments
		if (inMessage.getMsgType() != MessageType.SACK) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}
		// Close communication channel
		com.close();
	}
	
	/**
	 * Write in the logging file the updated student seat when he leaves
	 * 
	 * @param studentID student id
	 */
	public void updateSeatsAtLeaving(int studentID) {
		ClientCom com; // Client communication
		Message outMessage, inMessage; // outGoing and inGoing messages

		com = new ClientCom(serverHostName, serverPortNum);
		// Wait for a connection to be established
		while (!com.open()) {
			try {
				Thread.currentThread().sleep((long) (10));
			} catch (InterruptedException e) {
			}
		}

		outMessage = new Message(MessageType.SETUSEATATLEAVING, studentID);
		com.writeObject(outMessage); // Write outGoing message in the communication channel
		inMessage = (Message) com.readObject(); // Read inGoing message

		// Validate inGoing message type and arguments
		if (inMessage.getMsgType() != MessageType.SACK) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}
		// Close communication channel
		com.close();
	}

	/**
	 * Write in the logging file the new waiter state
	 * 
	 * @param state waiter state to set
	 */
	public void updateWaiterState(int waiterState) {
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

		outMessage = new Message(MessageType.SETUWS, waiterState);
		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if (inMessage.getMsgType() != MessageType.SACK) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}
		com.close();
	}

	/**
	 * Write in the logging file the new chef state
	 * 
	 * @param state chef state to set
	 */
	public void updateChefState(int chefState) {
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

		outMessage = new Message(MessageType.SETUCS, chefState);
		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if (inMessage.getMsgType() != MessageType.SACK) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}
		com.close();
	}
	
	/**
	 * Write in the logging file the update number of Course and new chef state
	 * 
	 * @param nCourse number of course to set
	 * @param chefState chef state to set
	 */
	public void updateCourse(int nCourse, int chefState) {
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

		outMessage = new Message(MessageType.SETUCOURSE, nCourse, chefState);
		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if (inMessage.getMsgType() != MessageType.SACK) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}
		com.close();
	}
	
	/**
	 * Write in the logging file the update number of Portion and new chef state
	 * 
	 * @param nPortion number of portion to set
	 * @param chefState chef state to set
	 */
	public void updatePortion(int nPortion, int chefState) {
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

		outMessage = new Message(MessageType.SETUPORTION, nPortion, chefState);
		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if (inMessage.getMsgType() != MessageType.SACK) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}
		com.close();
	}

	/**
	 * Write in the logging file the update number of Portion, number of Course and new chef state
	 * 
	 * @param nPortion number of portion to set
	 * @param nCourse number of course to set
	 * @param chefState chef state to set
	 */
	public synchronized void updatePortionAndCourse(int nPortion, int nCourse,int chefState) {
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

		outMessage = new Message(MessageType.SETUPORTIONANDCOURSE, nPortion, nCourse, chefState);
		com.writeObject(outMessage);
		inMessage = (Message) com.readObject();

		if (inMessage.getMsgType() != MessageType.SACK) {
			GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
			GenericIO.writelnString(inMessage.toString());
			System.exit(1);
		}
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
				Thread.currentThread().sleep((long) (10));
			} catch (InterruptedException e) {
			}
		}

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
