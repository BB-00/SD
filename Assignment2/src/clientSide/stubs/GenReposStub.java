package clientSide.stubs;

import clientSide.entities.Student;
import commInfra.*;
import genclass.GenericIO;

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
	 * @param serverHostName name of the platform where is located the table Server
	 * @param serverPortNum port number for listening to service requests
	 */
	public GenReposStub(String serverHostName, int serverPortNum) {
		this.serverHostName = serverHostName;
		this.serverPortNum  = serverPortNum;
	}
	
	public void initSimulation(String fileName, int nIter) {
		ClientCom com;                                                 // communication channel
	    Message outMessage,                                            // outgoing message
	            inMessage;                                             // incoming message

	    com = new ClientCom(serverHostName, serverPortNum);
	    while(!com.open()){
	    	try{
	    		Thread.sleep((long) (1000));
	        }
	        catch (InterruptedException e) {}
	    }
	    outMessage = new Message(MessageType.SETNFIC, fileName, nIter);
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    if(inMessage.getMsgType() != MessageType.NFICDONE) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid message type!");
	        GenericIO.writelnString(inMessage.toString());
	        System.exit(1);
	    }
	    com.close ();
	}
	
	public void updateStudentState(int studentID, int studentState) {
		ClientCom com;                                                 // communication channel
	    Message outMessage,                                            // outgoing message
	            inMessage;                                             // incoming message

	    com = new ClientCom(serverHostName, serverPortNum);
	    while(!com.open()){
	    	try{
	    		Thread.sleep((long) (1000));
	        }
	        catch (InterruptedException e) {}
	    }
	    outMessage = new Message(MessageType.SETUSSTATE, studentID, studentState);
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    if(inMessage.getMsgType() != MessageType.SACK) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid message type!");
	        GenericIO.writelnString(inMessage.toString());
	        System.exit(1);
	    }
	    com.close ();
	}
	
	public void updateStudentSeat(int studentID, int studentSeat) {
		ClientCom com;                                                 // communication channel
	    Message outMessage,                                            // outgoing message
	            inMessage;                                             // incoming message

	    com = new ClientCom(serverHostName, serverPortNum);
	    while(!com.open()){
	    	try{
	    		Thread.sleep((long) (1000));
	        }
	        catch (InterruptedException e) {}
	    }
	    outMessage = new Message(MessageType.SETUSSEAT, studentID, studentSeat);
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    if(inMessage.getMsgType() != MessageType.SACK) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid message type!");
	        GenericIO.writelnString(inMessage.toString());
	        System.exit(1);
	    }
	    com.close ();
	}
	
//	public int getStudentSeat(int studentSeat) {
//		ClientCom com;                                                 // communication channel
//	    Message outMessage,                                            // outgoing message
//	            inMessage;                                             // incoming message
//
//	    com = new ClientCom(serverHostName, serverPortNum);
//	    while(!com.open()){
//	    	try{
//	    		Thread.sleep((long) (1000));
//	        }
//	        catch (InterruptedException e) {}
//	    }
//	    outMessage = new Message(MessageType.SETGSSEAT, studentSeat);
//	    
//	    com.writeObject(outMessage);
//	    inMessage = (Message) com.readObject();
//	    
//	    if(inMessage.getMsgType() != MessageType.GSSEATDONE) {
//	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid message type!");
//	        GenericIO.writelnString(inMessage.toString());
//	        System.exit(1);
//	    }
//	    com.close ();
//	    
//	    return inMessage.getStudent();
//	}
	
	public void updateWaiterState(int waiterState) {
		ClientCom com;                                                 // communication channel
	    Message outMessage,                                            // outgoing message
	            inMessage;                                             // incoming message

	    com = new ClientCom(serverHostName, serverPortNum);
	    while(!com.open()){
	    	try{
	    		Thread.sleep((long) (1000));
	        }
	        catch (InterruptedException e) {}
	    }
	    outMessage = new Message(MessageType.SETUWS, waiterState);
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    if(inMessage.getMsgType() != MessageType.SACK) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid message type!");
	        GenericIO.writelnString(inMessage.toString());
	        System.exit(1);
	    }
	    com.close ();
	}
	
	public void updateChefState(int chefState) {
		ClientCom com;                                                 // communication channel
	    Message outMessage,                                            // outgoing message
	            inMessage;                                             // incoming message

	    com = new ClientCom(serverHostName, serverPortNum);
	    while(!com.open()){
	    	try{
	    		Thread.sleep((long) (1000));
	        }
	        catch (InterruptedException e) {}
	    }
	    outMessage = new Message(MessageType.SETUCS, chefState);
	    
	    com.writeObject(outMessage);
	    inMessage = (Message) com.readObject();
	    
	    if(inMessage.getMsgType() != MessageType.SACK) {
	    	GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid message type!");
	        GenericIO.writelnString(inMessage.toString());
	        System.exit(1);
	    }
	    com.close ();
	}
	
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
