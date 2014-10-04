package build;

import java.util.Vector;

import scriptparser.DScriptBlock;
import scriptparser.DScriptParser;

public class DScriptCreator {

	public static void runParser(String testString) {
		DScriptParser parser = new DScriptParser(testString);
		boolean isSucessfull = false;

		try {
			isSucessfull = parser.parseText();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		if(isSucessfull) {
			System.out.println("Sucess");
			System.out.println("");
			Vector<DScriptBlock> parsedText = parser.getParsedText();

			for(DScriptBlock word:parsedText) {
				System.out.println("Word:");
				word.debugBlocks(0);
				System.out.println("----------");
				System.out.println("Recode:");
				System.out.println(word.getCodeText());
				System.out.println("");
				
				word.concatenateChains();
				System.out.println("Word Concatenated:");
				word.debugBlocks(0);
				System.out.println("");
			}

		} else {
			System.out.println("Failure");
		}

	}
}