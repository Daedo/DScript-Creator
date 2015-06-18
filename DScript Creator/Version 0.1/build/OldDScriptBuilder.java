package build;

import scriptparser.DScriptBlock;

@Deprecated
public class OldDScriptBuilder {
	
	public static DScriptVector buildWord(DScriptBlock wordStart) {
		
		System.out.println("Start Build...");
		//Set Start Flag.
		DScriptFlag startFlag = new DScriptFlag();
		startFlag.setWordstart(true);
		buildBlock(wordStart,startFlag);
		//Start Recursion:
			//Generate Subflags
			//Build All Subdocuments with their Subflags
		//Combine all Subdocuments with this Document
		
		
		
		//Get DScriptBlock
		//Get LigatureID/TextID & DSVG File
		//Build File with the provided flags -> DOM
		//DOM -> DScriptVector
		//Iterate over DScript Vector (Breadth First) Iterate over Vectors
		return null;
	}
	
	private static void buildBlock(DScriptBlock block, DScriptFlag flag) {
		boolean hasSubBlocks = false;
		
		if(block.chainExist("c")) {
			hasSubBlocks = true;
			DScriptFlag subFlag = new DScriptFlag();
			subFlag.setInputFlag("c", true);
			flag.setOutputFlag("c", true);
			
			DScriptBlock subBlock = block.getChain("c");
			buildBlock(subBlock, subFlag);
		}
		
		if(block.chainExist("l")) {
			hasSubBlocks = true;
			DScriptFlag subFlag = new DScriptFlag();
			subFlag.setInputFlag("r", true);
			flag.setOutputFlag("l", true);
			
			DScriptBlock subBlock = block.getChain("l");
			buildBlock(subBlock, subFlag);
		}
		
		if(block.chainExist("r")) {
			hasSubBlocks = true;
			DScriptFlag subFlag = new DScriptFlag();
			subFlag.setInputFlag("l", true);
			flag.setOutputFlag("r", true);
			
			DScriptBlock subBlock = block.getChain("r");
			buildBlock(subBlock, subFlag);
		}
		
		if(block.chainExist("i")) {
			hasSubBlocks = true;
			DScriptFlag subFlag = new DScriptFlag();
			subFlag.setInputFlag("i", true);
			flag.setOutputFlag("i", true);
			
			DScriptBlock subBlock = block.getChain("i");
			buildBlock(subBlock, subFlag);
		}
		
		if(!hasSubBlocks) {
			flag.setWordend(true);
		}
		
		System.out.println(block.getText()+" - "+flag);
		//We have now the completed Flag
		//Now we try to get the Ligature ID, the Text and the Text ID
		String out = "";
		out = "Text: "			+ block.getText();
		out +=" - LigatureID: "	+ block.getLigatureID();
		out +=" - TextID: "		+ block.getTextID(); 
		out +=" - DocumentURI: "+ block.getDocumentURI();
		System.out.println(out);
		
		block.gatherLigatureInformation();
		
		out ="";
		out ="Text: "			+ block.getText();
		out +=" - LigatureID: "	+ block.getLigatureID();
		out +=" - TextID: "		+ block.getTextID(); 
		out +=" - DocumentURI: "+ block.getDocumentURI();
		System.out.println(out);
		
		//Then we get the DSVG Document and parse it
		try {
			DSVGParser.parseDSVGFile("Ligatures\\"+block.getDocumentURI(), flag);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done");
		System.out.println();
	}
	
}
