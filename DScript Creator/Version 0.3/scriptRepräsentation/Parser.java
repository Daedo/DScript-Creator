package scriptRepräsentation;


import java.util.Stack;


public class Parser {
	private static final int STANDARD_EXIT 	= 2;
	private static final int STANDARD_ENTRY = 1;
	
	private DScriptText text;
	private Stack<Connection> connections;
	private boolean isInConnection;
	private boolean isInLigature;
	private String currentParseText;
	private int entryID;
	private int parseIndex;
	private String wordText;
	private Glyph lastGlyph;

	public DScriptText parseDScript(String inputText) throws ParseException {
		this.text 				= new DScriptText(0, 0);
		this.connections 		= new Stack<>();
		this.isInConnection 	= false;
		this.isInLigature 		= false;
		this.currentParseText 	= "";
		this.entryID			= 1;
		this.wordText			= "";

		String inp = inputText.replaceAll("\\s+", " ");
		inp = inp.trim();
		
		for(this.parseIndex=0;this.parseIndex<inp.length();this.parseIndex++) {
			char chr = inp.charAt(this.parseIndex);
			this.wordText+=chr;
			try {
				parseChar(chr);
			} catch(Exception e) {
				throw new ParseException("Parse Error at: "+this.parseIndex, e);
			}
		}
		endWord();
		return this.text;
	}

	private void parseChar(char chr) throws ParseException {
		//TODO Remove linefeeds
		boolean addToConnection = this.isInConnection && chr!=']';
		boolean addToLigature 	= this.isInLigature && chr!=')';

		if(addToConnection || addToLigature) {
			this.currentParseText+=chr;
		} else {
			switch(chr) {
			case '(':
				startLigature();
				break;
			case ')':
				endLigature();
				break;
			case '{':
				startSideChains();
				break;
			case '}':
				endSidechains();
				break;
			case '[':
				startConnection();
				break;
			case ']':
				endConnection();
				break;
			case ' ':
				endWord();
				break;
			default:
				this.currentParseText = ""+chr;
				addGlyph();
			}
		}


	}

	private void startWord() {
		Wordstart start = new Wordstart(STANDARD_EXIT);
		this.connections.push(start);
		this.text.words.add(start);
	}

	private void endWord() {
		//Remove last Connection of the word, that is not used
		this.lastGlyph.connections.remove(this.connections.peek());
		this.connections.clear();
		this.text.words.lastElement().wordText = this.wordText.trim();
		this.wordText = "";
	}

	private void startConnection() {
		this.isInConnection = true;
	}

	private void endConnection() throws ParseException {
		this.isInConnection = false;
		//Entry,Exit((,CarryTransform  or -)(,NonCarryTransform)?)?
		String[] connectionDetails = this.currentParseText.split(",");
		if(connectionDetails.length>=2) {
			int entry	= Integer.parseInt(connectionDetails[0]);
			int exit 	= Integer.parseInt(connectionDetails[1]);

			this.entryID = entry;
			this.connections.peek().exitID = exit;

			//TODO parse Transforms
		} else {
			throw new ParseException("Parse Error with Connection at Position "+this.parseIndex);
		}

	}

	private void startLigature() {
		this.isInLigature = true;
	}

	private void endLigature() {
		this.isInLigature = false;
		addGlyph();
	}

	private void endSidechains() {
		this.connections.pop();
	}

	private void startSideChains() {
		this.connections.push(this.connections.peek());
	}

	private void addGlyph() {
		if(this.connections.isEmpty()) {
			startWord();
		}
		Connection add = this.connections.pop();
		Glyph newGlyph = new Glyph(this.entryID, this.currentParseText);
		add.end = newGlyph;
		Connection newConnection = new Connection(STANDARD_EXIT);
		newGlyph.connections.add(newConnection);
		this.connections.push(newConnection);

		this.entryID = STANDARD_ENTRY;
		this.currentParseText = "";
		this.lastGlyph = newGlyph;
	}
}
