package com.algorithm;

import java.util.function.IntPredicate;

public class Result {
	public Result(int[] bestRoute, float bestLength) {
		super();
		this.bestRoute = bestRoute;
		this.bestLength = bestLength;
	}

	int[] bestRoute;
	float bestLength;

	public int[] getBestRoute() {
		return bestRoute;
	}

	public void setBestRoute(int[] bestRoute) {
		this.bestRoute = bestRoute;
	}

	public float getBestLength() {
		return bestLength;
	}

	public void setBestLength(float bestLength) {
		this.bestLength = bestLength;
	}
}
