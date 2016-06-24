package block.cell;

import java.util.regex.*;

public class TotalCell implements Cell {

	protected String firstLink = "";
	protected String secondLink = "";
	protected String regex = "[à-ÿÀ-ß¸¨]";
	protected String letter;
	
	public TotalCell() {
		letter = "";
	}
	
	public TotalCell(String letter) {
		this();
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(letter);
		if(m.matches()) {
			this.letter = letter;	
		}
	}
	
	@Override
	public boolean setLetter(String letter) {
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(letter);
		if(m.matches()) {
			this.letter = letter;
			return true;
		}
		return false;
	}
	
	@Override
	public String getLetter() {
		return letter;
	}
	
	@Override
	public boolean setFirstLink(String link) {
		this.firstLink = link;
		if(this.firstLink.equals(link)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public String getFirstLink() {
		return this.firstLink;
	}

	@Override
	public boolean setSecondLink(String link) {
		this.secondLink = link;
		if(this.secondLink.equals(link)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public String getSecondLink() {
		return this.secondLink;
	}
}