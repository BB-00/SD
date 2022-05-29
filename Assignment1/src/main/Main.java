package main;

import entities.*;
import sharedRegions.*;

public class Main {

	public static void main(String[] args) {

		Waiter waiter;
		Chef chef;
		Student[] student = new Student[ExecConsts.N];
		Bar bar;
		Kitchen kitchen;
		Table table;
		GenRepos repos;

		// Initialization
		repos = new GenRepos("log");
		table = new Table(repos);
		bar = new Bar(repos, table);
		kitchen = new Kitchen(repos);

		chef = new Chef("Chef", kitchen, bar);
		waiter = new Waiter("Waiter", kitchen, bar, table);
		for (int i = 0; i < ExecConsts.N; i++) {
			student[i] = new Student("Student_" + i, i, bar, table);
		}

		// Start of simulation
		chef.start();
		waiter.start();
		for (int i = 0; i < ExecConsts.N; i++) {
			student[i].start();
		}

		for (int i = 0; i < ExecConsts.N; i++) {
			try {
				student[i].join();
			} catch (InterruptedException e) {
				System.out.print("Error occured while executing Student " + i);
			}
		}

		try {
			waiter.join();
		} catch (InterruptedException e) {
			System.out.print("Error occured while executing Waiter");
		}

		try {
			chef.join();
		} catch (InterruptedException e) {
			System.out.print("Error occured while executing Chef");
		}

	}

}