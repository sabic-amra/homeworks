package ba.bitcamp.homework26.task02;

import java.util.Random;

/**
 * TASK #2 � THREE THREADS OF ONE CLASS Napraviti tri Thread-a. Prvi isprinta
 * brojeve od 1 do 10, sa pauzom od 200 [ms] izmedu svakog broja. Drugi isprinta
 * cetiri puta �BitCamp� sa pauzom od 1000 [ms], a treci isprinta pet Random
 * brojeva u rasponu od 1 do 5 sa pauzom od 700 [ms] izmedu svakog.
 * 
 * @author Amra
 *
 */
public class OneClassThreads implements Runnable {

	private String name;

	// constructor accepts only one string
	public OneClassThreads(String word) {
		this.name = word;
	}

	public static void main(String[] args) {

		OneClassThreads r1 = new OneClassThreads("BitCamp");
		OneClassThreads r2 = new OneClassThreads("Counter");
		OneClassThreads r3 = new OneClassThreads("Random");

		Thread t1 = new Thread(r1);
		Thread t2 = new Thread(r2);
		Thread t3 = new Thread(r3);

		try {
			System.out.println("First thread: ");
			t1.start();
			t1.join();
			if(!t1.isAlive()) {
				System.out.println("Second thread: ");
				t2.start();
				t2.join();
			} 
			if(!t2.isAlive()) {
				System.out.println("Third thread: ");
				t3.start();
				t3.join();
			} 
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void run() {

		int counter = 0;
		if (this.name.equals("Counter")) {
			while (counter < 10) {
				System.out.println(counter++);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if (this.name.equals("BitCamp")) {
			counter = 0;
			while (counter < 4) {
				System.out.println("BitCamp");
				counter++;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if (this.name.equals("Random")) {
			counter = 0;
			Random r = new Random();
			while (counter < 5) {
				int rand = r.nextInt(5 + 1);
				System.out.println(rand);
				counter++;
				try {
					Thread.sleep(700);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
