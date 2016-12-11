package com.algorithm;

import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public class antAgent implements Callable<Result> {

	private static final ThreadLocalRandom Random_uniform = ThreadLocalRandom.current();
	private Vector<Integer> tabuList;
	private Vector<Integer> allowedCities;
	private float[][] delta;
	private int[][] distance;
	private float alpha;
	private float beta;
	private float rho;
	private pheromone pheromone;
	private int tourLength;
	private int[] tour;
	private int cityNum;

	private int firstCity;

	private int currentCity;

	public antAgent(pheromone p, int cityNum, int[][] distance, metaParameters paras) throws Exception {
		// TODO Auto-generated method stub
		this.cityNum = cityNum;
		this.firstCity = Random_uniform.nextInt(cityNum);
		this.currentCity = firstCity;

		this.pheromone = p;
		this.distance = distance;
		this.tourLength = 0;
		tour = new int[cityNum + 1];
		this.delta = new float[cityNum][cityNum];
		this.alpha = paras.getAlpha();
		this.beta = paras.getBeta();
		this.rho = paras.getRho();
		this.tabuList = new Vector<>();
		this.allowedCities = new Vector<>();
		this.delta = new float[cityNum][cityNum];
		tabuList.add(firstCity);
		for (int i = 0; i < cityNum; i++) {
			if (i != firstCity)
				allowedCities.add(i);
		}
		for (int i = 0; i < cityNum; i++) {
			for (int j = 0; j < cityNum; j++) {
				delta[i][j] = 0.0f;
			}
		}
	}

	public int calculateTourLength() {
		int len = 0;
		for (int i = 0; i < cityNum; i++) {
			len += distance[this.tabuList.get(i).intValue()][this.tabuList.get(i + 1).intValue()];
		}
		return len;
	}

	private void selectNextCity(int currentCity) {
		float[] p = new float[cityNum];
		float sum = 0.0f;
		// calculate numerator
		for (Integer i : allowedCities) {
			sum += Math.pow(pheromone.readPheromone(currentCity, i), alpha)
					* Math.pow(1.0 / distance[currentCity][i], beta);
		}
		for (int i = 0; i < cityNum; i++) {
			// find the city that has not been selected
			if (allowedCities.contains(i)) {
				p[i] = (float) (Math.pow(pheromone.readPheromone(currentCity, i), alpha)
						* Math.pow(1.0 / distance[currentCity][i], beta)) / sum;
			} else {
				p[i] = 0.f;
			}
		}

		float selectP = Random_uniform.nextFloat();

		int selectCity = 0;
		float sum1 = 0.f;
		for (int i = 0; i < cityNum; i++) {
			// sum1 += p[i];
			if (allowedCities.contains(i) && p[i] >= selectP) {
				selectCity = i;
				break;
			} else if (allowedCities.contains(i) && p[i] < selectP) {
				sum1 += p[i];
				if (sum1 >= selectP) {
					selectCity = i;
					break;
				}
			}else{
				continue;
			}
		}

		for (Integer i : allowedCities) {
			if (i.intValue() == selectCity) {
				this.allowedCities.remove(i);
				break;
			}
		}
		this.tabuList.add(Integer.valueOf(selectCity));
		this.currentCity = selectCity;
	}

	private void updataPheromone() {
		for (int i = 0; i < cityNum; i++) {
			this.delta[tabuList.get(i)][tabuList.get(i + 1)] = (float) (1. / tourLength);
			this.delta[tabuList.get(i + 1)][tabuList.get(i)] = (float) (1. / tourLength);
		}
		// pheromone evaporation
		for (int i = 0; i < cityNum; i++)
			for (int j = 0; j < cityNum; j++) {
				pheromone.adjustPhermone(i, j, pheromone.readPheromone(i, j) * (1 - rho));
			}
		// pheromone deposition
		for (int i = 0; i < cityNum; i++) {
			for (int j = 0; j < cityNum; j++) {
				pheromone.adjustPhermone(i, j, pheromone.readPheromone(i, j) + delta[i][j]);
			}
		}
	}

	@Override
	public Result call() throws Exception {
		// TODO Auto-generated method stub
		for (int i = 1; i < cityNum; i++) {
			selectNextCity(currentCity);
		}
		this.tabuList.add(this.firstCity);

		tourLength = calculateTourLength();

		for (int i = 0; i < cityNum + 1; i++) {
			tour[i] = tabuList.get(i);
		}
		this.updataPheromone();
		return new Result(tour, tourLength);
	}

}
