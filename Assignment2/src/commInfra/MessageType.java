package commInfra;

/**
 * Type of the exchanged messages. the Implementation of a client-server model
 * of type 2 (server replication). Communication is based on a communication
 * channel under the TCP protocol.
 */
public class MessageType {
	/**
	 * Initialization of the logging file name and the number of iterations
	 * (REQUEST).
	 */
	public static final int SETNFIC = 1;

	/**
	 * Logging file was initialized (REPLY).
	 */
	public static final int NFICDONE = 2;

	////////////// STUDENT TYPES /////////////// 3 -> 42

	/**
	 * enter (REQUEST).
	 */
	public static final int REQENT = 3;

	/**
	 * enter (REPLY).
	 */
	public static final int ENTDONE = 4;

	/**
	 * callWaiter (REQUEST).
	 */
	public static final int REQCW = 5;

	/**
	 * callWaiter (REPLY).
	 */
	public static final int CWDONE = 6;

	/**
	 * signalWaiter (REQUEST).
	 */
	public static final int REQSW = 7;

	/**
	 * signalWaiter (REPLY).
	 */
	public static final int SWDONE = 8;

	/**
	 * exit (REQUEST).
	 */
	public static final int REQEXIT = 9;

	/**
	 * exit (REPLY)
	 */
	public static final int EXITDONE = 10;

	/**
	 * seatAtTable (REQUEST)
	 */
	public static final int REQSAT = 11;

	/**
	 * seatAtTable (REPLY)
	 */
	public static final int SATDONE = 12;

	/**
	 * readMenu (REQUEST)
	 */
	public static final int REQRM = 13;

	/**
	 * readMenu (REPLY)
	 */
	public static final int RMDONE = 14;

	/**
	 * getFirstToArrive (REQUEST)
	 */
	public static final int REQGFTA = 15;

	/**
	 * getFirstToArrive (REPLY)
	 */
	public static final int GFTADONE = 16;

	/**
	 * prepareOrder (REQUEST)
	 */
	public static final int REQPO = 17;

	/**
	 * prepareOrder (REPLY)
	 */
	public static final int PODONE = 18;

	/**
	 * everybodyHasChosen (REQUEST)
	 */
	public static final int REQEHC = 19;

	/**
	 * everybodyHasChosen (REPLY)
	 */
	public static final int EHCDONE = 20;

	/**
	 * addUpOnesChoices (REQUEST)
	 */
	public static final int REQAUOC = 21;

	/**
	 * addUpOnesChoices (REPLY)
	 */
	public static final int AUOCDONE = 22;

	/**
	 * describeOrder (REQUEST)
	 */
	public static final int REQDO = 23;

	/**
	 * describeOrder (REPLY)
	 */
	public static final int DODONE = 24;

	/**
	 * joinTalk (REQUEST)
	 */
	public static final int REQJT = 25;

	/**
	 * joinTalk (REPLY)
	 */
	public static final int JTDONE = 26;

	/**
	 * informCompanion (REQUEST)
	 */
	public static final int REQIC = 27;

	/**
	 * informCompanion (REPLY)
	 */
	public static final int ICDONE = 28;

	/**
	 * haveAllCoursesBeenEaten (REQUEST)
	 */
	public static final int REQHACBE = 29;

	/**
	 * haveAllCoursesBeenEaten (REPLY)
	 */
	public static final int HACBEDONE = 30;

	/**
	 * startEating (REQUEST)
	 */
	public static final int REQSE = 31;

	/**
	 * startEating (REPLY)
	 */
	public static final int SEDONE = 32;

	/**
	 * endEating (REQUEST)
	 */
	public static final int REQEE = 33;

	/**
	 * endEating (REPLY)
	 */
	public static final int EEDONE = 34;

	/**
	 * hasEverybodyFinishedEating (REQUEST)
	 */
	public static final int REQHEFE = 35;

	/**
	 * hasEverybodyFinishedEating (REPLY)
	 */
	public static final int HEFEDONE = 36;

	/**
	 * honourBill (REQUEST)
	 */
	public static final int REQHB = 37;

	/**
	 * honourBill (REPLY)
	 */
	public static final int HBDONE = 38;

	/**
	 * shouldHaveArrivedEarlier (REQUEST)
	 */
	public static final int REQSHAE = 39;

	/**
	 * shouldHaveArrivedEarlier (REPLY)
	 */
	public static final int SHAEDONE = 40;

	/**
	 * getLastToEat (REQUEST)
	 */
	public static final int REQGLTE = 41;

	/**
	 * getLastToEat (REPLY)
	 */
	public static final int GLTEDONE = 42;

	//////////// WAITER TYPES ///////////// 43 -> 66

	/**
	 * lookAround (REQUEST)
	 */
	public static final int REQLA = 43;

	/**
	 * lookAround (REPLY)
	 */
	public static final int LADONE = 44;

	/**
	 * sayGoodbye (REQUEST)
	 */
	public static final int REQSG = 45;

	/**
	 * sayGoodbye (REPLY)
	 */
	public static final int SGDONE = 46;

	/**
	 * prepareBill (REQUEST)
	 */
	public static final int REQPB = 47;

	/**
	 * prepareBill (REPLY)
	 */
	public static final int PBDONE = 48;

	/**
	 * saluteClient (REQUEST)
	 */

	public static final int REQSC = 49;

	/**
	 * saluteClient (REPLY)
	 */
	public static final int SCDONE = 50;

	/**
	 * getStudentBeingAnswered (REQUEST)
	 */
	public static final int REQGSBA = 51;

	/**
	 * getStudentBeingAnswered (REPLY)
	 */
	public static final int GSBADONE = 52;

	/**
	 * returnToBar (REQUEST)
	 */
	public static final int REQRTB = 53;

	/**
	 * returnToBar (REPLY)
	 */
	public static final int RTBDONE = 54;
	/**
	 * getThePad (REQUEST)
	 */
	public static final int REQGP = 55;

	/**
	 * getThePad (REPLY)
	 */
	public static final int GPDONE = 56;

	/**
	 * handNoteToChef (REQUEST)
	 */
	public static final int REQHNTC = 57;

	/**
	 * handNoteToChef (REPLY)
	 */
	public static final int HNTCDONE = 58;

	/**
	 * haveAllClientsBeenServed (REQUEST)
	 */
	public static final int REQHACBS = 59;

	/**
	 * haveAllClientsBeenServed (REPLY)
	 */
	public static final int HACBSDONE = 60;

	/**
	 * collectPortion (REQUEST)
	 */
	public static final int REQCPOR = 61;

	/**
	 * collectPortion (REPLY)
	 */
	public static final int CPORDONE = 62;

	/**
	 * deliverPortion (REQUEST)
	 */
	public static final int REQDP = 63;

	/**
	 * deliverPortion (REPLY)
	 */
	public static final int DPDONE = 64;

	/**
	 * presentBill (REQUEST)
	 */
	public static final int REQPREB = 65;

	/**
	 * presentBill (REPLY)
	 */
	public static final int PREBDONE = 66;

	//////////// CHEF TYPES ///////////// 67 -> 86

	/**
	 * watchTheNews (REQUEST)
	 */
	public static final int REQWTN = 67;

	/**
	 * watchTheNews (REPLY)
	 */
	public static final int WTNDONE = 68;

	/**
	 * startPreparation (REQUEST)
	 */
	public static final int REQSTP = 69;

	/**
	 * startPreparation (REPLY)
	 */
	public static final int STPDONE = 70;

	/**
	 * proceedPresentation (REQUEST)
	 */
	public static final int REQPTP = 71;

	/**
	 * proceedPresentation (REPLY)
	 */
	public static final int PTPDONE = 72;

	/**
	 * haveNextPortionReady (REQUEST)
	 */
	public static final int REQHNPR = 73;

	/**
	 * haveNextPortionReady (REPLY)
	 */
	public static final int HNPRDONE = 74;

	/**
	 * continuePreparation (REQUEST)
	 */
	public static final int REQCP = 75;

	/**
	 * continuePreparation (REPLY)
	 */
	public static final int CPDONE = 76;

	/**
	 * haveAllPortionsBeenDelivered (REQUEST)
	 */
	public static final int REQHAPBD = 77;

	/**
	 * haveAllPortionsBeenDelivered (REPLY)
	 */
	public static final int HAPBDDONE = 78;

	/**
	 * hasOrderBeenCompleted (REQUEST)
	 */
	public static final int REQHOBC = 79;

	/**
	 * hasOrderBeenCompleted (REPLY)
	 */
	public static final int HOBCDONE = 80;

	/**
	 * cleanUp (REQUEST)
	 */
	public static final int REQCU = 81;

	/**
	 * cleanUp (REPLY)
	 */
	public static final int CUDONE = 82;

	/**
	 * alertWaiter (REQUEST).
	 */
	public static final int REQAL = 83;

	/**
	 * alertWaiter (REPLY).
	 */
	public static final int ALDONE = 84;

	/**
	 * returnBar (REQUEST)
	 */
	public static final int REQRB = 85;

	/**
	 * returnBar (REPLY)
	 */
	public static final int RBDONE = 86;

	/**
	 * Server shutdown (REQUEST).
	 */
	public static final int SHUT = 87;

	/**
	 * Server was shutdown (REPLY).
	 */
	public static final int SHUTDONE = 88;

	/////////// GENERAL REPOS ////////////

	/**
	 * updateStudentState (REQUEST).
	 */
	public static final int SETUSSTATE = 89;

	/**
	 * updateWaiterState (REQUEST).
	 */
	public static final int SETUWS = 90;

	/**
	 * updateChefState (REQUEST).
	 */
	public static final int SETUCS = 91;

	/**
	 * updateStudentSeat (REQUEST)
	 */
	public static final int SETUSSEAT = 92;
	
	/**
	 * updateStudentSeatAndState (REQUEST)
	 */
	public static final int SETUSSEATANDSTATE = 93;
	
	/**
	 * updateSeatsAtLeaving (REQUEST)
	 */
	public static final int SETUSEATATLEAVING = 94;
	
	/**
	 * updateCourse (REQUEST)
	 */
	public static final int SETUCOURSE = 95;
	
	/**
	 * updatePortion (REQUEST)
	 */
	public static final int SETUPORTION = 96;
	
	/**
	 * updatePortionAndCourse (REQUEST)
	 */
	public static final int SETUPORTIONANDCOURSE = 97;
	
	/**
	 * Setting acknowledged (REPLY).
	 */
	public static final int SACK = 98;

	///////////// TABLE TYPES /////////////

	public static final int REQSFTA = 99;

	public static final int SFTADONE = 100;

	public static final int REQSLTA = 101;

	public static final int SLTADONE = 102;
}