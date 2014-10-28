package scriptparser;

import java.util.HashMap;
import java.util.Vector;

import build.DScriptBuilder;

public class DScriptBlock {
	private HashMap<String,DScriptBlock> chains;
	private String text;
	private String main;
	private String ligatureID;

	public DScriptBlock() {
		this.chains = new HashMap<>();
		this.text = "";
		this.main = "";
		this.ligatureID = "";
	}

	public static boolean isValidPosition(String position) {
		String posRegexp= "(i|r|l|c)m?";
		return position.matches(posRegexp);
	}
	
	//---------- Getter/Setter ----------
	public String getText() {
		return this.text;
	}

	public void setText(String newText) {
		this.text = newText;
	}

	public String getLigatureID() {
		return this.ligatureID;
	}

	public void setLigatureID(String ligatureID) {
		this.ligatureID = ligatureID;
	}
	
	//---------- Text ----------
	/**
	 * Returns the text in a written version. The order of the Subchains follows the original DScript rules
	 * (left, right, inside, bottom)
	 * 
	 * @param useMainChain If this is set to true. The Main Chain will be added at last. Instead of using the original rules
	 * @return
	 */
	public String getWrittenText(boolean useMainChain) {
		String writtenText;
		
		writtenText = this.text;
		
		boolean isUnignoredMainChain;
		
		isUnignoredMainChain = useMainChain && this.main == "l";
		if(this.chains.containsKey("l") && !isUnignoredMainChain) {
		 	writtenText += this.chains.get("l").getWrittenText(useMainChain);
		}
		
		isUnignoredMainChain = useMainChain && this.main == "r";
		if(this.chains.containsKey("r") && !isUnignoredMainChain) {
		 	writtenText += this.chains.get("r").getWrittenText(useMainChain);
		}
		
		isUnignoredMainChain = useMainChain && this.main == "i";
		if(this.chains.containsKey("i") && !isUnignoredMainChain) {
		 	writtenText += this.chains.get("i").getWrittenText(useMainChain);
		}
		
		isUnignoredMainChain = useMainChain && this.main == "c";
		if(this.chains.containsKey("c") && !isUnignoredMainChain) {
		 	writtenText += this.chains.get("c").getWrittenText(useMainChain);
		}
		
		if(useMainChain) {
			writtenText += this.chains.get(this.main).getWrittenText(useMainChain);
		}

		return writtenText;
	}
	
	//TODO Mainchain should be continued after the {}
	// 1{[l]2 [cm] (34|L7)}5 -/-> 1{[cm] (34|L7){[cm] 5}[l] 2}
	
	/**
	 * Returns the code, that would create this Block
	 * 
	 * @return Returns the code as a String 
	 */
	public String getCodeText() {
		String ownText = "";
		if(this.ligatureID == "") {
			ownText = this.text;
		} else {
			ownText = "("+this.text+"|"+this.ligatureID+")";
		}
		
		String subText = "";
		
		boolean hasSubchains = ! getSubchains().isEmpty();
		if(hasSubchains) {
			
			subText="{";
			
			if(chainExist("c")) {
				
				String pos = "c";
				if(getMainChainPosition().equals(pos)) {
					pos+="m";
				}
				
				subText += "["+pos+"] "+getChain("c").getCodeText();
			}
			
			if(chainExist("l")) {
				String pos = "l";
				if(getMainChainPosition().equals(pos)) {
					pos+="m";
				}
				
				subText += "["+pos+"] "+getChain("l").getCodeText();
			}
			
			if(chainExist("r")) {
				String pos = "r";
				if(getMainChainPosition().equals(pos)) {
					pos+="m";
				}
				
				subText += "["+pos+"] "+getChain("r").getCodeText();
			}
			
			if(chainExist("i")) {
				String pos = "i";
				//Inside is never a main Chain
				subText += "["+pos+"] "+getChain("i").getCodeText();
			}
			
			subText +="}";
		}
		
		return ownText+subText;
	}
	
	//---------- Subchains ----------
	public void addChain(String position,DScriptBlock subChain) {
		//Inside Chains can not continue the main chain
		boolean isMain = position.matches("[^i]m");
		boolean isValidPosition = position.matches("(l|r|i|c)m?");
		String realPosition = position.substring(0, 1);

		boolean isFreePosition = !this.chains.containsKey(realPosition);

		boolean isValidMain = (isMain && this.main == "")||!isMain;

		boolean isValidBlock = (subChain != null);		

		if(isValidPosition(position) && isFreePosition && isValidMain && isValidBlock && isValidPosition) {
			this.chains.put(realPosition, subChain);
			if(isMain) {
				this.main = realPosition;
			}
		}
	}

	public DScriptBlock getChain(String position) {
		return this.chains.get(position);
	}

	public boolean chainExist(String position) {
		return this.chains.containsKey(position);
	}

	/**
	 * @return Returns a Vector with all Subchains
	 */
	public Vector<DScriptBlock> getSubchains() {
		Vector<DScriptBlock> subchains = new Vector<>();
		
		if(this.chains.containsKey("c")) {
		 	subchains.add(this.chains.get("c"));
		}

		if(this.chains.containsKey("r")) {
			subchains.add(this.chains.get("r"));
		}

		if(this.chains.containsKey("l")) {
			subchains.add(this.chains.get("l"));
		}

		if(this.chains.containsKey("i")) {
			subchains.add(this.chains.get("i"));
		}
		
		return subchains;
	}
	
	public String getMainChainPosition() {
		return this.main;
	}

	public DScriptBlock getMainChain() {		
		if(this.main!="") {
			return this.chains.get(this.main);
		}

		if(this.chains.containsKey("c")) {
			return this.chains.get("c");
		}

		if(this.chains.containsKey("r")) {
			return this.chains.get("r");
		}

		if(this.chains.containsKey("l")) {
			return this.chains.get("l");
		}

		//Inside chains can not continue the main Chain.

		return null;
	}

	public DScriptBlock getMainChainEnd() {
		if(this.chains.isEmpty()) {
			return this;
		}
		return this.getMainChain().getMainChainEnd();
	}
	
	//---------- Space Methods ----------
	/**
	 * Calculates the width of the entire Word
	 * 
	 * @return
	 */
	public int getWidth() {
		int width = 1;

		DScriptBlock leftChain   = this.chains.get("l");
		if(leftChain != null) {
			width += leftChain.getWidth();
		}

		DScriptBlock rightChain  = this.chains.get("r");
		if(rightChain != null) {
			width += rightChain.getWidth();
		}

		DScriptBlock insideChain = this.chains.get("i");
		if(insideChain != null) {
			width += insideChain.getWidth();
		}

		DScriptBlock centerChain = this.chains.get("c");
		if(centerChain != null) {
			width += centerChain.getWidth()-1;
		}

		return width;
	}

	/**
	 * IMPORTAND NOTE: Inside Chains [i] will be drawn on the Left side of the chain, between [l] and [c]
	 * It also will be calculated in the getLeftSpace method
	 */

	/**
	 * Calculates the space needed on the left side of this chain when drawn
	 * 
	 * @return The space as an integer
	 */
	public int getLeftSpace() {
		int width = 0;

		DScriptBlock leftChain   = this.chains.get("l");
		if(leftChain != null) {
			width += leftChain.getWidth();
		}

		DScriptBlock insideChain = this.chains.get("i");
		if(insideChain != null) {
			width += insideChain.getWidth();
		}

		DScriptBlock centerChain = this.chains.get("c");
		if(centerChain != null) {
			width += centerChain.getLeftSpace();
		}

		return width;
	}

	/**
	 * Calculates the space needed on the right side of this chain when drawn
	 * 
	 * @return The space as an integer
	 */
	public int getRightSpace() {
		int width = 0;

		DScriptBlock leftChain   = this.chains.get("r");
		if(leftChain != null) {
			width += leftChain.getWidth();
		}

		DScriptBlock centerChain = this.chains.get("c");
		if(centerChain != null) {
			width += centerChain.getRightSpace();
		}

		return width;
	}

	//---------- Concatenate Method ----------
	/**
	 * Combines Fitting center Chain together into one single chain
	 * 
	 * E.G.
	 * {...[cm]text ...}text2
	 * will become
	 * {...[cm]texttext2 ...}
	 * This makes it easier for the later Editor to handle those Blocks
	 */
	public void concatenateChains() {

		boolean canConcatenate = true;

		if(chainExist("l")){
			canConcatenate = false;
			this.chains.get("l").concatenateChains();
		}

		if(chainExist("r")){
			canConcatenate = false;
			this.chains.get("r").concatenateChains();
		}

		if(chainExist("i")){
			canConcatenate = false;
			this.chains.get("i").concatenateChains();
		}

		if(chainExist("c")){
			DScriptBlock centerChain = this.chains.get("c");
			centerChain.concatenateChains();

			if(canConcatenate && this.hasMatchingProperties(centerChain)) {

				this.chains.remove("c");

				this.main = centerChain.getMainChainPosition();
				this.text += centerChain.getText();

				if(centerChain.chainExist("l")) {
					this.chains.put("l", centerChain.getChain("l"));
				}

				if(centerChain.chainExist("r")) {
					this.chains.put("r", centerChain.getChain("r"));
				}

				if(centerChain.chainExist("c")) {
					this.chains.put("c", centerChain.getChain("c"));
				}

				if(centerChain.chainExist("i")) {
					this.chains.put("i", centerChain.getChain("i"));
				}
			}
		}		

	}

	/**
	 * Compares the LigatueID of two Blocks. Returns true if they are empty
	 * 
	 * @param testBlock the Block to compare
	 * @return Returns True if the LigatureID are empty
	 */
	public boolean hasMatchingProperties(DScriptBlock testBlock) {
		if(this.ligatureID == testBlock.getLigatureID() && this.ligatureID == "") {
			return true;
		}
		return false;
	}

	//---------- Debug Method ----------
	public void debugBlocks(int recursionLevel) {
		String tabs = "";

		tabs = utils.Utilitys.repeatString("\t", recursionLevel);

		String output = tabs+this.text;

		output +=" - Sub Chains: "+this.chains.size()+ " - Required Width: "+this.getWidth()+" ( l: "+getLeftSpace()+" r: "+getRightSpace()+")";
		if(this.getMainChainPosition()!="") {
			output += " - Main Chain: ["+this.getMainChainPosition().toUpperCase()+"]";
		}

		if(this.ligatureID!="") {
			output += " - Ligature ID: "+this.ligatureID;
		}
		
		Vector<DScriptLigature> ligatures = getLigatures();
		for (DScriptLigature ligature: ligatures) {
			output+= "\n"+tabs+ligature.getLigatureID()+" - "+ligature.getText()+" - "+ligature.getTextID();
		}
		
		System.out.println(output);
		
		if(this.chains.containsKey("c")) {
			System.out.println(tabs+"[C]:");
			this.chains.get("c").debugBlocks(recursionLevel+1);
		}

		if(this.chains.containsKey("r")) {
			System.out.println(tabs+"[R]:");
			this.chains.get("r").debugBlocks(recursionLevel+1);
			System.out.println("");
		}

		if(this.chains.containsKey("l")) {
			System.out.println(tabs+"[L]:");
			this.chains.get("l").debugBlocks(recursionLevel+1);
			System.out.println("");
		}

		if(this.chains.containsKey("i")) {
			System.out.println(tabs+"[I]:");
			this.chains.get("i").debugBlocks(recursionLevel+1);
			System.out.println("");
		}
	}

	//---------- Ligature Methods ----------
	public boolean isValidLigature() {
		if(this.ligatureID=="") {
			return true;
		}
		return this.ligatureID.matches("L?\\d+");
	}
	
	/**
	 * @return Returns a Vector with all Ligatures used in this block and all of its subblocks.
	 * The Vector does not contain duplicates, unless two Ligatures are defined once by their Text ID
	 * and by their Ligature ID at another position.
	 */
	public Vector<DScriptLigature> getLigatures() {
		
		Vector<DScriptLigature> ligatures = new Vector<>();
		
		if(this.ligatureID!="") {
			if(isValidLigature()) {
				DScriptLigature ligature;
				if(this.ligatureID.matches("L\\d+")) {
					String LID = this.ligatureID.replaceAll("L", "");
					int ligID = Integer.parseInt(LID);
					ligature = new DScriptLigature(ligID);
				} else {
					int ligID = Integer.parseInt(this.ligatureID);
					ligature = new DScriptLigature(this.text,ligID);
				}
				
				if(! ligatures.contains(ligature)) {
					ligatures.add(ligature);
				}
				
			} else {
				// Error
				System.err.println("ERROR INVALID LIGATURE");
			}
		} else {
			
			for (int i = 0;i<this.text.length();i++) {
				String textChar = this.text.charAt(i)+"";
				DScriptLigature ligature = new DScriptLigature(textChar,1);
				
				if(! ligatures.contains(ligature)) {
					ligatures.add(ligature);
				}
			}
			
		}
		//Recursion with all Subblocks
		
		Vector<DScriptBlock> subblocks = getSubchains();
		
		for(DScriptBlock subblock: subblocks) {
			Vector<DScriptLigature> subLigatures = subblock.getLigatures();
			
			for(DScriptLigature subLigature: subLigatures) {
				if(!ligatures.contains(subLigature)) {
					ligatures.add(subLigature);
				}
			}
		}
		
		return ligatures;
	}
}