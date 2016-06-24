package block.scanword.impls;

import java.util.*;
import block.cell.*;
import block.scanword.api.*;

public class SimpleGenerator implements ArrowsAndWordsListGenerator {

	private enum Direction {
		HORIZONTAL, VERTICAL, BOTH;
	}
	
	private boolean isTypeCell(Cell cell, Class<? extends Cell> typeOfCell) {
		return (cell.getClass().getName().equals(typeOfCell.getName())) ? true: false;
	}
	
	private Integer[] rewriteArray (Integer[] array, Integer firstPositionValue, Integer secondPositionValue) {
		Integer[] newArray = new Integer[array.length + 2];
		 for (int index = 0; index < array.length; index++) {
			 newArray[index] = array[index];
		 }
		 newArray[0]++;
		 newArray[newArray.length-2] = firstPositionValue;
		 newArray[newArray.length-1] = secondPositionValue;
		 return newArray;
	}
	
	private Integer[] getInfoAboutNeighbourCells(Cell[][] array, int cellRowIndex, int cellColumnIndex, 
			Class<? extends Cell> typeOfCell, Class<? extends Cell> typeOfNeighbourCells) {
		Integer[] result = new Integer[1];
		result[0] = 0;
		if (array[cellRowIndex][cellColumnIndex].getClass().getName().equals(typeOfCell.getName())) {
			result = (cellColumnIndex < array[0].length - 1 && array[cellRowIndex][cellColumnIndex+1].getClass().getName().equals(typeOfNeighbourCells.getName()))? 
					rewriteArray(result, Integer.valueOf(cellRowIndex), Integer.valueOf(cellColumnIndex+1)): result;
			result = (cellRowIndex < array.length-1 && cellColumnIndex < array[0].length - 1 && array[cellRowIndex+1][cellColumnIndex+1].getClass().getName().equals(typeOfNeighbourCells.getName()))? 
					rewriteArray(result, Integer.valueOf(cellRowIndex+1), Integer.valueOf(cellColumnIndex+1)): result;
			result = (cellRowIndex < array.length - 1 && array[cellRowIndex+1][cellColumnIndex].getClass().getName().equals(typeOfNeighbourCells.getName()))?
					rewriteArray(result, Integer.valueOf(cellRowIndex+1), Integer.valueOf(cellColumnIndex)): result;
			result = (cellRowIndex < array.length - 1 && cellColumnIndex > 0 && array[cellRowIndex+1][cellColumnIndex-1].getClass().getName().equals(typeOfNeighbourCells.getName()))?
					rewriteArray(result, Integer.valueOf(cellRowIndex+1), Integer.valueOf(cellColumnIndex-1)): result;
			result = (cellColumnIndex > 0 && array[cellRowIndex][cellColumnIndex-1].getClass().getName().equals(typeOfNeighbourCells.getName()))?
				    rewriteArray(result, Integer.valueOf(cellRowIndex), Integer.valueOf(cellColumnIndex-1)): result;
			result = (cellRowIndex > 0 && cellColumnIndex > 0 && array[cellRowIndex-1][cellColumnIndex-1].getClass().getName().equals(typeOfNeighbourCells.getName()))?
					rewriteArray(result, Integer.valueOf(cellRowIndex-1), Integer.valueOf(cellColumnIndex-1)): result;
			result = (cellRowIndex > 0 && array[cellRowIndex-1][cellColumnIndex].getClass().getName().equals(typeOfNeighbourCells.getName()))?
					rewriteArray(result, Integer.valueOf(cellRowIndex-1), Integer.valueOf(cellColumnIndex)): result;
			result = (cellRowIndex > 0 && cellColumnIndex < array[0].length - 1 && array[cellRowIndex-1][cellColumnIndex+1].getClass().getName().equals(typeOfNeighbourCells.getName()))?
					rewriteArray(result, Integer.valueOf(cellRowIndex-1), Integer.valueOf(cellColumnIndex+1)): result;
		}
		return result;
	}
	
	private Integer[] getInfoAboutNeighbourCellsWithFreeLinks(Cell[][] array, int cellRowIndex, int cellColumnIndex,
			Class<? extends Cell> typeOfCell, Class<? extends Cell> typeOfNeighbourCells) {
		Integer[] info = getInfoAboutNeighbourCells(array, cellRowIndex, 
				cellColumnIndex, typeOfCell, typeOfNeighbourCells);
		Integer[] result = new Integer[1];
		result[0] = 0;
		for (int index = 0; index < info[0]; index++) {
			result = (getCountFreeLinksIntoCell(array[info[2*index+1]][info[2*index+2]]) > 0)?
					result = rewriteArray(result, info[2*index+1], info[2*index+2]): result;
			}
		return result;
	}
	
	private Integer[] getInfoAboutNeighbourCellsWithTwoFreeLinks(Cell[][] array, int cellRowIndex, int cellColumnIndex,
			Class<? extends Cell> typeOfCell, Class<? extends Cell> typeOfNeighbourCells) {
		Integer[] info = getInfoAboutNeighbourCells(array, cellRowIndex, 
				cellColumnIndex, typeOfCell, typeOfNeighbourCells);
		Integer[] result = new Integer[1];
		result[0] = 0;
		for (int index = 0; index < info[0]; index++) {
			result = (getCountFreeLinksIntoCell(array[info[2*index+1]][info[2*index+2]]) == 2)?
					result = rewriteArray(result, info[2*index+1], info[2*index+2]): result;
			}
		return result;
	}
	
	@Override
	public void defineActiveCells(Cell[][] array) {

		for(int row = 0; row < array.length; row++) {
			for(int column = 0; column < array[0].length; column++) {
				if (isTypeCell(array[row][column] , SimpleCell.class) 
						|| isTypeCell(array[row][column], ActiveCell.class)
						&& ((getInfoAboutNeighbourCells(array, row, column, ActiveCell.class, CommentCell.class)[0] > 0)
								|| (getInfoAboutNeighbourCells(array, row, column, SimpleCell.class, CommentCell.class)[0] > 0))) {
					int index = 0;
					String word = "";
					while( (column + index) < array[0].length && 
							!(isTypeCell(array[row][column+index], CommentCell.class) 
							|| isTypeCell(array[row][column + index], DisableCell.class))) {
						word += array[row][column+index].getLetter();
						index++;
					}
					if(index > 1) {
						if (isTypeCell(array[row][column], ActiveCell.class)) {
							((ActiveCell)array[row][column]).setHVirtualWord(word);
							((ActiveCell)array[row][column]).setHDirectioinWordLength(index);
						}
						else {
							ActiveCell cell = new ActiveCell(array[row][column].getLetter());
							cell.setHVirtualWord(word);
							cell.setHDirectioinWordLength(index);
							array[row][column] = cell;
						}
					}
					column+= index;
				}
			}
		}
		
		for(int column = 0; column < array[0].length; column++) {
			for(int row = 0; row < array.length; row++) {
				if ((isTypeCell(array[row][column], SimpleCell.class) 
						|| isTypeCell(array[row][column], ActiveCell.class))
						&& (getInfoAboutNeighbourCells(array, row, column, ActiveCell.class, CommentCell.class)[0] > 0
								|| getInfoAboutNeighbourCells(array, row, column, SimpleCell.class, CommentCell.class)[0] > 0)) {
					int index = 0;
					String word = "";
					while( (row + index) < array.length
							&& !(isTypeCell(array[row+index][column], CommentCell.class) 
							|| isTypeCell(array[row + index][column], DisableCell.class))) {
						word += array[row + index][column].getLetter();
						index++;
					}
					if(index > 1) {
						if (isTypeCell(array[row][column], ActiveCell.class)) {
							((ActiveCell)array[row][column]).setVVirtualWord(word);
							((ActiveCell)array[row][column]).setVDirectioinWordLength(index);
						}
						else {
							ActiveCell cell = new ActiveCell(array[row][column].getLetter());
							cell.setLetter(word);
							cell.setVDirectioinWordLength(index);
							array[row][column] = cell;
						}
					}
					row+= index;
				}
			}
		}		
	}

	private void defineFreeLinksInCells(Cell mainCell, String mainLink, Cell secondCell, String secondLink) {
		if (secondCell.getFirstLink().equals("")) {
			secondCell.setFirstLink(secondLink);
			secondCell.setSecondLink(secondCell.getSecondLink());
			if (!mainCell.getFirstLink().equals("") && mainCell.getSecondLink().equals("")) {
				mainCell.setFirstLink(mainCell.getFirstLink());
				mainCell.setSecondLink(mainLink);
			}
			else if (mainCell.getFirstLink().equals("") && !mainCell.getSecondLink().equals("")) {
				mainCell.setFirstLink(mainLink);
				mainCell.setSecondLink(mainCell.getSecondLink());
			}
			else if (mainCell.getFirstLink().equals("") && mainCell.getSecondLink().equals("")) {
				mainCell.setFirstLink(mainLink);
				mainCell.setSecondLink(mainCell.getSecondLink());
			}
		}
		else {
			if (secondCell.getSecondLink().equals("")) {
				secondCell.setFirstLink(secondCell.getFirstLink());
				secondCell.setSecondLink(secondLink);
				if (!mainCell.getFirstLink().equals("") && mainCell.getSecondLink().equals("")) {
					mainCell.setFirstLink(mainCell.getFirstLink());
					mainCell.setSecondLink(mainLink);
				}
				else if (mainCell.getFirstLink().equals("") && !mainCell.getSecondLink().equals("")) {
					mainCell.setFirstLink(mainLink);
					mainCell.setSecondLink(mainCell.getSecondLink());
				}
				else if (mainCell.getFirstLink().equals("") && mainCell.getSecondLink().equals("")) {
					mainCell.setFirstLink(mainLink);
					mainCell.setSecondLink(mainCell.getSecondLink());
				}

			}
		}
	}
	
	private int getCountFreeLinksIntoCell(Cell cell) {
		if ((!cell.getFirstLink().equals("")
				&& !cell.getSecondLink().equals(""))) {
			return 0;
		}
		else if ((cell.getFirstLink().equals("")
				&& !cell.getSecondLink().equals(""))
			|| (!cell.getFirstLink().equals("")
					&& cell.getSecondLink().equals(""))) {
			return 1;
		}
		else {
			return 2;
		}
	}
	
	private Direction getDirectionWordInActiveCell(Cell cell) {
		if (((ActiveCell)cell).getHDirectionWordLength() > 0
				&& ((ActiveCell)cell).getVDirectionWordLength() == 0) {
			return Direction.HORIZONTAL;
		}
		else if (((ActiveCell)cell).getHDirectionWordLength() == 0
				&& ((ActiveCell)cell).getVDirectionWordLength() > 0) {
			return Direction.VERTICAL;
		}
		else {
			return Direction.BOTH;
		}
	}
	
	private String[] getLinkValue (int mCellRow, int mCellColumn, int sCellRow, int sCellColumn) {
		StringBuilder mLink = new StringBuilder();
		String sLink = "";
		if (sCellRow > mCellRow) {
			if (sCellColumn > mCellColumn) {
				mLink.append("2.2.");
				sLink = "6.";
			}
			else if (sCellColumn < mCellColumn) {
				mLink.append("4.4.");
				sLink = "8.";
			}
			else {
				mLink.append("3.3.");
				sLink = "7.";
			}
		}
		else if (sCellRow < mCellRow) {
			if (sCellColumn > mCellColumn) {
				mLink.append("8.8.");
				sLink = "4.";
			}
			else if (sCellColumn < mCellColumn) {
				mLink.append("6.6.");
				sLink = "2.";
			}
			else {
				mLink.append("7.7.");
				sLink = "3.";
			}
		}
		else {
			if (sCellColumn > mCellColumn) {
				mLink.append("1.1.");
				sLink = "5.";
			}
			else if (sCellColumn < mCellColumn) {
				mLink.append("5.5.");
				sLink = "1.";
			}
		}
		String[] str = {mLink.toString(), sLink};
		return str;
	}
	
	private void setLink(Cell[][] array, int mCellRow, int mCellColumn, int sCellRow, int sCellColumn, Direction d) {
		String lastIndex = "";
		switch(d) {
		case HORIZONTAL:
			lastIndex = "1";
			break;
		case VERTICAL:
			lastIndex = "3";
			break;
		case BOTH:
			int diffRow = mCellRow - sCellRow;
			int diffCol = mCellColumn - sCellColumn;
			if (getCountFreeLinksIntoCell(array[sCellRow][sCellColumn]) == 2) {
				if (diffRow < 0 && diffCol == 0) {
					lastIndex = "3";
				}
				else if (diffRow == 0 && diffCol < 0) {
					lastIndex = "1";
				}
			}
			else if (getCountFreeLinksIntoCell(array[sCellRow][sCellColumn]) == 1) {
				if (!array[sCellRow][sCellColumn].getFirstLink().equals("")) {
					if (array[sCellRow][sCellColumn].getFirstLink().substring(2, 3).equals("1")) {
						lastIndex = "3";
					}
					else if (array[sCellRow][sCellColumn].getFirstLink().substring(2, 3).equals("3")) {
						lastIndex = "1";
					}
				}
				else if (!array[sCellRow][sCellColumn].getSecondLink().equals("")){
					if (array[sCellRow][sCellColumn].getSecondLink().substring(2, 3).equals("1")) {
						lastIndex = "3";
					}
					else if (array[sCellRow][sCellColumn].getSecondLink().substring(2, 3).equals("3")) {
						lastIndex = "1";
					}
				}
			}
			break;
		default:
			break;
		}
		String[] link = getLinkValue(mCellRow, mCellColumn, sCellRow, sCellColumn);
		defineFreeLinksInCells(array[mCellRow][mCellColumn], link[0] + lastIndex, array[sCellRow][sCellColumn], link[1]+lastIndex); 
	}
	
	private void firstStep(Cell[][] array) {
		for (int row = 0; row < array.length; row++) {
			for (int column = 0; column < array[0].length; column++) {
				if (isTypeCell(array[row][column], ActiveCell.class)) {
					if (getDirectionWordInActiveCell(array[row][column]) == Direction.HORIZONTAL
							||getDirectionWordInActiveCell(array[row][column]) == Direction.VERTICAL) {
						array[row][column].setFirstLink("0.0");
					}
				}
				if (isTypeCell(array[row][column], CommentCell.class) 
						&& getCountFreeLinksIntoCell(array[row][column]) > 0) {
					Integer[] info = getInfoAboutNeighbourCells(array, row, column, CommentCell.class, ActiveCell.class);
					if (info[0] == 1) {
						array[row][column].setFirstLink("0.0.0");
					}
				}
			}
		}
	}
	
	private void secondStep(Cell[][] array) {
		for (int row = 0; row < array.length; row++) {
			for (int column = 0; column < array[0].length; column++) {
				if (isTypeCell(array[row][column], CommentCell.class) 
						&& getCountFreeLinksIntoCell(array[row][column]) > 0) {
					Integer[] info = getInfoAboutNeighbourCellsWithFreeLinks(array, row, column, CommentCell.class, ActiveCell.class);
					if (info[0] > 0) {
						for (int index = 0; index < info[0]; index++) {
							Integer[] innerInfo = getInfoAboutNeighbourCellsWithFreeLinks(array, info[2*index+1], info[2*index+2], ActiveCell.class, CommentCell.class);
							if (innerInfo[0] == 2) {
								if ((info[2*index+1] == innerInfo[1] && info[2*index+2] > innerInfo[2])
										&& (info[2*index+1] > innerInfo[3] && info[2*index+2] == innerInfo[4])
										&& getDirectionWordInActiveCell(array[info[2*index+1]][info[2*index+2]]) == Direction.BOTH) {
									setLink(array, innerInfo[3], innerInfo[4], info[2*index+1], info[2*index+2], getDirectionWordInActiveCell(array[info[2*index+1]][info[2*index+2]]));
									setLink(array, innerInfo[1], innerInfo[2], info[2*index+1], info[2*index+2], getDirectionWordInActiveCell(array[info[2*index+1]][info[2*index+2]]));
								}
							}
						}
					}
					if (info[0] == 1) {
						setLink(array, row, column, info[1], info[2], getDirectionWordInActiveCell(array[info[1]][info[2]]));
					}
				}
				fillCommentCells(array, row, column);
			}
		}
	}

	private void thirdStep (Cell[][] array) {
		for (int row = 0; row < array.length; row++) {
			for (int column = 0; column < array[0].length; column++) {
				if (isTypeCell(array[row][column], CommentCell.class) 
						&& getCountFreeLinksIntoCell(array[row][column]) > 0) {
					Integer[] info = getInfoAboutNeighbourCellsWithFreeLinks(array, row, column, CommentCell.class, ActiveCell.class);
					if (info[0] > 1) {
						for (int index = 0; index < info[0]; index++) {
							Integer[] innerInfo = getInfoAboutNeighbourCellsWithFreeLinks(array, info[2*index+1], info[2*index+2], ActiveCell.class, CommentCell.class);
							if (innerInfo[0] == 1) {
								setLink(array, innerInfo[1], innerInfo[2], info[2*index+1], info[2*index+2], getDirectionWordInActiveCell(array[info[2*index+1]][info[2*index+2]]));
							}
						}
					}
				}
				fillCommentCells(array, row, column);
			}
		}
	} 
	
	private void forthStep (Cell[][] array) {
		for (int row = 0; row < array.length; row++) {
			for (int column = 0; column < array[0].length; column++) {
				if (isTypeCell(array[row][column], ActiveCell.class) 
						&& getCountFreeLinksIntoCell(array[row][column]) > 0) {
					Integer[] info = getInfoAboutNeighbourCellsWithTwoFreeLinks(array, row, column, ActiveCell.class, CommentCell.class);
					Integer[] innerInfo = getInfoAboutNeighbourCellsWithFreeLinks(array, row, column, ActiveCell.class, CommentCell.class);
					if (info[0] == 1) {
						if (row>info[1] && column==info[2]) {
							setLink(array, info[1], info[2], row, column, getDirectionWordInActiveCell(array[row][column]));
						}
						else if (row == info[1] && column > info[2]) {
							setLink(array, info[1], info[2], row, column, getDirectionWordInActiveCell(array[row][column]));
						}
						else if (row == info[1] && column < info[2]) {
							setLink(array, info[1], info[2], row, column, getDirectionWordInActiveCell(array[row][column]));
						}
						else {
							int label = 0;
							for (int index = 0; index < innerInfo[0]; index++) {
								if (row>innerInfo[2*index+1] && column==innerInfo[2*index+2]) {
									label = 1;
								}
								if (row==innerInfo[2*index+1] && column>innerInfo[2*index+2]) {
									label = 1;
								}
								if (row<innerInfo[2*index+1] && column==innerInfo[2*index+2]) {
									label = 1;
								}
								if (row==innerInfo[2*index+1] && column<innerInfo[2*index+2]) {
									label = 1;
								}
							}
							if (label == 0) {
								setLink(array, info[1], info[2], row, column, getDirectionWordInActiveCell(array[row][column]));
							}
						}
					}
					if (info[0] == 2) {
						if (row == info[1] && column < info[2] && row > info[3] && column == info[4]) {
							if (getInfoAboutNeighbourCellsWithTwoFreeLinks(array, row-1, column+1, ActiveCell.class, CommentCell.class)[0] == 2) {
								setLink(array, info[1], info[2], row, column, getDirectionWordInActiveCell(array[row][column]));
								setLink(array, info[3], info[4], row-1, column+1, getDirectionWordInActiveCell(array[row-1][column+1]));
							}
						}
						else if (row == info[3] && column < info[4] && row > info[1] && column == info[2]) {
							if (getInfoAboutNeighbourCellsWithTwoFreeLinks(array, row-1, column+1, ActiveCell.class, CommentCell.class)[0] == 2) {
								setLink(array, info[3], info[4], row, column, getDirectionWordInActiveCell(array[row][column]));
								setLink(array, info[1], info[2], row-1, column+1, getDirectionWordInActiveCell(array[row-1][column+1]));
							}
						}
						
						if (row == info[1] && column > info[2] && row < info[3] && column > info[4]) {
							if (getInfoAboutNeighbourCellsWithTwoFreeLinks(array, row+1, column, ActiveCell.class, CommentCell.class)[0] == 2) {
								setLink(array, info[1], info[2], row, column, getDirectionWordInActiveCell(array[row][column]));
								setLink(array, info[3], info[4], row+1, column, getDirectionWordInActiveCell(array[row+1][column]));
							}
						}
						else if (row == info[3] && column > info[4] && row < info[1] && column > info[2]) {
							if (getInfoAboutNeighbourCellsWithTwoFreeLinks(array, row+1, column, ActiveCell.class, CommentCell.class)[0] == 2) {
								setLink(array, info[3], info[4], row, column, getDirectionWordInActiveCell(array[row][column]));
								setLink(array, info[1], info[2], row+1, column, getDirectionWordInActiveCell(array[row+1][column]));
							}
						}
						
						if (row < info[1] && column == info[2] && row < info[3] && column < info[4]) {
							if (getInfoAboutNeighbourCellsWithTwoFreeLinks(array, row+2, column, ActiveCell.class, CommentCell.class)[0] >= 2) {
//								array[info[1]][info[2]].setFirstLink("0.0.0");
								setLink(array, info[1], info[2], row+2, column, getDirectionWordInActiveCell(array[row+2][column]));
//								array[info[3]][info[4]].setFirstLink("0.0.0");
								setLink(array, info[3], info[4], row, column, getDirectionWordInActiveCell(array[row][column]));
							}
						}
						else if (row < info[3] && column == info[4] && row < info[1] && column < info[2]) {
							if (getInfoAboutNeighbourCellsWithTwoFreeLinks(array, row+2, column, ActiveCell.class, CommentCell.class)[0] >= 2) {
//								array[info[3]][info[4]].setFirstLink("0.0.0");
								setLink(array, info[3], info[4], row+2, column, getDirectionWordInActiveCell(array[row+2][column]));
//								array[info[1]][info[2]].setFirstLink("0.0.0");
								setLink(array, info[1], info[2], row, column, getDirectionWordInActiveCell(array[row][column]));
							}
						}
						
						if (row > info[1] && column == info[2] && row > info[3] && column > info[4]) {
							if (getInfoAboutNeighbourCellsWithTwoFreeLinks(array, row, column-1, ActiveCell.class, CommentCell.class)[0] >= 2) {
//								array[info[1]][info[2]].setFirstLink("0.0.0");
								setLink(array, info[1], info[2], row, column, getDirectionWordInActiveCell(array[row][column]));
//								array[info[3]][info[4]].setFirstLink("0.0.0");
								setLink(array, info[3], info[4], row, column-1, getDirectionWordInActiveCell(array[row][column-1]));
							}
						}
						else if (row > info[3] && column == info[4] && row > info[1] && column > info[2]) {
							if (getInfoAboutNeighbourCellsWithTwoFreeLinks(array, row, column-1, ActiveCell.class, CommentCell.class)[0] >= 2) {
//								array[info[3]][info[4]].setFirstLink("0.0.0");
								setLink(array, info[3], info[4], row, column, getDirectionWordInActiveCell(array[row][column]));
//								array[info[1]][info[2]].setFirstLink("0.0.0");
								setLink(array, info[1], info[2], row, column-1, getDirectionWordInActiveCell(array[row][column-1]));
							}
						}
						
					}
				}
				fillCommentCells(array, row, column);
			}
		}
	}
	
	private void fifthStep (Cell[][] array) {
		for (int row = 0; row < array.length; row++) {
			for (int column = 0; column < array[0].length; column++) {
				if (isTypeCell(array[row][column], ActiveCell.class) 
						&& getCountFreeLinksIntoCell(array[row][column]) > 0) {
					Integer[] info = getInfoAboutNeighbourCellsWithFreeLinks(array, row, column, ActiveCell.class, CommentCell.class);
					Integer[] innerInfo = getInfoAboutNeighbourCellsWithTwoFreeLinks(array, row, column, ActiveCell.class, CommentCell.class);
					if (info[0] > 1 && innerInfo[0] == 0) {
						for (int index = 0; index < info[0]; index++) {
							if (row>info[2*index+1] && column==info[2*index+2]) {
								setLink(array, info[2*index+1], info[2*index+2], row, column, getDirectionWordInActiveCell(array[row][column]));
							}
							if (row == info[2*index+1] && column > info[2*index+2]) {
								setLink(array, info[2*index+1], info[2*index+2], row, column, getDirectionWordInActiveCell(array[row][column]));
							}
							if (row == info[2*index+1] && column < info[2*index+2]) {
								setLink(array, info[2*index+1], info[2*index+2], row, column, getDirectionWordInActiveCell(array[row][column]));
							}
							if (row < info[2*index+1] && column == info[2*index+2]) {
								setLink(array, info[2*index+1], info[2*index+2], row, column, getDirectionWordInActiveCell(array[row][column]));
							}
						}
					}
				}
				fillCommentCells(array, row, column);
			}
		}
	}
	
	private void sixthStep (Cell[][] array) {
		for (int row = 0; row < array.length; row++) {
			for (int column = 0; column < array[0].length; column++) {
				if (isTypeCell(array[row][column], CommentCell.class) 
						&& getCountFreeLinksIntoCell(array[row][column]) > 0) {
					Integer[] info = getInfoAboutNeighbourCellsWithFreeLinks(array, row, column, CommentCell.class, ActiveCell.class);
					if (info[0] == 1) {
						setLink(array, row, column, info[1], info[2], getDirectionWordInActiveCell(array[info[1]][info[2]]));
					}
				}
				fillCommentCells(array, row, column);
			}
		}
	}
		
	private int countOfCells(Cell[][] array, Class<? extends Cell> typeOfCell) {
		int index = 0;
		for (int row = 0; row < array.length ; row++) {
			for (int column = 0; column < array[0].length; column++) {
				if (isTypeCell(array[row][column],typeOfCell)) {
					if ((array[row][column].getFirstLink().equals("")
							|| array[row][column].getSecondLink().equals(""))) {
						index++;
					}
				}
			}
		}
		return index;
	}


	private void fillCommentCells(Cell[][] array, int row, int column) {
		if (isTypeCell(array[row][column], CommentCell.class)) {
			Integer[] info = getInfoAboutNeighbourCells(array, row, column, CommentCell.class, ActiveCell.class);
			int countActiveCells = 0;
			for (int index = 0; index < info[0]; index++) {
				if (array[info[2*index + 1]][info[2*index + 2]].getFirstLink().equals("")
						|| array[info[2*index + 1]][info[2*index + 2]].getSecondLink().equals("")) {
					countActiveCells++;
				}
			}
			if (countActiveCells == 0) {
				for (int index = 0; index < info[0]; index++) {
					if (array[row][column].getFirstLink().equals("")) {
						array[row][column].setFirstLink("0.0.0");
					}
					if ( array[row][column].getSecondLink().equals("")) {
						array[row][column].setSecondLink("0.0.0");
					}
				}
			}
		}
	}

	@Override
	public void defineArrowsInActiveCells(Cell[][] array) {
		int count = 0;
		int lastIndex = countOfCells(array, ActiveCell.class) + countOfCells(array, CommentCell.class);
		int step = 0;
		firstStep(array);
		step++;
		System.out.println(countOfCells(array, CommentCell.class) + ", "+countOfCells(array, ActiveCell.class)+ ", " +count+" "+step);
		step++;
		while (lastIndex > 0 && count < 3) {
			int active = countOfCells(array, ActiveCell.class);
			int comment = countOfCells(array, CommentCell.class);
			secondStep(array);
			if (lastIndex == (active + comment)) {
				count ++;
			}
			lastIndex = active + comment;
		}
		System.out.println(countOfCells(array, CommentCell.class) + ", "+countOfCells(array, ActiveCell.class)+ ", " +count+" "+step);
		
		count = 0;
		lastIndex = countOfCells(array, ActiveCell.class) + countOfCells(array, CommentCell.class);
		step++;
		while(lastIndex > 0 && count < 3) {
			int active = countOfCells(array, ActiveCell.class);
			int comment = countOfCells(array, CommentCell.class);
			thirdStep(array);
			if (lastIndex == (active + comment)) {
				count ++;
			}
			lastIndex = active + comment;
		}
		System.out.println(countOfCells(array, CommentCell.class) + ", "+countOfCells(array, ActiveCell.class)+ ", " +count+" "+step);
		
		count = 0;
		lastIndex = countOfCells(array, ActiveCell.class) + countOfCells(array, CommentCell.class);
		step++;
		while(lastIndex > 0 && count < 3) {
			int active = countOfCells(array, ActiveCell.class);
			int comment = countOfCells(array, CommentCell.class);
			forthStep(array);
			if (lastIndex == (active + comment)) {
				count ++;
			}
			lastIndex = active + comment;
		}
		System.out.println(countOfCells(array, CommentCell.class) + ", "+countOfCells(array, ActiveCell.class)+ ", " +count+" "+step);
		
		count = 0;
		lastIndex = countOfCells(array, ActiveCell.class) + countOfCells(array, CommentCell.class);
		step++;
		while(lastIndex > 0 && count < 3) {
			int active = countOfCells(array, ActiveCell.class);
			int comment = countOfCells(array, CommentCell.class);
			fifthStep(array);
			if (lastIndex == (active + comment)) {
				count ++;
			}
			lastIndex = active + comment;
		}
		System.out.println(countOfCells(array, CommentCell.class) + ", "+countOfCells(array, ActiveCell.class)+ ", " +count+" "+step);
		
		count = 0;
		lastIndex = countOfCells(array, ActiveCell.class) + countOfCells(array, CommentCell.class);
		step++;
		while(lastIndex > 0 && count < 3) {
			int active = countOfCells(array, ActiveCell.class);
			int comment = countOfCells(array, CommentCell.class);
			sixthStep(array);
			if (lastIndex == (active + comment)) {
				count ++;
			}
			lastIndex = active + comment;
		}
		System.out.println(countOfCells(array, CommentCell.class) + ", "+countOfCells(array, ActiveCell.class)+ ", " +count+" "+step);
		
	}
		
	@Override
	public List<String> getWordsList(Cell[][] array) {
		List<String> list = new LinkedList<String> ();
		for (int row = 0; row < array.length; row++) {
			for (int column = 0; column < array[0].length; column++) {
				if(isTypeCell(array[row][column], ActiveCell.class)) {
					list.add(((ActiveCell)array[row][column]).getHVirtualWord());
					list.add(((ActiveCell)array[row][column]).getVVirtualWord());
				}
			}
		}
		return list;
	}
}