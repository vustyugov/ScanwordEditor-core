package block.cell;

import java.util.regex.*;

import org.apache.logging.log4j.*;

public class CommentCell extends TotalCell {
	private String regex = "[0-8]{1}.[0-8]{1}.[0-8]{1}";
	private static Logger logger = LogManager.getLogger(CommentCell.class);
	
	public CommentCell() {
		super();
	}

	@Override
	public String getFirstLink() {
		return super.firstLink;
	}
	
	@Override
	public boolean setFirstLink(String link) {
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(link);
		if(m.matches()) {
			super.firstLink = link;
			logger.debug("New first link value {} was write into cell.", link);
			return true;
		}
		logger.debug("New first link value wasn't write into cell.");
		return false;
	}

	@Override
	public String getSecondLink() {
		return super.secondLink;
	}
	
	@Override
	public boolean setSecondLink(String link) {
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(link);
		if(m.matches()) {
			super.secondLink = link;
			logger.debug("New second link value {} was write into cell.", link);
			return true;
		}
		logger.debug("New first link value wasn't write into cell.");
		return false;
	}
	
	@Override
	public boolean equals (Object obj) {
		if(obj instanceof CommentCell) {
			logger.debug("Object obj and current object is equals.");
			return (((CommentCell) obj).firstLink.equals(this.firstLink)
					&& ((CommentCell) obj).secondLink.equals(this.secondLink)) ? true: false;
		}
		else {
			logger.debug("Object obj and current object is not equals.");
			return false;
		}
	}
	
	@Override
	public String toString () {
		StringBuilder buf = new StringBuilder("[CC;");
		buf.append((letter.equals(""))?" ":letter);
		buf.append(";");
		buf.append((firstLink.equals(""))?"     ":firstLink);
		buf.append(",");
		buf.append((secondLink.equals(""))?"     ":secondLink);
		buf.append("]");
		logger.debug("[CC;{};{},{}]",letter,firstLink, secondLink);
		return buf.toString();
	}
}