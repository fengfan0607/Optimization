package com.algorithm;

public class pheromone {
	float[][] pheromone;

	public pheromone(int cityNum) {
		// TODO Auto-generated constructor stub
		pheromone = new float[cityNum][cityNum];
		init();
	}

	public float readPheromone(int x, int y) {
		return pheromone[x][y];
	}

	public void adjustPhermone(int x, int y, float value) {
		pheromone[x][y] = value;
	}

	private void init() {
		for (int i = 0; i < pheromone.length; i++) {
			for (int j = 0; j < pheromone.length; j++) {
				pheromone[i][j] = 0.1f;
			}
		}
	}
}
