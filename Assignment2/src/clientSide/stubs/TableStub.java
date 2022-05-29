package clientSide.stubs;

import clientSide.entities.*;
import genclass.GenericIO;
import commInfra.*;
import serverSide.entities.*;

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
	 * @param serverHostName name of the platform where is located the table Server
	 * @param serverPortNum port number for listening to service requests
	 */
	public TableStub(String serverHostName, int serverPortNum) {
		this.serverHostName = serverHostName;
		this.serverPortNum  = serverPortNum;
	}
	
	public void setFirstToArrive(int studentID) {
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
	    outMessage = new Message(MessageType.REQSFTA, studentID);
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.SFTADONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }

		com.close();
	}

	public int getFirstToArrive() {
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
	    outMessage = new Message(MessageType.REQGFTA);
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.GFTADONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }

		com.close();
		return inMessage.getFirstToArrive();
	}
	
	public void setLastToArrive(int studentID) {
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
	    outMessage = new Message(MessageType.REQSLTA, studentID);
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.SLTADONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
		com.close();
	}

	public int getLastToEat() {
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
	    outMessage = new Message(MessageType.REQGLTE);
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.GLTEDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }

		com.close();
		return inMessage.getLastToEat();
	}
	
	public void saluteClient(int studentIDBeingAnswered) {
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
	    outMessage = new Message(MessageType.REQSC, studentIDBeingAnswered, ((Waiter) Thread.currentThread()).getWaiterState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if(inMessage.getMsgType() != MessageType.SCDONE) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getWaiterState() < WaiterStates.APPRAISING_SITUATION || inMessage.getWaiterState() > WaiterStates.RECEIVING_PAYMENT) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Waiter State!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getStudentBeingAnswered() != -1) {
	    	GenericIO.writelnString("Thread Student"+inMessage.getStudentID()+": Invalid Student ID!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Waiter) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
	    com.close();
	}
	
	public void returnBar() {
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
	    outMessage = new Message(MessageType.REQRB, ((Waiter) Thread.currentThread()).getWaiterState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.RBDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getWaiterState() < WaiterStates.APPRAISING_SITUATION || inMessage.getWaiterState() > WaiterStates.RECEIVING_PAYMENT) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Waiter State!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Waiter) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
	    com.close();
	}
	
	public void getThePad() {
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
	    outMessage = new Message(MessageType.REQGP, ((Waiter) Thread.currentThread()).getWaiterState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.GPDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getWaiterState() < WaiterStates.APPRAISING_SITUATION || inMessage.getWaiterState() > WaiterStates.RECEIVING_PAYMENT) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Waiter State!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Waiter) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
	    com.close();
	}
	
	public boolean haveAllClientsBeenServed() {
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
	    outMessage = new Message(MessageType.REQHACBS);
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.HACBSDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    com.close();
	    
	    return inMessage.getAllClientsBeenServed();
	}
	
	public void deliverPortion() {
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
	    outMessage = new Message(MessageType.REQDP);
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.DPDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    com.close();
	}
	
	public void presentBill() {
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
	    
	    if(inMessage.getWaiterState() < WaiterStates.APPRAISING_SITUATION || inMessage.getWaiterState() > WaiterStates.RECEIVING_PAYMENT) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Waiter State!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Waiter) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
	    com.close();
	}
	
	
	
	public void seatAtTable() {
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
	    outMessage = new Message(MessageType.REQSAT, ((StudentCloning) Thread.currentThread()).getStudentID(),((StudentCloning) Thread.currentThread()).getStudentState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.SATDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getStudentID() != ((StudentCloning) Thread.currentThread()).getStudentID()) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student ID!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getStudentState() < StudentStates.GOING_TO_THE_RESTAURANT || inMessage.getStudentState() > StudentStates.GOING_HOME) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student State!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((StudentCloning) Thread.currentThread()).setStudentState(inMessage.getStudentState());
	    com.close();
	}
	
	public void readMenu() {
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
	    outMessage = new Message(MessageType.REQRM, ((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.RMDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getStudentID() != ((Student) Thread.currentThread()).getStudentID()) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student ID!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getStudentState() < StudentStates.GOING_TO_THE_RESTAURANT || inMessage.getStudentState() > StudentStates.GOING_HOME) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student State!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
	    com.close();
	}
	
	public void prepareOrder() {
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
	    outMessage = new Message(MessageType.REQPO, ((Student) Thread.currentThread()).getStudentState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.PODONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
//	    if(inMessage.getStudentID() != ((Student) Thread.currentThread()).getStudentID()) {
//	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student ID!");
//	    	GenericIO.writelnString(inMessage.toString());
//	    	System.exit(1);
//	    }
	    
	    if(inMessage.getStudentState() < StudentStates.GOING_TO_THE_RESTAURANT || inMessage.getStudentState() > StudentStates.GOING_HOME) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student State!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
	    com.close();
	}
	
	public boolean everybodyHasChosen() {
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
	    outMessage = new Message(MessageType.REQEHC);
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.EHCDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
		com.close();
	    
	    return inMessage.getEverybodyHasChosen();
	}
	
	public void addUpOnesChoices() {
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
	    outMessage = new Message(MessageType.REQAUOC);
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.AUOCDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    com.close();
	}
	
	public void describeOrder() {
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
	    outMessage = new Message(MessageType.REQDO);
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.DODONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    com.close();
	}
	
	public void joinTalk() {
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
	    outMessage = new Message(MessageType.REQJT, ((Student) Thread.currentThread()).getStudentState());
	    
	    GenericIO.writelnString("Thread Student State: "+((Student) Thread.currentThread()).getStudentState());
	    
	    GenericIO.writelnString("JOIN TALK MESSAGE:\n"+outMessage.toString());
	    GenericIO.writelnString();
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.JTDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getStudentState() < StudentStates.GOING_TO_THE_RESTAURANT || inMessage.getStudentState() > StudentStates.GOING_HOME) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student State!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
	    com.close();
	}
	
	public void informCompanion() {
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
	    outMessage = new Message(MessageType.REQIC, ((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.ICDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getStudentID() != ((Student) Thread.currentThread()).getStudentID()) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student ID!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getStudentState() < StudentStates.GOING_TO_THE_RESTAURANT || inMessage.getStudentState() > StudentStates.GOING_HOME) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student State!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
	    com.close();
	}
	
	public void startEating() {
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
	    outMessage = new Message(MessageType.REQSE, ((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.SEDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getStudentID() != ((Student) Thread.currentThread()).getStudentID()) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student ID!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getStudentState() < StudentStates.GOING_TO_THE_RESTAURANT || inMessage.getStudentState() > StudentStates.GOING_HOME) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student State!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
	    com.close();
	}
	
	public void endEating() {
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
	    outMessage = new Message(MessageType.REQEE, ((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.EEDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getStudentID() != ((Student) Thread.currentThread()).getStudentID()) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student ID!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getStudentState() < StudentStates.GOING_TO_THE_RESTAURANT || inMessage.getStudentState() > StudentStates.GOING_HOME) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student State!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
	    com.close();
	}
	
	public boolean hasEverybodyFinishedEating() {
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
	    outMessage = new Message(MessageType.REQHEFE, ((Student) Thread.currentThread()).getStudentID());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.HEFEDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getStudentID() != ((Student) Thread.currentThread()).getStudentID()) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student ID!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
		
		
	    com.close();
	    
	    return inMessage.getHasEverybodyFinishedEating();
	}
	
	public void honourBill() {
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
	    outMessage = new Message(MessageType.REQHB);
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.HBDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    //((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
	    com.close();
	}
	
	public boolean haveAllCoursesBeenEaten() {
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
	    outMessage = new Message(MessageType.REQHACBE);
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.HACBEDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    com.close();
	    
	    return inMessage.getAllCoursesEaten();
	}
	
	public boolean shouldHaveArrivedEarlier() {
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
	    outMessage = new Message(MessageType.REQSHAE, ((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.SHAEDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getStudentID() != ((Student) Thread.currentThread()).getStudentID()) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student ID!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getStudentState() < StudentStates.GOING_TO_THE_RESTAURANT || inMessage.getStudentState() > StudentStates.GOING_HOME) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student State!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
	    com.close();
	    
	    return inMessage.getArrivedEarlier();
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
