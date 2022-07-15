package serverSide.sharedRegions;

import serverSide.entities.*;
import clientSide.entities.ChefStates;
import clientSide.entities.WaiterStates;
import commInfra.*;

/**
 * Interface to the Kitchen.
 *
 * It is responsible to validate and process the incoming message, execute the
 * corresponding method on the Kitchen and generate the outgoing message.
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 */
public class KitchenInterface {
	/**
	 * Reference to the kitchen.
	 */
	private final Kitchen kitchen;

	/**
	 * Instantiation of an interface to the kitchen.
	 *
	 * @param kitchen reference to the kitchen
	 */
	public KitchenInterface(Kitchen kitchen) {
		this.kitchen = kitchen;
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
		Message outMessage = null; // outgoing message

		// validation of the incoming message

		switch (inMessage.getMsgType()) {
		// Chef Messages that require type and state verification
		case MessageType.REQWTN: // Watching the news request
		case MessageType.REQSTP: // Start preparation of a course request
		case MessageType.REQPTP: // Proceed to presentation request
		case MessageType.REQHNPR: // Have next portion ready
		case MessageType.REQCP: // Continue preparation
		case MessageType.REQCU: // Clean up
			if ((inMessage.getChefState() < ChefStates.WAITING_FOR_AN_ORDER)
					|| (inMessage.getChefState() > ChefStates.CLOSING_SERVICE))
				throw new MessageException("Invalid Chef state!", inMessage);
			break;

		// Chef Messages that require only type verification
		case MessageType.REQHAPBD: // Have all portions been delivered
		case MessageType.REQHOBC: // Has the order been completed
		case MessageType.SHUT: // Kitchen shutdown
			break;

		// Waiter Messages that require type and state verification
		case MessageType.REQHNTC: // Hand note to chef
		case MessageType.REQRTB: // Return to bar
		case MessageType.REQCPOR: // Collect portion
			if (inMessage.getWaiterState() < WaiterStates.APPRAISING_SITUATION
					|| inMessage.getWaiterState() > WaiterStates.RECEIVING_PAYMENT)
				throw new MessageException("Invalid Waiter state!", inMessage);
			break;
		default:
			throw new MessageException("Invalid message type!", inMessage);
		}

		// processing

		switch (inMessage.getMsgType()) {
		case MessageType.REQWTN:
			((KitchenClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
			kitchen.watchTheNews();
			outMessage = new Message(MessageType.WTNDONE, ((KitchenClientProxy) Thread.currentThread()).getChefState());
			break;

		case MessageType.REQSTP:
			((KitchenClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
			kitchen.startPreparation();
			outMessage = new Message(MessageType.STPDONE, ((KitchenClientProxy) Thread.currentThread()).getChefState());
			break;

		case MessageType.REQPTP:
			((KitchenClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
			kitchen.proceedToPresentation();
			outMessage = new Message(MessageType.PTPDONE, ((KitchenClientProxy) Thread.currentThread()).getChefState());
			break;

		case MessageType.REQHNPR:
			((KitchenClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
			kitchen.haveNextPortionReady();
			outMessage = new Message(MessageType.HNPRDONE,
					((KitchenClientProxy) Thread.currentThread()).getChefState());
			break;

		case MessageType.REQCP:
			((KitchenClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
			kitchen.continuePreparation();
			outMessage = new Message(MessageType.CPDONE, ((KitchenClientProxy) Thread.currentThread()).getChefState());
			break;

		case MessageType.REQHAPBD:
			boolean portsDelivered = kitchen.haveAllPortionsBeenDelivered();
			outMessage = new Message(MessageType.HAPBDDONE, portsDelivered);
			break;

		case MessageType.REQHOBC:
			boolean orderCompleted = kitchen.hasOrderBeenCompleted();
			outMessage = new Message(MessageType.HOBCDONE, orderCompleted);
			break;

		case MessageType.REQCU:
			((KitchenClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
			kitchen.cleanUp();
			outMessage = new Message(MessageType.CUDONE, ((KitchenClientProxy) Thread.currentThread()).getChefState());
			break;

		case MessageType.REQRTB:
			((KitchenClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
			kitchen.returnToBar();
			outMessage = new Message(MessageType.RTBDONE,
					((KitchenClientProxy) Thread.currentThread()).getWaiterState());
			break;

		case MessageType.REQHNTC:
			((KitchenClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
			kitchen.handNoteToChef();
			outMessage = new Message(MessageType.HNTCDONE,
					((KitchenClientProxy) Thread.currentThread()).getWaiterState());
			break;

		case MessageType.REQCPOR:
			((KitchenClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
			kitchen.collectPortion();
			outMessage = new Message(MessageType.CPORDONE,
					((KitchenClientProxy) Thread.currentThread()).getWaiterState());
			break;

		case MessageType.SHUT:
			kitchen.shutdown();
			outMessage = new Message(MessageType.SHUTDONE);
			break;
		}

		return (outMessage);
	}

}
