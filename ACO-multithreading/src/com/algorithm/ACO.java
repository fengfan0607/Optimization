package com.algorithm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ACO {

	private int cityNum;
	private int[][] distance;
	private ExecutorService threadPool = Executors.newFixedThreadPool(1);
	private final ExecutorCompletionService<Result> agentService = new ExecutorCompletionService<>(threadPool);
	private pheromone pheromone;
	private int agentNum;
	private metaParameters paras;
	private int Max_iterations;

	public ACO(int iterations, int agentNum, int cityNum, metaParameters paras) {
		// TODO Auto-generated constructor stub
		Max_iterations = iterations;
		this.agentNum = agentNum;
		this.pheromone = new pheromone(cityNum);
		this.paras = paras;
		this.cityNum = cityNum;
	}

	public void init(String fileName) throws IOException {
		int[] x;
		int[] y;
		String buffString;
		BufferedReader data = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

		this.distance = new int[cityNum][cityNum];
		x = new int[cityNum];
		y = new int[cityNum];
		for (int i = 0; i < cityNum; i++) {
			buffString = data.readLine();
			String[] strcol = buffString.split(" ");
			x[i] = Integer.valueOf(strcol[1]);// x坐标
			y[i] = Integer.valueOf(strcol[2]);// y坐标
		}
		// 计算距离矩阵
		// 针对具体问题，距离计算方法也不一样，此处用的是att48作为案例，它有48个城市，距离计算方法为伪欧氏距离，最优值为10628
		for (int i = 0; i < cityNum - 1; i++) {
			distance[i][i] = 0; // 对角线为0
			for (int j = i + 1; j < cityNum; j++) {
				double rij = Math.sqrt(((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j])) / 10.0);
				// 四舍五入，取整
				int tij = (int) Math.round(rij);
				if (tij < rij) {
					distance[i][j] = tij + 1;
					distance[j][i] = distance[i][j];
				} else {
					distance[i][j] = tij;
					distance[j][i] = distance[i][j];
				}
			}
		}
		this.distance[cityNum - 1][cityNum - 1] = 0;
	}

	public void solve() throws Exception {
		Result curr = new Result(new int[cityNum], Integer.MAX_VALUE);
		while (Max_iterations-- > 0) {
			for (int i = 0; i < agentNum; i++) {
				agentService.submit(new antAgent(this.pheromone, cityNum, distance, paras));
				Result result = agentService.take().get();
				if (result.getBestLength() <= curr.getBestLength()) {
					curr = result;
					System.err.println("Agent returned with new best distance of: " + result.bestLength);
					String s = "";
					int[] bestTour = result.getBestRoute();
					for (int j = 0; j < cityNum; j++) {
						s += bestTour[j] + "->";
					}
					s += bestTour[cityNum];
					System.err.println("The optimal tour is:" + s);
				}
			}
		}

		threadPool.shutdown();
	}

	public static void main(String[] args) throws Exception {
		System.err.println("Start");
		metaParameters paras = new metaParameters(1.f, 3.f, 0.5f);
		ACO aco = new ACO(100, 10, 48, paras);
		aco.init("data.txt");
		aco.solve();
	}
}
