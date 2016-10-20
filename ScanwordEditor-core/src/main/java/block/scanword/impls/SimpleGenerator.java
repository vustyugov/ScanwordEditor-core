package block.scanword.impls;

import java.util.*;
import block.cell.*;
import block.scanword.api.*;

public class SimpleGenerator implements ArrowsAndWordsListGenerator {
	
	private static final int ACTIVE_CELL_TYPE = 0;
	private static final int COMMENT_CELL_TYPE = 1;
	private static final int HORIZONTAL_CASE = 0;
	private static final int VERTICAL_CASE = 1;
	private static final int TOP_BOUNDARY_CASE = 2;
	private static final int LEFT_BOUNDARY_CASE = 3;

	private static final int CENTRAL_CASE = 0;
	private static final int LEFT_TOP_BOUNDARY_CASE = 1;
	private static final int RIGHT_TOP_BOUNDARY_CASE = 4;
	private static final int RIGHT_BOUNDARY_CASE = 5;
	private static final int LEFT_BOTTOM_BOUNDARY_CASE = 6;
	private static final int RIGHT_BOTTOM_BOUNDARY_CASE = 7;
	private static final int BOTTOM_BOUNDARY_CASE = 8;
	
	private static final int ONE_FREE_LINK_IN_CELL = 1;
	private static final int TWO_FREE_LINKS_IN_CELL = 2; 
	
	private List<Position> listPosition = new ArrayList(9);
	
	public SimpleGenerator() {
		listPosition.add(0, new Position(8, 1,0,1, 2,1,1, 3,1,0, 4,1,-1, 5,0,-1, 6,-1,-1, 7,-1,0, 8,-1,1));
		listPosition.add(1, new Position(3, 1,0,1, 2,1,1, 3,1,0));
		listPosition.add(2, new Position(5, 1,0,1, 2,1,1, 3,1,0, 4,1,-1, 5,0,-1));
		listPosition.add(3, new Position(5, 1,0,1, 2,1,1, 3,1,0, 7,-1,0, 8,-1,1));
		listPosition.add(4, new Position(3, 3,1,0, 4,1,-1, 5,0,-1));
		listPosition.add(5, new Position(5, 3,1,0, 4,1,-1, 5,0,-1, 6,-1,-1, 7,-1,0));
		listPosition.add(6, new Position(3, 1,0,1, 7,-1,0, 8,-1,1));
		listPosition.add(7, new Position(3, 5,0,-1, 6,-1,-1, 7,-1,0));
		listPosition.add(8, new Position(5, 1,0,1, 5,0,-1, 6,-1,-1, 7,-1,0, 8,-1,1));
	}
	
	private boolean isTypeCell(Cell cell, Class<? extends Cell> firstTypeOfCell, Class<? extends Cell> secondTypeOfCell) {
		if (firstTypeOfCell != null & secondTypeOfCell != null) {
			if (cell.getClass().getName().equals(firstTypeOfCell.getName()) 
					| cell.getClass().getName().equals(secondTypeOfCell.getName())) {
				return true;
			}
			else {
				return false;
			}
		}
		else if (firstTypeOfCell != null & secondTypeOfCell == null) {
			if (cell.getClass().getName().equals(firstTypeOfCell.getName())) {
				return true;
			}
			else {
				return false;
			}
		}
		else if (firstTypeOfCell == null & secondTypeOfCell != null) {
			if (cell.getClass().getName().equals(secondTypeOfCell.getName())) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
		
	private void setLengthWordInActiveCell(Cell[][] array, int indexR, int indexC, int type) {
		int index = 0; int condition = 0;
		int countR = 0;	int countC = 0;
		int newIndexR = indexR;	int newIndexC = indexC;
		int oldIndexR = indexR;	int oldIndexC = indexC;
		int length = 0;	int result = 0;
		boolean mainCondition = false;
		
		switch(type) {
		case HORIZONTAL_CASE:
			newIndexC += 1; 
			oldIndexC += 1;
			condition = newIndexC; 
			length = array[0].length; 
			result = 1;
			mainCondition = isTypeCell(array[indexR][indexC], CommentCell.class, DisableCell.class);
			break;
		case VERTICAL_CASE:
			newIndexR += 1; 
			oldIndexR += 1;
			condition = newIndexR; 
			length = array.length; 
			result = 1;
			mainCondition = isTypeCell(array[indexR][indexC], CommentCell.class, DisableCell.class);
			break;
		case TOP_BOUNDARY_CASE:
			newIndexR += 1;
			oldIndexC += 1;
			condition = newIndexR; 
			length = array.length;
			mainCondition = isTypeCell(array[indexR][indexC], SimpleCell.class, ActiveCell.class);
		case LEFT_BOUNDARY_CASE:
			newIndexC += 1;
			condition = newIndexC; 
			length = array[0].length;
			mainCondition = isTypeCell(array[indexR][indexC], SimpleCell.class, ActiveCell.class);
			break;
		default:
			break;
		}
		
		if (condition >= length) {
			return;
		}
		
		if(mainCondition) {
			while(isTypeCell(array[newIndexR+countR][newIndexC+countC], ActiveCell.class, SimpleCell.class)) {
				if (type == HORIZONTAL_CASE | type == LEFT_BOUNDARY_CASE) {
					index = ++countC;
				}
				else if (type == VERTICAL_CASE | type == TOP_BOUNDARY_CASE) {
					index = ++countR;
				}
				if (condition+countR+countC >= length) {
					break;
				}
			}
			
			if (isTypeCell(array[newIndexR][newIndexC], DisableCell.class, CommentCell.class)) {
				return;
			}
			
			if (index > result) {
				array[oldIndexR][oldIndexC] = new ActiveCell(array[oldIndexR][oldIndexC].getLetter());
				if (type == HORIZONTAL_CASE | type == LEFT_BOUNDARY_CASE) {
					((ActiveCell)array[oldIndexR][oldIndexC]).setHDirectioinWordLength(index+1-result);
				}
				else if (type == VERTICAL_CASE | type == TOP_BOUNDARY_CASE) {
					((ActiveCell)array[oldIndexR][oldIndexC]).setVDirectioinWordLength(index+1-result);
				}
			}
		}
	}
	@Override
	public void defineActiveCells(Cell[][] array) {
		for (int row = 0; row < array.length; row++) {
			for(int column = 0; column < array[0].length; column++) {
				setLengthWordInActiveCell(array, 0, column, TOP_BOUNDARY_CASE);
				setLengthWordInActiveCell(array, row, 0, LEFT_BOUNDARY_CASE);
				setLengthWordInActiveCell(array, row, column, HORIZONTAL_CASE);
				setLengthWordInActiveCell(array, row, column, VERTICAL_CASE);
			}
		}		
	}

	
	private int countFreeLinksInCell(Cell cell) {
		int count = 0;
		if (cell.getFirstLink().equals("") | cell.getFirstLink().equals(null)) {
			count++;
		}
		if (cell.getSecondLink().equals("") | cell.getSecondLink().equals(null)) {
			count++;
		}
		return count;
	}
	
	private int getNumberCellsWithFreeLinks(Cell[][] array, Class<? extends Cell> cellType) {
		int count = 0;
		for (int row = 0; row < array.length; row++) {
			for (int column = 0; column < array[0].length; column++) {
				if (isTypeCell(array[row][column], cellType, null) && countFreeLinksInCell(array[row][column]) > 0) {
					count++;
				}
			}
		}
		return count;
	}

	private List<Integer[]> getNeighbour(Cell[][] array, int indexR, int indexC, int countFreeLinksInCell,  
			Class<? extends Cell> typeOfCell, Class<? extends Cell> typeOfNeighbourCell) {
		List<Integer[]> list = new LinkedList<Integer[]> ();
		Position position = null;
		if (indexR == 0) {
			if (indexC == 0) {
				position = listPosition.get(LEFT_TOP_BOUNDARY_CASE);
			}
			else if (indexC > 0 & indexC < array[0].length-1) {
				position = listPosition.get(TOP_BOUNDARY_CASE);
			}
			else if (indexC == array[0].length-1) {
				position = listPosition.get(RIGHT_TOP_BOUNDARY_CASE);
			}
		}
		else if (indexR == array.length-1) {
			if (indexC == 0) {
				position = listPosition.get(LEFT_BOTTOM_BOUNDARY_CASE);
			}
			else if (indexC > 0 & indexC < array[0].length-1) {
				position = listPosition.get(BOTTOM_BOUNDARY_CASE);
			} 
			else if (indexC == array[0].length-1) {
				position = listPosition.get(RIGHT_BOTTOM_BOUNDARY_CASE);
			}
		}
		else if (indexR > 0 & indexR < array.length-1) {
			if (indexC == 0) {
				position = listPosition.get(LEFT_BOUNDARY_CASE);
			}
			else if (indexC > 0 & indexC < array[0].length-1) {
				position = listPosition.get(CENTRAL_CASE);
			}
			else if (indexC == array[0].length-1) {
				position = listPosition.get(RIGHT_BOUNDARY_CASE);
			}
		}
		
		if (isTypeCell(array[indexR][indexC],typeOfCell,null)) {
			for(int index = 0; index < position.posNumber.length; index++) {
				if(isTypeCell(array[indexR+position.getPositionX(index)][indexC+position.getPositionY(index)],typeOfNeighbourCell,null)
						&& countFreeLinksInCell(array[indexR+position.getPositionX(index)][indexC+position.getPositionY(index)])==countFreeLinksInCell) {
					Integer[] values = new Integer[4];
					if (isTypeCell(array[indexR][indexC],CommentCell.class,null)) {
						values[3] = COMMENT_CELL_TYPE;
					}
					else if (isTypeCell(array[indexR][indexC],ActiveCell.class,null)) {
						values[3] = ACTIVE_CELL_TYPE;
					}
					values[0] = position.getPositionNumber(index); values[1] = position.getPositionX(index); values[2] = position.getPositionY(index);
					list.add(values);
				}
			}
		}
		return list;
	}
	private int getDirectionInActiveCell(Cell cell) {
		if (isTypeCell(cell, ActiveCell.class, null)) {
			if (((ActiveCell)cell).getHDirectionWordLength()>0 
					& ((ActiveCell)cell).getVDirectionWordLength()==0) {
				return 0;
			}
			else if (((ActiveCell)cell).getHDirectionWordLength()==0 
					& ((ActiveCell)cell).getVDirectionWordLength()>0) {
				return 1;
			}
			else if (((ActiveCell)cell).getHDirectionWordLength()==0 
					& ((ActiveCell)cell).getVDirectionWordLength()==0) {
				return 2;
			}
		}
		return -1;
	}
	private void setLink(Cell mainCell, Cell secondCell, String mainLink, String secondLink){
		if (secondCell.getFirstLink().equals(secondLink) | secondCell.getSecondLink().equals(secondLink)
				& (mainCell.getFirstLink().equals(mainLink) | mainCell.getSecondLink().equals(mainLink))) {
			return;
		}
		if(secondCell.getFirstLink().equals("")) {
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
	private void setLinks(Cell fCell, Cell sCell, int index, int type) {
		if (type == COMMENT_CELL_TYPE) {
			switch (index) {
			case 1:
				setLink(fCell, sCell, "1.1.1", "5.1");
				setLink(fCell, sCell, "0.0.0", "0.0");
				break;
			case 2:
				setLink(fCell, sCell, "2.2.1", "6.1");
				setLink(fCell, sCell, "0.0.0", "0.0");
				break;
			case 3:
				setLink(fCell, sCell, "3.3.3", "7.3");
				setLink(fCell, sCell, "0.0.0", "0.0");
				break;
			case 6:
				if (getDirectionInActiveCell(sCell) == 1) {
					setLink(fCell, sCell, "6.6.3","2.3");
					setLink(fCell, sCell, "0.0.0","0.0");
				}
				else if (getDirectionInActiveCell(sCell) == 0) {
					setLink(fCell, sCell, "6.6.1","2.1");
					setLink(fCell, sCell, "0.0.0","0.0");
				}
				break;
			case 8:
				if (getDirectionInActiveCell(sCell) == 1) {
					setLink(fCell, sCell, "8.8.3","4.3");
					setLink(fCell, sCell, "0.0.0","0.0");
				}
				else if (getDirectionInActiveCell(sCell) == 0) {
					setLink(fCell, sCell, "8.8.1","4.1");
					setLink(fCell, sCell, "0.0.0","0.0");
				}
				break;
			default:
				break;
			}
		}
		else if (type == ACTIVE_CELL_TYPE) {
			switch (index) {
			case 7:
				setLink(fCell, sCell, "3.3.3", "7.3");
				setLink(fCell, sCell, "", "0.0");
				break;
			case 2:
				if (getDirectionInActiveCell(sCell) == 1) {
					setLink(fCell, sCell, "6.6.3","2.3");
				}
				else if (getDirectionInActiveCell(sCell) == 0) {
					setLink(fCell, sCell, "6.6.1","2.1");
				}
				break;
			case 5:
				if (getDirectionInActiveCell(sCell) == 0) {
					setLink(fCell, sCell, "1.1.1", "5.1");
					setLink(fCell, sCell, "", "0.0");
				}
				else if (getDirectionInActiveCell(sCell) == 1) {
					setLink(fCell, sCell, "1.1.3","5.3");
					setLink(fCell, sCell, "", "0.0");
				} 
				break;
			case 6:
				setLink(fCell, sCell, "2.2.1", "6.1");
				break;
			default:
				break;
			}
		}
	}
	private void getNeighbours(Cell[][] array, int indexR, int indexC, int countFreeLinksInCell,
			Class<? extends Cell> typeOfCell, Class<? extends Cell> typeOfNeighbourCell) {
		List<Integer[]> list = new LinkedList<Integer[]> ();
		if (isTypeCell(array[indexR][indexC], CommentCell.class, null)
				& countFreeLinksInCell(array[indexR][indexC]) == countFreeLinksInCell) {
			list = getNeighbour(array, indexR, indexC, TWO_FREE_LINKS_IN_CELL, CommentCell.class, ActiveCell.class);

			if (list.size() == 1) {
				setLinks(array[indexR][indexC], array[indexR+list.get(0)[1]][indexC+list.get(0)[2]], list.get(0)[0],list.get(0)[3]);
			}
		}
		else if (isTypeCell(array[indexR][indexC], ActiveCell.class, null)
					& countFreeLinksInCell(array[indexR][indexC])==countFreeLinksInCell) {
				list = getNeighbour(array, indexR, indexC, TWO_FREE_LINKS_IN_CELL, ActiveCell.class, CommentCell.class);
			}
			
		if (list.size() == 1) {
			setLinks(array[indexR+list.get(0)[1]][indexC+list.get(0)[2]], array[indexR][indexC], list.get(0)[0], list.get(0)[3]);
		}
	}
	@Override
	public void defineArrowsInActiveCells(Cell[][] array) {
		int count = 0;
		int newCountFreeCommentCells = 0;
		int newCountFreeActiveCells = 0;
		int oldCountFreeActiveCells = 0;
		int oldCountFreeCommentCells = 0;
		do {
			for(int row = 0; row < array.length; row++) {
				for(int column = 0; column < array[0].length; column++) {
					getNeighbours(array, row, column, TWO_FREE_LINKS_IN_CELL, CommentCell.class, ActiveCell.class);
					if (isTypeCell(array[row][column],CommentCell.class,null) 
							&& (getNeighbour(array, row, column, TWO_FREE_LINKS_IN_CELL, CommentCell.class, ActiveCell.class).size()==0
							|| getNeighbour(array, row, column, ONE_FREE_LINK_IN_CELL, CommentCell.class, ActiveCell.class).size()==0)) {
						if(!array[row][column].getFirstLink().equals("") && array[row][column].getSecondLink().equals("")
								|!array[row][column].getFirstLink().equals("") && array[row][column].getSecondLink().equals(null)) {
							array[row][column].setSecondLink("0.0.0");
						}
						else if (!array[row][column].getSecondLink().equals("") && array[row][column].getFirstLink().equals("")
								|!array[row][column].getSecondLink().equals("") && array[row][column].getFirstLink().equals(null)) {
							array[row][column].setFirstLink("0.0.0");
						}
					}
					if (isTypeCell(array[row][column],ActiveCell.class,null) 
							&& (getNeighbour(array, row, column, TWO_FREE_LINKS_IN_CELL, ActiveCell.class, CommentCell.class).size()==0
							|| getNeighbour(array, row, column, ONE_FREE_LINK_IN_CELL, ActiveCell.class, CommentCell.class).size()==0)) {
						if(!array[row][column].getFirstLink().equals("") && array[row][column].getSecondLink().equals("")
								|!array[row][column].getFirstLink().equals("") && array[row][column].getSecondLink().equals(null)) {
							array[row][column].setSecondLink("0.0");
						}
						else if (!array[row][column].getSecondLink().equals("") && array[row][column].getFirstLink().equals("")
								|!array[row][column].getSecondLink().equals("") && array[row][column].getFirstLink().equals(null)) {
							array[row][column].setFirstLink("0.0");
						}
					}
				}
			}
			oldCountFreeActiveCells = newCountFreeActiveCells;
			oldCountFreeCommentCells = newCountFreeCommentCells;
			newCountFreeCommentCells = getNumberCellsWithFreeLinks(array, CommentCell.class);
			newCountFreeActiveCells = getNumberCellsWithFreeLinks(array, ActiveCell.class);
			count++;
		} while((newCountFreeActiveCells != oldCountFreeActiveCells) && (newCountFreeCommentCells != oldCountFreeCommentCells));
		
		System.out.print("Count: "+count);
		System.out.print(" Count free links in Comment cells: "+ getNumberCellsWithFreeLinks(array, CommentCell.class));
		System.out.println("  Count free links in Active cells: "+ getNumberCellsWithFreeLinks(array, ActiveCell.class));
	}

	@Override
	public List<String> getWordsList(Cell[][] array) {
		return null;
	}

	@Override
	public void setPatterns(List<Cell[][]> list) {
		
	}
}


class Position {
	int[] posX;
	int[] posY;
	int[] posNumber;
	int count;
	
	Position(int count, int ... values) {
		this.count = count;
		posX = new int[count];
		posY = new int[count];
		posNumber = new int[count];
		for (int index = 0; index < count; index++) {
			posNumber[index] = values[index*3];
			posX[index] = values[index*3+1];
			posY[index] = values[index*3+2];
		}
	}

	public void setValues(int index, int pos, int x, int y) {
		posNumber[index] = pos;
		posX[index] = x;
		posY[index] = y;
	}
	
	public int getLength() {
		return count;
	}
	
	public int getPositionX(int index) {
		return posX[index];
	}
	
	public int getPositionY(int index) {
		return posY[index];
	}
	
	public int getPositionNumber(int index) {
		return posNumber[index];
	}
}