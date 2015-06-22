package scriptRepräsentation;

import java.util.Vector;

public class DScriptText {
	public double width;
	public double height;
	public Vector<Wordstart> words;
	
	public DScriptText(double w,double h) {
		this.width = w;
		this.height = h;
		this.words = new Vector<>();
	}

	public void update(DScriptText newText) {
		Vector<Wordstart> newWords = newText.words;
		
		for(int i=0;i<newWords.size();i++) {
			System.out.println("New: "+newWords.get(i).wordText);
		}
		
		for(int i=0;i<this.words.size();i++) {
			System.out.println("Old: "+this.words.get(i).wordText);
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
				String oldStringFront	= this.words.get(i).wordText;
				String newStringFront	= newWords.get(i).wordText;
				
				if(oldStringFront.matches(newStringFront)) {
					System.out.println("Front: "+oldStringFront +" = "+ newStringFront);
					matchesFront++;
				} else {
					matchFront = false;
				}
			}
			
			if(matchBack) {
				String oldStringEnd		= this.words.get(oldSize-i-1).wordText;
				String newStringEnd		= newWords.get(newSize-i-1).wordText;
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
		
		Vector<Wordstart> newWordVec = new Vector<>();
		for(int i=0;i<newSize;i++) {
			Wordstart add;
			
			if(i<matchesFront) {
				//Get Front
				add = this.words.get(i);
				System.out.println("Front Add: "+add.wordText);
			} else if((newSize-i-1)<matchesBack) {
				//Get End
				add = this.words.get(oldSize-newSize+i);
				System.out.println("End Add: "+add.wordText);
			} else {
				//New
				add = newWords.get(i);
				System.out.println("New Add: "+add.wordText);
			}
			newWordVec.add(add);
		}
		
		this.words = newWordVec;
	}
	
}
