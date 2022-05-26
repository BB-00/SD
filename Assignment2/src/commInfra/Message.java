package commInfra;

import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = 2022L;

    private int msgType = -1;

    private int chefState = -1;

    private int waiterState = -1;

    private int studentID = -1;

    private int studentState = -1;

    private int var1 = -1;

    private int var2 = -1;

    private Boolean check = null;

    private ServiceRequest request = null;

    public Message(int type) {
        this.msgType = type;
    }

    public Message(int type, int state) {
        this.msgType = type;
        if (type >= 40 && type <= 59)
            this.waiterState = state;
        else if (type >= 60 && type <= 75)
            this.chefState = state;
        else if (type > 86) {
            this.waiterState = state;
            this.chefState = state;
        }
    }

    public Message(int type, Boolean check, int state) {
        this.msgType = type;
        this.check = check;
        if (type >= 40 && type <= 59)
            this.waiterState = state;
        else if (type >= 60 && type <= 75)
            this.chefState = state;
        else if (type > 86) {
            this.waiterState = state;
            this.chefState = state;
        }

    }

    public Message(int type, ServiceRequest request, int state) {
        this.msgType = type;
        this.request = request;
        if (type >= 40 && type <= 59)
            this.waiterState = state;
    }

    public Message(int type, int studentID, int state) {
        this.msgType = type;
        this.studentID = studentID;
        if (type >= 0 && type <= 39)
            this.studentState = state;
        else if (type >= 40 && type <= 59)
            this.waiterState = state;
    }

    public Message(int type, Boolean check, int studentID, int state) {
        this.msgType = type;
        this.studentID = studentID;
        if (type >= 0 && type <= 39)
            this.studentState = state;
        else if (type >= 40 && type <= 59)
            this.waiterState = state;
        this.check = check;
    }

    public Message(int type, int var1, String fileName) {
        this.msgType = type;
        this.var1 = var1;
    }

    public Message(int type, int var1, int var2, String fileName) {
        this.msgType = type;
        this.var1 = var1;
        this.var2 = var2;
    }

    public int getMsgType() {
        return msgType;
    }

    public Boolean getCheck() {
        return check;
    }

    public ServiceRequest getRequest() {
        return request;
    }

    public int getStudentID() {
        return studentID;
    }

    public int getStudentState() {
        return studentState;
    }

    public int getWaiterState() {
        return waiterState;
    }

    public int getChefState() {
        return chefState;
    }

    public int getVar1() {
        return var1;
    }

    public int getVar2() {
        return var2;
    }

    @Override
    public String toString() {
        return ("Message type: " + msgType
                + "\nCheck: " + check
                + "\nRequest: " + request
                + "\nStudent ID: " + studentID
                + "\nStudent State: " + studentState
                + "\nWaiter State: " + waiterState
                + "\nChef State: " + chefState
                + "\nVar 1: " + var1
                + "\nVar 2: " + var2);
    }

}