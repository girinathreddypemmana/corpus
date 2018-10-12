package com.sam.main.app;

import java.io.File;

import com.sam.algorithm.AStarAlgorithm;

public class MainApplication {
	public static void main(String[] args) {
		String workingDir = System.getProperty("user.dir");
		AStarAlgorithm.getInstance().searchSmallCostPath(new File(workingDir+"\\input.txt"),new File(workingDir+"\\output.txt"));
	}
}
