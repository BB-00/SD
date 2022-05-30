package serverSide.sharedRegions;

import clientSide.entities.ChefStates;
import clientSide.entities.StudentStates;
import clientSide.entities.WaiterStates;
import commInfra.*;
import genclass.GenericIO;

/**
 *  Interface to the General Repository of Information.
 *
 *    It is responsible to validate and process the incoming message, execute the corresponding method on the
 *    General Repository and generate the outgoing message.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class GenReposInterface {
  /**
   *  Reference to the general repository.
   */

   private final GenRepos repos;

  /**
   *  Instantiation of an interface to the general repository.
   *
   *    @param repos reference to the general repository
   */

   public GenReposInterface (GenRepos repos) {
      this.repos = repos;
   }

  /**
   *  Processing of the incoming messages.
   *
   *  Validation, execution of the corresponding method and generation of the outgoing message.
   *
   *    @param inMessage service request
   *    @return service reply
   *    @throws MessageException if the incoming message is not valid
   */
   
   
   public Message processAndReply (Message inMessage) throws MessageException {
      Message outMessage = null;                                     // mensagem de resposta

     // validation of the incoming message 

      switch(inMessage.getMsgType())
		{
		// verify Chef state
		case MessageType.SETUCS:
			if (inMessage.getChefState() < ChefStates.WAITING_FOR_AN_ORDER || inMessage.getChefState() > ChefStates.CLOSING_SERVICE)
				throw new MessageException ("Invalid Chef state!", inMessage);
			break;
		// verify Waiter state
		case MessageType.SETUWS:
			if (inMessage.getWaiterState() < WaiterStates.APPRAISING_SITUATION || inMessage.getWaiterState() > WaiterStates.RECEIVING_PAYMENT)
				throw new MessageException("Invalid Waiter state!", inMessage);
			break;
		// verify Student state
		case MessageType.SETUSSTATE:
		//case MessageType.REQUPDTSTUST2:
			if (inMessage.getStudentState() < StudentStates.GOING_TO_THE_RESTAURANT || inMessage.getStudentState() > StudentStates.GOING_HOME)
				throw new MessageException("Invalid Student state!", inMessage);
			break;
		// verify only message type
		case MessageType.SETUSSEAT:
		case MessageType.SHUT:
			break;
		default:
			throw new MessageException ("Invalid message type!", inMessage);
		}

     // processing

      switch (inMessage.getMsgType ()) { 
        case MessageType.SETUSSTATE:
        	repos.updateStudentState(inMessage.getStudentID(), inMessage.getStudentState());
            outMessage = new Message(MessageType.SACK);
            break;
            
        case MessageType.SETUSSEAT:
        	repos.updateStudentSeat(inMessage.getStudentID(), inMessage.getSeatAtTable());
            outMessage = new Message(MessageType.SACK);
            break;
            
        case MessageType.SETUCS:
        	repos.updateChefState(inMessage.getChefState());
            outMessage = new Message(MessageType.SACK);
            break;
        case MessageType.SETUWS:
        	repos.updateWaiterState(inMessage.getWaiterState());                              
            outMessage = new Message (MessageType.SACK);
            break;
            
        case MessageType.SHUT:
        	repos.shutdown();
            outMessage = new Message (MessageType.SHUTDONE);
            break;
      }

     return (outMessage);
   }
   
}