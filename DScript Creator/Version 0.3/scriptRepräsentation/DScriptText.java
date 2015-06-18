package scriptRepräsentation;

import java.util.Vector;

public class DScriptText {
	public double width;
	public double height;
	public Vector<Wordstart> words;
	
	public DScriptText(double w,double h) {
		this.width = w;
		this.height = h;
		this.words = new Vector<>();
	}
	
}
