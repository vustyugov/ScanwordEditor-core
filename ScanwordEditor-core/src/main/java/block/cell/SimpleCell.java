package block.cell;

import org.apache.logging.log4j.*;

public class SimpleCell extends TotalCell{
	private static Logger logger = LogManager.getLogger(SimpleCell.class);
	public SimpleCell() {
		super();
	}
	
	public SimpleCell(String letter) {
		super(letter);
		logger.debug("Create cell with letter {}", letter);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SimpleCell) {
			logger.debug("two cells {} {} is equals", this.getClass(), obj.getClass());
			return (((SimpleCell)obj).letter == this.letter) ? true: false;
		}
		else {
			logger.debug("two cells isn't equals.");
			return false;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder("[SC;");
		buf.append((letter.equals(""))?" ":letter);
		buf.append(";           ]");
		return buf.toString();
	}
}