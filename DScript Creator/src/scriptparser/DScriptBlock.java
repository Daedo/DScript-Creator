package scriptparser;

import java.util.HashMap;

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

	public String getText() {
		return this.text;
	}
	
	/*
	 * TODO getWrittenText
	 * return text+l.getWrittenText+r.getWrittenText+c.getWrittenText;
	 */
	
	public void setText(String newText) {
		this.text = newText;
	}

	public String getLigatureID() {
		return ligatureID;
	}

	public void setLigatureID(String ligatureID) {
		this.ligatureID = ligatureID;
	}

	public static boolean isValidPosition(String position) {
		String posRegexp= "(i|r|l|c)m?";
		return position.matches(posRegexp);
	}

	public void addChain(String position,DScriptBlock subChain) {
		//Inside Chains can not continue the main chain
		boolean isMain = position.matches("[^i]m");
		String realPosition = position.substring(0, 1);

		boolean isFreePosition = !this.chains.containsKey(realPosition);

		boolean isValidMain = (isMain && this.main == "")||!isMain;

		boolean isValidBlock = (subChain != null);		

		if(isValidPosition(position) && isFreePosition && isValidMain && isValidBlock) {
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
	
	public void debugBlocks(int recursionLevel) {
		String tabs = "";
		
		tabs = utils.Utilitys.repeatString("\t", recursionLevel);

		String output = tabs+this.text;

		output +=" - Sub Chains: "+this.chains.size()+ " - Required Width: "+this.getWidth()+" ( l: "+getLeftSpace()+" r: "+getRightSpace()+")";
		if(this.getMainChainPosition()!="") {
			output += " - Main Chain: ["+this.getMainChainPosition().toUpperCase()+"]";
		}
		
		if(this.ligatureID!="") {
			output += " - Ligature ID: "+ligatureID;
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
