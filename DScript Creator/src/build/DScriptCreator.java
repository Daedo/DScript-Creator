package build;

import java.util.Vector;

import scriptparser.DScriptBlock;
import scriptparser.DScriptLine;
import scriptparser.DScriptParser;

public class DScriptCreator {

	/**
	 * Takes a Line of Text an parses it into a {@link DScriptLine} Instance
	 * 
	 * @param testString The String to parse
	 * @return Returns a {@link DScriptLine} Instance
	 */
	public static DScriptLine runParser(String testString) {
		DScriptLine line = null;
		
		DScriptParser parser = new DScriptParser(testString);
		boolean isSucessfull = false;

		try {
			isSucessfull = parser.parseText();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		if(isSucessfull) {
			Vector<DScriptBlock> parsedText = parser.getParsedText();
			line = new DScriptLine();
			
			for(DScriptBlock word:parsedText) {
				//word.concatenateChains();
				line.addWord(word);
			}
		} else {
			System.err.println("Failure");
		}
		
		return line;
	}
}