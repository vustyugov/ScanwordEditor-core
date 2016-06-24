package block.scanword.api;

import java.util.List;

import block.cell.Cell;

public interface ArrowsAndWordsListGenerator {
	public void defineActiveCells(Cell[][] array);
	public void defineArrowsInActiveCells(Cell[][] array);
	public List<String> getWordsList(Cell[][] array);
}
