package textparser;

import java.util.Stack;
import java.util.Vector;

public class Parser {
	private Stack<Glyph> chainStack;
	private Vector<Glyph>words;
	private Glyph  wordRoot;
	private String currentString;
	private String currentPosition;
	private boolean isInLigature;
	private boolean isInPosition;

	/**
	 * Parses a String of the new notation
	 * @param str
	 * @throws ParseException 
	 */
	public Glyph parse(String str) throws ParseException {		
		if(str == null) {
			return null;
		}

		this.currentString 	= "";
		this.currentPosition= "";
		this.isInLigature 	= false;
		this.isInPosition 	= false;
		this.chainStack 	= new Stack<>();
		this.words			= new Vector<>();

		String input = str.trim();

		for(int i=0;i<input.length();i++) {
			char currentChar = input.charAt(i);
			parseChar(currentChar);			
		}
		endWord();

		for(Glyph g:this.words) {
			System.out.println("Word: ");
			g.debugGlyph(0);
		}

		Glyph in = new Glyph("");
		return in;
	}

	private void parseChar(char chr) throws ParseException {
		if(chr==' ' && !this.isInLigature && !this.isInPosition) {
			endWord();
		} else if(chr=='-' && this.isInPosition) {
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

		if(this.isInLigature) {
			System.out.println("Lig: "+this.currentString);
		} else {	
			System.out.println("Cur: "+this.currentString);
		}

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
		}

		this.chainStack.push(newGlyph);		

		this.currentPosition = "";
		this.currentString   = "";
	}

	private void endWord() {
		endGlyph();
		this.words.add(this.wordRoot);
		this.wordRoot = null;
		this.chainStack.clear();
	}
}
