package clientSide.main;

import clientSide.entities.Student;
import clientSide.stubs.BarStub;
import clientSide.stubs.TableStub;
import clientSide.main.ExecConsts;

public class StudentMain {
		
	public static void main(String[] args) {
		Student[] students = new Student[ExecConsts.N];
		
		//Initialization
		BarStub bar;
		TableStub table;
		
		bar = new BarStub();
		table = new TableStub();
		
		for(int i=0; i<ExecConsts.N; i++){
            students[i] = new Student("Student_"+i, i, bar, table);
        }
		
		
		// Start of simulation
		for(int i=0; i<ExecConsts.N; i++){
            students[i].start();
        }
		
		for (int i=0; i<ExecConsts.N; i++){
        	try {
        		students[i].join();
        	} catch (InterruptedException e) {
        		System.out.print("Error occured while executing Student "+i);
        	}
        }
	}
}
