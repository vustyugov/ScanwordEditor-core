package block.cell;

public class DisableCell extends TotalCell {
	
	public DisableCell() {
		super();
	}

	@Override
	public boolean setLetter(String letter) {
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
