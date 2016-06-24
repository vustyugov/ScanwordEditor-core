package block.cell;

import java.util.regex.*;

public class CommentCell extends TotalCell {
	private String regex = "[0-8]{1}.[0-8]{1}.[0-8]{1}";
	
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
			return true;
		}
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
			return true;
		}
		return false;
	}
	
	@Override
	public boolean equals (Object obj) {
		if(obj instanceof CommentCell) {
			return (((CommentCell) obj).firstLink.equals(this.firstLink)
					&& ((CommentCell) obj).secondLink.equals(this.secondLink)) ? true: false;
		}
		else {
			return false;
		}
	}
	
	@Override
	public String toString () {
		StringBuilder buf = new StringBuilder("[CC - ");
		buf.append((firstLink.equals(""))?"     ":firstLink);
		buf.append(",");
		buf.append((secondLink.equals(""))?"     ":secondLink);
		buf.append("]");
		return buf.toString();
	}
}