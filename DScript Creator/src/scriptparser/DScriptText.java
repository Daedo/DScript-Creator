package scriptparser;

import java.util.Vector;

public class DScriptText {
	private Vector<DScriptLine> lines;
	
	public DScriptText() {
		this.lines = new Vector<>();
	}
	
	public void addLine(DScriptLine line) {
		if(line!=null) {
			this.lines.add(line);
		}
	}
	
	public int getLinecount() {
		return this.lines.size();
	}
	
	public void debugText() {
		System.out.println("Dscript Text");
		System.out.println("Linecount: "+getLinecount());
		System.out.println("");
		
		for(DScriptLine line:this.lines) {
			line.debugLine();
			System.out.println("");
		}
	}
	
	public Vector<DScriptLigature> getLigatures() {
		Vector<DScriptLigature> ligatures = new Vector<>();
		for(DScriptLine line:this.lines) {
			Vector<DScriptLigature> subLigatures = line.getLigatures();
			
			for(DScriptLigature subLigature: subLigatures) {
				if(!ligatures.contains(subLigature)) {
					ligatures.add(subLigature);
				}
			}
		}
		
		return ligatures;
	}

	public void buildText() {
		for(DScriptLine line: this.lines) {
			line.buildLine();
		}
	}
}
