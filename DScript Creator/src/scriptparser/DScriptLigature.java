package scriptparser;

public class DScriptLigature {
	private int ligatureID;
	private int textID;
	private String text;

	public DScriptLigature(int newLigatueID) {
		if(newLigatueID > 0) {
			this.ligatureID = newLigatueID;
			this.textID = -1;
			this.text = "";
		}
	}

	public DScriptLigature(String newText,int newTextID) {
		if(newText!="" && newTextID > 0) {
			this.text = newText;
			this.textID = newTextID;
			this.ligatureID = -1;
		}
	}

	public void setLigatureID(int newLigatureID) {
		this.ligatureID = newLigatureID;
	}

	@Override
	public boolean equals(Object arg0) {
		
		if(arg0 instanceof DScriptLigature) {
			DScriptLigature lig = (DScriptLigature) arg0;
			boolean hasEqualText = lig.getText().equals(this.text);
			boolean hasEqualTextID = lig.getTextID() == this.textID;
			boolean hasEqualLigID = lig.getLigatureID() == this.ligatureID;
			return hasEqualLigID && hasEqualText && hasEqualTextID;
		} 
		
		return false;
	}

	@Override
	public int hashCode() {
		String hash = this.text + " - "+this.textID+" - "+this.ligatureID;
		return hash.hashCode();
	}

	public void setText(String newText,int newTextID) {
		this.text = newText;
		this.textID = newTextID;
	}

	public int getLigatureID() {
		return this.ligatureID;
	}

	public int getTextID() {
		return this.textID;
	}

	public String getText() {
		return this.text;
	}
}
