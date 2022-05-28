package clientSide.stubs;

import clientSide.entities.*;
import genclass.GenericIO;
import commInfra.*;

public class BarStub {
	
	/**
	 * Name of the platform where is located the bar Server
	 */
	private String serverHostName;
	
	/**
	 * Port number for listening to service requests
	 */
	private int serverPortNum;
	
	
	/**
	 * Instantiation of a stub to the Bar
	 * @param serverHostName name of the platform where is located the bar Server
	 * @param serverPortNum port number for listening to service requests
	 */
	public BarStub(String serverHostName, int serverPortNum) {
		this.serverHostName = serverHostName;
		this.serverPortNum  = serverPortNum;
	}
	
	
	public void enter() {
		ClientCom com;                                                 // communication channel
	    Message outMessage,                                            // outgoing message
	            inMessage;                                             // incoming message

	    com = new ClientCom (serverHostName, serverPortNum);
	    while (!com.open()) {
	    	try {
	    		Thread.currentThread().sleep((long)(10));
	    	} catch(InterruptedException e) {}
	    }
	    
	    System.out.println("Connected");
	    
	    //MESSAGES
	    outMessage = new Message(MessageType.REQENT, ((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
	    	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    GenericIO.writelnString(inMessage.toString());
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.ENTDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getStudentID() != ((Student) Thread.currentThread()).getStudentID()) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student ID!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if((inMessage.getStudentState() != StudentStates.TAKING_A_SEAT_AT_THE_TABLE)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student State!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
	    com.close();
	}
	
	public void callWaiter() {
		ClientCom com;                                                 // communication channel
	    Message outMessage,                                            // outgoing message
	            inMessage;                                             // incoming message

	    com = new ClientCom (serverHostName, serverPortNum);
	    while (!com.open()) {
	    	try {
	    		Thread.currentThread().sleep((long)(10));
	    	} catch(InterruptedException e) {}
	    }
	    
	    //MESSAGES
	    outMessage = new Message(MessageType.REQCW, ((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.CWDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getStudentID() != ((Student) Thread.currentThread()).getStudentID()) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student ID!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
	    com.close();
	}
	
	public void signalWaiter() {
		ClientCom com;                                                 // communication channel
	    Message outMessage,                                            // outgoing message
	            inMessage;                                             // incoming message

	    com = new ClientCom (serverHostName, serverPortNum);
	    while (!com.open()) {
	    	try {
	    		Thread.currentThread().sleep((long)(10));
	    	} catch(InterruptedException e) {}
	    }
	    
	    //MESSAGES
	    outMessage = new Message(MessageType.REQSW, ((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.SWDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getStudentID() != ((Student) Thread.currentThread()).getStudentID()) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student ID!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
	    com.close();
	}
	
	public void exit() {
		ClientCom com;                                                 // communication channel
	    Message outMessage,                                            // outgoing message
	            inMessage;                                             // incoming message

	    com = new ClientCom (serverHostName, serverPortNum);
	    while (!com.open()) {
	    	try {
	    		Thread.currentThread().sleep((long)(10));
	    	} catch(InterruptedException e) {}
	    }
	    
	    //MESSAGES
	    outMessage = new Message(MessageType.REQEXIT, ((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.EXITDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getStudentID() != ((Student) Thread.currentThread()).getStudentID()) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student ID!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if((inMessage.getStudentState() != StudentStates.GOING_HOME)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student State!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
	    com.close();
	}
	
	
	
	
	public int getStudentBeingAnswered() {
		ClientCom com;                                                 // communication channel
	    Message outMessage,                                            // outgoing message
	            inMessage;                                             // incoming message

	    com = new ClientCom (serverHostName, serverPortNum);
	    while (!com.open()) {
	    	try {
	    		Thread.currentThread().sleep((long)(10));
	    	} catch(InterruptedException e) {}
	    }
	    
	    //MESSAGES
	    outMessage = new Message(MessageType.REQGSBA, ((Waiter) Thread.currentThread()).getWaiterState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.GSBADONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Waiter) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
	    com.close();
	    
	    return inMessage.getStudentID();
	}
	
	
	public char lookAround() {
		ClientCom com;                                                 // communication channel
	    Message outMessage,                                            // outgoing message
	            inMessage;                                             // incoming message

	    com = new ClientCom (serverHostName, serverPortNum);
	    while (!com.open()) {
	    	try {
	    		Thread.currentThread().sleep((long)(10));
	    	} catch(InterruptedException e) {}
	    }
	    
	    //MESSAGES
	    GenericIO.writelnString("WAITER LOOKAROUND");
	    
	    outMessage = new Message(MessageType.REQLA, ((Waiter) Thread.currentThread()).getWaiterState());
	    
	    GenericIO.writelnString("outMessage Waiter State: "+outMessage.getWaiterState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
//	    GenericIO.writelnString("WAITER STATE: "+((Waiter) Thread.currentThread()).getWaiterState());
//	    GenericIO.writelnString("inMessage WAITER STATE: "+ inMessage.getWaiterState());
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.LADONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    //if((inMessage.getWaiterState() < WaiterStates.APPRAISING_SITUATION && inMessage.getWaiterState() > WaiterStates.RECEIVING_PAYMENT)) {
	    if(inMessage.getWaiterState() != WaiterStates.APPRAISING_SITUATION) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Waiter State!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Waiter) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
	    com.close();
	    
	    return inMessage.getChar();
	}
	
	public boolean sayGoodbye() {
		ClientCom com;                                                 // communication channel
	    Message outMessage,                                            // outgoing message
	            inMessage;                                             // incoming message

	    com = new ClientCom (serverHostName, serverPortNum);
	    while (!com.open()) {
	    	try {
	    		Thread.currentThread().sleep((long)(10));
	    	} catch(InterruptedException e) {}
	    }
	    
	    //MESSAGES
	    outMessage = new Message(MessageType.REQSG, ((Waiter) Thread.currentThread()).getWaiterState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.SGDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Waiter) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
	    com.close();
	    
	    return inMessage.getCheck();
	}
	
	public void preprareBill() {
		ClientCom com;                                                 // communication channel
	    Message outMessage,                                            // outgoing message
	            inMessage;                                             // incoming message

	    com = new ClientCom (serverHostName, serverPortNum);
	    while (!com.open()) {
	    	try {
	    		Thread.currentThread().sleep((long)(10));
	    	} catch(InterruptedException e) {}
	    }
	    
	    //MESSAGES
	    outMessage = new Message(MessageType.REQPB, ((Waiter) Thread.currentThread()).getWaiterState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.PBDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if((inMessage.getWaiterState() != WaiterStates.PROCESSING_THE_BILL)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Waiter State!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Waiter) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
	    com.close();
	}
	
	
	
	
	
	
	
	
	public void alertWaiter() {
		ClientCom com;                                                 // communication channel
	    Message outMessage,                                            // outgoing message
	            inMessage;                                             // incoming message

	    com = new ClientCom (serverHostName, serverPortNum);
	    while (!com.open()) {
	    	try {
	    		Thread.currentThread().sleep((long)(10));
	    	} catch(InterruptedException e) {}
	    }
	    
	    //MESSAGES
	    outMessage = new Message(MessageType.REQAL, ((Chef) Thread.currentThread()).getChefState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.ALDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if((inMessage.getChefState() != ChefStates.DELIVERING_THE_PORTIONS)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Chef State!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Chef) Thread.currentThread()).setChefState(inMessage.getChefState());
	    com.close();
	}
	
	
	
	/**
	 *   Operation server shutdown.
	 *
	 *   New operation.
	 */
	public void shutdown(){
		ClientCom com;                                                 // communication channel
		Message outMessage,                                            // outgoing message
				inMessage;                                             // incoming message
	
		com = new ClientCom(serverHostName, serverPortNum);
		while (!com.open ()) {
			try {
				Thread.sleep((long) (1000));
	        }
	        catch (InterruptedException e) {}
		}
		
		//MESSAGES
	    outMessage = new Message(MessageType.SHUT);
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    if (inMessage.getMsgType() != MessageType.SHUTDONE) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid message type!");
	        GenericIO.writelnString(inMessage.toString());
	        System.exit(1);
	    }
	    com.close();
	}
}
