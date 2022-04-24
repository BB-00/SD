package main;

import entities.*;
import sharedRegions.*;


public class restaurantSImulation {

    public static void main(String[] args){

        Waiter waiter;
        Chef chef;
        Student[] student = new Student[7];
        Bar bar;
        Kitchen kitchen;
        Table table;
        GenRepos repos;

        // Initialization
        bar = new Bar(repos);
        kitchen = new Kitchen(repos);
        table = new Table(repos);

        waiter = new Waiter("Waiter", kitchen, bar, table);
        chef = new Chef("Chef", kitchen, bar);
        for(int i=0; i<7; i++){
            student[i] = new Student("Student_"+i, i, bar, table);
        }

        // Start of simulation
        waiter.run();
        chef.run();
        for(int i=0; i<7; i++){
            student[i].run();
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

        for (int i=0; i<7; i++){
            try {
                waiter.join();
            } catch (InterruptedException e) {
                System.out.print("Student with id "+ i + " has left the restaurant");
            }
        }

    }
    
}
