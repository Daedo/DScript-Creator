package scriptparser;

import java.util.Stack;
import java.util.Vector;

public class DScriptParser {

	private boolean isReady;
	private String parseText;
	private Vector<DScriptBlock> parsedText;

	//Parsing Vars
	private boolean isEscaped;
	private String currentString;
	
	private boolean isLigature;
	private boolean isLigatureID;
	private String currentLigatureID;
	
	private int sideChainLevel;
	private boolean isPosition;
	private String currentPosition;

	private DScriptBlock wordRoot;
	private Stack<DScriptBlock> rootBlocks;
	

	/**
	 * Creates a new DScriptParser instance
	 */
	public DScriptParser() {
		this.isReady = false;
		this.parsedText = null;
	}

	/**
	 * Creates a new DScriptParser instance and initialize it with a text
	 * Same as new DScriptParser() + setText(text)
	 * 
	 * @param text The initialization Text as a String
	 */
	public DScriptParser(String text) {
		setText(text);
		this.parsedText = null;
	}

	/**
	 * Sets the Text that you want to parse.
	 * Warning Overwrites the old parsedText Vector and sets it to null!
	 * 
	 * @param text The Text to parse as a String
	 */
	public void setText(String text) {
		if(text != null) {
			this.isReady = true;
			this.parseText = text;

			this.parseText = this.parseText.replaceAll("\\s", " ");
		} else {
			this.isReady = false;
		}
		this.parsedText = null;
	}

	/**
	 * Parses the Text. Returns true if the parsing was a success
	 * 
	 * @return Returns true or false depending on success.
	 */
	public boolean parseText() throws Exception {
		if(this.isReady) {

			this.parsedText = new Vector<>();
			prepareParsing();

			int textLenght = this.parseText.length();
			for(int i = 0; i < textLenght ; i++) {
				char currentChar = this.parseText.charAt(i);

				parseChar(currentChar);
			}
			endWord();
			endParsing();
			return true;
		}
		return false;
	}

	/**
	 * Interprets the Char. Returns False if the char is invalid
	 * 
	 * @param CurrentChar The Char to interpret
	 * @return Returns False if the char is invalid
	 */
	/**
	 * Things that happens at every char:
	 * 
	 * (	EndBlock StartLigature
	 * 
	 * |	EndBlock StartLigatureID
	 * 
	 * )	SetLigatureID EndLigature EndLigatureID
	 * 
	 * {	EndBlock AddTopOfStackToStack Sidechainlevel++ 
	 * 
	 * }	PopsTop EndBlock PopsOldTop AddsEndOfOldTopToStack SidechainLevel--
	 * 
	 * [	PopsTop AddTopOfStackToStack EndBlock SwitchToPosition
	 * 
	 * ]	EndPosition
	 * 
	 * EndBlock	CreatesNewBlockWithCurrentInfos AddChainToTopAsSubChain PositionDefaultCM BecomesNewTop
	 */
	private void parseChar(char currentChar) throws Exception{

		boolean isWhitespace = currentChar == ' ';

		//End of a Word
		if(isWhitespace && this.sideChainLevel==0) {
			endWord();
		} else {

			if(this.isEscaped) {
				addChar(currentChar);
			} else {

				switch(currentChar) {
				case '(': openLigature(); break;
				case '|': toggleLigature(); break;
				case ')': closeLigature();; break;

				case '{': openSideChain();  break;
				case '}': closeSidechain(); break;
				case ' ': /*Spacebars in Side Chains will be ignored*/ break;

				case '[':
					openPosition();
					addSidechain();   		break;
				case ']': closePosition();  break;

				default:
					addChar(currentChar);
					break;
				}
			}
		}
	}


	/**
	 * Initializes the variables for parsing
	 */
	private void prepareParsing() {
		this.isEscaped = false;
		this.currentString = "";

		// (|) Ligatures
		this.isLigature = false;
		this.isLigatureID = false;
		this.currentLigatureID = "";
		
		// {} Sidechain
		this.sideChainLevel = 0;

		// [] Position of Sidechain
		this.isPosition = false;
		this.currentPosition = "";

		this.rootBlocks = new Stack<>();
		this.wordRoot = new DScriptBlock();
	}

	/**
	 * Adds the char to the right string depending on the parsing
	 * 
	 * @param charToAdd The char that will be added
	 */
	private void addChar(char charToAdd) {
		if(this.isPosition) {
			this.currentPosition += charToAdd;
		} else {
			if(this.isLigatureID) {
				this.currentLigatureID += charToAdd;
			} else {
				this.currentString += charToAdd;
			}
		}
	}

	/**
	 * Interprets the '(' char. 
	 */
	private void openLigature() throws Exception{
		if(this.isLigature) {
			throw new Exception("Parsing Error: Unmatched '('");
		}
		
		endBlock();
		this.isLigature = true;
		this.isLigatureID = false;
	}
	
	/**
	 * Interprets the '|' char
	 */
	private void toggleLigature() throws Exception {
		endBlock();
		
		if(!this.isLigature) {
			throw new Exception("Parsing Error: Missing '('");
		}
		
		if(this.isLigatureID) {
			throw new Exception("Parsing Error: Missing ')'");
		}
		this.isLigatureID = true;
	}
	
	/**
	 * Interprets the ')' char. 
	 */
	private void closeLigature() throws Exception {
		if(!this.isLigature) {
			throw new Exception("Parsing Error: Missing '('");
		}
		
		if(!this.isLigatureID) {
			throw new Exception("Parsing Error: Missing '|'");
		}
		
		DScriptBlock ligature = this.rootBlocks.peek();
		ligature.setLigatureID(this.currentLigatureID);
		
		this.currentLigatureID = "";
		this.isLigatureID = false;
		this.isLigature = false;
	}	
	
	/**
	 * Interprets the '{' char. Prepares the current root for side chains
	 */
	private void openSideChain() throws Exception{
		endBlock();
		
		if(this.isLigature) {
			throw new Exception("Parsing Error: Unmatched '('");
		}

		DScriptBlock oldRoot = this.rootBlocks.peek();
		this.rootBlocks.push(oldRoot);

		this.sideChainLevel++;
	}

	/**
	 * Interprets the '}' char. Ends the side chains pops the side chain stack
	 */
	private void closeSidechain() throws Exception{
		if(this.rootBlocks.size()>1) {
			endBlock();
			this.rootBlocks.pop();
			
			DScriptBlock oldRoot = this.rootBlocks.pop();
			DScriptBlock newRoot = oldRoot.getMainChainEnd();
			this.rootBlocks.push(newRoot);
			
			if(this.sideChainLevel > 0) {
				this.sideChainLevel--;
			} else {
				throw new Exception("Parsing Error: Unmatched '}'");
			}
		} else {
			throw new Exception("Parsing Error: Invalid Sidechain");
		}
	}

	/**
	 * Prepares Stack for next Side Chain
	 */
	private void addSidechain() throws Exception{
		if(this.rootBlocks.size()>1) {
			this.rootBlocks.pop();

			DScriptBlock oldRoot = this.rootBlocks.peek();
			this.rootBlocks.push(oldRoot);
		} else {
			throw new Exception("Parsing Error: Invalid Side Chain");
		}
	}

	/**
	 * Interprets the '[' char
	 */
	private void openPosition() throws Exception{
		if(this.sideChainLevel > 0) {
			endBlock();
			this.isPosition = true;
		} else {
			throw new Exception("Parsing Error: Invalid Side Chain");
		}
	}

	/**
	 * Interprets the ']' char
	 */
	private void closePosition() throws Exception{

		if(this.sideChainLevel > 0 && this.isPosition) {
			this.isPosition = false;
		} else {
			throw new Exception("Parsing Error: Unmatched ']'");
		}		
	}

	/**
	 * Ends the current Word and adds it to the ouput Vector
	 */
	private void endWord() {
		endBlock();
		this.parsedText.add(this.wordRoot);
		this.rootBlocks.clear();
	}

	/**
	 * Ends the current block and adds it to the root
	 * this new block becomes the new root
	 */
	private void endBlock() {
		if(this.currentString == "") {
			return;
		}

		boolean isEmptyPosition = this.currentPosition == "";
		if(isEmptyPosition) {
			this.currentPosition = "cm";
		}

		DScriptBlock newBlock = new DScriptBlock();
		newBlock.setText(this.currentString);

		if(!this.rootBlocks.isEmpty()) {
			DScriptBlock currentRoot = this.rootBlocks.pop();

			currentRoot.addChain(this.currentPosition, newBlock);
		} else {
			this.wordRoot = newBlock;
		}
		
		this.rootBlocks.push(newBlock);
		this.currentPosition = "";
		this.currentString = "";
	}

	/**
	 * Checks if the parsing went well.
	 * 
	 * @throws Exception Throws a Exception if there is a parsing Error
	 */
	private void endParsing() throws Exception {
		if(this.isLigatureID || this.isLigature) {
			throw new Exception("Parsing Error: Unmatched '('");
		}
		
		if(this.isPosition) {
			throw new Exception("Parsing Error: Unmatched '['");
		}
		
		if(this.sideChainLevel>0) {
			throw new Exception("Parsing Error: Unmatched '{'");
		}
	}
	
	/**
	 * @return Returns the parsed Text as a Vector of DScriptBlocks
	 */
	public Vector<DScriptBlock> getParsedText() {
		return this.parsedText; 
	}
	
	/*
	 * Testcases that may cause Problems
	 * 
	 * 1{[l]2 [cm] 3}
	 * 
	 * 1{[l]2 [cm] 34}5
	 * 
	 * 1{ [l]2 {[r]3 [cm]4} [cm]5}6
	 * 
	 */
	
	/*
	 * Example of a Parsing run (Visualization of the Stack while reading)
	 * 1{ [l]2 {[r]3 [cm]4} [cm]5}6
	 * 
	 * Char:	Stack From bottom to top:
	 * 1		-
	 * {		1		->	1,1			End Block 1; Add Duplicate of Top to Top
	 * [		1		->	1,1			Pop Top; Add Duplicate of Top to Top
	 * l								No change (Reading)
	 * ]								No change (Changes Reading Position)
	 * 2
	 *  
	 * {		1,2		->	1,2,2		End Block; Add Duplicate of Top to Top
	 * [		1,2		->	1,2,2		Pop Top; Add Duplicate of Top to Top
	 * r								No change (Reading)
	 * ]								No change (Changes Reading Position)
	 * 4								No change (Reading)
	 * }		1,2,4	->	1,1			EndBlock; Pop Top;  Pops Old Top, Adds End Of Old Top To Stack
	 *  
	 * [		1		->	1,1			Pop Top; Add Duplicate of Top to Top
	 * c								No change (Reading)
	 * m								No change (Reading)
	 * ]								No change (Changes Reading Position)
	 * 5								No change (Reading)
	 * }		1,5		->	5			EndBlock; Pop Top;  Pops Old Top, Adds End Of Old Top To Stack
	 * 6								No change (Reading)
	 * 			6						End Block; (Changes Normscript Level)
	 * 									END WORD
	 * 
	 * Output
	 * 1 -[l ]- 2	  -[r ]- 3
	 * 				  -[cm]- 4
	 * 	 -[cm]- 5 	  -[cm]- 6
	 */
	
}
