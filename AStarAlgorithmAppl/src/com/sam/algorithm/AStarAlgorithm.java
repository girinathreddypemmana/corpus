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
	private static List<Character> wakableElements = Arrays.asList('.','@','X','*','^');
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
	int totalLines = 0;
	String firstLine;
	String lastLine;
	String currentLine;
	String nextLine = null;
	int cost = 0;
	int nextLineStartIndex = 0;
	int i = 0;
	boolean lineChanged = true;

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
			while(sc.hasNextLine()) {
				String lineData = sc.nextLine();
				if(lineData.contains("@")) {
					fileDataLinesList.add(lineData.trim());
					continue;
				}
				if(fileDataLinesList.size()>0 ) {
					fileDataLinesList.add(lineData.trim());
					lineData.contains("X");
					break;
				}
			}
			//read and keep first line
			firstLine = fileDataLinesList.get(0);
			//read and keep last line
			lastLine = fileDataLinesList.get(fileDataLinesList.size()-1);
			
			totalLines = fileDataLinesList.size();
			for(;i<totalLines-1;i++) {
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
				
				if(isLastLine) {
					
				}else{
					if(isFirstLine) {
						if(currentLine.contains("@")) {
							nextLineStartIndex = currentLine.indexOf("@");
						}
					}
					if(nextLineStartIndex>0) {
						findLowCostPathFromNonZeroIndex();
					}else {
						findLowCostPath();
					}
				}
				if(lineChanged) {
					if(isFirstLine) {
						if(currentLine.contains("@")) {
							currentLine.replaceFirst("@", "#");
						}
					}
					i++;
					//break;
				}else {
					if(isFirstLine) {
						if(currentLine.contains("@")) {
							currentLine.replaceFirst("@", "#");
						}
					}
					i--;
					//break;
				}
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

	private static Map<Character, Integer> setElementCostMap() {
		Map<Character, Integer> map = new HashMap<>();
		map.put('.', 1);
		map.put('@', 1);
		map.put('X', 1);
		map.put('*', 2);
		map.put('^', 3);
		return map;
	}
	
	private void findLowCostPath() throws IOException {
		char sameLineNextChar = currentLine.charAt(nextLineStartIndex+1);
		char nextLineBelowChar = nextLine.charAt(nextLineStartIndex);
		char nextLineNextChar = nextLine.charAt(nextLineStartIndex+1);
		int  sameLineNextCharCost = 0;
		int  nextLineBelowCharCost = 0;
		int  nextLineNextCharCost = 0;
		if(wakableElements.contains(sameLineNextChar)) {
			sameLineNextCharCost = elementCostMap.get(sameLineNextChar);
			sameLineNextCharCost = cost + sameLineNextCharCost;
			int distanceToGoal = Math.abs( (nextLineStartIndex+1)- (lastLine.length()-1) ) + Math.abs(0- (totalLines-1-i) );
			sameLineNextCharCost = sameLineNextCharCost + distanceToGoal;
		}
		if(wakableElements.contains(nextLineBelowChar)) {
			nextLineBelowCharCost = elementCostMap.get(nextLineBelowChar);
			nextLineBelowCharCost = cost + nextLineBelowCharCost;
			int distanceToGoal = Math.abs(nextLineStartIndex- (lastLine.length()-1) ) + Math.abs(0- (totalLines-1-(i+1)) );
			nextLineBelowCharCost = nextLineBelowCharCost + distanceToGoal;
		}
		if(wakableElements.contains(nextLineNextChar)) {
			nextLineNextCharCost = elementCostMap.get(nextLineNextChar);
			nextLineNextCharCost = cost + nextLineNextCharCost;
			int distanceToGoal = Math.abs( (nextLineStartIndex+1) - (lastLine.length()-1) ) + Math.abs( 0- (totalLines-1-(i+1)) );
			nextLineNextCharCost = nextLineNextCharCost + distanceToGoal;
		}
		
		if(sameLineNextCharCost!=0 &&  (sameLineNextCharCost < (nextLineBelowCharCost==0?999999999:nextLineBelowCharCost)) && sameLineNextCharCost <  (nextLineNextCharCost==0?999999999:nextLineNextCharCost)) {
			//currentLine.replaceFirst("\\*", "#");
			//currentLine = currentLine.replace(Pattern.quote(String.valueOf(sameLineNextChar)), "#");
			if(nextLineStartIndex == 0) {
				currentLine = currentLine.substring(0,1)+"#"+currentLine.substring(2);
			}else {
				currentLine = currentLine.substring(0,nextLineStartIndex)+"#"+currentLine.substring(nextLineStartIndex+2);
			}
			fileWriter.write(currentLine.replaceFirst("@", "#")+"\n");
			nextLineStartIndex++;
			lineChanged = false;
		}else if(nextLineBelowCharCost != 0 && nextLineBelowCharCost < (sameLineNextCharCost==0?999999999:sameLineNextCharCost) && nextLineBelowCharCost <  (nextLineNextCharCost==0?999999999:nextLineNextCharCost)) {
			//nextLine = nextLine.replaceFirst(String.valueOf(nextLineBelowChar), "#");
			
			if(nextLineStartIndex == 0) {
				nextLine = "#"+nextLine.substring(1);
			}else {
				nextLine = nextLine.substring(0,nextLineStartIndex)+"#"+nextLine.substring(nextLineStartIndex+2);
			}
			
			fileWriter.write(currentLine.replaceFirst("@", "#")+"\n");
			System.out.println(currentLine);
			//currentLine = null;
			System.out.println(nextLine);
			fileWriter.write(nextLine+"\n");
			lineChanged = true;
		}else if(nextLineNextCharCost != 0 && nextLineNextCharCost < (sameLineNextCharCost==0?999999999:sameLineNextCharCost)  &&  nextLineNextCharCost < (nextLineBelowCharCost==0?999999999:nextLineBelowCharCost) ) {
			//nextLine = nextLine.substring(0,1)+"#"+nextLine.substring(2);
			
			if(nextLineStartIndex == 0) {
				nextLine = nextLine.substring(0,1)+"#"+nextLine.substring(2);
			}else {
				nextLine = nextLine.substring(0,nextLineStartIndex)+"#"+nextLine.substring(nextLineStartIndex+2);
			}
			
			
			fileWriter.write(currentLine.replaceFirst("@", "#")+"\n");
			//currentLine = null;
			fileWriter.write(nextLine+"\n");
			nextLineStartIndex++ ;
			lineChanged = true;
		}else {
			System.out.println("something went wrong");
		}
		System.out.println("scores = "+sameLineNextChar+":"+sameLineNextCharCost+" , "+nextLineBelowChar+":"+nextLineBelowCharCost+" , "+nextLineNextChar+":"+nextLineNextCharCost);
	}
	
	
	private void findLowCostPathFromNonZeroIndex() throws IOException {
			char sameLineNextChar = currentLine.charAt(nextLineStartIndex+1);
			char nextLineBelowChar = nextLine.charAt(nextLineStartIndex);
			char nextLineNextChar = nextLine.charAt(nextLineStartIndex+1);
			char nextLinePrevChar = nextLine.charAt(nextLineStartIndex-1);
			int  sameLineNextCharCost = 0;
			int  nextLineBelowCharCost = 0;
			int  nextLineNextCharCost = 0;
			int  nextLinePrevCharCost = 0;
			if(wakableElements.contains(sameLineNextChar)) {
				sameLineNextCharCost = elementCostMap.get(sameLineNextChar);
				sameLineNextCharCost = cost + sameLineNextCharCost;
				int distanceToGoal = Math.abs((nextLineStartIndex+1)- (lastLine.length()-1) ) + Math.abs(0- (totalLines-1-i) );
				sameLineNextCharCost = sameLineNextCharCost + distanceToGoal;
			}
			if(wakableElements.contains(nextLineBelowChar)) {
				nextLineBelowCharCost = elementCostMap.get(nextLineBelowChar);
				nextLineBelowCharCost = cost + nextLineBelowCharCost;
				int distanceToGoal = Math.abs(nextLineStartIndex- (lastLine.length()-1) ) + Math.abs(0- (totalLines-1-(i+1)) );
				nextLineBelowCharCost = nextLineBelowCharCost + distanceToGoal;
			}
			if(wakableElements.contains(nextLineNextChar)) {
				nextLineNextCharCost = elementCostMap.get(nextLineNextChar);
				nextLineNextCharCost = cost + nextLineNextCharCost;
				int distanceToGoal = Math.abs( (nextLineStartIndex+1) - (lastLine.length()-1) ) + Math.abs( 0- (totalLines-1-(i+1)) );
				nextLineNextCharCost = nextLineNextCharCost + distanceToGoal;
			}
			if(wakableElements.contains(nextLinePrevChar)) {
				nextLinePrevCharCost = elementCostMap.get(nextLinePrevChar);
				nextLinePrevCharCost = cost + nextLinePrevCharCost;
				int distanceToGoal = Math.abs( (nextLineStartIndex-1) - (lastLine.length()-1) ) + Math.abs( 0- (totalLines-1-(i+1)) );
				nextLinePrevCharCost = nextLinePrevCharCost + distanceToGoal;
			}
			
			if(sameLineNextCharCost!=0 && (sameLineNextCharCost < (nextLineBelowCharCost==0?999999999:nextLineBelowCharCost)) && sameLineNextCharCost <  (nextLineNextCharCost==0?999999999:nextLineNextCharCost) && sameLineNextCharCost <  (nextLinePrevCharCost==0?999999999:nextLinePrevCharCost)) {
				//currentLine.replaceFirst(Pattern.quote(String.valueOf(sameLineNextChar)), "#");
				//currentLine = currentLine.replaceFirst("@", "#").replaceFirst(Pattern.quote(String.valueOf(sameLineNextChar)), "#");
				
				if(nextLineStartIndex == 0) {
					currentLine = currentLine.substring(0,1)+"#"+currentLine.substring(2);
				}else {
					currentLine = currentLine.substring(0,nextLineStartIndex)+"#"+currentLine.substring(nextLineStartIndex+2);
				}
				
				fileWriter.write(currentLine);
				nextLine = null;
				nextLineStartIndex++;
				lineChanged = false;
				i--;
			}else if(nextLineBelowCharCost != 0 && nextLineBelowCharCost < (sameLineNextCharCost==0?999999999:sameLineNextCharCost) && nextLineBelowCharCost <  (nextLineNextCharCost==0?999999999:nextLineNextCharCost) && nextLineBelowCharCost < (nextLinePrevCharCost==0?999999999:nextLinePrevCharCost)) {
				//nextLine = nextLine.replaceFirst(String.valueOf(nextLineBelowChar), "#");
				
				if(nextLineStartIndex == 0) {
					nextLine = "#"+nextLine.substring(1);
				}else {
					nextLine = nextLine.substring(0,nextLineStartIndex)+"#"+nextLine.substring(nextLineStartIndex+1);
				}
				
				fileWriter.write(currentLine.replaceFirst("@", "#")+"\n");
				System.out.println(currentLine);
				//currentLine = null;
				System.out.println(nextLine);
				fileWriter.write(nextLine);
				lineChanged = true;
			}else if(nextLineNextCharCost != 0 && nextLineNextCharCost < (sameLineNextCharCost==0?999999999:sameLineNextCharCost)  &&  nextLineNextCharCost < (nextLineBelowCharCost==0?999999999:nextLineBelowCharCost)  &&  nextLineNextCharCost < (nextLinePrevCharCost==0?999999999:nextLinePrevCharCost)) {
				//nextLine = nextLine.substring(0,1)+"#"+nextLine.substring(2);
				
				if(nextLineStartIndex == 0) {
					nextLine = nextLine.substring(0,1)+"#"+nextLine.substring(2);
				}else {
					nextLine = nextLine.substring(0,nextLineStartIndex+1)+"#"+nextLine.substring(nextLineStartIndex+2);
				}
				
				fileWriter.write(currentLine.replaceFirst("@", "#")+"\n");
				//currentLine = null;
				fileWriter.write(nextLine);
				nextLineStartIndex++;
				lineChanged = true;
			}else if(nextLinePrevCharCost !=0 && nextLinePrevCharCost < (sameLineNextCharCost==0?999999999:sameLineNextCharCost)  &&  nextLinePrevCharCost < (nextLineBelowCharCost==0?999999999:nextLineBelowCharCost)  &&  nextLinePrevCharCost <  (nextLineNextCharCost==0?999999999:nextLineNextCharCost) ) {
				//nextLine = nextLine.substring(0,1)+"#"+nextLine.substring(2);
				
				nextLine = nextLine.substring(0,nextLineStartIndex-1)+"#"+nextLine.substring(nextLineStartIndex);
				
				fileWriter.write(currentLine.replaceFirst("@", "#")+"\n");
				//currentLine = null;
				fileWriter.write(nextLine);
				nextLineStartIndex--;
				lineChanged = true;
			}else {
				System.out.println("something went wrong 222");
			}
			System.out.println("scores = "+sameLineNextChar+":"+sameLineNextCharCost+" , "+nextLineBelowChar+":"+nextLineBelowCharCost+" , "+nextLineNextChar+":"+nextLineNextCharCost+" , "+nextLinePrevChar+":"+nextLinePrevCharCost);
		//continue;
	}

}
