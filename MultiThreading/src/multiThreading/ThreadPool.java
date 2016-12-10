package multiThreading;

import java.io.BufferedWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPool {

	public static void main(String[] args) {
		ExecutorService service = Executors.newFixedThreadPool(3);

		Set<Future<Integer>> set = new HashSet<>();
		String[] word = { "a", "b", "c" };
		for (String s : word) {
			Callable<Integer> agent = new Agent(s);
			Future future = service.submit(agent);
			set.add(future);
		}

		int sum = 0;
		for (Future<Integer> agent : set) {
			try {

				sum += agent.get();
				System.err.println("current sum is = " + sum + "; task exuceuted is done " + agent.isDone());
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.err.println("the sum of length is: " + sum);
		System.exit(sum);
	}

}
