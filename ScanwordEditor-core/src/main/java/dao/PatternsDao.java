package dao;

import java.io.IOException;
import java.util.List;
import block.cell.Cell;

public interface PatternsDao {
	public List<Cell[][]> read(String path) throws IOException;
}
