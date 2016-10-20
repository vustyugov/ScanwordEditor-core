package dao;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import block.cell.*;

public class PatternsFileDao implements PatternsDao{

	@Override
	public List<Cell[][]> read(String path) throws IOException{
		return separateLine(readFile(path));
	}
	
	private String readFile(String path) throws IOException {
		FileReader file = new FileReader(path);
		BufferedReader buf = new BufferedReader(file);
		StringBuilder str = new StringBuilder();
		String line = buf.readLine();
		
		while (line != null) {
			str.append(line);
			str.append(System.lineSeparator());
			line = buf.readLine();
		}
		return str.toString();
	}
	
	private List<Cell[][]> separateLine(String line) {
		StringTokenizer token = new StringTokenizer(line, ";");
		List<String[]> list = new LinkedList<String[]>();
		while (token.hasMoreElements()) {
			String[] args = new String[3];
			args[0] = token.nextToken();
			args[1] = token.nextToken();
			args[2] = token.nextToken();
			list.add(args);
		}
		List<Cell[][]> elements = new LinkedList<Cell[][]>();
		for (int index = 0; index < list.size();index++) {
			elements.add(parse(list.get(index)));
		}
		return elements;
	}
	
	private Cell[][] parse(String[] array) {
		int row = Integer.valueOf(array[0]).intValue();
		int column = Integer.valueOf(array[1]).intValue();
		Cell[][] cells = new TotalCell[row][column];
		List<String> list = new LinkedList<String>();
		StringTokenizer token = new StringTokenizer(array[2], " ");
		while(token.hasMoreElements()) {
			list.add(token.nextToken());
		}
		
		for (int index = 0; index < list.size(); index++) {
			int indexR = index / column;
			int indexC = index - (index / column)*column;
			String firstLink = "";
			String secondLink = "";
			if (list.get(index).length() > 2) {
				int startIndex = list.get(index).indexOf(":");
				int endIndex = list.get(index).indexOf(",");
				if (endIndex != -1) {
					firstLink = list.get(index).substring(startIndex+1, endIndex);
					secondLink = list.get(index).substring(endIndex+1, list.get(index).length());
				} else {
					firstLink = list.get(index).substring(startIndex+1, list.get(index).length());
					secondLink = "";
				}
				
				
			}
			
			if (list.get(index).contains("sc")) {
				cells[indexR][indexC] = new SimpleCell();
			}
			if (list.get(index).contains("dc")) {
				cells[indexR][indexC] = new DisableCell();
			}
			if (list.get(index).contains("ac")) {
				cells[indexR][indexC] = new ActiveCell();
				if (firstLink != null | secondLink != null) {
					cells[indexR][indexC].setFirstLink(firstLink);
					cells[indexR][indexC].setSecondLink(secondLink);
				}
			}
			if (list.get(index).contains("cc")) {
				cells[indexR][indexC] = new CommentCell();
				cells[indexR][indexC].setFirstLink(firstLink);
				cells[indexR][indexC].setSecondLink(secondLink);
			}
		}
		return cells;
	}

}
