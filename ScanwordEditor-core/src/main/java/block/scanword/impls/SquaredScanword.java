package block.scanword.impls;

import java.util.*;
import block.cell.*;
import block.scanword.api.*;

public class SquaredScanword implements Scanword {
	private String name;
	private String creationDate;
	private String finishDate;
	private int rowNumber;
	private int columnNumber;
	private Cell[][] array;
	private ArrowsAndWordsListGenerator generator;
	
	public SquaredScanword(String name, int rowNumber, int columnNumber) {
		this.name = name;
		this.rowNumber = rowNumber;
		this.columnNumber = columnNumber;
		setCreationOrFinishDate(creationDate);
		setCreationOrFinishDate(finishDate);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getCreationDate() {
		return creationDate;
	}

	@Override
	public String getFinishDate() {
		return finishDate;
	}

	private void setCreationOrFinishDate(String date) {
		try{
			date = Date.class.newInstance().toString().substring(0, 10);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int getRowNumber() {
		return rowNumber;
	}

	@Override
	public int getColumnNumber() {
		return columnNumber;
	}

	@Override
	public boolean setArray(Cell[][] array) {
		if(rowNumber == array.length && columnNumber == array[0].length) {
			this.array = array;
			setCreationOrFinishDate(finishDate);
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public Cell[][] getArray() {
		return array;
	}

	@Override
	public boolean setCell(Cell cell, int row, int column) {
		if(array != null){
			if ((row >= 0 && row < rowNumber) 
					&& (column >= 0 && column < columnNumber)) {
				if(array[row][column] == null) {
					array[row][column] = cell;
				} else {
					if(!array[row][column].equals(cell)) {
						array[row][column] = cell;
					}
				}
			}
			setCreationOrFinishDate(finishDate);
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public Cell getCell(int row, int column) {
		if(array != null){
			if ((row >= 0 && row < rowNumber) 
					&& (column >= 0 && column < columnNumber)) {
						if(array[row][column] == null) {
							return null;
						}
						else {
							return array[row][column];
						}
			}
		}
		return null;
	}

	@Override
	public void setActiveCellsGenerator(ArrowsAndWordsListGenerator generator) {
		this.generator = generator;
	}

	@Override
	public void executeGenerator() {
		if(generator != null) {
			generator.defineActiveCells(array);
			generator.defineArrowsInActiveCells(array);
			setCreationOrFinishDate(finishDate);
		}
	}

	@Override
	public List<String> getWordsList() {
		if (generator != null) {
			return generator.getWordsList(array);
		}
		else {
			return null;
		}
	}
}