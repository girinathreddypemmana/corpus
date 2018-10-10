package com.sam.algorithm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;	

public class AStarAlgorithm {
	private static List<Character> wakableElements = Arrays.asList('.','X','*','^');
	//private static char nonWakableElement = '~';
	private static Map<Character, Integer> elementCostMap = setElementCostMap();

	/*
	  Non-walkable: 
	  N/A = Water (~)
	------------------------  
	  Walkable:
	  1 = Flatlands (. or @ or X) 
	  2 = Forest (*) 
	  3 = Mountain (^)
	 */
	
	Scanner sc  = null;
	FileWriter fileWriter = null;
	List<String> fileDataLinesList = null;
	List<String> resultantfileDataLinesList = null;
	Map<Integer,List<List<Integer>>> pathNumberFollowedTileMap = new HashMap<>();
	Map<Integer,Integer> pathNumberCostMap = new HashMap<>();
	int totalLines = 0;
	String firstLine;
	String lastLine;
	String currentLine;
	String nextLine = null;
	int cost = 0;
	int nextLineStartIndex = 0;
	int i = 0;
	int targetIndex = 0;
	int path_1 = 0;
	boolean isPathRemoved = false;
	boolean pathFound = false;

	public void searchSmallCostPath(File largeMapFile, File targetFile) {
		//String prevLine;
		boolean isFirstLine = true;
		boolean isLastLine = false;
		try {
			//declare scanner pointing to source file to read content line by line
			sc = new Scanner(largeMapFile);
			
			fileWriter = new FileWriter(targetFile);
			//read all lines and keep them in list
			fileDataLinesList = new ArrayList<>();
			resultantfileDataLinesList = new ArrayList<>();
			while(sc.hasNextLine()) {
				String lineData = sc.nextLine();
				if(lineData.contains("@")) {
					fileDataLinesList.add(lineData.trim());
					continue;
				}
				if(fileDataLinesList.size()>0 ) {
					fileDataLinesList.add(lineData.trim());
					if(lineData.contains("X")) {
						targetIndex = lineData.indexOf("X");
						break;
					}
				}
			}
			//read and keep first line
			firstLine = fileDataLinesList.get(0);
			//read and keep last line
			lastLine = fileDataLinesList.get(fileDataLinesList.size()-1);
			
			totalLines = fileDataLinesList.size();
			boolean lineChanged = true;
			List<List<Integer>> list1 = new ArrayList<>();
			//list.add(new ArrayList<Integer>());
			pathNumberCostMap.put(path_1, cost);
			pathNumberFollowedTileMap.put(path_1,list1);
			for(;i<=totalLines-1;i++) {
				if(lineChanged) {
					currentLine = fileDataLinesList.get(i);
				}
				
				if(i==0) {
					isFirstLine = true;
				}else {
					isFirstLine = false;
				}
				
				if(i == (totalLines-1) ) {
					isLastLine = true;
				}
				
				if(!isLastLine) {
					nextLine = fileDataLinesList.get(i+1);
				}
				findPath(0, 0, 0);
				/*if(isLastLine) {
					findLowCostPathForEndLine();
				}else{
					if(isFirstLine) {
						if(currentLine.contains("@")) {
							nextLineStartIndex = currentLine.indexOf("@");
						}
					}
					if(nextLineStartIndex>0) {
						lineChanged = findLowCostPathFromNonZeroIndex(i,cost,currentLine,nextLine,path_1);
					}else {
						lineChanged = findLowCostPath(i,cost,currentLine,nextLine,path_1);
					}
				}
				*/
				break;
			}
			System.out.println(pathNumberCostMap);
			System.out.println("HI:"+pathNumberFollowedTileMap.toString());
			
			int rowCount = 0 ;
			for(List<List<Integer>> obj : pathNumberFollowedTileMap.values()) {
				fileWriter.write("list count = "+rowCount+++" size = "+obj.size()+"\n");
			}
			rowCount = 0 ;
			for(Entry<Integer, List<List<Integer>>> entry : pathNumberFollowedTileMap.entrySet()) {
				String rowData = fileDataLinesList.get(rowCount);
				List<List<Integer>> list2 = entry.getValue();
				for (List<Integer> list : list2) {
					for(Integer innerList : list) {
						if( innerList  > 0) {
							rowData = rowData.substring(0, innerList)+'#'+rowData.substring(innerList+1);
							i++;
						}else {
							rowData ='#'+rowData.substring(innerList+1);
						}
					}
					resultantfileDataLinesList.add(rowData);
					rowCount++;
					
				}
			}
			System.out.println("fileRows = "+fileDataLinesList.size() +", resultant rows = "+resultantfileDataLinesList.size());
			for(String line : resultantfileDataLinesList) {
				fileWriter.write(line);
				fileWriter.write("\n");
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

	private void findPath(int j, int pathNumber,int cost) {
		if(pathNumber == 22) {
			System.out.println();
		}
		boolean lineChanged = true;
		boolean isFirstLine;
		boolean isLastLine = false;
		
		System.out.println(pathNumberCostMap);
		System.out.println(pathNumberFollowedTileMap);
		try {
			//Thread.sleep(1000);
			for(;j<=totalLines-1;j++) {
				if(lineChanged) {
					currentLine = fileDataLinesList.get(j);
				}
				
				
				if(j==0) {
					isFirstLine = true;
				}else {
					isFirstLine = false;
				}
				
				if(j == (totalLines-1) ) {
					isLastLine = true;
				}
				if(pathNumber == 103) {
					System.out.println("found");
				}
				if(!isLastLine) {
					nextLine = fileDataLinesList.get(j+1);
				}
				
				if(isLastLine) {
					findLowCostPathForEndLine(cost, currentLine, currentLine, pathNumber);
				}else{
					if(nextLineStartIndex>0) {
						lineChanged = findLowCostPathFromNonZeroIndex(j,cost,currentLine,nextLine,pathNumber,lineChanged);
					}else {
						lineChanged = findLowCostPath(j,cost,currentLine,nextLine,pathNumber,lineChanged);
					}
				}
				if(isPathRemoved) {
					isPathRemoved = false;
					return;
				}
				if(pathFound) {
					return;
				}
				if(lineChanged) {
					if(isFirstLine) {
						if(currentLine.contains("@")) {
							currentLine.replaceFirst("@", "#");
						}
					}
				}else {
					if(isFirstLine) {
						if(currentLine.contains("@")) {
							currentLine.replaceFirst("@", "#");
						}
					}
					j--;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void findLowCostPathForEndLine(int cost, String currentLine, String nextLine,int path) throws IOException {
		boolean lineChanged2 = true;
		if(targetIndex < nextLineStartIndex) {
			while(true) {
				char sameLineNextChar = currentLine.charAt(nextLineStartIndex-1);
				int  sameLineNextCharCost = 0;
				if(wakableElements.contains(sameLineNextChar)) {
					sameLineNextCharCost = elementCostMap.get(sameLineNextChar);
					sameLineNextCharCost = cost + sameLineNextCharCost;
					int distanceToGoal = Math.abs( (nextLineStartIndex-1)- targetIndex) + Math.abs(0- 0 );
					sameLineNextCharCost = sameLineNextCharCost + distanceToGoal;
				}
				if(sameLineNextCharCost!=0) {
					if(lineChanged2) {
						lineChanged2 = false;
						List<Integer> list1 = new ArrayList<>();
						//List<List<Integer>> list2 = new ArrayList<>();
						list1.add(nextLineStartIndex);
						//list2.add(list1);
						pathNumberFollowedTileMap.get(path).add(list1);
					}else {
						pathNumberFollowedTileMap.get(path).get(pathNumberFollowedTileMap.get(path).size()-1).add(nextLineStartIndex);
					}
					if(sameLineNextChar == 'X') {
						pathFound = true;
						break;
					}
					cost = cost+sameLineNextCharCost;
					pathNumberCostMap.put(path,cost);
				}else {
					pathFound = false;
					System.out.println("{before}size = "+pathNumberFollowedTileMap.size() + "   ,  "+pathNumberCostMap.size() );
					pathNumberFollowedTileMap.remove(path);
					pathNumberCostMap.remove(path);
					System.out.println("{after}size = "+pathNumberFollowedTileMap.size() + "   ,  "+pathNumberCostMap.size() );
					isPathRemoved = true;
					break;
				}
				nextLineStartIndex--;
			}
		}else if(targetIndex > nextLineStartIndex) {
			while(true) {
				char sameLineNextChar = currentLine.charAt(nextLineStartIndex+1);
				int  sameLineNextCharCost = 0;
				if(wakableElements.contains(sameLineNextChar)) {
					sameLineNextCharCost = elementCostMap.get(sameLineNextChar);
					sameLineNextCharCost = cost + sameLineNextCharCost;
					int distanceToGoal = Math.abs( (nextLineStartIndex+1)- targetIndex) + Math.abs(0- 0 );
					sameLineNextCharCost = sameLineNextCharCost + distanceToGoal;
				}
				if(sameLineNextCharCost!=0) {
					if(lineChanged2) {
						lineChanged2 = false;
						List<Integer> list1 = new ArrayList<>();
						//List<List<Integer>> list2 = new ArrayList<>();
						list1.add(nextLineStartIndex);
						//list2.add(list1);
						pathNumberFollowedTileMap.get(path).add(list1);
					}else {
						pathNumberFollowedTileMap.get(path).get(pathNumberFollowedTileMap.get(path).size()-1).add(nextLineStartIndex);
					}
					if(sameLineNextChar == 'X') {
						System.out.println(" -------   found            -------"+path);
						pathFound = true;
						break;
					}
					cost = cost+sameLineNextCharCost;
					pathNumberCostMap.put(path,cost);
				}else {
					pathFound = false;
					fileWriter.write(" ------- not  found            -------"+path);
					System.out.println("{before}size = "+pathNumberFollowedTileMap.size() + "   ,  "+pathNumberCostMap.size() );
					pathNumberFollowedTileMap.remove(path);
					pathNumberCostMap.remove(path);
					System.out.println("{after}size = "+pathNumberFollowedTileMap.size() + "   ,  "+pathNumberCostMap.size() );
					isPathRemoved = true;
					break;
				}
				nextLineStartIndex++;
			}
		}else {
			char sameLineNextChar = currentLine.charAt(nextLineStartIndex);
			if(sameLineNextChar == 'X') {
				if(lineChanged2) {
					lineChanged2 = false;
					List<Integer> list1 = new ArrayList<>();
					//List<List<Integer>> list2 = new ArrayList<>();
					list1.add(nextLineStartIndex);
					//list2.add(list1);
					pathNumberFollowedTileMap.get(path).add(list1);
				}else {
					pathNumberFollowedTileMap.get(path).get(pathNumberFollowedTileMap.get(path).size()-1).add(nextLineStartIndex);
				}
				cost = cost+elementCostMap.get('X');
				pathNumberCostMap.put(path,cost);
				pathFound = true;
			}else {
				System.out.println("{before}size = "+pathNumberFollowedTileMap.size() + "   ,  "+pathNumberCostMap.size() );
				pathNumberFollowedTileMap.remove(path);
				pathNumberCostMap.remove(path);
				System.out.println("{after}size = "+pathNumberFollowedTileMap.size() + "   ,  "+pathNumberCostMap.size() );
				isPathRemoved = true;
			}
		}
		
		//currentLine = currentLine.replace("X", "#");
		//pathFound = true;
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
	
	private boolean findLowCostPath(int i, int cost, String currentLine, String nextLine,int path, boolean lineChanged2) throws IOException {
		boolean lineChanged = false;
		char sameLineNextChar = currentLine.length()-1>nextLineStartIndex?currentLine.charAt(nextLineStartIndex+1):'~';
		char nextLineBelowChar =  nextLine.length()-1>nextLineStartIndex?nextLine.charAt(nextLineStartIndex):'~';
		char nextLineNextChar = nextLine.length()-1>nextLineStartIndex?nextLine.charAt(nextLineStartIndex+1):'~';
		int  sameLineNextCharCost = 0;
		int  nextLineBelowCharCost = 0;
		int  nextLineNextCharCost = 0;
		int movableTileCount = 0 ;
		if(wakableElements.contains(sameLineNextChar)) {
			sameLineNextCharCost = elementCostMap.get(sameLineNextChar);
			sameLineNextCharCost = cost + sameLineNextCharCost;
			int distanceToGoal = Math.abs( (nextLineStartIndex+1)- targetIndex) + Math.abs(0- (totalLines-1-i) );
			sameLineNextCharCost = sameLineNextCharCost + distanceToGoal;
		}
		if(wakableElements.contains(nextLineBelowChar)) {
			nextLineBelowCharCost = elementCostMap.get(nextLineBelowChar);
			nextLineBelowCharCost = cost + nextLineBelowCharCost;
			int distanceToGoal = Math.abs(nextLineStartIndex- targetIndex) + Math.abs(0- (totalLines-1-(i+1)) );
			nextLineBelowCharCost = nextLineBelowCharCost + distanceToGoal;
		}
		if(wakableElements.contains(nextLineNextChar)) {
			nextLineNextCharCost = elementCostMap.get(nextLineNextChar);
			nextLineNextCharCost = cost + nextLineNextCharCost;
			int distanceToGoal = Math.abs( (nextLineStartIndex+1) - targetIndex) + Math.abs( 0- (totalLines-1-(i+1)) );
			nextLineNextCharCost = nextLineNextCharCost + distanceToGoal;
		}
		
		if(nextLineBelowCharCost ==0 && sameLineNextCharCost==0 && nextLineNextCharCost==0) {
			pathNumberFollowedTileMap.remove(path);
			pathNumberCostMap.remove(path);
			isPathRemoved = true;
			return false;
		}
		
		int index = nextLineStartIndex;
		if(sameLineNextCharCost!=0 &&  (sameLineNextCharCost < (nextLineBelowCharCost==0?999999999:nextLineBelowCharCost)) && sameLineNextCharCost <  (nextLineNextCharCost==0?999999999:nextLineNextCharCost)) {
			lineChanged = false;
			nextLineStartIndex++;
			if(movableTileCount > 1) {
				path++;
				addNextPathToMap(path,cost);
				findPath(i, path, cost);
				nextLineStartIndex = index;
				path--;
			}
			cost = cost + sameLineNextCharCost;
			pathNumberCostMap.put(path, cost);
		}else if(nextLineBelowCharCost != 0 && nextLineBelowCharCost < (sameLineNextCharCost==0?999999999:sameLineNextCharCost) && nextLineBelowCharCost <  (nextLineNextCharCost==0?999999999:nextLineNextCharCost)) {
			lineChanged = true;
			if(movableTileCount > 1) {
				path++;
				addNextPathToMap(path,cost);
				findPath(i+1, path, cost);
				nextLineStartIndex = index;
				path--;
			}
			cost =cost + nextLineBelowCharCost;
			pathNumberCostMap.put(path, cost);
		}else if(nextLineNextCharCost != 0 && nextLineNextCharCost < (sameLineNextCharCost==0?999999999:sameLineNextCharCost)  &&  nextLineNextCharCost < (nextLineBelowCharCost==0?999999999:nextLineBelowCharCost) ) {
			nextLineStartIndex++ ;
			lineChanged = true;
			if(movableTileCount > 1) {
				path++;
				addNextPathToMap(path,cost);
				findPath(i+1, path, cost);
				nextLineStartIndex = index;
				path--;
			}
			cost =cost + nextLineNextCharCost;
			pathNumberCostMap.put(path, cost);
		}else {
			if(nextLineBelowCharCost !=0) {
				movableTileCount++;
			}if(sameLineNextCharCost!=0) {
				movableTileCount++;
			}if(nextLineNextCharCost!=0) {
				movableTileCount++;
			}
			
			if(sameLineNextCharCost!=0 ) {
				lineChanged = false;
				nextLineStartIndex++;
				if(movableTileCount > 1) {
					path++;
					addNextPathToMap(path,cost);
					findPath(i, path, cost);
					nextLineStartIndex = index;
					path--;
					if(pathFound)
						return false;
				}
				cost = cost + sameLineNextCharCost;
				pathNumberCostMap.put(path, cost);
			}
			if(nextLineBelowCharCost != 0 ) {
				lineChanged = true;
				if(movableTileCount > 1) {
					path++;
					addNextPathToMap(path,cost);
					findPath(i+1, path, cost);
					nextLineStartIndex = index;
					path--;
					if(pathFound)
						return false;
				}
				cost =cost + nextLineBelowCharCost;
				pathNumberCostMap.put(path, cost);
			}
			if(nextLineNextCharCost != 0 ) {
				nextLineStartIndex++ ;
				lineChanged = true;
				if(movableTileCount > 1) {
					path++;
					addNextPathToMap(path,cost);
					findPath(i+1, path, cost);
					nextLineStartIndex = index;
					path--;
					if(pathFound)
						return false;
				}
				cost =cost + nextLineNextCharCost;
				pathNumberCostMap.put(path, cost);
			}
		}
		
		
		if(lineChanged2) {
			List<Integer> list1 = new ArrayList<>();
			//List<List<Integer>> list2 = new ArrayList<>();
			list1.add(index);
			//list2.add(list1);
			pathNumberFollowedTileMap.get(path).add(list1);
		}else {
			pathNumberFollowedTileMap.get(path).get(pathNumberFollowedTileMap.get(path).size()-1).add(index);
		}

		System.out.println("scores = "+sameLineNextChar+":"+sameLineNextCharCost+" , "+nextLineBelowChar+":"+nextLineBelowCharCost+" , "+nextLineNextChar+":"+nextLineNextCharCost);
		return lineChanged;
	}
	
	
	private boolean findLowCostPathFromNonZeroIndex(int i,int cost, String currentLine, String nextLine, int path, boolean lineChanged2) throws IOException {
			boolean lineChanged = false;
			int movableTileCount = 0;
			char sameLineNextChar = currentLine.length()-1>nextLineStartIndex?currentLine.charAt(nextLineStartIndex+1):'~';
			//char sameLinePrevChar = currentLine.charAt(nextLineStartIndex-1);
			char nextLineBelowChar = nextLine.length()-1>nextLineStartIndex?nextLine.charAt(nextLineStartIndex):'~';
			char nextLineNextChar = nextLine.length()-1>nextLineStartIndex?nextLine.charAt(nextLineStartIndex+1):'~';
			char nextLinePrevChar = nextLine.length()-1>nextLineStartIndex?nextLine.charAt(nextLineStartIndex-1):'~';
			int  sameLineNextCharCost = 0;
			int  sameLinePrevCharCost = 0;
			int  nextLineBelowCharCost = 0;
			int  nextLineNextCharCost = 0;
			int  nextLinePrevCharCost = 0;
			if(wakableElements.contains(sameLineNextChar)) {
				sameLineNextCharCost = elementCostMap.get(sameLineNextChar);
				sameLineNextCharCost = cost + sameLineNextCharCost;
				int distanceToGoal = Math.abs((nextLineStartIndex+1)- targetIndex) + Math.abs(0- (totalLines-1-i) );
				sameLineNextCharCost = sameLineNextCharCost + distanceToGoal;
			}
			if(wakableElements.contains(nextLineBelowChar)) {
				nextLineBelowCharCost = elementCostMap.get(nextLineBelowChar);
				nextLineBelowCharCost = cost + nextLineBelowCharCost;
				int distanceToGoal = Math.abs(nextLineStartIndex- targetIndex) + Math.abs(0- (totalLines-1-(i+1)) );
				nextLineBelowCharCost = nextLineBelowCharCost + distanceToGoal;
			}
			if(wakableElements.contains(nextLineNextChar)) {
				nextLineNextCharCost = elementCostMap.get(nextLineNextChar);
				nextLineNextCharCost = cost + nextLineNextCharCost;
				int distanceToGoal = Math.abs( (nextLineStartIndex+1) - targetIndex) + Math.abs( 0- (totalLines-1-(i+1)) );
				nextLineNextCharCost = nextLineNextCharCost + distanceToGoal;
				cost = cost + nextLineNextCharCost;
			}
			if(wakableElements.contains(nextLinePrevChar)) {
				nextLinePrevCharCost = elementCostMap.get(nextLinePrevChar);
				nextLinePrevCharCost = cost + nextLinePrevCharCost;
				int distanceToGoal = Math.abs( (nextLineStartIndex-1) - targetIndex) + Math.abs( 0- (totalLines-1-(i+1)) );
				nextLinePrevCharCost = nextLinePrevCharCost + distanceToGoal;
			}
			/*if(wakableElements.contains(sameLinePrevChar)) {
				sameLinePrevCharCost = elementCostMap.get(sameLinePrevChar);
				sameLinePrevCharCost = cost + sameLinePrevCharCost;
				int distanceToGoal = Math.abs( (nextLineStartIndex-1) - targetIndex) + Math.abs( 0- (totalLines-1-(i+1)) );
				sameLinePrevCharCost = sameLinePrevCharCost + distanceToGoal;
			}*/
			
			if(nextLineBelowCharCost ==0 && sameLineNextCharCost==0 && nextLinePrevCharCost==0 && nextLineNextCharCost==0 && sameLinePrevCharCost==0) {
				pathNumberFollowedTileMap.remove(path);
				pathNumberCostMap.remove(path);
				isPathRemoved = true;
				return false;
			}			
			currentLine = currentLine.substring(0,nextLineStartIndex)+"#"+currentLine.substring(nextLineStartIndex+1);
			
			int index = nextLineStartIndex;
			if(sameLineNextCharCost!=0 && (sameLineNextCharCost < (nextLineBelowCharCost==0?999999999:nextLineBelowCharCost)) && sameLineNextCharCost <  (nextLineNextCharCost==0?999999999:nextLineNextCharCost) && sameLineNextCharCost <  (nextLinePrevCharCost==0?999999999:nextLinePrevCharCost)) {
				lineChanged = false;
				nextLineStartIndex++;
				if(movableTileCount > 1) {
					path++;
					addNextPathToMap(path,cost);
					findPath(i, path, cost);
					nextLineStartIndex = index;
					path--;
				}
				cost = cost + sameLineNextCharCost;
				pathNumberCostMap.put(path, cost);
				
			}else if(nextLineBelowCharCost != 0 && nextLineBelowCharCost < (sameLineNextCharCost==0?999999999:sameLineNextCharCost) && nextLineBelowCharCost <  (nextLineNextCharCost==0?999999999:nextLineNextCharCost) && nextLineBelowCharCost < (nextLinePrevCharCost==0?999999999:nextLinePrevCharCost)) {
				lineChanged = true;
				
				if(movableTileCount > 1) {
					path++;
					addNextPathToMap(path,cost);
					findPath(i+1, path, cost);
					nextLineStartIndex = index;
					path--;
				}
				cost = cost + nextLineBelowCharCost;
				pathNumberCostMap.put(path, cost);
			}else if(nextLineNextCharCost != 0 && nextLineNextCharCost < (sameLineNextCharCost==0?999999999:sameLineNextCharCost)  &&  nextLineNextCharCost < (nextLineBelowCharCost==0?999999999:nextLineBelowCharCost)  &&  nextLineNextCharCost < (nextLinePrevCharCost==0?999999999:nextLinePrevCharCost)) {
				nextLineStartIndex++;
				lineChanged = true;
				if(movableTileCount > 1) {
					path++;
					addNextPathToMap(path,cost);
					findPath(i+1, path, cost);
					nextLineStartIndex = index;
					path--;
				}
				cost = cost + nextLineNextCharCost;
				pathNumberCostMap.put(path, cost);
			}else if(nextLinePrevCharCost !=0 && nextLinePrevCharCost < (sameLineNextCharCost==0?999999999:sameLineNextCharCost)  &&  nextLinePrevCharCost < (nextLineBelowCharCost==0?999999999:nextLineBelowCharCost)  &&  nextLinePrevCharCost <  (nextLineNextCharCost==0?999999999:nextLineNextCharCost) ) {
				nextLineStartIndex--;
				lineChanged = true;
				if(movableTileCount > 1) {
					path++;
					addNextPathToMap(path,cost);
					findPath(i+1, path, cost);
					nextLineStartIndex = index;
					path--;
				}
				cost = cost + nextLinePrevCharCost;
				pathNumberCostMap.put(path, cost);
			}else if(sameLinePrevCharCost !=0) {
				nextLineStartIndex--;
				lineChanged = false;
				
				if(movableTileCount > 1) {
					path++;
					addNextPathToMap(path,cost);
					findPath(i, path, cost);
					nextLineStartIndex = index;
					path--;
				}
				cost = cost + sameLinePrevCharCost;
				pathNumberCostMap.put(path, cost);
			}else {

				if(nextLineBelowCharCost !=0) {
					movableTileCount++;
				}if(sameLineNextCharCost!=0) {
					movableTileCount++;
				}if(nextLinePrevCharCost!=0 ) {
					movableTileCount++;
				}if(nextLineNextCharCost!=0) {
					movableTileCount++;
				}if(sameLinePrevCharCost!=0) {
					movableTileCount++;
				}
			
				
				if(sameLineNextCharCost!=0) {
					lineChanged = false;
					nextLineStartIndex++;
					if(movableTileCount > 1) {
						path++;
						addNextPathToMap(path,cost);
						findPath(i, path, cost);
						if(pathFound)
							return false;
						nextLineStartIndex = index;
						path--;
					}
					cost = cost + sameLineNextCharCost;
					pathNumberCostMap.put(path, cost);
					
				} 
				if(nextLineBelowCharCost != 0) {
					lineChanged = true;
					
					if(movableTileCount > 1) {
						path++;
						addNextPathToMap(path,cost);
						findPath(i+1, path, cost);
						nextLineStartIndex = index;
						if(pathFound)
							return false;
						path--;
					}
					cost = cost + nextLineBelowCharCost;
					pathNumberCostMap.put(path, cost);
				} 
				if(nextLineNextCharCost != 0) {
					nextLineStartIndex++;
					lineChanged = true;
					if(movableTileCount > 1) {
						path++;
						addNextPathToMap(path,cost);
						findPath(i+1, path, cost);
						nextLineStartIndex = index;
						if(pathFound)
							return false;
						path--;
					}
					cost = cost + nextLineNextCharCost;
					pathNumberCostMap.put(path, cost);
				} 
				if(nextLinePrevCharCost !=0) {
					nextLineStartIndex--;
					lineChanged = true;
					if(movableTileCount > 1) {
						path++;
						addNextPathToMap(path,cost);
						findPath(i+1, path, cost);
						nextLineStartIndex = index;
						path--;
						if(pathFound)
							return false;
					}
					cost = cost + nextLinePrevCharCost;
					pathNumberCostMap.put(path, cost);
				}
				if(sameLinePrevCharCost !=0) {
					nextLineStartIndex--;
					lineChanged = false;
					
					if(movableTileCount > 1) {
						path++;
						addNextPathToMap(path,cost);
						findPath(i, path, cost);
						nextLineStartIndex = index;
						path--;
						if(pathFound)
							return false;
					}
					cost = cost + sameLinePrevCharCost;
					pathNumberCostMap.put(path, cost);
				}
			}
			
			if(lineChanged2) {
				List<Integer> list1 = new ArrayList<>();
				//List<List<Integer>> list2 = new ArrayList<>();
				list1.add(index);
				//list2.add(list1);
				pathNumberFollowedTileMap.get(path).add(list1);
			}else {
				pathNumberFollowedTileMap.get(path).get(pathNumberFollowedTileMap.get(path).size()-1).add(index);
			}
			System.out.println("scores = "+sameLineNextChar+":"+sameLineNextCharCost+" , "+nextLineBelowChar+":"+nextLineBelowCharCost+" , "+nextLineNextChar+":"+nextLineNextCharCost+" , "+nextLinePrevChar+":"+nextLinePrevCharCost);
			return lineChanged;
	}

	private void addNextPathToMap(int path,int cost) {
		List<List<Integer>> list = new ArrayList<>();
		for(List<Integer> innerList : pathNumberFollowedTileMap.get(path-1)) {
			list.add(new ArrayList<>(innerList));
		}
		pathNumberCostMap.put(path, cost);
		pathNumberFollowedTileMap.put(path,list);
		System.out.println();
	}

}
