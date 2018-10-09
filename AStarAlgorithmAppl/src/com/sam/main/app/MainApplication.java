package com.sam.main.app;

import java.io.File;

import com.sam.algorithm.AStarAlgorithm;

public class MainApplication {
	public static void main(String[] args) {
		new AStarAlgorithm().searchSmallCostPath(new File("D:\\workspace\\practice\\AStarAlgorithm\\small_map.txt"),new File("D:\\workspace\\practice\\AStarAlgorithm\\small11_map.txt"));
	}
}
