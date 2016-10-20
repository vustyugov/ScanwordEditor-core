package block.scanword.api;

import java.util.List;

import block.cell.Cell;
import block.scanword.impls.PatternsList;

public interface ArrowsAndWordsListGenerator {
	public void defineActiveCells(Cell[][] array);
	public void defineArrowsInActiveCells(Cell[][] array);
	public List<String> getWordsList(Cell[][] array);
	public void setPatterns(List<Cell[][]> list);
}
