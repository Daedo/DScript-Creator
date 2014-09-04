package build;

import java.util.Scanner;
import java.util.Vector;

import scriptparser.DScriptBlock;
import scriptparser.DScriptParser;
import scriptparser.DScriptTreePrinter;

public class DScriptCreator {

	//TODO Remove old Code
	/*public static void main(String[] args) {
		//String testString = "D{[r]S [cm] C}<RIPT> Wort<Test test2>";
		//String testString = "1<2> 3{[r] 4 [cm]5}6 <7<8{[l] 9 [cm] 10}>11>";

		boolean isFinished = false;
		String testString = ""; 

		System.out.println("Input your DScript Code. Type END to quit: ");


		try (Scanner sc = new Scanner(System.in)) {
			while(!isFinished) {

				if(sc.hasNext()) {
					testString = sc.nextLine();
					testString = testString.trim();
					testString = testString.replaceAll("\\n", "");
					
					if(!"END".equals(testString)) {
						//System.out.println("!"+testString);
						if(testString != "") {
							runParser(testString);
							System.out.println("");
							System.out.println("Input your DScript Code. Type END to quit: ");
						}
					} else {
						System.out.println("QUIT");
						isFinished = true;
					}
				}
			}
			sc.close();
		}
		catch(Exception e) {
			System.out.println("Error: "+e.getMessage());
		}

	}*/

	public static void runParser(String testString) {
		DScriptParser parser = new DScriptParser(testString);
		boolean isSucessfull = false;

		try {
			isSucessfull = parser.parseText();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		if(isSucessfull) {
			System.out.println("Sucess");
			System.out.println("");
			Vector<DScriptBlock> parsedText = parser.getParsedText();

			for(DScriptBlock word:parsedText) {
				System.out.println("Word:");
				word.debugBlocks(0);
				System.out.println("");
				
				word.concatenateChains();
				System.out.println("Word Concatenated:");
				word.debugBlocks(0);
				System.out.println("");
				
				/*DScriptTreePrinter treePrinter = new DScriptTreePrinter(word);
				treePrinter.printTree();
				System.out.println("");*/
			}

		} else {
			System.out.println("Failure");
		}

	}
}