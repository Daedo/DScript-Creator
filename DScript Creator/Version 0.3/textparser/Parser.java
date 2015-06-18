package textparser;

import java.util.Stack;
import java.util.Vector;

public class Parser {
	private Stack<Glyph> chainStack;
	private Vector<Word>words;
	private Glyph  wordRoot;
	private String currentString;
	private String currentPosition;
	private boolean isInLigature;
	private boolean isInPosition;
	private String word;

	/**
	 * Parses a String of the new notation
	 * @param str
	 * @throws ParseException 
	 */
	public Vector<Word> parse(String str) throws ParseException {		
		if(str == null) {
			return null;
		}

		this.currentString 	= "";
		this.currentPosition= "";
		this.isInLigature 	= false;
		this.isInPosition 	= false;
		this.chainStack 	= new Stack<>();
		this.words			= new Vector<>();
		this.word			= "";
		String input = str.trim();

		for(int i=0;i<input.length();i++) {
			char currentChar = input.charAt(i);
			this.word = this.word+currentChar;
			parseChar(currentChar);			
		}
		endWord();

		/*for(Word g:this.words) {
			System.out.println("Word: ");
			g.debugGlyph(0);
		}
		System.out.println("End\n");*/
		return this.words;
	}

	private void parseChar(char chr) throws ParseException {
		if(chr==' ' && !this.isInLigature && !this.isInPosition) {
			endWord();
		} else if(chr==';' && this.isInPosition) {
			togglePosition();
		} else {
			switch(chr) {
			case '(':
				openPosition();
				break;

			case ')':
				closePosition();
				break;

			case '[':
				openLigature();
				break;

			case ']':
				closeLigature();
				break;

			default:
				if(this.isInPosition) {
					this.currentPosition+=chr;
				} else {
					this.currentString+=chr;
					if(!this.isInLigature) {
						endGlyph();
					}
				}
				break;
			}
		}
	}

	private void openLigature() throws ParseException {
		if(this.isInLigature) {
			throw new ParseException("Parsing Error: Unmatched '['");
		}

		endGlyph();
		this.isInLigature = true;
	}

	private void closeLigature() throws ParseException {
		endGlyph();

		if(!this.isInLigature) {
			throw new ParseException("Parsing Error: Unmatched ']'");
		}
		this.isInLigature = false;
	}

	private void openPosition() throws ParseException {
		if(this.isInPosition) {
			throw new ParseException("Parsing Error: Unmatched '('");
		}
		if(!this.chainStack.isEmpty()) {
			this.chainStack.push(this.chainStack.peek());
		}
		endGlyph();
		this.isInPosition = true;
	}

	private void togglePosition() throws ParseException {
		if(!this.isInPosition) {
			throw new ParseException("Parsing Error: Missing '('");
		}
		this.isInPosition = false;
	}

	private void closePosition() throws ParseException {
		endGlyph();
		
		if(!this.chainStack.isEmpty()) {
			this.chainStack.pop();
		}
		if(this.isInPosition) {
			throw new ParseException("Parsing Error: Missing '-'");
		}
	}

	private void endGlyph() {
		if(this.currentString=="") {
			return;
		}

		Glyph newGlyph = new Glyph(this.currentString);

		/*if(this.isInLigature) {
			System.out.println("Lig: "+this.currentString);
		} else {	
			System.out.println("Cur: "+this.currentString);
		}*/

		Glyph prevGlyph = null;
		if(!this.chainStack.isEmpty()) {
			prevGlyph = this.chainStack.pop();

			if(this.currentPosition!="") {
				System.out.println("Pos: "+this.currentPosition);
				prevGlyph.addGlyph(newGlyph, this.currentPosition);
			} else {
				prevGlyph.addGlyph(newGlyph, "2,1");
			}
		} else {
			this.wordRoot = newGlyph;
			//First Glyph with transform/ Position
			if(this.currentPosition!="") {
				System.out.println("Pos Start: "+this.currentPosition);
				//Create Dummy Glyph with Connection
				Glyph dummy = new Glyph("");
				dummy.addGlyph(this.wordRoot, this.currentPosition);
				this.wordRoot = dummy;
			}
		}

		this.chainStack.push(newGlyph);		

		this.currentPosition = "";
		this.currentString   = "";
	}

	private void endWord() {
		endGlyph();
		
		Word outWord= new Word(this.word.trim(), this.wordRoot);
		//System.out.println("Added: "+this.word);
		this.word = "";
		this.words.add(outWord);
		this.wordRoot = null;
		this.chainStack.clear();
	}
}
