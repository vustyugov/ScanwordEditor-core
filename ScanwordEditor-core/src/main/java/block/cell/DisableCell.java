package block.cell;

import org.apache.logging.log4j.*;

public class DisableCell extends TotalCell {
	private static Logger logger = LogManager.getLogger(DisableCell.class);
	
	public DisableCell() {
		super();
	}

	@Override
	public boolean setLetter(String letter) {
		logger.debug("New value is {}, but can't write letter.", letter);
		return false;
	}
		
	@Override
	public String getLetter() {
		return "";
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof DisableCell) ? true : false;
	}
	
	@Override
	public String toString() {
		return "[DC -            ]";
	}
}