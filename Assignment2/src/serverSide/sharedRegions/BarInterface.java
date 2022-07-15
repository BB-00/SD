package serverSide.sharedRegions;

import serverSide.entities.*;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

/**
 * Interface to the Bar.
 *
 * It is responsible to validate and process the incoming message, execute the
 * corresponding method on the Bar and generate the outgoing message.
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 */
public class BarInterface {
	/**
	 * Reference to the bar.
	 */
	private final Bar bar;

	/**
	 * Instantiation of an interface to the bar.
	 *
	 * @param bar reference to the bar
	 */
	public BarInterface(Bar bar) {
		this.bar = bar;
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
		// Chef Messages that require type and state verification
		case MessageType.REQAL: // Alert the Waiter Request
			if (inMessage.getChefState() < ChefStates.WAITING_FOR_AN_ORDER
					|| inMessage.getChefState() > ChefStates.CLOSING_SERVICE)
				throw new MessageException("Invalid Chef state!", inMessage);
			break;

		// Waiter Messages that require only type verification
		case MessageType.REQLA: // Look around Request
		case MessageType.SHUT: // Bar shutdown
			break;
		// Waiter Messages that require type and state verification
		case MessageType.REQPB: // Prepare the bill Request
		case MessageType.REQSG: // Say goodbye Request
			if (inMessage.getWaiterState() < WaiterStates.APPRAISING_SITUATION
					|| inMessage.getWaiterState() > WaiterStates.RECEIVING_PAYMENT)
				throw new MessageException("Inavlid Waiter state!", inMessage);
			break;

		// Student Messages that require only type and id verification (already done in
		// Message Constructor)
		case MessageType.REQCW: // Call the waiter Request
			break;
		// Student Messages that require type, state and id verification (done in
		// Message Constructor)
		case MessageType.REQENT: // Enter Request
		case MessageType.REQSW: // Signal the waiter Request
		case MessageType.REQEXIT: // exit Request
			if (inMessage.getStudentState() < StudentStates.GOING_TO_THE_RESTAURANT
					|| inMessage.getStudentState() > StudentStates.GOING_HOME)
				throw new MessageException("Invalid Student state!", inMessage);
			break;

		// Additional Messages
		case MessageType.REQGSBA:
			break;
		default:
			throw new MessageException("Invalid message type!", inMessage);
		}

		switch (inMessage.getMsgType()) {
		case MessageType.REQENT:
			((BarClientProxy) Thread.currentThread()).setStudentID(inMessage.getStudentID());
			((BarClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
			bar.enter();
			outMessage = new Message(MessageType.ENTDONE, ((BarClientProxy) Thread.currentThread()).getStudentID(),
					((BarClientProxy) Thread.currentThread()).getStudentState());
			break;

		case MessageType.REQCW:
			((BarClientProxy) Thread.currentThread()).setStudentID(inMessage.getStudentID());
			bar.callWaiter();
			outMessage = new Message(MessageType.CWDONE, ((BarClientProxy) Thread.currentThread()).getStudentID());
			break;

		case MessageType.REQSW:
			((BarClientProxy) Thread.currentThread()).setStudentID(inMessage.getStudentID());
			((BarClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
			bar.signalWaiter();
			outMessage = new Message(MessageType.SWDONE, ((BarClientProxy) Thread.currentThread()).getStudentID(),
					((BarClientProxy) Thread.currentThread()).getStudentState());
			break;

		case MessageType.REQEXIT:
			((BarClientProxy) Thread.currentThread()).setStudentID(inMessage.getStudentID());
			((BarClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
			bar.exit();
			outMessage = new Message(MessageType.EXITDONE, ((BarClientProxy) Thread.currentThread()).getStudentID(),
					((BarClientProxy) Thread.currentThread()).getStudentState());
			break;

		case MessageType.REQLA:
			char c = bar.lookAround();
			outMessage = new Message(MessageType.LADONE, c);
			break;

		case MessageType.REQSG:
			((BarClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
			boolean b = bar.sayGoodbye();
			outMessage = new Message(MessageType.SGDONE, b);
			break;

		case MessageType.REQPB:
			((BarClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
			bar.preprareBill();
			outMessage = new Message(MessageType.PBDONE, ((BarClientProxy) Thread.currentThread()).getWaiterState());
			break;

		case MessageType.REQAL:
			((BarClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
			bar.alertWaiter();
			outMessage = new Message(MessageType.ALDONE, ((BarClientProxy) Thread.currentThread()).getChefState());
			break;

		case MessageType.REQGSBA:
			int id = bar.getStudentBeingAnswered();
			outMessage = new Message(MessageType.GSBADONE, id);
			break;

		case MessageType.SHUT:
			bar.shutdown();
			outMessage = new Message(MessageType.SHUTDONE);
			break;
		}

		return (outMessage);
	}
}
