package block.scanword.api;

import java.util.*;
import block.cell.*;

public interface Scanword {
	public String getName();
	public String getCreationDate();
	public String getFinishDate();
	public int getRowNumber();
	public int getColumnNumber();
	public boolean setArray(Cell[][] array);
	public Cell[][] getArray();
	public boolean setCell(Cell cell, int row, int column);
	public Cell getCell(int row, int column);
	public void setActiveCellsGenerator(ArrowsAndWordsListGenerator generator);
	public ArrowsAndWordsListGenerator getGenerator();
	public void executeGenerator();
	public List<String> getWordsList();
}
