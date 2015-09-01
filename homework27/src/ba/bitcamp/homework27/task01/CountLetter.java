package ba.bitcamp.homework27.task01;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Ucitati bilo koju tekst datoteku i odrediti koliko se prvo slovo prvog reda
 * ponavlja u cijelom File-u. Podijeliti posao na vise Thread-ova pomocu
 * Producer Consumer metodologije, tako da svaki Thread dobije jednu liniju
 * File-a. Program nije osjetljiv na velika i mala slova.
 * 
 * @author amra.sabic
 *
 */
public class CountLetter {
	// declaration of parameters
	private static final int WORKERS = 4;

	private static LinkedBlockingQueue<Task> queue;
	private static ArrayList<Worker> workers;
	private static Object lock = new Object();
	private static BufferedReader reader;

	private static int counter = 0;

	/**
	 * Main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		workers = new ArrayList<>();
		queue = new LinkedBlockingQueue<Task>();

		File f = null;
		String line = null;
		String first = null;
		String firstLowerCase = null;
		Task t = null;

		try {

			f = new File("C:/Users/Amra/Documents/workspace/homeworks/homework27/src/lalaland.txt");
			reader = new BufferedReader(new FileReader(new File(f.getAbsolutePath())));

			// initialize task, add task to queue
			int num = numberOfLines(f);

			for (int i = 0; i < num; i++) {

				if (i == 0) {
					line = reader.readLine();
					first = line.charAt(0) + "";
					firstLowerCase = first.toLowerCase();
					t = new Task(line, firstLowerCase);
					queue.add(t);

				} else {

					while (reader.ready()) {
						line = reader.readLine();
						t = new Task(line, firstLowerCase);
						queue.add(t);
					}
				}
			}

		} catch (NullPointerException | IOException e) {
			System.out.println("Something is wrong with file input..");
		}

		long time = System.currentTimeMillis();
		// initialize worker, start and add him to list of workers
		for (int i = 0; i < WORKERS; i++) {
			Worker w = new Worker();
			w.start();
			workers.add(w);

			try {
				w.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Time [ms]: " + (System.currentTimeMillis() - time));
		System.out.printf("Letter \"%s\" repeats in this file %d times.", firstLowerCase, counter);
	}

	/**
	 * Worker
	 * <p>
	 * Assign task to worker.
	 * 
	 * @author amra.sabic
	 *
	 */
	static class Worker extends Thread {
		@Override
		public void run() {
			while (!queue.isEmpty()) {
				try {
					Task t = queue.take();
					t.run();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Task
	 * <p>
	 * Count the same char as sent and adds that value at the very end to counter.
	 * 
	 * @author amra.sabic
	 *
	 */
	static class Task implements Runnable {

		private String line;
		private String first;

		// private static BufferedReader reader;

		public Task(String line, String first) {
			this.line = line;
			this.first = first;
		}

		@Override
		public void run() {

			try {
				synchronized (lock) {
					for (int i = 0; i < line.length(); i++) {
						String tmp = line.charAt(i) + "";
						if (first.equals(tmp.toLowerCase())) {
							counter++;
						}
					}
					System.out.println(counter);
				}

			} catch (StringIndexOutOfBoundsException e) {
				System.out.println("0");
			}

		}
	}

	/**
	 * Number of lines
	 * <p>
	 * For extra large files.. This method should be use to split work among
	 * workers.
	 * 
	 * @param f
	 * @return
	 */
	public static int numberOfLines(File f) {

		int counter = 0;
		String fileName = f.getAbsolutePath();

		List<String> lines;
		try {
			lines = Files.readAllLines(Paths.get(fileName), Charset.defaultCharset());
			for (String line : lines) {
				counter++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return counter;
	}
}