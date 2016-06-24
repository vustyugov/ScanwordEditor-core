package block;

import java.util.*;

import block.scanword.api.*;

public class Block {

	private String name;
	private String creationDate;
	private String finishDate;
	private List<Scanword> scanwords;
	private Map<String, List<String>> wordsList;
	
	public Block(String name, int countOfScanwords) {
		this.name = name;
		scanwords = new ArrayList<Scanword>(countOfScanwords);
		try{
			creationDate = Date.class.newInstance().toString().substring(0, 10);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public String getName() {
		return name;
	}
	
	public String getCreationDate() {
		return creationDate;
	}
	
	public String getFinishDate() {
		return finishDate;
	}
	
	public List<Scanword> getScanwords() {
		return scanwords;
	}
	
	public boolean setScanwords (List<Scanword> list) {
		if(list.size() <= scanwords.size()) {
			scanwords.addAll(list);
			return true;
		}
		else {
			return false;
		}
	}
	
	public Scanword getScanword(int index) {
		if(index < scanwords.size() && index > 0) {
			return scanwords.get(index);
		}
		else {
			return null;
		}
	}

	public boolean setScanword(Scanword scanword, int index) {
		if (index >=0 && index < scanwords.size()) {
			scanwords.add(index, scanword);
			return true;
		}
		else {
			return false;
		}
	}
	
	public Map<String, List<String>> getWordsList() {
		wordsList = new HashMap<String, List<String>>();
		for(Scanword scanword: scanwords) {
			if(scanword != null) {
				wordsList.put(scanword.getName(), scanword.getWordsList());
			}
		}
		return wordsList;
	}
}