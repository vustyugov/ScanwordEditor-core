package dictionary;

import java.util.*;

public class Word {

	private String value;
	private Map<DictionaryType, String> description;
	private List<Category> categories;

	public Word(String value, Map<DictionaryType, String> desc) {
		this.value = value;
		if(desc == null) {
			description = new HashMap<DictionaryType, String>();
		} else {
			description = desc;
		}
		categories = new LinkedList<Category>();
	}
	
	public String getValue() {
		return value;
	}
	
	public String getDescription(DictionaryType key) {
		return description.get(key);
	}
	
	public boolean containsDescription(String desc) {
		for(String element: description.values()) {
			if(element.contains(desc)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	public void setDescription(DictionaryType key, String value) {
		this.description.put(key, value);
	}
	
	public List<Category> getCategories() {
		return categories;
	}
	
	public boolean containCategory(String categoryValue) {
		for(Category element: categories) {
			if(element.getValue().equals(categoryValue)) {
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}
	
	public void setCategory(int index, Category categ) {
		if(index < categories.size() & index >= 0) {
			categories.set(index, categ);
		}
	}
		
	public void addCategory(Category categ) {
		if(!categories.contains(categ)) {
			categories.add(categ);
		}
	}

}