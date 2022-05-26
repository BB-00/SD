package serverSide.sharedRegions;

import serverSide.entities.*;
import commInfra.*;


/**
 *  Interface to the Bar.
 *
 *    It is responsible to validate and process the incoming message, execute the corresponding method on the
 *    Bar and generate the outgoing message.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class BarInterface {
  /**
   *  Reference to the bar.
   */

   private final Bar bar;

  /**
   *  Instantiation of an interface to the bar.
   *
   *    @param bar reference to the bar
   */

   public BarInterface(Bar bar) {
      this.bar = bar;
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
   
   
   public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null; // mensagem de resposta

        /* validation of the incoming message */

        /* processing */

//        switch (inMessage.getMsgType()) {
//            case MessageType.LA:
//                if ((inMessage.getWaiterState() < 0) || (inMessage.getWaiterState() > 6))
//                    throw new MessageException("Invalid waiter state!", inMessage);
//                break;
//            case MessageType.ENT:
//                if ((inMessage.getStudentState() < 0) || (inMessage.getStudentState() > 7))
//                    throw new MessageException("Invalid student state!", inMessage);
//                if ((inMessage.getStudentID() < 0) || (inMessage.getStudentID() > 6))
//                    throw new MessageException("Invalid student ID!", inMessage);
//                break;
//            case MessageType.CW:
//                if ((inMessage.getStudentState() < 0) || (inMessage.getStudentState() > 7))
//                    throw new MessageException("Invalid student state!", inMessage);
//                if ((inMessage.getStudentID() < 0) || (inMessage.getStudentID() > 6))
//                    throw new MessageException("Invalid student ID!", inMessage);
//                break;
//            case MessageType.AL:
//                if ((inMessage.getChefState() < 0) || (inMessage.getChefState() > 4))
//                    throw new MessageException("Invalid chef state!", inMessage);
//                break;
//            case MessageType.SW:
//                if ((inMessage.getStudentState() < 0) || (inMessage.getStudentState() > 7))
//                    throw new MessageException("Invalid student state!", inMessage);
//                if ((inMessage.getStudentID() < 0) || (inMessage.getStudentID() > 6))
//                    throw new MessageException("Invalid student ID!", inMessage);
//                break;
//            case MessageType.SHOULD_HAVE_ARRIVED_EARLIER:
//                if ((inMessage.getStudentState() < 0) || (inMessage.getStudentState() > 7))
//                    throw new MessageException("Invalid student state!", inMessage);
//                if ((inMessage.getStudentID() < 0) || (inMessage.getStudentID() > 6))
//                    throw new MessageException("Invalid student ID!", inMessage);
//                break;
//            case MessageType.PB:
//                if ((inMessage.getWaiterState() < 0) || (inMessage.getWaiterState() > 6))
//                    throw new MessageException("Invalid waiter state!", inMessage);
//                break;
//            case MessageType.SG:
//                if ((inMessage.getWaiterState() < 0) || (inMessage.getWaiterState() > 6))
//                    throw new MessageException("Invalid waiter state!", inMessage);
//                break;
//            case MessageType.EXIT:
//                if ((inMessage.getStudentState() < 0) || (inMessage.getStudentState() > 7))
//                    throw new MessageException("Invalid student state!", inMessage);
//                if ((inMessage.getStudentID() < 0) || (inMessage.getStudentID() > 6))
//                    throw new MessageException("Invalid student ID!", inMessage);
//                break;
//            case MessageType.SHUT:
//                break;
//            default:
//                throw new MessageException("Invalid message type!", inMessage);
//        }

        // check nothing

        // processing 

        switch(inMessage.getMsgType()) {
        	case MessageType.REQENT:  
        		((BarClientProxy) Thread.currentThread()).setStudentID(inMessage.getStudentID());
                ((BarClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                if(bar.enter()) {
                	outMessage = new Message(MessageType.ENTDONE,
                            ((BarClientProxy) Thread.currentThread()).getStudentID(),
                            ((BarClientProxy) Thread.currentThread()).getStudentState());
                }
                //   else outMessage = new Message (MessageType.BSHOPF,
                //                                  ((BarClientProxy) Thread.currentThread ()).getCustomerId (),
                //                                  ((BarClientProxy) Thread.currentThread ()).getCustomerState ());
               break;
            case MessageType.REQCW:
            	((BarClientProxy) Thread.currentThread()).setStudentID(inMessage.getStudentID());
                ((BarClientProxy) Thread.currentThread()).setStudentID(inMessage.getStudentState());
                if(bar.callWaiter()) {
                	outMessage = new Message(MessageType.CWDONE,
                			((BarClientProxy) Thread.currentThread()).getStudentID(),
                            ((BarClientProxy) Thread.currentThread()).setStudentState());
                }
                //nao sei se falta alguma coisa
                break;                                                    

            case MessageType.REQEXIT:
            	((BarClientProxy) Thread.currentThread()).setStudentID(inMessage.getStudentID());
                ((BarClientProxy) Thread.currentThread()).setStudentID(inMessage.getStudentState());
                if(bar.exit()) {
                	outMessage = new Message(MessageType.EXITDONE,
                            ((BarClientProxy) Thread.currentThread()).getStudentID(),
                            ((BarClientProxy) Thread.currentThread()).setStudentState());
                }
                //nao sei se falta alguma coisa
                break;

            case MessageType.REQLA:
            	((BarClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                if(bar.lookAround()) {
                	outMessage = new Message(MessageType.LADONE,
                            ((BarClientProxy)Thread.currentThread()).setWaiterState());
                }
                //nao sei se falta alguma coisa
                break;

            case MessageType.REQSG:
            	((BarClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                if(bar.sayGoodbye()) {
                	outMessage = new Message(MessageType.SGDONE,
                            ((BarClientProxy) Thread.currentThread()).setWaiterState());
                }
                //nao sei se falta alguma coisa
                break;
                
            case MessageType.REQPB:
            	((BarClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                if(bar.preprareBill()) {
                	outMessage = new Message(MessageType.PBDONE,
                            ((BarClientProxy) Thread.currentThread()).setWaiterState());
                }
                //nao sei se falta alguma coisa
                break;
            
            case MessageType.REQAL:
            	((BarClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
                if(bar.alertWaiter()) {
                	outMessage = new Message(MessageType.ALDONE,
                            ((BarClientProxy) Thread.currentThread()).setChefState());
                }
                //nao sei se falta alguma coisa
                break;

            case MessageType.SHUT:
            	bar.shutdown();
                outMessage = new Message(MessageType.SHUTDONE);
                break;
        }

        return (outMessage);
    }
}
