package block.cell;

import java.util.regex.*;

import org.apache.logging.log4j.*;


public class TotalCell implements Cell {

	protected String firstLink;
	protected String secondLink;
	protected String regex = "[à-ÿÀ-ß¸¨]";
	protected String letter;
	
	private static Logger logger = LogManager.getLogger(TotalCell.class);
	
	public TotalCell() {
		letter = "";
		firstLink = "";
		secondLink = "";
	}
	
	public TotalCell(String letter) {
		this();
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(letter);
		if(m.matches()) {
			this.letter = letter;	
			logger.debug("Create cell with letter {}", letter);
		}
	}
	
	@Override
	public boolean setLetter(String letter) {
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(letter);
		if(m.matches()) {
			this.letter = letter;
			logger.debug("Old value in cell was change by new value equals {}", letter);
			return true;
		}
		logger.debug("Old value in cell wasn't change");
		return false;
	}
	
	@Override
	public String getLetter() {
		return letter;
	}
	
	@Override
	public boolean setFirstLink(String link) {
		firstLink = link;
		logger.debug("Value first link is {}", link);
		return (firstLink.equals(link))?true:false;
	}
	
	@Override
	public String getFirstLink() {
		return this.firstLink;
	}

	@Override
	public boolean setSecondLink(String link) {
		secondLink = link;
		logger.debug("Value second link is {}", link);
		return (this.secondLink.equals(link)) ? true: false;
	}
	
	@Override
	public String getSecondLink() {
		return this.secondLink;
	}
}