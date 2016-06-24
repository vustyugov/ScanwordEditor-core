package block.cell;

public interface Cell {
	public boolean setLetter(String letter);
	public String getLetter();
	public boolean setFirstLink(String link);
	public String getFirstLink();
	public boolean setSecondLink(String link);
	public String getSecondLink();
}
