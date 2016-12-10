package multiThreading;

import java.util.concurrent.Callable;

/*
 * experiment with callable interface
 */
public class Agent implements Callable<Integer> {

	private String word;

	public Agent(String word) {
		this.word = word;
	}

	@Override
	public Integer call() throws Exception {
		// TODO Auto-generated method stub
		System.err.println();
		System.err.println(word);
		return Integer.valueOf(word.length());
	}

}
