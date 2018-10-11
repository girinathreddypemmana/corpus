package com.sam.algorithm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	Map<Integer,Long> pathNumberCostMap = new HashMap<>();
	long totalLines = 0;
	String firstLine;
	String lastLine;
	String currentLine;
	String nextLine = null;
	long cost = 0;
	int nextLineStartIndex = 0;
	long targetIndex = 0;
	int path_1 = 0;
	boolean isPathRemoved = false;
	boolean pathFound = false;

	public void searchSmallCostPath(File largeMapFile, File targetFile) {
		//String prevLine;
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
						targetIndex = lineData.trim().indexOf("X");
						break;
					}
				}
			}
			//read and keep first line
			firstLine = fileDataLinesList.get(0);
			//read and keep last line
			lastLine = fileDataLinesList.get(fileDataLinesList.size()-1);
			
			totalLines = fileDataLinesList.size();
			List<List<Integer>> list1 = new ArrayList<>();
			//list.add(new ArrayList<Integer>());
			pathNumberCostMap.put(path_1, cost);
			pathNumberFollowedTileMap.put(path_1,list1);
			currentLine = fileDataLinesList.get(0);
			
			findPath(0, 0, 0,true);
			
			System.out.println(pathNumberCostMap);
			System.out.println(pathNumberFollowedTileMap);
			
			int rowCount = 0 ;
			/*for(List<List<Integer>> obj : pathNumberFollowedTileMap.values()) {
				fileWriter.write("list count = "+rowCount+++" size = "+obj.size()+"\n");
			}*/
			rowCount = 0 ;
			for(List<Integer> list : pathNumberFollowedTileMap.get(pathNumberFollowedTileMap.size()-1)) {
				String rowData = fileDataLinesList.get(rowCount);
				for(Integer innerList : list) {
					if( innerList  > 0) {
						rowData = rowData.substring(0, innerList)+'#'+rowData.substring(innerList+1);
					}else {
						rowData ='#'+rowData.substring(innerList+1);
					}
				}
				resultantfileDataLinesList.add(rowData);
				rowCount++;
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

	private void findPath(int j, int pathNumber,long cost , boolean lineChanged) {
		boolean isFirstLine;
		boolean isLastLine = false;
		
		System.out.println(pathNumberCostMap);
		System.out.println(pathNumberFollowedTileMap);
		try {
			//Thread.sleep(1000);
			for(;j<=totalLines-1;j++) {
				currentLine = fileDataLinesList.get(j);
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
					findLowCostPathForEndLine(pathNumberCostMap.get(pathNumber), currentLine, currentLine, pathNumber);
				}else{
					lineChanged = findLowCostPathFromNonZeroIndex(j,pathNumberCostMap.get(pathNumber),currentLine,nextLine,pathNumber,lineChanged);
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

	private void findLowCostPathForEndLine(long cost, String currentLine, String nextLine,int path) throws IOException {
		boolean lineChanged2 = true;
		if(targetIndex < nextLineStartIndex) {
			while(true) {
				char sameLineNextChar = currentLine.charAt(nextLineStartIndex-1);
				long  sameLineNextCharCost = 0;
				if(wakableElements.contains(sameLineNextChar)) {
					sameLineNextCharCost = elementCostMap.get(sameLineNextChar);
					sameLineNextCharCost = cost + sameLineNextCharCost;
					long distanceToGoal = Math.abs( (nextLineStartIndex-1)- targetIndex) + Math.abs(0- 0 );
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
				long  sameLineNextCharCost = 0;
				if(wakableElements.contains(sameLineNextChar)) {
					sameLineNextCharCost = elementCostMap.get(sameLineNextChar);
					sameLineNextCharCost = cost + sameLineNextCharCost;
					long distanceToGoal = Math.abs( (nextLineStartIndex+1)- targetIndex) + Math.abs(0- 0 );
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
						System.out.println(" -------   found            -------"+path+"\n");
						pathFound = true;
						break;
					}
					cost = cost+sameLineNextCharCost;
					pathNumberCostMap.put(path,cost);
				}else {
					pathFound = false;
					//fileWriter.write(" ------- not  found            -------"+path+"\n");
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
	
	
	
	private boolean findLowCostPathFromNonZeroIndex(int i,long cost, String currentLine, String nextLine, int path, boolean isNewLine) throws IOException {
			boolean lineChanged = false;
			long movableTileCount = 0;
			char sameLineNextChar = currentLine.length()-1>nextLineStartIndex?currentLine.charAt(nextLineStartIndex+1):'~';
			//char sameLinePrevChar = currentLine.charAt(nextLineStartIndex-1);
			char nextLineBelowChar = nextLine.charAt(nextLineStartIndex);
			char nextLineNextChar = nextLine.length()-1>nextLineStartIndex?nextLine.charAt(nextLineStartIndex+1):'~';
			char nextLinePrevChar = nextLine.length()-1>nextLineStartIndex-1 && nextLineStartIndex!=0 ?nextLine.charAt(nextLineStartIndex-1):'~';
			long  sameLineNextCharCost = 0;
			long  sameLinePrevCharCost = 0;
			long  nextLineBelowCharCost = 0;
			long  nextLineNextCharCost = 0;
			long  nextLinePrevCharCost = 0;
			long  sameLineNextCharScore = 0;
			//long  sameLinePrevCharScore = 0;
			long  nextLineBelowCharScore = 0;
			long  nextLineNextCharScore = 0;
			long  nextLinePrevCharScore = 0;
			if(wakableElements.contains(sameLineNextChar)) {
				sameLineNextCharCost = elementCostMap.get(sameLineNextChar);
				sameLineNextCharCost = cost + sameLineNextCharCost;
				long distanceToGoal = Math.abs((nextLineStartIndex+1)- targetIndex) + Math.abs(0- (totalLines-1-i) );
				sameLineNextCharScore = sameLineNextCharCost + distanceToGoal;
			}
			if(wakableElements.contains(nextLineBelowChar)) {
				nextLineBelowCharCost = elementCostMap.get(nextLineBelowChar);
				nextLineBelowCharCost = cost + nextLineBelowCharCost;
				long distanceToGoal = Math.abs(nextLineStartIndex- targetIndex) + Math.abs(0- (totalLines-1-(i+1)) );
				nextLineBelowCharScore = nextLineBelowCharCost + distanceToGoal;
			}
			if(wakableElements.contains(nextLineNextChar)) {
				nextLineNextCharCost = elementCostMap.get(nextLineNextChar);
				nextLineNextCharCost = cost + nextLineNextCharCost;
				long distanceToGoal = Math.abs( (nextLineStartIndex+1) - targetIndex) + Math.abs( 0- (totalLines-1-(i+1)) );
				nextLineNextCharScore = nextLineNextCharCost + distanceToGoal;
				//cost = cost + nextLineNextCharCost;
			}
			if(wakableElements.contains(nextLinePrevChar)) {
				nextLinePrevCharCost = elementCostMap.get(nextLinePrevChar);
				nextLinePrevCharCost = cost + nextLinePrevCharCost;
				long distanceToGoal = Math.abs( (nextLineStartIndex-1) - targetIndex) + Math.abs( 0- (totalLines-1-(i+1)) );
				nextLinePrevCharScore = nextLinePrevCharCost + distanceToGoal;
			}
			/*if(wakableElements.contains(sameLinePrevChar)) {
				sameLinePrevCharCost = elementCostMap.get(sameLinePrevChar);
				sameLinePrevCharCost = cost + sameLinePrevCharCost;
				long distanceToGoal = Math.abs( (nextLineStartIndex-1) - targetIndex) + Math.abs( 0- (totalLines-1-(i+1)) );
				sameLinePrevCharCost = sameLinePrevCharCost + distanceToGoal;
			}*/
			
			if(nextLineBelowCharCost ==0 && sameLineNextCharCost==0 && nextLinePrevCharCost==0 && nextLineNextCharCost==0 && sameLinePrevCharCost==0) {
				pathNumberFollowedTileMap.remove(path);
				pathNumberCostMap.remove(path);
				isPathRemoved = true;
				return false;
			}			
			//currentLine = currentLine.substring(0,nextLineStartIndex)+"#"+currentLine.substring(nextLineStartIndex+1);
			
			int index = nextLineStartIndex;
			if(sameLineNextCharScore!=0 && (sameLineNextCharScore < (nextLineBelowCharScore==0?999999999:nextLineBelowCharScore)) && sameLineNextCharScore <  (nextLineNextCharScore==0?999999999:nextLineNextCharScore) && sameLineNextCharScore <  (nextLinePrevCharScore==0?999999999:nextLinePrevCharScore)) {
				lineChanged = false;
				nextLineStartIndex++;
				cost = sameLineNextCharCost;
				pathNumberCostMap.put(path, cost);
				addPathIndexesToMap(isNewLine, index, path, i);				
			}else if(nextLineBelowCharScore != 0 && nextLineBelowCharScore < (sameLineNextCharScore==0?999999999:sameLineNextCharScore) && nextLineBelowCharScore <  (nextLineNextCharScore==0?999999999:nextLineNextCharScore) && nextLineBelowCharScore < (nextLinePrevCharScore==0?999999999:nextLinePrevCharScore)) {
				lineChanged = true;
				cost = nextLineBelowCharCost;
				pathNumberCostMap.put(path, cost);
				addPathIndexesToMap(isNewLine, index, path, i);
			}else if(nextLineNextCharScore != 0 && nextLineNextCharScore < (sameLineNextCharScore==0?999999999:sameLineNextCharScore)  &&  nextLineNextCharScore < (nextLineBelowCharScore==0?999999999:nextLineBelowCharScore)  &&  nextLineNextCharScore < (nextLinePrevCharScore==0?999999999:nextLinePrevCharScore)) {
				nextLineStartIndex++;
				lineChanged = true;
				cost = nextLineNextCharCost;
				pathNumberCostMap.put(path, cost);
				addPathIndexesToMap(isNewLine, index, path, i);
			}else if(nextLinePrevCharScore !=0 && nextLinePrevCharScore < (sameLineNextCharScore==0?999999999:sameLineNextCharScore)  &&  nextLinePrevCharScore < (nextLineBelowCharScore==0?999999999:nextLineBelowCharScore)  &&  nextLinePrevCharScore <  (nextLineNextCharScore==0?999999999:nextLineNextCharScore) ) {
				nextLineStartIndex--;
				lineChanged = true;
				cost = nextLinePrevCharCost;
				pathNumberCostMap.put(path, cost);
				addPathIndexesToMap(isNewLine, index, path, i);
			}else if(sameLinePrevCharCost !=0) {
				nextLineStartIndex--;
				lineChanged = false;
				cost = sameLinePrevCharCost;
				pathNumberCostMap.put(path, cost);
				addPathIndexesToMap(isNewLine, index, path, i);
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
			
				if(targetIndex >= nextLineStartIndex) {
					if(nextLineNextCharCost != 0) {
						addPathIndexesToMap(isNewLine, index, path, i);
						nextLineStartIndex++;
						lineChanged = true;
						if(movableTileCount > 1) {
							path++;
							addNextPathToMap(path,cost);
							findPath(i+1, path, cost, lineChanged);
							nextLineStartIndex = index;
							path--;
							if(pathFound) {
								return false;
							}else {
								removePathIndexesFromMap(isNewLine, index, path, i);
							}
						}
						cost = nextLineNextCharCost;
						pathNumberCostMap.put(path, cost);
					}
					if(nextLineBelowCharCost != 0) {
						addPathIndexesToMap(isNewLine, index, path, i);
						lineChanged = true;
						if(movableTileCount > 1) {
							path++;
							addNextPathToMap(path,cost);
							findPath(i+1, path, cost,lineChanged);
							nextLineStartIndex = index;
							path--;
							if(pathFound) {
								return false;
							}else {
								removePathIndexesFromMap(isNewLine, index, path, i);
							}
							
						}
						cost = nextLineBelowCharCost;
						pathNumberCostMap.put(path, cost);
					} 
					
					if(nextLinePrevCharCost !=0) {
						addPathIndexesToMap(isNewLine, index, path, i);
						nextLineStartIndex--;
						lineChanged = true;
						if(movableTileCount > 1) {
							path++;
							addNextPathToMap(path,cost);
							findPath(i+1, path, cost, lineChanged);
							nextLineStartIndex = index;
							path--;
							if(pathFound) {
								return false;
							}else {
								removePathIndexesFromMap(isNewLine, index, path, i);
							}
							
						}
						cost = nextLinePrevCharCost;
						pathNumberCostMap.put(path, cost);
					}
					
					if(sameLineNextCharCost!=0) {
						addPathIndexesToMap(isNewLine, index, path, i);
						lineChanged = false;
						nextLineStartIndex++;
						if(movableTileCount > 1) {
							path++;
							addNextPathToMap(path,cost);
							findPath(i, path, cost, lineChanged);
							path--;
							if(pathFound) {
								return false;
							}else {
								removePathIndexesFromMap(isNewLine, index, path, i);
							}
							nextLineStartIndex = index;
						}
						cost = sameLineNextCharCost;
						pathNumberCostMap.put(path, cost);
						
					} 
				}else if(targetIndex < nextLineStartIndex) {
					if(nextLinePrevCharCost !=0) {
						addPathIndexesToMap(isNewLine, index, path, i);
						nextLineStartIndex--;
						lineChanged = true;
						if(movableTileCount > 1) {
							path++;
							addNextPathToMap(path,cost);
							findPath(i+1, path, cost, lineChanged);
							nextLineStartIndex = index;
							path--;
							if(pathFound) {
								return false;
							}else {
								removePathIndexesFromMap(isNewLine, index, path, i);
							}
						}
						cost = nextLinePrevCharCost;
						pathNumberCostMap.put(path, cost);
					}
					if(sameLinePrevCharCost !=0) {
						addPathIndexesToMap(isNewLine, index, path, i);
						nextLineStartIndex--;
						lineChanged = false;
						
						if(movableTileCount > 1) {
							path++;
							addNextPathToMap(path,cost);
							findPath(i, path, cost, lineChanged);
							nextLineStartIndex = index;
							path--;
							if(pathFound) {
								return false;
							}else {
								removePathIndexesFromMap(isNewLine, index, path, i);
							}
						}
						cost = sameLinePrevCharCost;
						pathNumberCostMap.put(path, cost);
					}
					if(nextLineBelowCharCost != 0) {
						addPathIndexesToMap(isNewLine, index, path, i);
						lineChanged = true;
						if(movableTileCount > 1) {
							path++;
							addNextPathToMap(path,cost);
							findPath(i+1, path, cost,lineChanged);
							nextLineStartIndex = index;
							path--;
							if(pathFound) {
								return false;
							}else {
								removePathIndexesFromMap(isNewLine, index, path, i);
							}
						}
						cost = nextLineBelowCharCost;
						pathNumberCostMap.put(path, cost);
					} 
					if(nextLineNextCharCost != 0) {
						addPathIndexesToMap(isNewLine, index, path, i);
						nextLineStartIndex++;
						lineChanged = true;
						if(movableTileCount > 1) {
							path++;
							addNextPathToMap(path,cost);
							findPath(i+1, path, cost, lineChanged);
							nextLineStartIndex = index;
							path--;
							if(pathFound) {
								return false;
							}else {
								removePathIndexesFromMap(isNewLine, index, path, i);
							}
						}
						cost = nextLineNextCharCost;
						pathNumberCostMap.put(path, cost);
					} 
					if(sameLineNextCharCost!=0) {
						addPathIndexesToMap(isNewLine, index, path, i);
						lineChanged = false;
						nextLineStartIndex++;
						if(movableTileCount > 1) {
							path++;
							addNextPathToMap(path,cost);
							findPath(i, path, cost, lineChanged);
							path--;
							if(pathFound) {
								return false;
							}else {
								removePathIndexesFromMap(isNewLine, index, path, i);
							}
							nextLineStartIndex = index;
						}
						cost = sameLineNextCharCost;
						pathNumberCostMap.put(path, cost);
						
					} 
				}
			}
			System.out.println("scores = "+sameLineNextChar+":"+sameLineNextCharCost+" , "+nextLineBelowChar+":"+nextLineBelowCharCost+" , "+nextLineNextChar+":"+nextLineNextCharCost+" , "+nextLinePrevChar+":"+nextLinePrevCharCost);
			return lineChanged;
	}

	

	private void addNextPathToMap(int path,long cost) {
		List<List<Integer>> list = new ArrayList<>();
		for(List<Integer> innerList : pathNumberFollowedTileMap.get(path-1)) {
			list.add(new ArrayList<>(innerList));
		}
		pathNumberCostMap.put(path, cost);
		pathNumberFollowedTileMap.put(path,list);
		System.out.println();
	}
	
	private void addPathIndexesToMap(boolean isNewLine, int index, int path, int i) {
		
		try{
			if(isNewLine) {
				List<Integer> list1 = new ArrayList<>();
				//List<List<Integer>> list2 = new ArrayList<>();
				list1.add(index);
				//list2.add(list1);
				pathNumberFollowedTileMap.get(path).add(list1);
				//fileWriter.write("added = "+i+" , index = "+index+"\n");
			}else {
				if(pathNumberFollowedTileMap.get(path).isEmpty()) {
					List<Integer> list1 = new ArrayList<>();
					list1.add(index);
					pathNumberFollowedTileMap.get(path).add(list1);
				}else {
					pathNumberFollowedTileMap.get(path).get(pathNumberFollowedTileMap.get(path).size()-1).add(index);
				}
				//fileWriter.write("added = "+i+" , index = "+index+"\n");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void removePathIndexesFromMap(boolean isNewLine, int index, int path, int i) {
		try{
			if(isNewLine) {
				List<Integer> list1 = new ArrayList<>();
				//List<List<Integer>> list2 = new ArrayList<>();
				list1.add(index);
				//list2.add(list1);
				pathNumberFollowedTileMap.get(path).remove(pathNumberFollowedTileMap.get(path).size()-1);
				//fileWriter.write("removed = "+i+" , index = "+index+"\n");
			}else {
				if(pathNumberFollowedTileMap.get(path).isEmpty()) {
					List<Integer> list1 = new ArrayList<>();
					list1.add(index);
					pathNumberFollowedTileMap.get(path).remove(pathNumberFollowedTileMap.get(path).size()-1);
				}else {
					pathNumberFollowedTileMap.get(path).get(pathNumberFollowedTileMap.get(path).size()-1).remove(pathNumberFollowedTileMap.get(path).get(pathNumberFollowedTileMap.get(path).size()-1).size()-1);
				}
				//fileWriter.write("removed = "+i+" , index = "+index+"\n");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
		
}
