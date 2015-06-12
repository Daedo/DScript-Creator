package scriptparser;

import java.util.Vector;

public class DScriptLine {
	private Vector<DScriptBlock> words;
	
	public DScriptLine() {
		this.words = new Vector<>();
	}
	
	public void addWord(DScriptBlock word) {
		if(word!=null) {
			this.words.add(word);
		}
	}
	
	public void addWords(Vector<DScriptBlock> newWords) {
		if(newWords != null) {
			this.words.addAll(newWords);
		}
	}
	
	public int getWordcount() {
		return this.words.size();
	}
	
	public DScriptBlock getWord(int index) {
		return this.words.get(index);
	}
	
	public void debugLine() {
		System.out.println("DScript Line:");
		System.out.println("Wordcount: "+getWordcount());
		
		for(DScriptBlock word:this.words) {
			System.out.println("Word: ");
			word.debugBlocks(0);
			System.out.println("");
		}
	}
	

	public void buildLine() {
		for(DScriptBlock word:this.words) {
			//build Word isStart, inDirection
			//word.buildBlock("c", true);
		}
	}
}
