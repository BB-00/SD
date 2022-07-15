package serverSide.sharedRegions;

import serverSide.entities.*;
import clientSide.entities.StudentStates;
import clientSide.entities.WaiterStates;
import commInfra.*;

/**
 * Interface to the Table.
 *
 * It is responsible to validate and process the incoming message, execute the
 * corresponding method on the Table and generate the outgoing message.
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 */
public class TableInterface {
	/**
	 * Reference to the table.
	 */
	private final Table table;

	/**
	 * Instantiation of an interface to the table.
	 *
	 * @param table reference to the kitchen
	 */
	public TableInterface(Table table) {
		this.table = table;
	}

	/**
	 * Processing of the incoming messages.
	 *
	 * Validation, execution of the corresponding method and generation of the
	 * outgoing message.
	 *
	 * @param inMessage service request
	 * @return service reply
	 * @throws MessageException if the incoming message is not valid
	 */
	public Message processAndReply(Message inMessage) throws MessageException {
		Message outMessage = null; // mensagem de resposta

		/* validation of the incoming message */

		/* processing */

		switch (inMessage.getMsgType()) {
		// Waiter Messages that require only type verification
		case MessageType.REQHACBS: // Have all clients been served
		case MessageType.REQDP: // Deliver portion
		case MessageType.SHUT: // Table shutdown
			break;
		// Waiter Messages that require type and state verification
		case MessageType.REQSC: // Salute the clients
		case MessageType.REQRB: // Return to the bar
		case MessageType.REQGP: // Get the pad
		case MessageType.REQPB: // Present the bill
			if (inMessage.getWaiterState() < WaiterStates.APPRAISING_SITUATION
					|| inMessage.getWaiterState() > WaiterStates.RECEIVING_PAYMENT)
				throw new MessageException("Invalid Waiter state!", inMessage);
			break;
		// Student Messages that require only type verification
		case MessageType.REQEHC: // Has everybody chosen
		case MessageType.REQAUOC: // Add up ones choices
		case MessageType.REQDO: // Describe order
		case MessageType.REQHEFE: // Has everybody finished eating
		case MessageType.REQHB: // Honour the bill
		case MessageType.REQHACBE: // Have all courses been eaten
			break;
		// Student Messages that require type, state and id verification (done in
		// Message Constructor)
		case MessageType.REQSAT: // Seat at table
		case MessageType.REQRM: // Read menu
		case MessageType.REQPO: // Prepare the order
		case MessageType.REQJT: // Join the talk
		case MessageType.REQIC: // Inform companion
		case MessageType.REQSE: // Start eating
		case MessageType.REQEE: // End eating
		case MessageType.REQSHAE: // Should have arrived earlier
			if (inMessage.getStudentState() < StudentStates.GOING_TO_THE_RESTAURANT
					|| inMessage.getStudentState() > StudentStates.GOING_HOME)
				throw new MessageException("Inavlid Student state!", inMessage);
			break;
		// Aditional messages that require type verification
		case MessageType.REQGFTA: // Get first to arrive
		case MessageType.REQGLTE: // Get last to eat
		case MessageType.REQSFTA: // Set first to arrive
		case MessageType.REQSLTA: // Set last to arrive
			break;
		default:
			throw new MessageException("Invalid message type!", inMessage);
		}

		// processing

		switch (inMessage.getMsgType()) {
		case MessageType.REQSC:
			((TableClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
			((TableClientProxy) Thread.currentThread()).setStudentBeingAnswered(inMessage.getStudentIDBeingAnswered());
			int studentID = inMessage.getStudentBeingAnswered();
			table.saluteClient(studentID);
			outMessage = new Message(MessageType.SCDONE,
					((TableClientProxy) Thread.currentThread()).getStudentBeingAnswered(),
					((TableClientProxy) Thread.currentThread()).getWaiterState());
			break;

		case MessageType.REQRB:
			((TableClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
			table.returnBar();
			outMessage = new Message(MessageType.RBDONE, ((TableClientProxy) Thread.currentThread()).getWaiterState());
			break;

		case MessageType.REQGP:
			((TableClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
			table.getThePad();
			outMessage = new Message(MessageType.GPDONE, ((TableClientProxy) Thread.currentThread()).getWaiterState());
			break;

		case MessageType.REQHACBS:
			boolean b = table.haveAllClientsBeenServed();
			outMessage = new Message(MessageType.HACBSDONE, b);
			break;

		case MessageType.REQDP:
			table.deliverPortion();
			outMessage = new Message(MessageType.DPDONE);
			break;

		case MessageType.REQPB:
			((TableClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
			table.presentBill();
			outMessage = new Message(MessageType.PBDONE, ((TableClientProxy) Thread.currentThread()).getWaiterState());
			break;

		case MessageType.REQSAT:
			((TableClientProxy) Thread.currentThread()).setStudentID(inMessage.getStudentID());
			((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
			table.seatAtTable();
			outMessage = new Message(MessageType.SATDONE, ((TableClientProxy) Thread.currentThread()).getStudentID(),
					((TableClientProxy) Thread.currentThread()).getStudentState());
			break;

		case MessageType.REQRM:
			((TableClientProxy) Thread.currentThread()).setStudentID(inMessage.getStudentID());
			((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
			table.readMenu();
			outMessage = new Message(MessageType.RMDONE, ((TableClientProxy) Thread.currentThread()).getStudentID(),
					((TableClientProxy) Thread.currentThread()).getStudentState());
			break;

		case MessageType.REQPO:
			((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
			table.prepareOrder();
			outMessage = new Message(MessageType.PODONE, ((TableClientProxy) Thread.currentThread()).getStudentState());
			break;

		case MessageType.REQEHC:
			boolean everybodyChosen = table.everybodyHasChosen();
			outMessage = new Message(MessageType.EHCDONE, everybodyChosen);
			break;

		case MessageType.REQAUOC:
			table.addUpOnesChoices();
			outMessage = new Message(MessageType.AUOCDONE);
			break;

		case MessageType.REQDO:
			table.describeOrder();
			outMessage = new Message(MessageType.DODONE);
			break;

		case MessageType.REQJT:
			((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
			table.joinTalk();
			outMessage = new Message(MessageType.JTDONE, ((TableClientProxy) Thread.currentThread()).getStudentState());
			break;

		case MessageType.REQIC:
			((TableClientProxy) Thread.currentThread()).setStudentID(inMessage.getStudentID());
			((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
			table.informCompanion();
			outMessage = new Message(MessageType.ICDONE, ((TableClientProxy) Thread.currentThread()).getStudentID(),
					((TableClientProxy) Thread.currentThread()).getStudentState());
			break;

		case MessageType.REQSE:
			((TableClientProxy) Thread.currentThread()).setStudentID(inMessage.getStudentID());
			((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
			table.startEating();
			outMessage = new Message(MessageType.SEDONE, ((TableClientProxy) Thread.currentThread()).getStudentID(),
					((TableClientProxy) Thread.currentThread()).getStudentState());
			break;

		case MessageType.REQEE:
			((TableClientProxy) Thread.currentThread()).setStudentID(inMessage.getStudentID());
			((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
			table.endEating();
			outMessage = new Message(MessageType.EEDONE, ((TableClientProxy) Thread.currentThread()).getStudentID(),
					((TableClientProxy) Thread.currentThread()).getStudentState());
			break;

		case MessageType.REQHEFE:
			((TableClientProxy) Thread.currentThread()).setStudentID(inMessage.getStudentID());
			boolean everybodyEaten = table.hasEverybodyFinishedEating();
			outMessage = new Message(MessageType.HEFEDONE, ((TableClientProxy) Thread.currentThread()).getStudentID(),
					everybodyEaten);
			break;

		case MessageType.REQHB:
			table.honourBill();
			outMessage = new Message(MessageType.HBDONE);
			break;

		case MessageType.REQHACBE:
			boolean allCoursesEaten = table.haveAllCoursesBeenEaten();
			outMessage = new Message(MessageType.HACBEDONE, allCoursesEaten);
			break;

		case MessageType.REQSHAE:
			((TableClientProxy) Thread.currentThread()).setStudentID(inMessage.getStudentID());
			((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
			boolean lastToArrive = table.shouldHaveArrivedEarlier();
			outMessage = new Message(MessageType.SHAEDONE, ((TableClientProxy) Thread.currentThread()).getStudentID(),
					((TableClientProxy) Thread.currentThread()).getStudentState(), lastToArrive);
			break;

		case MessageType.REQGFTA:
			int id = table.getFirstToArrive();
			outMessage = new Message(MessageType.GFTADONE, id);
			break;

		case MessageType.REQGLTE:
			int idEat = table.getLastToEat();
			outMessage = new Message(MessageType.GLTEDONE, idEat);
			break;

		case MessageType.REQSFTA:
			table.setFirstToArrive(inMessage.getFirstToArrive());
			outMessage = new Message(MessageType.SFTADONE);
			break;

		case MessageType.REQSLTA:
			table.setLastToArrive(inMessage.getLastToArrive());
			outMessage = new Message(MessageType.SLTADONE);
			break;

		case MessageType.SHUT:
			table.shutdown();
			outMessage = new Message(MessageType.SHUTDONE);
			break;
		}

		return (outMessage);
	}

}