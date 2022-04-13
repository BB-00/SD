package commInfra;

public class FIFO {
    private char[] fifo= new char[50] ;
    private int idx; 

    public FIFO(){
        this.idx=0;
    }

    public void push(char c){
        this.fifo[idx] = c;
        this.idx++;
    }

    public boolean isEmpty(){
        return idx==0;
    }

    public char pop(){
        char ret = this.fifo[0];

        for (int i=1; i<idx; i++){
            this.fifo[i-1] = this.fifo[i];
        }

        
        idx--;
        this.fifo[idx] = '\0';
        
        return ret;

    }

    @Override
    public String toString(){
        String prnt = "";
        for (int i=0; i<idx; i++){
            prnt+=this.fifo[i];
        } 
        return prnt;
    }
}
