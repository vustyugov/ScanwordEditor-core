package block.scanword.impls;

import java.util.*;

import org.apache.logging.log4j.*;

import block.cell.*;
import block.scanword.api.*;

public class SquaredScanword implements Scanword {
	private final String name;
	private String creationDate, finishDate;
	private final int rowNumber, columnNumber;
	private Cell[][] array;
	private ArrowsAndWordsListGenerator generator;
	private static Logger logger = LogManager.getLogger(SquaredScanword.class);
	
	public SquaredScanword(String name, int rowNumber, int columnNumber) {
		logger.entry();
		logger.info("Creat new object SquaredScanword class");
		this.name = name;
		this.rowNumber = rowNumber;
		this.columnNumber = columnNumber;
		logger.info("name = {}, row number = {}, column number = {}", name, rowNumber, columnNumber);
		setCreationOrFinishDate(creationDate);
		setCreationOrFinishDate(finishDate);
		logger.info("object was creat in {}", creationDate);
		logger.exit();
	}
	
	public SquaredScanword(String name, Cell[][] array) {
		this(name, array.length, array[0].length);
		this.array = array;
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
		logger.entry();
		try{
			date = Date.class.newInstance().toString().substring(0, 10);
			logger.info("Creation date of scanword is sucessfully change {}", date);
		} catch (InstantiationException e) {
			logger.error("Creation date of scanword is not change");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			logger.error("Creation date of scanword is not change");
		}
		logger.exit();
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
		logger.entry();
		if(rowNumber == array.length && columnNumber == array[0].length) {
			this.array = array;
			setCreationOrFinishDate(finishDate);
			logger.info("Scanword array was sucessfully change.");
			logger.exit();
			return true;
		}
		else {
			logger.info("Scanword array wasn't change.");
			logger.exit();
			return false;
		}
	}

	@Override
	public Cell[][] getArray() {
		return array;
	}

	@Override
	public boolean setCell(Cell cell, int row, int column) {
		logger.entry();
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
			logger.info("Cell[{}][{}] value was change on {}", row, column, cell);
			return true;
		}
		else {
			logger.info("Cell[{}][{}] value wasn't change", row, column);
			logger.exit();
			return false;
		}
	}

	@Override
	public Cell getCell(int row, int column) {
		if(array != null){
			if ((row >= 0 && row < rowNumber) 
					&& (column >= 0 && column < columnNumber)) {
						return (array[row][column] == null) ? null: array[row][column];
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
		return (generator != null) ? generator.getWordsList(array): null;
	}

	@Override
	public ArrowsAndWordsListGenerator getGenerator() {
		return this.generator;
	}
}