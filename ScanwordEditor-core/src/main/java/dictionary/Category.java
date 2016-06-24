package dictionary;

public class Category {
	private String value;
	private String description;
	
	public Category(String value, String desc) {
		this.value = value;
		this.description = desc;
	}

	public String getValue() {
		return value;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String value) {
		this.description = value;
	}
}