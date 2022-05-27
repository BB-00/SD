package clientSide.stubs;

import clientSide.entities.Student;
import clientSide.entities.StudentStates;
import clientSide.entities.Waiter;
import clientSide.entities.WaiterStates;
import genclass.GenericIO;
import commInfra.*;

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
	    if((inMessage.getMsgType() != MessageType.SCDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getWaiterState() != WaiterStates.PRESENTING_THE_MENU) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Waiter State!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getStudentID() != studentIDBeingAnswered) {
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
	    
	    if(inMessage.getWaiterState() != WaiterStates.APPRAISING_SITUATION) {
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
	    outMessage = new Message(MessageType.REQGB, ((Waiter) Thread.currentThread()).getWaiterState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.GBDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getWaiterState() != WaiterStates.TAKING_THE_ORDER) {
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
	    outMessage = new Message(MessageType.REQRB, ((Waiter) Thread.currentThread()).getWaiterState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.RBDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Waiter) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
	    com.close();
	    
	    return inMessage.getCheck();
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
	    outMessage = new Message(MessageType.REQDP, ((Waiter) Thread.currentThread()).getWaiterState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.DPDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Waiter) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
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
	    
	    if(inMessage.getWaiterState() != WaiterStates.RECEIVING_PAYMENT) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Waiter State!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Waiter) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
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
	    outMessage = new Message(MessageType.REQGFTA, ((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.GFTADONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
	    com.close();
	    
	    return inMessage.getStudentID();
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
	    outMessage = new Message(MessageType.REQGLTE, ((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.GLTEDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
	    com.close();
	    
	    return inMessage.getStudentID();
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
	    outMessage = new Message(MessageType.REQSAT, ((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.SATDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getStudentID() != ((Student) Thread.currentThread()).getStudentID()) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student ID!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getStudentState() != StudentStates.TAKING_A_SEAT_AT_THE_TABLE) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student State!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
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
	    
	    if(inMessage.getStudentState() != StudentStates.SELECTING_THE_COURSES) {
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
	    outMessage = new Message(MessageType.REQPO, ((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
	    
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
	    
	    if(inMessage.getStudentState() != StudentStates.ORGANIZING_THE_ORDER) {
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
	    outMessage = new Message(MessageType.REQEHC, ((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.EHCDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
	    com.close();
	    
	    return inMessage.getCheck();
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
	    outMessage = new Message(MessageType.REQAUOC, ((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.AUOCDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
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
	    outMessage = new Message(MessageType.REQDO, ((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.DODONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
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
	    outMessage = new Message(MessageType.REQJT, ((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.JTDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    if(inMessage.getStudentState() != StudentStates.CHATTING_WITH_COMPANIONS) {
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
	    
	    if(inMessage.getStudentState() != StudentStates.CHATTING_WITH_COMPANIONS) {
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
	    
	    if(inMessage.getStudentState() != StudentStates.ENJOYING_THE_MEAL) {
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
	    
	    if(inMessage.getStudentState() != StudentStates.CHATTING_WITH_COMPANIONS) {
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
	    outMessage = new Message(MessageType.REQHEFE, ((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
	    
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
	    
	    ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
	    com.close();
	    
	    return inMessage.getCheck();
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
	    outMessage = new Message(MessageType.REQHB, ((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.HBDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
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
	    outMessage = new Message(MessageType.REQHACBE, ((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    //TODO Message Types - enter
	    if((inMessage.getMsgType() != MessageType.HACBEDONE)) { // && (inMessage.getMsgType() != MessageType.FALTA_DAR_NOME_A_ESTA_MERDA)) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
	    com.close();
	    
	    return inMessage.getCheck();
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
	    
	    if(inMessage.getStudentState() != StudentStates.PAYING_THE_MEAL) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student State!");
	    	GenericIO.writelnString(inMessage.toString());
	    	System.exit(1);
	    }
	    
	    ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
	    com.close();
	    
	    return inMessage.getCheck();
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
