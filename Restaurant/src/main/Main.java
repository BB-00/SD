package main;

import entities.*;
import sharedRegions.*;


public class Main {

    public static void main(String[] args){

        Waiter waiter;
        Chef chef;
        Student[] student = new Student[ExecConsts.N];
        Bar bar;
        Kitchen kitchen;
        Table table;
        GenRepos repos;
        
        // Initialization
        repos = new GenRepos("teste.txt");
        table = new Table(repos);
        bar = new Bar(repos, table);
        kitchen = new Kitchen(repos);

        chef = new Chef("Chef", kitchen, bar);
        waiter = new Waiter("Waiter", kitchen, bar, table);
        for(int i=0; i<ExecConsts.N; i++){
            student[i] = new Student("Student_"+i, i, bar, table);
        }

        // Start of simulation
        chef.start();
        waiter.start();
        for(int i=0; i<ExecConsts.N; i++){
            student[i].start();
        }

        for (int i=0; i<ExecConsts.N; i++){
        	try {
        		student[i].join();
        	} catch (InterruptedException e) {
        		System.out.print("Student with id "+ i + " has left the restaurant");
        	}
        }
        
        try {
            waiter.join();
        } catch (InterruptedException e) {
            System.out.print("Waiter has finished is shift");
        }

        try {
            chef.join();
        } catch (InterruptedException e) {
            System.out.print("Chef has finished is shift");
        }


    }
    
}