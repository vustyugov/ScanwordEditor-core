package block.cell;

import java.util.regex.*;

import org.apache.logging.log4j.*;

public class ActiveCell extends TotalCell{
	private String vVirtualWord;
	private int vDirectionWordLength;
	private String hVirtualWord;
	private int hDirectionWordLength;
	private static Logger logger = LogManager.getLogger(ActiveCell.class);
	
	public ActiveCell() {
		super();
		logger.entry();
		this.vVirtualWord = "";
		this.vDirectionWordLength = 0;
		this.hVirtualWord = "";
		this.hDirectionWordLength = 0;
		logger.info("ActiveCell object was create.");
		logger.exit();
	}
	
	public ActiveCell(String letter) {
		super(letter);
		logger.entry();
		this.vVirtualWord = "";
		this.vDirectionWordLength = 0;
		this.hVirtualWord = "";
		this.hDirectionWordLength = 0;
		logger.info("ActiveCell object was create with letter {}.",letter);
		logger.exit();
	}
		
	@Override
	public String getFirstLink() {
		return super.firstLink;
	}
	
	@Override
	public boolean setFirstLink(String link) {
		logger.entry();
		Pattern pattern = Pattern.compile("[0-8]{1}.[0,1,3]{1}");
		Matcher m = pattern.matcher(link);
		if(m.matches()) {
			super.firstLink = link;
			logger.debug("New first link value is {}", link);
			logger.exit();
			return true;
		}
		logger.error("First link value wasn't change.");
		logger.exit();
		return false;
	}
	
	@Override
	public String getSecondLink() {
		return secondLink;
	}
	
	@Override
	public boolean setSecondLink(String link) {
		logger.entry();
		Pattern pattern = Pattern.compile("[0-8]{1}.[0,1,3]{1}");
		Matcher m = pattern.matcher(link);
		if(m.matches()) {
			super.secondLink = link;
			logger.debug("New second link value is {}", link);
			logger.exit();
			return true;
		}
		logger.error("First link value wasn't change.");
		logger.exit();
		return false;
	}
	
	public String getVVirtualWord() {
		return vVirtualWord;
	}
	
	public void setVVirtualWord(String word) {
		logger.entry();
		this.vVirtualWord = word;
		logger.debug("Value contains vertical word is {}", word);
		logger.exit();
	}
	
	public String getHVirtualWord() {
		return hVirtualWord;
	}
	
	public void setHVirtualWord(String word) {
		logger.entry();
		this.hVirtualWord = word;
		logger.debug("Value contains horizontal word is {}", word);
		logger.exit();
	}
	
	public boolean setVDirectioinWordLength(int length) {
		if(length < 50 && length > 0) {
			this.vDirectionWordLength = length;
			logger.debug("Length vertical word is {}", length);
			return true;
		}
		logger.error("Length vertical words had error value");
		return false;
	}
	
	public int getVDirectionWordLength() {
		return this.vDirectionWordLength;
	}
	
	public boolean setHDirectioinWordLength(int length) {
		if(length < 50 && length > 0) {
			this.hDirectionWordLength = length;
			logger.debug("Length horizontal word is {}", length);
			return true;
		}
		logger.error("Length horizontal words had error value");
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