package gui;

import java.util.Vector;

import textparser.Word;

public class GUIData {
	public Vector<GUIWord> words;
	public double width;
	public double height;

	public GUIData() {
		this.words = new Vector<>();
		this.width = 0;
		this.height = 0;
	}

	public void update(Vector<Word> newWords) {
		
		for(int i=0;i<newWords.size();i++) {
			System.out.println("New: "+newWords.get(i).text);
		}
		
		for(int i=0;i<this.words.size();i++) {
			System.out.println("Old: "+this.words.get(i).word.text);
		}
		
		int matchesFront 	= 0;
		int matchesBack  	= 0;
		
		int newSize = newWords.size();
		int oldSize = this.words.size();
		int minSize	= Math.min(newSize,oldSize);
		
		System.out.println("Size: "+minSize);
		
		boolean matchFront = true;
		boolean matchBack = true;
		
		for(int i=0;i<minSize;i++) {
			if(matchFront) {
				String oldStringFront	= this.words.get(i).word.text;
				String newStringFront	= newWords.get(i).text;
				
				if(oldStringFront.matches(newStringFront)) {
					System.out.println("Front: "+oldStringFront +" = "+ newStringFront);
					matchesFront++;
				} else {
					matchFront = false;
				}
			}
			
			if(matchBack) {
				String oldStringEnd		= this.words.get(oldSize-i-1).word.text;
				String newStringEnd		= newWords.get(newSize-i-1).text;
				if(oldStringEnd.equals(newStringEnd)) {
					System.out.println("End: "+oldStringEnd+ " = "+ newStringEnd);
					matchesBack++;
				} else {
					matchBack = false;
				}
			}
			
			if((!matchBack)&&(!matchFront)) {
				break;
			}
		}
		
		Vector<GUIWord> newWordVec = new Vector<>();
		for(int i=0;i<newSize;i++) {
			GUIWord add;
			
			if(i<matchesFront) {
				//Get Front
				add = this.words.get(i);
				System.out.println("Front Add: "+add.word.text);
			} else if((newSize-i-1)<matchesBack) {
				//Get End
				add = this.words.get(oldSize-newSize+i);
				System.out.println("End Add: "+add.word.text);
			} else {
				//New
				add = new GUIWord();
				add.word = newWords.get(i);
				System.out.println("New Add: "+add.word.text);
			}
			newWordVec.add(add);
		}
		
		this.words = newWordVec;

	}
}
