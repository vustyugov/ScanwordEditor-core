package block.cell;

public class SimpleCell extends TotalCell{
	
	public SimpleCell() {
		super();
	}
	
	public SimpleCell(String letter) {
		super(letter);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SimpleCell) {
			if (((SimpleCell)obj).letter == this.letter) {
				return true;
			} else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "[SC -            ]";
	}
}