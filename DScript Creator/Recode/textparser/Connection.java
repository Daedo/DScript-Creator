package textparser;

import utils.Utilitys;

public class Connection {
	private Glyph start;
	private Glyph end;
	private String type;
	
	public Connection(Glyph startGlyph,Glyph endGlyph,String connectionType) {
		this.start= startGlyph;
		this.end  = endGlyph;
		this.type = connectionType;
	}

	public Glyph getStart() {
		return this.start;
	}

	public void setStart(Glyph startGlyph) {
		this.start = startGlyph;
	}

	public Glyph getEnd() {
		return this.end;
	}

	public void setEnd(Glyph endGlyph) {
		this.end = endGlyph;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String connectionType) {
		this.type = connectionType;
	}

	public void debugConnection(int tabs) {
		String tab = Utilitys.repeatString("\t", tabs);
		System.out.println(tab+this.type);
		this.end.debugGlyph(tabs+1);
	}
}
