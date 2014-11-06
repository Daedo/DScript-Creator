package scriptparser;

import java.util.HashMap;
import java.util.Vector;

import build.DScriptBuilder;

public class DScriptBlock {
	private HashMap<String,DScriptBlock> chains;
	private String text;
	private String mainChain;
	private int ligatureID;
	private int textID;

	public DScriptBlock() {
		this.chains 	= new HashMap<>();
		this.text 		= "";
		this.mainChain 	= "";
		this.ligatureID = -1;
		this.textID		= -1;
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

	public int getLigatureID() {
		return this.ligatureID;
	}

	public void setLigatureID(int newLigatureID) {
		this.ligatureID = newLigatureID;
	}
	
	public int getTextID() {
		return this.textID;
	}

	public void setTextID(int newTextID) {
		this.textID = newTextID;
	}

	/**
	 * Changes the LigatureID or TextID from a String
	 * LID (e.g. L56)
	 * TID (e.g. 4)
	 * 
	 * @param newID The new ID from a String
	 */
	public void setIDFromString(String newID) {
		String ligatureRegex = "L?\\d+";
		boolean isValidID = newID.matches(ligatureRegex);
		
		if(isValidID) {
			String LIDRegex = "L\\d+";
			boolean isLigatureID = newID.matches(LIDRegex);
			if(isLigatureID) {
				//Ligature ID
				String ID = newID.substring(1);
				this.ligatureID=Integer.parseInt(ID);
			} else {
				//Text ID
				this.textID=Integer.parseInt(newID);
			}
		}
		
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
		
		isUnignoredMainChain = useMainChain && this.mainChain == "l";
		if(this.chains.containsKey("l") && !isUnignoredMainChain) {
		 	writtenText += this.chains.get("l").getWrittenText(useMainChain);
		}
		
		isUnignoredMainChain = useMainChain && this.mainChain == "r";
		if(this.chains.containsKey("r") && !isUnignoredMainChain) {
		 	writtenText += this.chains.get("r").getWrittenText(useMainChain);
		}
		
		isUnignoredMainChain = useMainChain && this.mainChain == "i";
		if(this.chains.containsKey("i") && !isUnignoredMainChain) {
		 	writtenText += this.chains.get("i").getWrittenText(useMainChain);
		}
		
		isUnignoredMainChain = useMainChain && this.mainChain == "c";
		if(this.chains.containsKey("c") && !isUnignoredMainChain) {
		 	writtenText += this.chains.get("c").getWrittenText(useMainChain);
		}
		
		if(useMainChain) {
			writtenText += this.chains.get(this.mainChain).getWrittenText(useMainChain);
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
		if(this.ligatureID == -1) {
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

		boolean isValidMain = (isMain && this.mainChain == "")||!isMain;

		boolean isValidBlock = (subChain != null);		

		if(isValidPosition(position) && isFreePosition && isValidMain && isValidBlock && isValidPosition) {
			this.chains.put(realPosition, subChain);
			if(isMain) {
				this.mainChain = realPosition;
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
		return this.mainChain;
	}

	public DScriptBlock getMainChain() {		
		if(this.mainChain!="") {
			return this.chains.get(this.mainChain);
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

				this.mainChain = centerChain.getMainChainPosition();
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
		if(this.ligatureID == testBlock.getLigatureID() && this.ligatureID == -1) {
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

		if(this.ligatureID!=-1) {
			output += " - Ligature ID: "+this.ligatureID;
		}
		
		if(this.textID!=-1) {
			output += " - Text ID: "+this.textID;
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

}