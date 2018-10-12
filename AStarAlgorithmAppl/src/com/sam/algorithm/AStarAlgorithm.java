package com.sam.algorithm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;	

public class AStarAlgorithm {
	private static List<Character> wakableElements = Arrays.asList('.','@','X','*','^');
	//private static char nonWakableElement = '~';
	private static Map<Character, Integer> elementCostMap = setElementCostMap();
	
	Scanner sc  = null;
	FileWriter fileWriter = null;
	List<String> fileDataLinesList = null;
	
	char[][] myArray = null;
	char[][] destArray = null;
	int x1 = 0;
	int x2 = 0;
	int y1 = 0;
	int y2 = 0;
	int rows=0;
	int cols=0;

	public void searchSmallCostPath(File largeMapFile, File targetFile) {
		try {
			//declare scanner pointing to source file to read content line by line
			sc = new Scanner(largeMapFile);
			
			fileWriter = new FileWriter(targetFile);
			//read all lines and keep them in list
			fileDataLinesList = new ArrayList<String>();
			int j=0;
			while(sc.hasNextLine()) {
				String lineData = sc.nextLine();
				fileDataLinesList.add(lineData.trim());
				//System.out.println("lineData.trim():"+lineData.trim().length());
				if (cols < lineData.trim().length()) {
					cols=lineData.trim().length();
				}
			}
			//System.out.println("fileDataLinesList:"+fileDataLinesList.toString());
			rows=fileDataLinesList.size();
			//System.out.println("rows:"+rows+",cols:"+cols);
			myArray = new char[rows][cols];
			destArray = new char[rows][cols];
			/*for (int i = 0; i < myArray.length; i++) {
				for (int k = 0; k < myArray.length; k++) {
					System.out.print("("+i+","+k+")");
				}
				System.out.println("\n");
			}*/
			for (String string : fileDataLinesList) {
				//System.out.println("string:"+string);
				for (int i = 0; i < string.length(); i++) {
				//System.out.println("i:"+j+"j:"+i+"char At:"+string.charAt(i));
					myArray[j][i] = string.charAt(i);
					if (string.charAt(i) == 'X') {
						x2 = j;
						y2 = i;
					}
					if (string.charAt(i) == '@') {
						x1 = j;
						y1 = i;
					}
				}
				j++;
			}
			//System.out.println("x1:y1->("+x1+":"+y1+")  x2:y2->("+x2+":"+y2+")");
			/*for (int i = 0; i < rows; i++) {
				for (int k = 0; k < cols; k++) {
					System.out.print(myArray[i][k] +" ");
				}
				System.out.println("\n");
			}*/
			findShortPath();
			for (int i = 0; i < rows; i++) {
				for (int k = 0; k < cols; k++) {
					System.out.print(destArray[i][k] +" ");
				}
				System.out.println("\n");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(sc != null) {
				try {	
					sc.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			if(fileWriter != null ) {
				try {
					fileWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void findShortPath() {
		//System.out.println("=============================================");
		int currentLineNo = 0;
		Boolean isDestFind = false;
		//Boolean isCurrentLineMoved = false;
		for (; currentLineNo < rows;) {
			for (int j = 0; j < cols; j++) {
				if (!isDestFind) {
					//System.out.println("isDestFind:"+isDestFind);
					if (x1 == currentLineNo && y1 == j) {
						x1 = currentLineNo;
						y1 = j;
						destArray[currentLineNo][j] = '#';
						findTheCostOfSaroundingTiles();
					}else{
						try {
							destArray[currentLineNo][j] = myArray[currentLineNo][j];
							if (j == cols-1 && currentLineNo == x1) {
								x1=x1+1;
							}
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("Exception:"+currentLineNo+",j:"+j);
						}
					}
					if (x2 == currentLineNo && y2 == j) {
						isDestFind = true;
						
					}
				}else{
					if (myArray[currentLineNo][j] == 'X') {
						destArray[currentLineNo][j] = '#';
					}else {
						destArray[currentLineNo][j] = myArray[currentLineNo][j];
					}
					if (j == cols-1 ) {
						x1=x1+1;
					}
				}
			}
			if (currentLineNo <= x1) {
				currentLineNo++;
				//isCurrentLineMoved = false;
			}
		}
	}

	private void findTheCostOfSaroundingTiles() {
		int currentLineRight = findCostOfCurrentLineNext(x1,y1+1);
		int currentLineLeft = findCostOfCurrentLineNext(x1,y1-1);
		int nextLineSamePos = findCostOfCurrentLineNext(x1+1,y1);
		int nextLineRight = findCostOfCurrentLineNext(x1+1,y1+1);
		int nextLineLeft = findCostOfCurrentLineNext(x1+1,y1-1);
		int prevLineSamePos = findCostOfCurrentLineNext(x1-1,y1);
		int PrevLineRight = findCostOfCurrentLineNext(x1-1,y1+1);
		int prevLineLeft = findCostOfCurrentLineNext(x1-1,y1-1);
		List<Integer> list = new ArrayList<Integer>();
		list.add(currentLineRight);
		list.add(currentLineLeft);
		list.add(nextLineSamePos);
		list.add(nextLineRight);
		list.add(nextLineLeft);
		list.add(prevLineSamePos);
		list.add(PrevLineRight);
		list.add(prevLineLeft);
		int min = Collections.min(list);
		if (currentLineRight == min) {
			y1 = y1+1;
		}else if (currentLineLeft == min) {
			y1 = y1-1;
		}else if (nextLineSamePos == min) {
			x1 = x1+1;
		}else if (nextLineRight == min) {
			x1 = x1+1;
			y1 = y1+1;
		}else if (nextLineLeft == min) {
			x1 = x1+1;
			y1 = y1-1;
		}else if (prevLineSamePos == min) {
			x1 = x1-1;
		}else if (PrevLineRight == min) {
			x1 = x1-1;
			y1 = y1+1;
		} else if (prevLineLeft == min){
			x1 = x1-1;
			y1 = y1-1;
		}
	}

	private int findCostOfCurrentLineNext(int i, int j) {
		int cost = 0;
		if (i == -1 || i >= rows || j == -1 || j >= cols) {
			cost = 999999;
		}else{
			try {
				char nextChar = myArray[i][j];
				if(wakableElements.contains(nextChar)) {
					cost = elementCostMap.get(nextChar);
					int distanceToGoal = Math.abs(i-x2) + Math.abs(j-y2);
					cost = cost + distanceToGoal;
				}else{
					cost = 999999;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Exception##findCostOfCurrentLineNext##i:"+i+",j:"+j);
			}
		}
		return cost;
	}

	private static Map<Character, Integer> setElementCostMap() {
		Map<Character, Integer> map = new HashMap<>();
		map.put('.', 1);
		map.put('@', 1);
		map.put('X', 1);
		map.put('*', 2);
		map.put('^', 3);
		return map;
	}
	
}
