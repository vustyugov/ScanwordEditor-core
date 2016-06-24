package block.cell;

import java.util.regex.*;

public class ActiveCell extends TotalCell{
	private String vVirtualWord;
	private int vDirectionWordLength;
	private String hVirtualWord;
	private int hDirectionWordLength;
	
	public ActiveCell() {
		super();
		this.vVirtualWord = "";
		this.vDirectionWordLength = 0;
		this.hVirtualWord = "";
		this.hDirectionWordLength = 0;
	}
	
	public ActiveCell(String letter) {
		super(letter);
		this.vVirtualWord = "";
		this.vDirectionWordLength = 0;
		this.hVirtualWord = "";
		this.hDirectionWordLength = 0;
	}
		
	@Override
	public String getFirstLink() {
		return super.firstLink;
	}
	
	@Override
	public boolean setFirstLink(String link) {
		Pattern pattern = Pattern.compile("[0-8]{1}.[0,1,3]{1}");
		Matcher m = pattern.matcher(link);
		if(m.matches()) {
			super.firstLink = link;
			return true;
		}
		return false;
	}
	
	@Override
	public String getSecondLink() {
		return secondLink;
	}
	
	@Override
	public boolean setSecondLink(String link) {
		Pattern pattern = Pattern.compile("[0-8]{1}.[0,1,3]{1}");
		Matcher m = pattern.matcher(link);
		if(m.matches()) {
			super.secondLink = link;
			return true;
		}
		return false;
	}
	
	public String getVVirtualWord() {
		return vVirtualWord;
	}
	
	public void setVVirtualWord(String word) {
		this.vVirtualWord = word;
	}
	
	public String getHVirtualWord() {
		return hVirtualWord;
	}
	
	public void setHVirtualWord(String word) {
		this.hVirtualWord = word;
	}
	
	public boolean setVDirectioinWordLength(int length) {
		if(length < 50 && length > 0) {
			this.vDirectionWordLength = length;
			return true;
		}
		return false;
	}
	
	public int getVDirectionWordLength() {
		return this.vDirectionWordLength;
	}
	
	public boolean setHDirectioinWordLength(int length) {
		if(length < 50 && length > 0) {
			this.hDirectionWordLength = length;
			return true;
		}
		return false;
	}
	
	public int getHDirectionWordLength() {
		return this.hDirectionWordLength;
	}
	
	@Override
	public boolean equals (Object obj) {
		if(obj instanceof ActiveCell) {
			return (((ActiveCell) obj).letter.equals(this.letter) &&
					((ActiveCell)obj).hDirectionWordLength == this.hDirectionWordLength &&
						((ActiveCell)obj).vDirectionWordLength == this.vDirectionWordLength)? true: false;
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder("[AC - ");
		buf.append (" ");
		buf.append ((firstLink.equals("")) ?"   ":firstLink);
		buf.append (" , ");
		buf.append ((secondLink.equals("")) ?"   ": secondLink);
		buf.append(" ]");
		return buf.toString();
	}
}